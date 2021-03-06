package org.jboss.pressgang.ccms.ui.client.local.utilities;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.isStringNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.xml.client.Comment;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLFormat;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.searchresults.topics.TopicFilteredResultsAndDetailsPresenter;

public class InjectionResolver {
    /**
     * Used to identify that an <orderedlist> should be generated for the injection point
     */
    protected static final int ORDEREDLIST_INJECTION_POINT = 1;
    /**
     * Used to identify that an <itemizedlist> should be generated for the injection point
     */
    protected static final int ITEMIZEDLIST_INJECTION_POINT = 2;
    /**
     * Used to identify that an <xref> should be generated for the injection point
     */
    protected static final int XREF_INJECTION_POINT = 3;
    /**
     * Used to identify that an <xref> should be generated for the injection point
     */
    protected static final int LIST_INJECTION_POINT = 4;
    /**
     * This text identifies an option task in a list
     */
    protected static final String OPTIONAL_MARKER = "OPT:";
    /**
     * The text to be prefixed to a list item if a topic is optional
     */
    protected static final String OPTIONAL_LIST_PREFIX = "Optional: ";
    protected static final String ID_RE = "(\\d+|T(\\d+|(\\-[ ]*[A-Za-z][A-Za-z\\d\\-_]*)))";
    /**
     * A regular expression that identifies a topic id
     */
    protected static final String OPTIONAL_TOPIC_ID_RE = "(" + OPTIONAL_MARKER + "\\s*)?" + ID_RE;

    /**
     * A regular expression that matches an InjectSequence custom injection point
     */
    private static final String CUSTOM_INJECTION_SEQUENCE = "\\s*InjectSequence:\\s*((\\s*" + OPTIONAL_TOPIC_ID_RE + "\\s*," +
            ")*(\\s*" + OPTIONAL_TOPIC_ID_RE + ",?))\\s*";
    public static final RegExp CUSTOM_INJECTION_SEQUENCE_RE = RegExp.compile("<!--" + CUSTOM_INJECTION_SEQUENCE + "-->", "g");
    public static final RegExp DOC_CUSTOM_INJECTION_SEQUENCE_RE = RegExp.compile("^" + CUSTOM_INJECTION_SEQUENCE + "$");

    /**
     * A regular expression that matches an InjectList custom injection point
     */
    private static final String CUSTOM_INJECTION_LIST = "\\s*InjectList:\\s*((\\s*" + OPTIONAL_TOPIC_ID_RE + "\\s*," +
            ")*(\\s*" + OPTIONAL_TOPIC_ID_RE + ",?))\\s*";
    public static final RegExp CUSTOM_INJECTION_LIST_RE = RegExp.compile("<!--" + CUSTOM_INJECTION_LIST + "-->", "g");
    public static final RegExp DOC_CUSTOM_INJECTION_LIST_RE = RegExp.compile("^" + CUSTOM_INJECTION_LIST + "$");

    private static final String CUSTOM_INJECTION_LISTITEMS = "\\s*InjectListItems:\\s*((\\s*" + OPTIONAL_TOPIC_ID_RE + "\\s*," +
            ")*(\\s*" + OPTIONAL_TOPIC_ID_RE + ",?))\\s*";
    public static final RegExp CUSTOM_INJECTION_LISTITEMS_RE = RegExp.compile("<!--" + CUSTOM_INJECTION_LISTITEMS + "-->", "g");
    public static final RegExp DOC_CUSTOM_INJECTION_LISTITEMS_RE = RegExp.compile("^" + CUSTOM_INJECTION_LISTITEMS + "$");

    private static final String CUSTOM_ALPHA_SORT_INJECTION_LIST = "\\s*InjectListAlphaSort:\\s*((\\s*" + OPTIONAL_TOPIC_ID_RE + "\\s*," +
            ")*(\\s*" + OPTIONAL_TOPIC_ID_RE + ",?))\\s*";
    public static final RegExp CUSTOM_ALPHA_SORT_INJECTION_LIST_RE = RegExp.compile("<!--" + CUSTOM_ALPHA_SORT_INJECTION_LIST + "-->", "g");
    public static final RegExp DOC_CUSTOM_ALPHA_SORT_INJECTION_LIST_RE = RegExp.compile("^" + CUSTOM_ALPHA_SORT_INJECTION_LIST + "$");

