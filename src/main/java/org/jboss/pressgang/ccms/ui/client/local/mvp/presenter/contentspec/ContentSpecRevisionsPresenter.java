package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.contentspec;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.view.client.HasData;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTextContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.components.ComponentContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTextContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseCustomViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCallDatabase;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCallBack;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jboss.pressgang.mergelygwt.client.Mergely;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Dependent
public class ContentSpecRevisionsPresenter extends BaseTemplatePresenter {

    public interface Display extends BaseTemplateViewInterface, BaseCustomViewInterface<RESTTextContentSpecV1> {

        @NotNull
        EnhancedAsyncDataProvider<RESTTextContentSpecCollectionItemV1> getProvider();

        void setProvider(@NotNull final EnhancedAsyncDataProvider<RESTTextContentSpecCollectionItemV1> provider);

        @NotNull
        CellTable<RESTTextContentSpecCollectionItemV1> getResults();

        @NotNull
        SimplePager getPager();

        @NotNull
        Column<RESTTextContentSpecCollectionItemV1, String> getViewButton();

        @NotNull
        Column<RESTTextContentSpecCollectionItemV1, String> getDiffButton();

        /**
         * @return The currently selected revision content spec.
         */
        @Nullable
        RESTTextContentSpecV1 getRevisionContentSpec();

        /**
         * @param revisionTopic The currently selected revision content spec.
         */
        void setRevisionContentSpec(@Nullable RESTTextContentSpecV1 revisionTopic);

        PushButton getDone();

        PushButton getCancel();

        Mergely getMergely();

        void displayRevisions();

        void displayDiff(String lhs, boolean rhsReadOnly, String rhs);

        boolean isDisplayingRevisions();

        boolean isButtonsEnabled();

        void setButtonsEnabled(boolean buttonsEnabled);
    }

    /**
     * History token
     */
    public static final String HISTORY_TOKEN = "ContentSpecHistoryView";

    @Inject
    private Display display;

    /**
     * Holds the data required to populate and refresh the tags list
     */
    private final ProviderUpdateData<RESTTextContentSpecCollectionItemV1> providerData = new
            ProviderUpdateData<RESTTextContentSpecCollectionItemV1>();

    @NotNull
    public ProviderUpdateData<RESTTextContentSpecCollectionItemV1> getProviderData() {
        return providerData;
    }


    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended();
    }

    @Override
    public void close() {
    }

    public void bindExtended() {
        super.bind(display);
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {

    }

    public EnhancedAsyncDataProvider<RESTTextContentSpecCollectionItemV1> generateListProvider(@NotNull final Integer id,
            @NotNull final BaseTemplateViewInterface waitDisplay) {

        getProviderData().reset();

        final EnhancedAsyncDataProvider<RESTTextContentSpecCollectionItemV1> provider = new
                EnhancedAsyncDataProvider<RESTTextContentSpecCollectionItemV1>() {
            @Override
            protected void onRangeChanged(@NotNull final HasData<RESTTextContentSpecCollectionItemV1> list) {

                final RESTCallBack<RESTTextContentSpecV1> callback = new RESTCallBack<RESTTextContentSpecV1>() {
                    @Override
                    public void success(@NotNull final RESTTextContentSpecV1 retValue) {
                        checkArgument(retValue.getRevisions() != null, "Returned entity should have a valid revisions collection.");
                        checkArgument(retValue.getRevisions().getItems() != null,
                                "Returned entity should have a valid revisions collection with a valid items collection.");
                        checkArgument(retValue.getRevisions().getSize() != null,
                                "Returned entity should have a valid revisions collection with a valid size.");

                        if (retValue.getRevisions().getItems().size() != 0) {
                            checkArgument(retValue.getRevisions().getItems().get(0).getItem().getProperties() != null,
                                    "Returned entities should have a valid properties collection.");
                            checkArgument(retValue.getRevisions().getItems().get(0).getItem().getProperties().getItems() != null,
                                    "Returned entities should have a valid properties collection with a valid items collection.");
                            checkArgument(retValue.getRevisions().getItems().get(0).getItem().getProperties().getSize() != null,
                                    "Returned entities should have a valid properties collection with a valid size.");
                        }

                        // Fix the text
                        for (final RESTTextContentSpecCollectionItemV1 item : retValue.getRevisions().getItems()) {
                            ComponentContentSpecV1.fixDisplayedText(item.getItem());
                        }

                        getProviderData().setItems(retValue.getRevisions().getItems());
                        getProviderData().setSize(retValue.getRevisions().getSize());
                        displayAsynchronousList(getProviderData().getItems(), getProviderData().getSize(), getProviderData().getStartRow());
                    }
                };

                final int start = list.getVisibleRange().getStart();
                getProviderData().setStartRow(start);
                final int length = list.getVisibleRange().getLength();
                final int end = start + length;

                getFailOverRESTCall().performRESTCall(FailOverRESTCallDatabase.getContentSpecWithRevisions(id, start, end), callback, display);
            }
        };
        return provider;
    }
}
