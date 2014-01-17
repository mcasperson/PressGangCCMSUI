/*
    This JavaScript takes xml and scans it for xml elements that do not match the given condition.
    Since it uses regular expressions, it is somewhat tolerant of badly formatted XML.
 */

/**
 * Take the start and end positions of characters in a block of text that includes line breaks,
 * and convert it to start and end positions across a number of lines. This is required because
 * markings in the ace editor are determined by line number, start and end position.
 * @param text The text being analysed
 * @param startEnd The start and end positions to be converted to start and end positions over multiple lines
 * @returns {Array}
 */
function convert(text, startEnd) {
    var retValue = [];

    var beforeText = text.substr(0, startEnd.start);
    var beforeLines = beforeText.split("\n");

    var includingText = text.substr(startEnd.start, startEnd.end - startEnd.start);
    var includingLines = includingText.split("\n");

    var line = beforeLines.length;
    var start = beforeLines[beforeLines.length - 1].length;
    var end = includingLines.length > 1 ?
        start + includingLines[0].length :
        startEnd.end - (startEnd.start - start);

    retValue.push({line: line, start: start, end: end});

    if (includingLines.length > 1) {
        var remainingLength = includingText.length - end;

        for (var includingLinesIndex = 1, includingLinesCount = includingLines.length - 1; includingLinesIndex < includingLinesCount; ++includingLinesIndex) {
            retValue.push({line: line + includingLinesIndex, start: 0, end: includingLines[includingLinesIndex].length});
            remainingLength -= includingLines[includingLinesIndex].length;
        }

        retValue.push({line: line + includingLines.length, start: 0, end: remainingLength });
    }

    return retValue;
}

self.addEventListener('message', function (e) {

    // return an empty array if there is no condition selected
    if (!e.data.condition) {
        postMessage([]);
    }

    /**
     * A regex to match an XML element with a condition attribute
     * @type {RegExp}
     */
    var startTagRegex = /<\s*([^>]*?)\s+[^>]*?condition\s*=\s*['"](.*?)['"][^>]*?(\/)?\s*>/;
    var text = e.data.text;
    var textConditions = e.data.condition.split(":;,");
    var conditions = [];
    var totalLength = 0;

    for (var textConditionIndex = 0, textConditionCount = textConditions.length; textConditionIndex < textConditionCount; ++textConditionIndex) {
        var textCondition = textConditions[textConditionIndex];
        try {
            conditions.push(new RegExp(textCondition));
        } catch (e) {
            // bad regex, so ignore it
        }
    }

    var retValue = [];

    outerloop:
    while (text.length != 0) {
        var match = startTagRegex.exec(text);
        if (match == null) {
            break outerloop;
        }

        var include = false;
        for (var conditionIndex = 0, conditionCount = conditions.length; conditionIndex < conditionCount; ++conditionIndex) {
            var condition = conditions[conditionIndex];
            if(condition.exec(match[2])) {
                include = true;
                break;
            }
        }

        var start = match.index + totalLength;
        totalLength += match.index + match[0].length;
        text = text.substr(match.index + match[0].length);

        // This conditional element does not meet the condition. So we need to find the
        // end tag, and note the space that the conditional element takes up.
        if (!include) {
            // not a self closing element
            if (!match[3]) {
                // a regex to match any opening tag of the same type as the conditional one we are processing
                var openTag = new RegExp("<\s*" + match[1] + "\s*.*?>");
                // a regex to match any closing tag of the same type as the conditial one we are processing
                var closeTag = new RegExp("<\s*\/\s*" + match[1] + "\s*>");

                var childCount = 0;

                while (childCount >= 0) {
                    var openMatch = openTag.exec(text);
                    var closeMatch = closeTag.exec(text);

                    if (closeMatch == null) {
                        // no close tag exists. this is bad xml, but we will highlight it
                        // like the open tag is spread to the end of the text
                        retValue.push({start: start, end: totalLength + text.length});
                        break outerloop;
                    } else if (openMatch == null || openMatch.index > closeMatch.index) {
                        // we found a closing tag without finding an opening tag, or
                        // where the closing tag was before the next opening one
                        --childCount;
                        totalLength += closeMatch.index + closeMatch[0].length;
                        text = text.substr(closeMatch.index + closeMatch[0].length);
                    } else if (openMatch != null && openMatch.index < closeMatch.index) {
                        // we found an opening tag before the next closing one. This
                        // means the opening tag is a child of the conditional one
                        ++childCount;
                        totalLength += openMatch.index + openMatch[0].length;
                        text = text.substr(openMatch.index + openMatch[0].length);
                    }
                }

                retValue = retValue.concat(convert(e.data.text, {start: start, end: totalLength}));
            } else {
                retValue = retValue.concat(convert(e.data.text, {start: start, end: totalLength}));
            }
        }
    }

    postMessage(retValue);
});