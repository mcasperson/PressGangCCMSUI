package org.jboss.pressgang.ccms.ui.client.local.mvp.view.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.search.SearchLocalePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.search.SearchUILocaleEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.locale.SearchUILocales;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import java.util.List;

/**
 * The search locales view.
 */
@Dependent
public class SearchLocalesView extends BaseTemplateView implements SearchLocalePresenter.Display {

    /**
     * The GWT Editor Driver
     */
    private final SearchLocalePresenter.LocalesPresenterDriver driver = GWT.create(SearchLocalePresenter.LocalesPresenterDriver.class);

    private final SearchUILocales searchUILocales = new SearchUILocales();

    public SearchLocalesView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.SearchFields());
    }

    @Override
    public void display(@NotNull final List<String> entity, final boolean readonly) {
        searchUILocales.initialize(entity);

        /* SearchUIProjectsEditor is a grid */
        @NotNull final SearchUILocaleEditor editor = new SearchUILocaleEditor();
        /* Initialize the driver with the top-level editor */
        driver.initialize(editor);
        /* Copy the data in the object into the UI */
        driver.edit(searchUILocales);


        final SimplePanel localesPanel = new SimplePanel();
        localesPanel.addStyleName(CSSConstants.SearchView.LOCALE_PANEL);
        localesPanel.setWidget(editor);

         /* Add the projects */
        this.getPanel().setWidget(localesPanel);
    }

    @Override
    public SimpleBeanEditorDriver<SearchUILocales, SearchUILocaleEditor> getDriver() {
        return driver;
    }

    @NotNull
    @Override
    public SearchUILocales getSearchUILocales() {
        return searchUILocales;
    }
}