    /**
     * A regular expression that matches an Inject custom injection point
     */
    private static final String CUSTOM_INJECTION_SINGLE = "\\s*Inject:\\s*(" + OPTIONAL_TOPIC_ID_RE + ")\\s*";
    public static final RegExp CUSTOM_INJECTION_SINGLE_RE = RegExp.compile("<!--" + CUSTOM_INJECTION_SINGLE + "-->", "g");
    public static final RegExp DOC_CUSTOM_INJECTION_SINGLE_RE = RegExp.compile("^" + CUSTOM_INJECTION_SINGLE + "$");

    /**
     * Process a XML Document to resolve any custom injection references so that they can be rendered/validated. This method will
     * transform the injections into links to the editor for each injected topic. It will not make any external calls,
     * so the text displayed will be "Topic &lt;ID&gt;". This also means that any injections that use sorting will not be sorted,
     * since we don't have access to the topic titles.
     *
     * @return The processed XML with the injections resolved.
     */
    public static String resolveInjections(final RESTXMLFormat xmlFormat, final String xml) {
        // Make sure we have something to process.
        if (isStringNullOrEmpty(xml)) {
            return xml;
        }

        final String hostUrl = GWT.getHostPageBaseURL();

        // Process the comments to get the injection references
        final Map<Integer, Map<String, List<InjectionData>>> injections = new HashMap<Integer, Map<String, List<InjectionData>>>();
        processInjections(xml, injections, ORDEREDLIST_INJECTION_POINT, CUSTOM_INJECTION_SEQUENCE_RE);
        processInjections(xml, injections, XREF_INJECTION_POINT, CUSTOM_INJECTION_SINGLE_RE);
        processInjections(xml, injections, ITEMIZEDLIST_INJECTION_POINT, CUSTOM_INJECTION_LIST_RE);
        processInjections(xml, injections, ITEMIZEDLIST_INJECTION_POINT, CUSTOM_ALPHA_SORT_INJECTION_LIST_RE);
        processInjections(xml, injections, LIST_INJECTION_POINT, CUSTOM_INJECTION_LISTITEMS_RE);

        // Now make the custom injection point substitutions
        String fixedXML = xml;
        for (final Map.Entry<Integer, Map<String, List<InjectionData>>> entry : injections.entrySet()) {
            final Integer listType = entry.getKey();
            final Map<String, List<InjectionData>> items = entry.getValue();

            for (final Map.Entry<String, List<InjectionData>> typeEntry : items.entrySet()) {
                final String customInjectionComment = typeEntry.getKey();
                String replacement = null;

                // Generate the dummy injection elements based on the type
                if (typeEntry.getValue() != null && typeEntry.getValue().size() > 0) {
                    if (listType == ORDEREDLIST_INJECTION_POINT) {
                        replacement = createDummyOrderedList(xmlFormat, typeEntry.getValue(), hostUrl);
                    } else if (listType == XREF_INJECTION_POINT) {
                        replacement = createDummyXRef(xmlFormat, typeEntry.getValue().get(0), hostUrl);
                    } else if (listType == ITEMIZEDLIST_INJECTION_POINT) {
                        replacement = createDummyItemizedList(xmlFormat, typeEntry.getValue(), hostUrl);
                    } else if (listType == LIST_INJECTION_POINT) {
                        replacement = createDummyListItems(xmlFormat, typeEntry.getValue(), hostUrl);
                    }
                }

                // Substitute the dummy elements for the injection comment elements
                fixedXML = fixedXML.replace(customInjectionComment, replacement);
            }
        }
        return fixedXML;
    }

    /**
     * Processes a List of Comment elements to get any injection references.
     *
     * @param xml
     * @param injections         A map that will have any processed injections added to.
     * @param injectionPointType The injection type that is being processed.
     * @param regularExpression  The regular expression for the injection type.
     */
    protected static void processInjections(final String xml, final Map<Integer, Map<String, List<InjectionData>>> injections,
            final Integer injectionPointType, final RegExp regularExpression) {

        // Create the mapping if it doesn't exist for the injection type
        if (!injections.containsKey(injectionPointType)) {
            injections.put(injectionPointType, new HashMap<String, List<InjectionData>>());
        }

        // loop over all of the comments that were marked as injections
        for (MatchResult match = regularExpression.exec(xml); match != null; match = regularExpression.exec(xml)) {
            // If a match was found then extract the data
            if (match != null) {
                // Get the list of topics from the named group in the regular expression match
                final String reMatch = match.getGroup(1);

                // Make sure we actually found something
                if (reMatch != null) {
                    // Get the sequence of ids
                    final List<InjectionData> injectionData = processIdList(reMatch);
                    injections.get(injectionPointType).put(match.getGroup(0), injectionData);
                }
            }
        }
    }

