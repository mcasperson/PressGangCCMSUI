package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.blobconstants;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BasePopulatedEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.blobconstant.RESTBlobConstantV1DetailsEditor;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

/**
 * The presenter used to display the blob constants details.
 */
@Dependent
public class BlobConstantPresenter extends BaseTemplatePresenter {

    /**
     * The history token.
     */
    public static final String HISTORY_TOKEN = "BlobConstantView";

    @Inject
    private Display display;

    /**
     * The view this presenter is associated with.
     */
    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {

    }

    @Override
    public void bindExtended() {
        super.bind(display);
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended();
    }

    @Override
    public void close() {

    }

    // Empty interface declaration, similar to UiBinder
    public interface BlobConstantPresenterDriver extends SimpleBeanEditorDriver<RESTBlobConstantV1, RESTBlobConstantV1DetailsEditor> {
    }

    /**
     * The display that this presenter is associated with.
     */
    public interface Display extends BasePopulatedEditorViewInterface<RESTBlobConstantV1, RESTBlobConstantV1, RESTBlobConstantV1DetailsEditor> {
        /**
         * @return The editor that has bound the REST entity to the UI elements displayed by the view.
         */
        RESTBlobConstantV1DetailsEditor getEditor();
    }
}
