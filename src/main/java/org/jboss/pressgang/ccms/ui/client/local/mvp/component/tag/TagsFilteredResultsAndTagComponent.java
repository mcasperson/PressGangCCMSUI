package org.jboss.pressgang.ccms.ui.client.local.mvp.component.tag;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTTagInCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.components.ComponentRESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTTagInCategoryV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.component.base.ComponentBase;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.TagsFilteredResultsAndTagViewEvent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagCategoriesPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagFilteredResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagProjectsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndTagPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.tag.TagViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.preferences.Preferences;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls.RESTCallback;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jboss.pressgang.ccms.ui.client.local.utilities.Holder;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

@Dependent
public class TagsFilteredResultsAndTagComponent extends ComponentBase<TagsFilteredResultsAndTagPresenter.Display> implements
        TagsFilteredResultsAndTagPresenter.LogicComponent {

    @Inject
    private HandlerManager eventBus;
    
    /** The currently displayed view */
    private TagViewInterface displayedView;

    /** The last displayed view */
    private TagViewInterface lastDisplayedView;
    
    private TagFilteredResultsPresenter.Display filteredResultsDisplay;
    private TagFilteredResultsPresenter.LogicComponent filteredResultsComponent;
    private TagPresenter.Display resultDisplay;
    private TagPresenter.LogicComponent resultComponent;
    private TagProjectsPresenter.Display projectsDisplay;
    private TagProjectsPresenter.LogicComponent projectsComponent;
    private TagCategoriesPresenter.Display categoriesDisplay;           
    private TagCategoriesPresenter.LogicComponent categoriesComponent;

    /**
     * A click handler used to display the tag fields view
     */
    private final ClickHandler tagDetailsClickHandler = new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
            displayedView = resultDisplay;
            reInitialiseView();
        }

    };

    /**
     * A click handler used to display the tag projects view
     */
    private final ClickHandler tagProjectsClickHandler = new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
            displayedView = projectsDisplay;
            reInitialiseView();
        }
    };

    /**
     * A click handler used to display the tag categories view
     */
    private final ClickHandler tagCategoriesClickHandler = new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
            displayedView = categoriesDisplay;
            reInitialiseView();
        }
    };

    /**
     * A click handler used to save any changes to the tag
     */
    private final ClickHandler saveClickHandler = new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {

            /* Sync the UI to the underlying object */
            resultDisplay.getDriver().flush();

            final boolean unsavedTagChanges = unsavedTagChanged();
            final boolean unsavedCategoryChanges = categoriesComponent.unsavedCategoryChanges();

            /*
             * We refresh the list of categories when both save operations are complete. To do this we need to know when the
             * final REST call has succeeded, which is noted by these two variables.
             */
            final Holder<Boolean> tagSaveComplete = new Holder<Boolean>(!unsavedTagChanges);
            final Holder<Boolean> categorySaveComplete = new Holder<Boolean>(!unsavedCategoryChanges);

            /* Only attempt to save changes if there are changes to save */
            if (unsavedTagChanged()) {

                /* Save any changes made to the tag entity itself */
                final RESTCalls.RESTCallback<RESTTagV1> callback = new RESTCalls.RESTCallback<RESTTagV1>() {
                    @Override
                    public void begin() {
                        display.addWaitOperation();
                    }

                    @Override
                    public void generalException(final Exception e) {
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                        display.removeWaitOperation();
                        tagSaveComplete.setValue(true);
                    }

                    @Override
                    public void success(final RESTTagV1 retValue) {
                        try {
                            tagSaveComplete.setValue(true);

                            /* we are now viewing the object returned by the save */
                            retValue.cloneInto(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem(), true);

                            /* Update the list of tags with any saved changes */
                            retValue.cloneInto(filteredResultsComponent.getTagProviderData().getSelectedItem().getItem(), true);

                            /* refresh the list of tags */
                            filteredResultsDisplay.getProvider().displayNewFixedList(filteredResultsComponent.getTagProviderData().getItems());

                            /*
                             * Reload the list of categoires and projects if this is the last REST call to succeed
                             */
                            if (categorySaveComplete.getValue())
                                resetCategoryAndProjectsLists(false);

                            /* refresh the display */
                            reInitialiseView();
                        } finally {
                            display.removeWaitOperation();
                        }
                    }

                    @Override
                    public void failed() {
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                        display.removeWaitOperation();
                        tagSaveComplete.setValue(true);
                    }
                };

                /* Sync changes from the tag view */
                final RESTTagV1 updateTag = new RESTTagV1();
                updateTag.setId(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getId());
                updateTag.explicitSetDescription(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getDescription());
                updateTag.explicitSetName(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getName());

                /*
                 * Sync changes from the categories. categoriesComponent.getCategoryProviderData().getItems() contains a collection of all the
                 * categories, and their tags collections contain any added or removed tag relationships. Here we copy those
                 * modified relationships into the updateTag, so the changes are all done in one transaction.
                 */
                if (categoriesComponent.getCategoryProviderData().getItems() != null) {
                    updateTag.explicitSetCategories(new RESTCategoryInTagCollectionV1());
                    for (final RESTCategoryCollectionItemV1 category : categoriesComponent.getCategoryProviderData().getItems()) {
                        for (final RESTTagInCategoryCollectionItemV1 tag : category.getItem().getTags()
                                .returnDeletedAndAddedCollectionItems()) {
                            /*
                             * It should only be possible to add the currently displayed tag to the categories
                             */
                            if (tag.getItem().getId().equals(updateTag.getId())) {

                                final RESTCategoryInTagV1 addedCategory = new RESTCategoryInTagV1();
                                addedCategory.setId(category.getItem().getId());

                                final RESTCategoryInTagCollectionItemV1 collectionItem = new RESTCategoryInTagCollectionItemV1();
                                collectionItem.setState(tag.getState());
                                collectionItem.setItem(addedCategory);

                                updateTag.getCategories().getItems().add(collectionItem);
                            }
                        }
                    }
                }

                /*
                 * Sync changes from the projects.
                 */
                if (projectsComponent.getProjectProviderData().getItems() != null) {
                    updateTag.explicitSetProjects(new RESTProjectCollectionV1());
                    for (final RESTProjectCollectionItemV1 project : projectsComponent.getProjectProviderData().getItems()) {
                        for (final RESTTagCollectionItemV1 tag : project.getItem().getTags()
                                .returnDeletedAndAddedCollectionItems()) {
                            if (tag.getItem().getId().equals(updateTag.getId())) {

                                final RESTProjectV1 addedProject = new RESTProjectV1();
                                addedProject.setId(project.getItem().getId());

                                final RESTProjectCollectionItemV1 collectionItem = new RESTProjectCollectionItemV1();
                                collectionItem.setState(tag.getState());
                                collectionItem.setItem(addedProject);

                                updateTag.getProjects().getItems().add(collectionItem);
                            }
                        }
                    }
                }

                RESTCalls.saveTag(callback, updateTag);
            }

            /* Save the changes to the categories in a separate REST call */
            if (categoriesComponent.unsavedCategoryChanges()) {

                /* Save any changes made to the tag entity itself */
                final RESTCallback<RESTCategoryCollectionV1> callback = new RESTCalls.RESTCallback<RESTCategoryCollectionV1>() {
                    @Override
                    public void begin() {
                        display.addWaitOperation();
                    }

                    @Override
                    public void generalException(final Exception e) {
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                        display.removeWaitOperation();
                        categorySaveComplete.setValue(true);
                    }

                    @Override
                    public void success(final RESTCategoryCollectionV1 retValue) {
                        try {
                            /*
                             * Reload the list of categoires and projects if this is the last REST call to succeed
                             */
                            if (tagSaveComplete.getValue())
                                resetCategoryAndProjectsLists(false);

                            /* refresh the display */
                            reInitialiseView();
                        } finally {
                            display.removeWaitOperation();
                            categorySaveComplete.setValue(true);
                        }

                    }

                    @Override
                    public void failed() {
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                        display.removeWaitOperation();
                        categorySaveComplete.setValue(true);
                    }
                };

                final RESTCategoryCollectionV1 updatedCategories = new RESTCategoryCollectionV1();

                for (final RESTCategoryCollectionItemV1 category : categoriesComponent.getCategoryProviderData().getItems()) {
                    final List<RESTTagInCategoryCollectionItemV1> updatedItems = category.getItem().getTags()
                            .returnUpdatedCollectionItems();

                    /* this should always be greater than 0 */
                    if (updatedItems.size() != 0) {
                        /* Create the category that we are updating */
                        final RESTCategoryV1 updatedCategory = new RESTCategoryV1();
                        updatedCategory.setId(category.getItem().getId());
                        updatedCategory.explicitSetTags(new RESTTagInCategoryCollectionV1());

                        /* Add it to the collection */
                        updatedCategories.addItem(updatedCategory);

                        for (final RESTTagInCategoryCollectionItemV1 tag : updatedItems) {
                            /* create a new tag to represent the one that we are updating */
                            final RESTTagInCategoryV1 updatedTag = new RESTTagInCategoryV1();
                            updatedTag.explicitSetRelationshipId(tag.getItem().getRelationshipId());
                            updatedTag.explicitSetRelationshipSort(tag.getItem().getRelationshipSort());
                            updatedTag.setId(tag.getItem().getId());

                            /* add it to the collection */
                            updatedCategory.getTags().addUpdateItem(updatedTag);
                        }
                    }
                }

                RESTCalls.saveCategories(callback, updatedCategories);
            }
        }
    };

    @Override
    public void bind(final TagsFilteredResultsAndTagPresenter.Display display, BaseTemplateViewInterface waitDisplay,
            final TagFilteredResultsPresenter.Display filteredResultsDisplay,
            final TagFilteredResultsPresenter.LogicComponent filteredResultsComponent,
            final TagPresenter.Display resultDisplay, final TagPresenter.LogicComponent resultComponent,
            final TagProjectsPresenter.Display projectsDisplay, final TagProjectsPresenter.LogicComponent projectsComponent,
            final TagCategoriesPresenter.Display categoriesDisplay,           
            final TagCategoriesPresenter.LogicComponent categoriesComponent ) {
        
        super.bind(display, waitDisplay);
        
        this.filteredResultsDisplay = filteredResultsDisplay;
        this.filteredResultsComponent =filteredResultsComponent;
        this.resultDisplay = resultDisplay;
        this.resultComponent = resultComponent;
        this.projectsDisplay = projectsDisplay;
        this.projectsComponent = projectsComponent;
        this.categoriesDisplay = categoriesDisplay;
        this.categoriesComponent = categoriesComponent;
        
        
        bindTagViewButtons();
        bindSearchButtons();
        bindMainSplitResize();
        bindCategoryColumnButtons();
        bindProjectColumnButtons();
        bindTagListRowClicks();
    }

    /**
     * Binds behaviour to the project list buttons
     */
    private void bindProjectColumnButtons() {
        projectsDisplay.getButtonColumn().setFieldUpdater(new FieldUpdater<RESTProjectCollectionItemV1, String>() {
            @Override
            public void update(final int index, final RESTProjectCollectionItemV1 object, final String value) {
                boolean found = false;

                for (final RESTTagCollectionItemV1 tag : object.getItem().getTags().getItems()) {
                    if (tag.getItem().getId().equals(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getId())) {
                        /* Project was added and then removed */
                        if (tag.getState() == RESTBaseCollectionItemV1.ADD_STATE) {
                            object.getItem().getTags().getItems().remove(tag);
                        }

                        /* Project existed, was removed and then was added again */
                        if (tag.getState() == RESTBaseCollectionItemV1.REMOVE_STATE) {
                            tag.setState(RESTBaseCollectionItemV1.UNCHANGED_STATE);
                        }
                        /* Project existed and was removed */
                        else {
                            tag.setState(RESTBaseCollectionItemV1.REMOVE_STATE);
                        }

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    final RESTTagV1 newTag = filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().clone(true);
                    object.getItem().getTags().addNewItem(newTag);
                }

                /*
                 * In order for the warning to appear if selecting a new tag when unsaved changes exist, we need to set the
                 * configured parameters to reflect the fact that the category contains tags that will modify the database. So
                 * here we check to see if any tags have been added or removed. If there are none (i.e. a tag was added and then
                 * removed again without persisting the change in the database, or there were just no changes made) we remove
                 * the tags collection from the configured parameters.
                 */
                if (object.getItem().getTags().returnDeletedAndAddedCollectionItems().size() != 0) {

                    /*
                     * Need to mark the tags collection as dirty. The explicitSetTags provides a convenient way to set the
                     * appropriate configured parameter value
                     */
                    object.getItem().explicitSetTags(object.getItem().getTags());
                } else {
                    object.getItem().getConfiguredParameters().remove(RESTBaseCategoryV1.TAGS_NAME);
                }

                /* refresh the project list */
                projectsDisplay.getProvider().displayNewFixedList(projectsComponent.getProjectProviderData().getItems());
            }
        });
    }

    /**
     * Binds behaviour to the category list buttons
     */
    private void bindCategoryColumnButtons() {
        categoriesDisplay.getButtonColumn().setFieldUpdater(new FieldUpdater<RESTCategoryCollectionItemV1, String>() {
            @Override
            public void update(final int index, final RESTCategoryCollectionItemV1 object, final String value) {
                boolean found = false;
                for (final RESTTagInCategoryCollectionItemV1 tag : object.getItem().getTags().getItems()) {
                    if (tag.getItem().getId().equals(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getId())) {
                        /* Tag was added and then removed */
                        if (tag.returnIsAddItem()) {
                            object.getItem().getTags().getItems().remove(tag);
                        }
                        /* Tag existed, was removed and then was added again */
                        else if (tag.returnIsRemoveItem()) {
                            tag.setState(RESTBaseCollectionItemV1.UNCHANGED_STATE);
                        }
                        /* Tag existed and was removed */
                        else {
                            tag.setState(RESTBaseCollectionItemV1.REMOVE_STATE);
                            tag.getItem().setRelationshipSort(0);
                        }

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    final RESTTagInCategoryV1 newTag = new RESTTagInCategoryV1();
                    newTag.setId(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getId());
                    newTag.setName(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getName());
                    newTag.setRelationshipSort(0);

                    object.getItem().getTags().addNewItem(newTag);
                }

                /*
                 * In order for the warning to appear if selecting a new tag when unsaved changes exist, we need to set the
                 * configured parameters to reflect the fact that the category contains tags that will modify the database. So
                 * here we check to see if any tags have been added or removed. If there are none (i.e. a tag was added and then
                 * removed again without persisting the change in the database, or there were just no changes made) we remove
                 * the tags collection from the configured parameters.
                 */
                if (object.getItem().getTags().returnDeletedAndAddedCollectionItems().size() != 0) {

                    /*
                     * Need to mark the tags collection as dirty. The explicitSetTags provides a convenient way to set the
                     * appropriate configured parameter value
                     */
                    object.getItem().explicitSetTags(object.getItem().getTags());
                } else {
                    object.getItem().getConfiguredParameters().remove(RESTBaseCategoryV1.TAGS_NAME);
                }

                /* refresh the category list */
                categoriesDisplay.getProvider().displayNewFixedList(categoriesComponent.getCategoryProviderData().getItems());

                /*
                 * refresh the list of tags in the category
                 */
                categoriesDisplay.setTagsProvider(categoriesComponent.generateCategoriesTagListProvider());
            }
        });
    }

    /**
     * Saves the width of the split screen
     */
    private void bindMainSplitResize() {
        display.getSplitPanel().addResizeHandler(new ResizeHandler() {

            @Override
            public void onResize(final ResizeEvent event) {
                Preferences.INSTANCE.saveSetting(Preferences.TAG_CATEGORY_VIEW_MAIN_SPLIT_WIDTH, display.getSplitPanel()
                        .getSplitPosition(display.getResultsPanel()) + "");
            }
        });
    }

    /**
     * Bind the button click events for the topic editor screens
     */
    private void bindTagListRowClicks() {
        filteredResultsDisplay.getResults().addCellPreviewHandler(new Handler<RESTTagCollectionItemV1>() {
            @Override
            public void onCellPreview(final CellPreviewEvent<RESTTagCollectionItemV1> event) {
                /* Check to see if this was a click event */
                final boolean isClick = Constants.JAVASCRIPT_CLICK_EVENT.equals(event.getNativeEvent().getType());

                if (isClick) {
                    if (!checkForUnsavedChanges()) {
                        return;
                    }

                    /* The selected item will be the tag from the list. This is the unedited, unexpanded copy of the tag */
                    filteredResultsComponent.getTagProviderData().setSelectedItem(event.getValue());
                    /* All editing is done in a clone of the selected tag. Any expanded collections will be copied into this tag */
                    filteredResultsComponent.getTagProviderData().setDisplayedItem(event.getValue().clone(true));

                    /*
                     * If this is the first image selected, display the image view
                     */
                    if (displayedView == null) {
                        displayedView = resultDisplay;
                    }

                    resetCategoryAndProjectsLists(true);

                    reInitialiseView();
                }
            }
        });
    }

    /**
     * Compare the displayed tag (the one that is edited) with the selected tag (the one that exists in the collection used to
     * build the tag list). If there are unsaved changes, prompt the user.
     * 
     * @return true if the user wants to ignore the unsaved changes, false otherwise
     */
    public boolean checkForUnsavedChanges() {
        /* sync the UI with the underlying tag */
        if (filteredResultsComponent.getTagProviderData().getDisplayedItem() != null) {
            resultDisplay.getDriver().flush();

            if (unsavedTagChanged() || categoriesComponent.unsavedCategoryChanges() || projectsComponent.unsavedProjectChanges()) {
                return Window.confirm(PressGangCCMSUI.INSTANCE.UnsavedChangesPrompt());
            }
        }

        return true;
    }

    /**
     * 
     * @return true if the tag has any unsaved changes
     */
    public boolean unsavedTagChanged() {
        /*
         * See if any items have been added or removed from the project and category lists
         */
        final boolean unsavedCategoryChanges = categoriesComponent.getCategoryProviderData().getItems() != null
                && ComponentRESTBaseEntityV1.returnDirtyStateForCollectionItems(categoriesComponent.getCategoryProviderData().getItems());
        final boolean unsavedProjectChanges = projectsComponent.getProjectProviderData().getItems() != null
                && ComponentRESTBaseEntityV1.returnDirtyStateForCollectionItems( projectsComponent.getProjectProviderData().getItems());

        /* See if any of the fields were changed */
        final boolean unsavedDescriptionChanges = !GWTUtilities.compareStrings(filteredResultsComponent.getTagProviderData().getSelectedItem().getItem()
                .getDescription(), filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getDescription());
        final boolean unsavedNameChanges = !GWTUtilities.compareStrings(filteredResultsComponent.getTagProviderData().getSelectedItem().getItem().getName(),
                filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getName());

        return unsavedCategoryChanges || unsavedProjectChanges || unsavedDescriptionChanges || unsavedNameChanges;
    }

    /**
     * Bind behaviour to the buttons found in the tag views
     */
    private void bindTagViewButtons() {
        for (final TagViewInterface tagDisplay : new TagViewInterface[] { resultDisplay, projectsDisplay, categoriesDisplay }) {
            tagDisplay.getTagDetails().addClickHandler(tagDetailsClickHandler);
            tagDisplay.getTagProjects().addClickHandler(tagProjectsClickHandler);
            tagDisplay.getSave().addClickHandler(saveClickHandler);
            tagDisplay.getTagCategories().addClickHandler(tagCategoriesClickHandler);
        }
    }

    /**
     * Binds behaviour to the tag search and list view
     */
    private void bindSearchButtons() {
        filteredResultsDisplay.getSearch().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (checkForUnsavedChanges())
                    eventBus.fireEvent(new TagsFilteredResultsAndTagViewEvent(getQuery()));
            }
        });
    }
    
    private String getQuery() {
        final StringBuilder retValue = new StringBuilder(Constants.QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON);
        if (!filteredResultsDisplay.getIdFilter().getText().isEmpty()) {
            retValue.append(";tagIds=" + filteredResultsDisplay.getIdFilter().getText());
        }
        if (!filteredResultsDisplay.getNameFilter().getText().isEmpty()) {
            retValue.append(";tagName=" + filteredResultsDisplay.getNameFilter().getText());
        }
        if (!filteredResultsDisplay.getDescriptionFilter().getText().isEmpty()) {
            retValue.append(";tagDesc=" + filteredResultsDisplay.getDescriptionFilter().getText());
        }

        return retValue.toString();
    }

    /**
     * Called when the selected tag is changed, or the selected view is changed.
     */
    protected void reInitialiseView() {
        /* save any changes as we move between views */
        if (lastDisplayedView == resultDisplay) {
            resultDisplay.getDriver().flush();
        }

        /* Show/Hide any localised loading dialogs */
        if (lastDisplayedView != null)
            lastDisplayedView.setViewShown(false);

        /* update the new view */
        if (displayedView != null) {
            displayedView.initialize(filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem(), false);
            displayedView.setViewShown(true);
        }

        /* refresh the project list */
        if (displayedView == projectsDisplay) {
            /* If we switch to this view before the projects have been downloaded, there is nothing to update */
            if (projectsDisplay.getProvider() != null && projectsComponent.getProjectProviderData().getItems() != null) {
                projectsDisplay.getProvider().displayNewFixedList(projectsComponent.getProjectProviderData().getItems());
            }
        }
        /* refresh the category list */
        else if (displayedView == categoriesDisplay) {
            /* If we switch to this view before the categories have been downloaded, there is nothing to update */
            if (categoriesDisplay.getProvider() != null && categoriesComponent.getCategoryProviderData().getItems() != null) {
                categoriesDisplay.getProvider().displayNewFixedList(categoriesComponent.getCategoryProviderData().getItems());
            }
        }

        /* update the display widgets if we have changed displays */
        if (lastDisplayedView != displayedView) {
            display.getViewPanel().setWidget(displayedView.getPanel());
            display.getViewActionButtonsPanel().setWidget(displayedView.getTopActionPanel());
        }

        /* Update the page name */
        final StringBuilder title = new StringBuilder(displayedView.getPageName());
        if (this.filteredResultsComponent.getTagProviderData().getDisplayedItem() != null)
            title.append(": " + this.filteredResultsComponent.getTagProviderData().getDisplayedItem().getItem().getName());
        display.getPageTitle().setText(title.toString());

        lastDisplayedView = displayedView;
    }

    /**
     * Called when a new tag is selected or the tag is saved. This refreshes the list of categories and projects.
     * 
     * @param removeCatgeoryTagListFromScreen true if the list of tags within a category is to be removed from the screen
     */
    private void resetCategoryAndProjectsLists(final boolean removeCatgeoryTagListFromScreen) {
        /*
         * Reset the category and projects data. This is to clear out any added tags. Maybe cache this info if reloading is too
         * slow.
         */
        categoriesComponent.getCategoryProviderData().reset();
        projectsComponent.getProjectProviderData().reset();

        projectsComponent.getProjects();
        categoriesComponent.getCategories();

        /* remove the category tags list */
        if (removeCatgeoryTagListFromScreen) {
            categoriesComponent.getCategoryProviderData().setSelectedItem(null);
            categoriesComponent.getCategoryProviderData().setDisplayedItem(null);
            categoriesDisplay.getSplit().remove(categoriesDisplay.getTagsResultsPanel());
        }
    }

}