    /**
     * Create a dummy xref representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param injectionData      The injected topic information to create the link for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed.
     * @return A List of Elements that make up the injected dummy link.
     */
    protected static String createDummyXRef(final RESTXMLFormat xmlFormat, final InjectionData injectionData, final String hostUrl) {

        final StringBuilder retValue = new StringBuilder();
        if (injectionData.optional) {
            retValue.append("<emphasis>");
            retValue.append(OPTIONAL_LIST_PREFIX);
            retValue.append("</emphasis>");
        }

        final String url, title;
        if (injectionData.id.matches("^\\d+$")) {
            url = hostUrl + "#" + TopicFilteredResultsAndDetailsPresenter.HISTORY_TOKEN + ";query;" +
                    CommonFilterConstants.TOPIC_IDS_FILTER_VAR + "=" + injectionData.id;
            title = "Topic " + injectionData.id;
        } else {
            // TODO need to work out a way to link to a target
            url = hostUrl + "#";
            title = "Target " + injectionData.id;
        }

        // Use a link instead of a xref because the xref target won't exist and therefore won't validate
        if (xmlFormat == RESTXMLFormat.DOCBOOK_50) {
            retValue.append("<link xlink:href=\"");
            retValue.append(url);
            retValue.append("\">");
            retValue.append(title);
            retValue.append("</link>");
        } else {
            retValue.append("<ulink url=\"");
            retValue.append(url);
            retValue.append("\">");
            retValue.append(title);
            retValue.append("</ulink>");
        }

        return retValue.toString();
    }

    /**
     * Create a dummy itemizedlist representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param injectionDatas The list of injected topic information to create the list for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed for each topic.
     * @return The dummy itemized list representation.
     */
    protected static String createDummyItemizedList(final RESTXMLFormat xmlFormat, final List<InjectionData> injectionDatas,
            final String hostUrl) {
        final StringBuilder retValue = new StringBuilder("<para><itemizedlist>");

        retValue.append(createDummyListItems(xmlFormat, injectionDatas, hostUrl));

        retValue.append("</itemizedlist></para>");
        return retValue.toString();
    }

    /**
     * Create a dummy orderedlist representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param injectionDatas The list of injected topic information to create the list for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed for each topic.
     * @return The dummy ordered list representation.
     */
    protected static String createDummyOrderedList(final RESTXMLFormat xmlFormat, final List<InjectionData> injectionDatas,
            final String hostUrl) {
        final StringBuilder retValue = new StringBuilder("<para><orderedlist>");

        retValue.append(createDummyListItems(xmlFormat, injectionDatas, hostUrl));

        retValue.append("</orderedlist></para>");
        return retValue.toString();
    }

    protected static String createDummyListItems(final RESTXMLFormat xmlFormat, final List<InjectionData> injectionDatas,
            final String hostUrl) {
        final StringBuilder retValue = new StringBuilder();

        for (final InjectionData topicData : injectionDatas) {
            retValue.append("<listitem><para>");
            retValue.append(createDummyXRef(xmlFormat, topicData, hostUrl));
            retValue.append("</para></listitem>");
        }

        return retValue.toString();
    }

