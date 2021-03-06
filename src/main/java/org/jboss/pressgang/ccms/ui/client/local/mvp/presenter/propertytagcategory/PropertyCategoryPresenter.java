package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.propertytagcategory;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.ui.client.local.callbacks.ReadOnlyCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BasePopulatedEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCallDatabase;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCallBack;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.propertycategory.RESTPropertyCategoryV1DetailsEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The presenter for the property tag details view.
 */
@Dependent
public class PropertyCategoryPresenter extends BaseTemplatePresenter {

    // Empty interface declaration, similar to UiBinder
    public interface PropertyCategoryPresenterDriver extends SimpleBeanEditorDriver<RESTPropertyCategoryV1, RESTPropertyCategoryV1DetailsEditor> {
    }

    public interface Display extends BasePopulatedEditorViewInterface<RESTPropertyCategoryV1, RESTPropertyCategoryV1, RESTPropertyCategoryV1DetailsEditor> {

    }

    /**
     * History token.
     */
    public static final String HISTORY_TOKEN = "PropertyCategoryView";

    @Nullable
    private Integer entityId;

    @Inject
    private Display display;

    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void parseToken(@NotNull final String searchToken) {
        try {
            entityId = Integer.parseInt(removeHistoryToken(searchToken, HISTORY_TOKEN));
        } catch (@NotNull final NumberFormatException ex) {
            entityId = null;
        }
    }

    @Override
    protected void go() {
        bindExtended();
    }

    @Override
    public void close() {

    }

    public void bindExtended() {

    }

    /**
     * Get the category from the database and use it to populate the editor in the view
     */
    public void getEntity(@NotNull final Integer entityId) {
        final RESTCallBack<RESTPropertyCategoryV1> callback = new RESTCallBack<RESTPropertyCategoryV1>() {
            @Override
            public void success(@NotNull final RESTPropertyCategoryV1 retValue) {
                isReadOnlyMode(new ReadOnlyCallback() {
                    @Override
                    public void readonlyCallback(boolean readOnly) {
                        display.display(retValue, readOnly);
                    }
                });
            }
        };

        getFailOverRESTCall().performRESTCall(FailOverRESTCallDatabase.getPropertyCategory(entityId), callback, display);
    }
}
