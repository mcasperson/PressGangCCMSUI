package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.searchresults.topics;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.view.client.HasData;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.dataevents.EntityListReceived;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.filteredresults.BaseFilteredResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.filteredresults.BaseFilteredResultsViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCall;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCallDatabase;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCallBack;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Dependent
public class TopicFilteredResultsPresenter extends BaseFilteredResultsPresenter<RESTTopicCollectionItemV1>
        implements BaseTemplatePresenterInterface {

    public interface Display extends BaseFilteredResultsViewInterface<RESTTopicCollectionItemV1> {
        @NotNull
        PushButton getBulkImport();

        @NotNull
        PushButton getBulkOverwrite();

        @NotNull
        PushButton getAtomFeed();
    }

    public static final String HISTORY_TOKEN = "SearchResultsView";

    @Inject
    private FailOverRESTCall failOverRESTCall;

    @Inject
    private Display display;

    @Nullable
    private String queryString;

    @NotNull
    public Display getDisplay() {
        return display;
    }

    public TopicFilteredResultsPresenter() {

    }

    @Override
    public void parseToken(@NotNull final String searchToken) {
        queryString = removeHistoryToken(searchToken, HISTORY_TOKEN);
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtendedFilteredResults(queryString);
    }

    @Override
    public void close() {

    }

    public void bindExtendedFilteredResults(@Nullable final String queryString) {
        super.bindFilteredResults(queryString, display);
        this.queryString = queryString;

        if (queryString == null) {
            display.setProvider(generateListProvider());
        } else {
            display.setProvider(generateListProvider(queryString, display));
        }
    }

    @NotNull
    @Override
    public String getQuery() {
        return queryString;
    }

    @Override
    protected void displayQueryElements(@Nullable final String queryString) {

    }

    @NotNull
    protected EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> generateListProvider() {
        getProviderData().resetToEmpty();

        @NotNull final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTTopicCollectionItemV1>() {
            @Override
            protected void onRangeChanged(@NotNull final HasData<RESTTopicCollectionItemV1> list) {
                displayNewFixedList(getProviderData().getItems());
            }
        };
        return provider;
    }

    @Nullable
    @Override
    protected EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> generateListProvider(@Nullable final String queryString, @NotNull final BaseTemplateViewInterface waitDisplay) {

        if (queryString == null) {
            return null;
        }

        final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTTopicCollectionItemV1>() {
            @Override
            protected void onRangeChanged(@NotNull final HasData<RESTTopicCollectionItemV1> list) {

                getProviderData().setStartRow(list.getVisibleRange().getStart());
                final int length = list.getVisibleRange().getLength();
                final int end = getProviderData().getStartRow() + length;

                final RESTCallBack<RESTTopicCollectionV1> callback = new RESTCallBack<RESTTopicCollectionV1>() {
                    @Override
                    public void success(@NotNull final RESTTopicCollectionV1 retValue) {
                        try {
                            checkArgument(retValue.getItems() != null, "Returned collection should have a valid items collection.");
                            checkArgument(retValue.getSize() != null, "Returned collection should have a valid size.");

                            getProviderData().setItems(retValue.getItems());
                            getProviderData().setSize(retValue.getSize());
                            relinkSelectedItem();
                            displayAsynchronousList(getProviderData().getItems(), getProviderData().getSize(), getProviderData().getStartRow());
                        } finally {
                            getEventBus().fireEvent(new EntityListReceived<RESTTopicCollectionV1>(retValue));
                        }
                    }
                };

                failOverRESTCall.performRESTCall(FailOverRESTCallDatabase.getTopicsFromQuery(queryString, getProviderData().getStartRow(), end), callback, display);
            }
        };
        return provider;
    }

}
