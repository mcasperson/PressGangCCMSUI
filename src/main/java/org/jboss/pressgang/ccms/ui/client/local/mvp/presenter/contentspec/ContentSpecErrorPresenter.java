package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.contentspec;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTextContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Displays the text of a content spec.
 */
@Dependent
public class ContentSpecErrorPresenter extends BaseTemplatePresenter {

    public static final String HISTORY_TOKEN = "ContentSpecTextEditor";

    @Nullable
    private Integer contentSpecId;

    @Inject
    private Display display;

    @Override
    public void parseToken(@NotNull final String historyToken) {
        try {
            contentSpecId = Integer.parseInt(removeHistoryToken(historyToken, HISTORY_TOKEN));
        } catch (@NotNull final Exception ex) {
            // bad history token. silently fail
            contentSpecId = null;
        }
    }

    @Override
    public void bindExtended() {

    }

    @Override
    protected void go() {
        bindExtended();
    }

    @Override
    public void close() {
    }

    @NotNull
    public Display getDisplay() {
        return display;
    }

    public interface Display extends BaseTemplateViewInterface {
        void display(@NotNull final RESTTextContentSpecV1 contentSpec);
    }
}
