package org.jboss.pressgang.ccms.ui.client.local.data;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONNumber;
import edu.ycp.cs.dh.acegwt.client.tagdb.GetRESTServerCallback;
import edu.ycp.cs.dh.acegwt.client.tagdb.TagDB;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCall;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCallDatabase;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCallBack;
import org.jboss.pressgang.ccms.ui.client.local.server.ServerDetails;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EntityUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * Loads the topics that provide style information for tags.
 */
public class TagDBLoader {
    private final TagDB tagDB = new TagDB(new GetRESTServerCallback() {
        @Override
        public String getBaseRESTURL() {
            return ServerDetails.getSavedServer().getRestEndpoint();
        }
    });

    /**
     * A Logger
     */
    private static final Logger LOGGER = Logger.getLogger(TagDBLoader.class.getName());

    @Inject
    private FailOverRESTCall failOverRESTCall;
    @Inject
    private ServerSettings serverSettings;

    public TagDBLoader() {
        LOGGER.log(Level.INFO, "ENTER TagDBLoader()");
    }

    @PostConstruct
    public void postConstruct() {
        try {
            LOGGER.log(Level.INFO, "ENTER TagDBLoader.postConstruct()");

            final String query = Constants.QUERY_PATH_SEGMENT_PREFIX + CommonFilterConstants.PROPERTY_TAG_EXISTS + serverSettings
                    .getEntities().getTagStylePropertyTagId() + "=true";
            failOverRESTCall.performRESTCall(
                    FailOverRESTCallDatabase.getTopicsWithPropertiesFromQuery(query),
                    new RESTCallBack<RESTTopicCollectionV1>() {
                        public void success(@NotNull final RESTTopicCollectionV1 value) {
                            for (final RESTTopicCollectionItemV1 restTopicV1 : value.getItems())  {
                                final List<RESTAssignedPropertyTagV1> properties = EntityUtilities.returnProperties(restTopicV1.getItem(),
                                        serverSettings.getEntities().getTagStylePropertyTagId());
                                for (final RESTAssignedPropertyTagV1 property : properties) {
                                    if (property.getValue() != null) {
                                        tagDB.getDatabase().put(property.getValue(), new JSONNumber(restTopicV1.getItem().getId()));
                                    }
                                }
                            }

                            /*
                                Let the ACE editor know that the tag database has been loaded.
                             */
                            tagDB.setLoaded(true);
                        }
                    },
                    null,
                    true
            );
        } finally {
            LOGGER.log(Level.INFO, "EXIT TagDBLoader()");
        }
    }

    public TagDB getTagDB() {
        return tagDB;
    }
}
