/*
    This script scans an array of lines of text looking for xml tags, and passes a message
    back with the start and end points of the tags.
 */

var tags = null;

self.addEventListener('message', function (e) {
    if (e.data.tagDB) {
        var database = JSON.parse(e.data.tagDB);
        tags = [];

        for (var property in database) {
            if (database.hasOwnProperty(property)) {
                tags.push(property);
            }
        }
    }

    if (e.data.lines && tags != null) {
        var lines = e.data.lines;

        var retValue = [];
        for (var lineIndex = 0, linesLength = lines.length; lineIndex < linesLength; ++lineIndex) {

            var line = lines[lineIndex];
            var tagDetails = [];

            // match only xml/html elements
            var tagRe = /(<\s*\/?\s*)([^\s/>]+)/g;
            var tagMatch = null;
            while (tagMatch = tagRe.exec(line)) {
                var tag = tagMatch[2];

                if (tags.indexOf(tag) != -1) {
                    var prefix = tagMatch[1];
                    var start = tagMatch.index + prefix.length;
                    var end = start + tag.length;
                    tagDetails.push([start, end]);
                }
            }

            // match comments (specifically injection comments)
            var commentRe = /(<!--\s*)(.*?):.*?-->/g;
            var commentMatch = null;
            while (commentMatch = commentRe.exec(line)) {
                var comment = commentMatch[2];

                if (tags.indexOf(comment) != -1) {
                    var prefix = commentMatch[1];
                    var start = commentMatch.index + prefix.length;
                    var end = start + comment.length;
                    tagDetails.push([start, end]);
                }
            }

            retValue.push(tagDetails);
        }

        postMessage(retValue);
    }
});