    /**
     * Process a XML Document to resolve any custom injection references so that they can be rendered/validated. This method will
     * transform the injections into links to the editor for each injected topic. It will not make any external calls,
     * so the text displayed will be "Topic &lt;ID&gt;". This also means that any injections that use sorting will not be sorted,
     * since we don't have access to the topic titles.
     *
     * @param doc The document to be processed.
     */
    public static void resolveInjections(final RESTXMLFormat xmlFormat, final Document doc) {
        // Make sure we have something to process.
        if (doc == null) {
            return;
        }

        final String hostUrl = GWT.getHostPageBaseURL();

        // Find any comments that are injection references
        final List<Comment> injectionComments = new ArrayList<Comment>();
        for (final Comment comment : XMLUtilities.getComments(doc)) {
            if (comment.getNodeValue().matches("^\\s*Inject.*")) {
                injectionComments.add(comment);
            }
        }

        // Process the comments to get the injection references
        final Map<Integer, Map<Comment, List<InjectionData>>> injections = new HashMap<Integer, Map<Comment, List<InjectionData>>>();
        processInjections(injectionComments, injections, ORDEREDLIST_INJECTION_POINT, DOC_CUSTOM_INJECTION_SEQUENCE_RE);
        processInjections(injectionComments, injections, XREF_INJECTION_POINT, DOC_CUSTOM_INJECTION_SINGLE_RE);
        processInjections(injectionComments, injections, ITEMIZEDLIST_INJECTION_POINT, DOC_CUSTOM_INJECTION_LIST_RE);
        processInjections(injectionComments, injections, ITEMIZEDLIST_INJECTION_POINT, DOC_CUSTOM_ALPHA_SORT_INJECTION_LIST_RE);
        processInjections(injectionComments, injections, LIST_INJECTION_POINT, DOC_CUSTOM_INJECTION_LISTITEMS_RE);

        // Now make the custom injection point substitutions
        for (final Map.Entry<Integer, Map<Comment, List<InjectionData>>> entry : injections.entrySet()) {
            final Integer listType = entry.getKey();
            final Map<Comment, List<InjectionData>> items = entry.getValue();

            for (final Map.Entry<Comment, List<InjectionData>> typeEntry : items.entrySet()) {
                final Comment customInjectionCommentNode = typeEntry.getKey();
                List<Element> list = null;

                // Generate the dummy injection elements based on the type
                if (typeEntry.getValue() != null && typeEntry.getValue().size() > 0) {
                    if (listType == ORDEREDLIST_INJECTION_POINT) {
                        list = Arrays.asList(createDummyOrderedList(xmlFormat, doc, typeEntry.getValue(), hostUrl));
                    } else if (listType == XREF_INJECTION_POINT) {
                        list = createDummyXRef(xmlFormat, doc, typeEntry.getValue().get(0), hostUrl);
                    } else if (listType == ITEMIZEDLIST_INJECTION_POINT) {
                        list = Arrays.asList(createDummyItemizedList(xmlFormat, doc, typeEntry.getValue(), hostUrl));
                    } else if (listType == LIST_INJECTION_POINT) {
                        list = createDummyListItems(xmlFormat, doc, typeEntry.getValue(), hostUrl);
                    }
                }

                // Substitute the dummy elements for the injection comment elements
                if (list != null) {
                    for (final Element element : list) {
                        customInjectionCommentNode.getParentNode().insertBefore(element, customInjectionCommentNode);
                    }

                    customInjectionCommentNode.getParentNode().removeChild(customInjectionCommentNode);
                }
            }
        }
    }

    /**
     * Processes a List of Comment elements to get any injection references.
     *
     * @param injectionNodes     A list of Comment elements that are actually injection references
     * @param injections         A map that will have any processed injections added to.
     * @param injectionPointType The injection type that is being processed.
     * @param regularExpression  The regular expression for the injection type.
     */
    protected static void processInjections(final List<Comment> injectionNodes,
            final Map<Integer, Map<Comment, List<InjectionData>>> injections, final Integer injectionPointType,
            final RegExp regularExpression) {

        // Create the mapping if it doesn't exist for the injection type
        if (!injections.containsKey(injectionPointType)) {
            injections.put(injectionPointType, new HashMap<Comment, List<InjectionData>>());
        }

        // loop over all of the comments that were marked as injections
        for (final Comment comment : injectionNodes) {
            final String commentContent = comment.getNodeValue();

            // find any matches
            final MatchResult injectionMatchResult = regularExpression.exec(commentContent);

            // If a match was found then extract the data
            if (injectionMatchResult != null) {
                // Get the list of topics from the named group in the regular expression match
                final String reMatch = injectionMatchResult.getGroup(1);

                // Make sure we actually found something
                if (reMatch != null) {
                    // Get the sequence of ids
                    final List<InjectionData> injectionData = processIdList(reMatch);
                    injections.get(injectionPointType).put(comment, injectionData);
                }
            }
        }
    }

