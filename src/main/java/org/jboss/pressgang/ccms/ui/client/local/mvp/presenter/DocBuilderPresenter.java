package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

@Dependent
public class DocBuilderPresenter extends BaseTemplatePresenter implements BaseTemplatePresenterInterface {

    public static final String HISTORY_TOKEN = "DocBuilderView";

    public interface Display extends BaseTemplateViewInterface {
        void display(@Nullable final Integer id);
    }

    @Inject
    private Display display;

    private String queryString;
    private Integer id;

    @Override
    public void go(@NotNull final HasWidgets container) {
        display.setViewShown(true);
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended();
    }

    @Override
    public void close() {

    }

    @Override
    public void bindExtended() {
        super.bind(display);
        display.display(id);
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        this.queryString = removeHistoryToken(historyToken, HISTORY_TOKEN);

        try {
            id = Integer.parseInt(this.queryString);
        } catch (@NotNull final NumberFormatException ex) {
            id = null;
        }
    }
}
