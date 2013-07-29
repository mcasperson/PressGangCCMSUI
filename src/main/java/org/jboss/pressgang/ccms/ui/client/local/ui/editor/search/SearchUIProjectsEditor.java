package org.jboss.pressgang.ccms.ui.client.local.ui.editor.search;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.search.SearchTagPresenter.Display.SearchPresenterDriver;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUIProject;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUIProjects;
import org.jetbrains.annotations.NotNull;

public class SearchUIProjectsEditor extends DockLayoutPanel implements Editor<SearchUIProjects> {

    private static final int BUTTON_COLUMN_WIDTH = 16;

    private final SearchPresenterDriver driver;
    final SearchUIProjects searchUIProjects;
    private boolean showBulkTags;
    final ListEditor<SearchUIProject, SearchUIProjectEditor> projects = ListEditor.of(new SearchUIProjectEditorSource());
    private final FlexTable projectButtonPanel = new FlexTable();
    private final ScrollPanel scrollPanel = new ScrollPanel();
    private SearchUIProjectEditor selectedProject;

    public boolean isShowBulkTags() {
        return showBulkTags;
    }

    public void setShowBulkTags(boolean showBulkTags) {
        this.showBulkTags = showBulkTags;
    }

    @Ignore
    public FlexTable getProjectButtonPanel() {
        return projectButtonPanel;
    }

    @Ignore
    public SearchUIProjectEditor getSelectedProject() {
        return selectedProject;
    }

    /**
     * The EditorSource is used to create and orgainse the Editors that go into a ListEditor
     *
     * @author Matthew Casperson
     */
    private class SearchUIProjectEditorSource extends EditorSource<SearchUIProjectEditor> {
        @NotNull
        @Override
        public SearchUIProjectEditor create(final int index) {
            final SearchUIProjectEditor subEditor = new SearchUIProjectEditor(driver, searchUIProjects, showBulkTags);
            projectButtonPanel.setWidget(index, 0, subEditor.summary);

            subEditor.summary.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(@NotNull final ClickEvent event) {
                    selectedProject = subEditor;

                    if (SearchUIProjectsEditor.this.getCenter() != null) {
                        SearchUIProjectsEditor.this.remove(SearchUIProjectsEditor.this.getCenter());
                    }
                    SearchUIProjectsEditor.this.add(subEditor);

                    /* Untoggle the other buttons */
                    for (@NotNull final SearchUIProjectEditor projectEditor : projects.getEditors()) {
                        if (projectEditor.summary != subEditor.summary) {
                            projectEditor.summary.removeStyleName(CSSConstants.Common.CUSTOM_BUTTON_DOWN);
                            projectEditor.summary.removeStyleName(CSSConstants.Common.CUSTOM_BUTTON);

                            projectEditor.summary.addStyleName(CSSConstants.Common.CUSTOM_BUTTON);
                        }
                    }
                }
            });

            return subEditor;
        }

        @Override
        public void dispose(@NotNull final SearchUIProjectEditor subEditor) {
            subEditor.summary.removeFromParent();
            subEditor.removeFromParent();
        }

        @Override
        public void setIndex(@NotNull final SearchUIProjectEditor subEditor, final int index) {
            projectButtonPanel.setWidget(index, 0, subEditor);
        }
    }

    public SearchUIProjectsEditor(@NotNull final SearchPresenterDriver driver, @NotNull final SearchUIProjects searchUIProjects) {
        super(Style.Unit.EM);

        this.driver = driver;
        this.searchUIProjects = searchUIProjects;

        this.addStyleName(CSSConstants.TagListProjectsView.PROJECTS_LAYOUT);
        projectButtonPanel.addStyleName(CSSConstants.TagListProjectsView.PROJECTS_BUTTONS_LAYOUT);
        scrollPanel.addStyleName(CSSConstants.TagListProjectsView.PROJECTS_SCROLL_PANEL);

        scrollPanel.setWidget(projectButtonPanel);

        this.addWest(scrollPanel, BUTTON_COLUMN_WIDTH);
    }

}
