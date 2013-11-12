package org.jboss.pressgang.ccms.ui.client.local.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.xml.client.Comment;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Some utility methods for working with XML
 */
public class XMLUtilities {
    private static final RegExp CDATA_RE = RegExp.compile("<!\\[CDATA\\[.*?\\]\\]>", "g");
    private static final RegExp CDATA_START_HANGING_RE = RegExp.compile("<!\\[CDATA\\[.*?$", "g");
    private static final RegExp ELEMENT_RE = RegExp.compile("(<[^/!].*?)(/?)(>)", "g");

    /**
     * Parse the supplied text as XML, and return any errors.
     *
     * @param xml The XML to be parsed
     * @return Any errors, or null if none were found
     */
    @Nullable
    public static native String getXMLErrors(@NotNull final String xml) /*-{
        var parserError = "parsererror";

        // code for IE
        if ($wnd.ActiveXObject) {
            var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async = "false";
            xmlDoc.loadXML($doc.all(xml).value);

            if (xmlDoc.parseError.errorCode != 0) {
                var message = "Error Code: " + xmlDoc.parseError.errorCode + "\n";
                message = message + "Error Reason: " + xmlDoc.parseError.reason + "\n";
                message = message + "Error Line: " + xmlDoc.parseError.line;
                return message;
            }
        }
        // code for Mozilla, Firefox, Opera, etc.
        else if ($doc.implementation.createDocument) {
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(xml, "text/xml");

            if (xmlDoc.getElementsByTagName(parserError).length > 0) {
                var message = null;
                var foundH3 = false;
                checkXML = function (node) {
                    var nodeName = node.nodeName;
                    if (nodeName == "h3") {
                        if (foundH3) {
                            return;
                        }
                        foundH3 = true;
                    }
                    if (nodeName == "#text") {
                        if (message == null) {
                            message = node.nodeValue + "\n";
                        } else {
                            message = message + node.nodeValue + "\n";
                        }
                    }

                    var nodeLength = node.childNodes.length;
                    for (var i = 0; i < nodeLength; i++) {
                        checkXML(node.childNodes[i], message, foundH3);
                    }
                }

                checkXML(xmlDoc.getElementsByTagName(parserError)[0]);
                return message;
            }
        }

        return null;
    }-*/;

    /**
     * Strips out the xml preamble. This is usually done before the XML
     * is rendered in the UI
     *
     * @param xml The source xml
     * @return the xml without the preamble
     */
    public static String removeXmlPreamble(@NotNull final String xml) {
        final RegExp regExp = RegExp.compile("^\\s*<\\?[\\s\\S]*?\\?>");
        return regExp.replace(xml, "");
    }

    /**
     * Strips out the doctype preamble in XML. This is usually done before the XML
     * is rendered in the UI
     *
     * @param xml The source xml
     * @return the xml without the doctype preamble
     */
    public static String removeDoctypePreamble(@NotNull final String xml) {
        final RegExp regExp = RegExp.compile("^\\s*<\\s*!DOCTYPE[\\s\\S]*?>");
        return regExp.replace(xml, "");
    }

    public static String removeAllPreamble(@NotNull final String xml) {
        return removeDoctypePreamble(removeXmlPreamble(xml));
    }