    /**
     * Create a dummy xref representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param doc            The DOM Document the dummy link should be created for.
     * @param injectionData  The injected topic information to create the link for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed.
     * @return A List of Elements that make up the injected dummy link.
     */
    protected static List<Element> createDummyXRef(final RESTXMLFormat xmlFormat, final Document doc,
            final InjectionData injectionData,
            final String hostUrl) {
        final List<Element> retValue = new ArrayList<Element>();

        if (injectionData.optional) {
            final Element emphasis = doc.createElement("emphasis");
            emphasis.appendChild(doc.createTextNode(OPTIONAL_LIST_PREFIX));
            retValue.add(emphasis);
        }

        final String url, title;
        if (injectionData.id.matches("^\\d+$")) {
            url = hostUrl + "#" + TopicFilteredResultsAndDetailsPresenter.HISTORY_TOKEN + ";query;" +
                    CommonFilterConstants.TOPIC_IDS_FILTER_VAR + "=" + injectionData.id;
            title = "Topic " + injectionData.id;
        } else {
            // TODO need to work out a way to link to a target
            url = hostUrl + "#";
            title = "Target " + injectionData.id;
        }

        // Use a link instead of a xref because the xref target won't exist and therefore won't validate
        final Element xRef;
        if (xmlFormat == RESTXMLFormat.DOCBOOK_50) {
            xRef= doc.createElement("link");
            xRef.setAttribute("xlink:href", url);
        } else {
            xRef= doc.createElement("ulink");
            xRef.setAttribute("url", url);
        }
        xRef.appendChild(doc.createTextNode(title));
        retValue.add(xRef);

        return retValue;
    }

    /**
     * Create a dummy itemizedlist representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param doc            The DOM Document the dummy link should be created for.
     * @param injectionDatas The list of injected topic information to create the list for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed for each topic.
     * @return The dummy itemized list representation.
     */
    protected static Element createDummyItemizedList(final RESTXMLFormat xmlFormat, final Document doc,
            final List<InjectionData> injectionDatas, final String hostUrl) {
        final Element para = doc.createElement("para");

        final Element itemizedList = doc.createElement("itemizedlist");
        para.appendChild(itemizedList);

        final List<Element> listItems = createDummyListItems(xmlFormat, doc, injectionDatas, hostUrl);
        for (final Element listItem : listItems) {
            itemizedList.appendChild(listItem);
        }

        return para;
    }

    /**
     * Create a dummy orderedlist representation that can be used for validation/rendering
     *
     * @param xmlFormat
     * @param doc            The DOM Document the dummy link should be created for.
     * @param injectionDatas The list of injected topic information to create the list for.
     * @param hostUrl        The host url of the application, so an editor link can be constructed for each topic.
     * @return The dummy ordered list representation.
     */
    protected static Element createDummyOrderedList(final RESTXMLFormat xmlFormat, final Document doc,
            final List<InjectionData> injectionDatas, final String hostUrl) {
        final Element para = doc.createElement("para");

        final Element orderedList = doc.createElement("orderedlist");
        para.appendChild(orderedList);

        final List<Element> listItems = createDummyListItems(xmlFormat, doc, injectionDatas, hostUrl);
        for (final Element listItem : listItems) {
            orderedList.appendChild(listItem);
        }

        return para;
    }


    protected static List<Element> createDummyListItems(final RESTXMLFormat xmlFormat, final Document doc,
            final List<InjectionData> injectionDatas, final String hostUrl) {
        final List<Element> retValue = new ArrayList<Element>();

        for (final InjectionData topicData : injectionDatas) {
            final Element listitem = doc.createElement("listitem");
            retValue.add(listitem);

            final Element listItemPara = doc.createElement("para");
            listitem.appendChild(listItemPara);

            final List<Element> elements = createDummyXRef(xmlFormat, doc, topicData, hostUrl);
            for (final Element ele : elements) {
                listItemPara.appendChild(ele);
            }
        }

        return retValue;
    }

    /**
     * Takes a comma separated list of optional topic references, and returns an array of InjectionData.
     *
     * @param list
     * @return
     */
    protected static List<InjectionData> processIdList(final String list) {
        // Find the individual topic ids
        final String[] ids = list.split(",");

        final List<InjectionData> retValue = new ArrayList<InjectionData>(ids.length);

        // Clean the topic ids
        for (final String topicId : ids) {
            final String id = topicId.replaceAll(OPTIONAL_MARKER, "").trim();
            final boolean optional = topicId.contains(OPTIONAL_MARKER);

            retValue.add(new InjectionData(id, optional));
        }

        return retValue;
    }

    /**
     * A Class to hold information about a injection reference.
     */
    protected static class InjectionData {
        /**
         * The topic/target ID
         */
        public String id;
        /**
         * whether this topic was marked as optional
         */
        public boolean optional;

        public InjectionData(final String id, final boolean optional) {
            this.id = id;
            this.optional = optional;
        }
    }
}