    public static Document convertStringToDocument(final String xml) {
        try {
            final Document doc = XMLParser.parse(xml);

            // Workaround for http://code.google.com/p/google-web-toolkit/issues/detail?id=3613
            final NodeList parseErrors = doc.getElementsByTagName("parsererror");
            if (parseErrors.getLength() > 0) {
                return null;
            } else {
                return doc;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String resolveInjections(@NotNull final String xml) {
        return resolveInjections(xml, null);
    }

    public static String resolveInjections(@NotNull final String xml, @Nullable final String entities) {
        String fixedXml = xml;
        if (entities != null) {
            fixedXml = entities + xml;
        }
        final Document doc = convertStringToDocument(fixedXml);
        if (doc == null) {
            return xml;
        } else {
            InjectionResolver.resolveInjections(doc);
            return removeAllPreamble(doc.toString());
        }
    }

    /**
     * Scans a node and all of its children for nodes of a particular type.
     *
     * @param parent    The parent node to search from.
     * @param nodeNames A single node name or list of node names to search for
     * @return A List of all the nodes found matching the nodeName(s) under the parent
     */
    public static List<Node> getChildNodes(final Node parent, final String... nodeNames) {
        return getChildNodes(parent, true, nodeNames);
    }

    public static List<Comment> getComments(final Node parent) {
        final List<Node> commentNodes = getChildNodes(parent, "#comment");
        final List<Comment> retValue = new ArrayList<Comment>();
        for (final Node comment : commentNodes) {
            retValue.add((Comment) comment);
        }

        return retValue;
    }

    /**
     * Scans a node for directly related child nodes of a particular type. This method will not scan for nodes that aren't a child of the
     * parent node.
     *
     * @param parent    The parent node to search from.
     * @param nodeNames A single node name or list of node names to search for
     * @return A List of all the nodes found matching the nodeName(s) under the parent
     */
    public static List<Node> getDirectChildNodes(final Node parent, final String... nodeNames) {
        return getChildNodes(parent, false, nodeNames);
    }

    /**
     * Scans a node and all of its children for nodes of a particular type.
     *
     * @param parent          The parent node to search from.
     * @param recursiveSearch If the child nodes should be recursively searched.
     * @param nodeNames       A single node name or list of node names to search for
     * @return a List of all the nodes found matching the nodeName under the parent
     */
    protected static List<Node> getChildNodes(final Node parent, boolean recursiveSearch, final String... nodeNames) {
        final List<Node> nodes = new ArrayList<Node>();
        final NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            final Node child = children.item(i);

            for (final String nodeName : nodeNames) {
                if (child.getNodeName().equals(nodeName)) {
                    nodes.add(child);
                }
                if (recursiveSearch) {
                    nodes.addAll(getChildNodes(child, true, nodeName));
                }
            }
        }
        return nodes;
    }

    /**
     * Get a nodes text content.
     *
     * Note: At this stage this method only handles a node that has only text child nodes.
     *
     * @param xmlNode
     * @return
     */
    public static String getNodeText(Node xmlNode) {
        if (xmlNode == null) return "";
        final StringBuilder result = new StringBuilder(4096);
        for (Node child = xmlNode.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.TEXT_NODE) {
                result.append(child.getNodeValue());
            }
        }
        return result.toString();
    }

    /**
     * In order to link the rendered view to the line in the editor where the rendered text is coming from,
     * each element has an attribute pressgangeditorlinenumber added to it to reflect the original line number.
     * This can then be read when an element in the rendered view is clicked.
     *
     * @param topicXML The source XML
     * @return The XML with all the elements having pressgangeditorlinenumber attributes
     */
    @NotNull
    public static String addLineNumberAttributesToXML(@Nullable final String topicXML) {
        final StringBuilder retValue = new StringBuilder();

        if (topicXML != null) {
            /* true if the last line had a hanging cdata start */
            boolean inCData = false;
            int i = 0;
            for (final String line : topicXML.split("\n")) {
                ++i;

                /* true if the last line had a hanging cdata start and we did not find an end in this line */
                boolean lineIsOnlyCData = inCData;

                String fixedLine = line;

                final Map<String, String> endHangingCData = new HashMap<String, String>();
                if (inCData) {
                    final RegExp cdataEndHangingRe = RegExp.compile("^.*?\\]\\]>", "g");
                    final MatchResult matcher = cdataEndHangingRe.exec(fixedLine);
                    if (matcher != null) {
                        int marker = endHangingCData.size();
                        while (fixedLine.indexOf("#CDATAENDPLACEHOLDER" + marker + "#") != -1) {
                            ++marker;
                        }

                        endHangingCData.put("#CDATAENDPLACEHOLDER" + marker + "#", matcher.getGroup(0));

                        inCData = false;

                        /*
                         * We found an end to the hanging cdata start. this lets us know further down that
                         * we do need to process some elements in this line.
                         */
                        lineIsOnlyCData = false;
                    }

                    for (final String marker : endHangingCData.keySet()) {
                        fixedLine = fixedLine.replace(endHangingCData.get(marker), marker);
                    }
                }

                final Map<String, String> singleLineCData = new HashMap<String, String>();
                for (MatchResult matcher = CDATA_RE.exec(line); matcher != null; matcher = CDATA_RE.exec(line)) {
                    int marker = singleLineCData.size();
                    while (line.indexOf("#CDATAPLACEHOLDER" + marker + "#") != -1) {
                        ++marker;
                    }

                    singleLineCData.put("#CDATAPLACEHOLDER" + marker + "#", matcher.getGroup(0));
                }

                for (final String marker : singleLineCData.keySet()) {
                    fixedLine = fixedLine.replace(singleLineCData.get(marker), marker);
                }

                final Map<String, String> startHangingCData = new HashMap<String, String>();
                if (!inCData) {
                    final MatchResult matcher = CDATA_START_HANGING_RE.exec(fixedLine);
                    if (matcher != null) {
                        int marker = startHangingCData.size();
                        while (fixedLine.indexOf("#CDATASTARTPLACEHOLDER" + marker + "#") != -1) {
                            ++marker;
                        }

                        startHangingCData.put("#CDATASTARTPLACEHOLDER" + marker + "#", matcher.getGroup(0));

                        inCData = true;
                    }
                }

                for (final String marker : startHangingCData.keySet()) {
                    fixedLine = fixedLine.replace(startHangingCData.get(marker), marker);
                }

                if (!lineIsOnlyCData) {
                    fixedLine = ELEMENT_RE.replace(fixedLine, "$1 pressgangeditorlinenumber=\"" + i + "\"$2$3");
                }

                for (final String marker : endHangingCData.keySet()) {
                    fixedLine = fixedLine.replace(marker, endHangingCData.get(marker));
                }

                for (final String marker : singleLineCData.keySet()) {
                    fixedLine = fixedLine.replace(marker, singleLineCData.get(marker));
                }

                for (final String marker : startHangingCData.keySet()) {
                    fixedLine = fixedLine.replace(marker, startHangingCData.get(marker));
                }

                retValue.append(fixedLine);
                retValue.append("\n");
            }
        }

        return retValue.toString();
    }
}
