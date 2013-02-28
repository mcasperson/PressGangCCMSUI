package org.jboss.pressgang.ccms.ui.client.local.ui.search.tag;

import com.google.gwt.user.client.ui.TriStateSelectionState;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.entities.*;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.sort.SearchUINameSort;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.SearchViewBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * The REST interface does not define a hierarchy or projects->categories->tags. Instead, tags belong to both categories and
 * projects, but the projects and categories don't have any direct relationship.
 *
 * When being viewed however tags are displayed in the projects->categories->tags hierarchy. This class defines the top level
 * collection of projects.
 *
 * @author Matthew Casperson
 */
public class SearchUIProjects implements SearchViewBase {
    /**
     * A logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SearchUIProjects.class.getName());


    private final LinkedList<SearchUIProject> projects = new LinkedList<SearchUIProject>();


    /**
     * Default constructor. Does nothing.
     */
    public SearchUIProjects() {

    }

    /**
     * @param tags The collection of tags that is used to build the hierarchy of projects, categories and tags
     */
    public SearchUIProjects(final RESTTagCollectionV1 tags) {
        initialize(tags, null);
    }

    /**
     * @param tags   The collection of tags that is used to build the hierarchy of projects, categories and tags
     * @param filter The filter that defines the state of the tags
     */
    public SearchUIProjects(final RESTTagCollectionV1 tags, final RESTFilterV1 filter) {
        initialize(tags, filter);
    }


    /**
     * @return The list of projects
     */
    public final List<SearchUIProject> getProjects() {
        return this.projects;
    }

    /**
     * @param tags   The collection of tags that is used to build the hierarchy of projects, categories and tags
     * @param filter The filter that defines the state of the tags
     */
    public final void initialize(@NotNull final RESTTagCollectionV1 tags, @Nullable final RESTFilterV1 filter) {
        try {
            //LOGGER.log(Level.INFO, "ENTER SearchUIProjects.initialize()");

            this.projects.clear();

            for (final RESTTagCollectionItemV1 tag : tags.returnExistingAndAddedCollectionItems()) {
                checkState(tag.getItem().getProjects() != null, "tag.getItem().getProjects() cannot be null");

                /* Tags to be removed should not show up */
                for (final RESTProjectCollectionItemV1 project : tag.getItem().getProjects().returnExistingCollectionItems()) {
                    final SearchUIProject searchUIProject = new SearchUIProject(project);
                    if (!this.projects.contains(searchUIProject)) {
                        searchUIProject.populateCategories(project, tags, filter);
                        this.projects.add(searchUIProject);
                    }
                }
            }

            Collections.sort(this.projects, new SearchUINameSort());

            /*
             * Add the common project to the start of the list. Do this after all the projects have been added, so it won't get
             * confused with a project that might be called common.
             */
            final SearchUIProject common = new SearchUIProject(PressGangCCMSUI.INSTANCE.Common());
            common.populateCategoriesWithoutProject(tags, filter);
            if (common.getChildCount() != 0) {
                this.projects.addFirst(common);
            }
        } finally {
            //LOGGER.log(Level.INFO, "EXIT SearchUIProjects.initialize()");
        }
    }

    public void populateFilter(@NotNull final RESTFilterV1 filter) {
        // because a tag can be listed under multiple categories with different values,
        // we keep a track of the tags we have processed here
        final List<Integer> processedIds = new ArrayList<Integer>();

        for (final SearchUIProject project : projects) {
            for (final SearchUICategory category : project.getCategories()) {

                final RESTProjectV1 restProject = new RESTProjectV1();
                restProject.setId(project.getId());

                final RESTCategoryV1 restCategory = new RESTCategoryV1();
                restCategory.setId(category.getId());

                final RESTFilterCategoryV1 restFilterCategory = new RESTFilterCategoryV1();
                restFilterCategory.explicitSetProject(restProject);
                restFilterCategory.explicitSetCategory(restCategory);

                /*
                    Add the parameters for the category logic.
                 */
                if (category.isInternalLogicAnd() && category.isInternalLogicAnd() != Constants.DEFAULT_INTERNAL_AND_LOGIC) {
                    /*
                        If the internal "and" logic is specified, and the internal "and" logic is not the default value (i.e. Constants.DEFAULT_INTERNAL_AND_LOGIC is false),
                        then add a query parameter.
                     */
                    restFilterCategory.explicitSetState(CommonFilterConstants.CATEGORY_INTERNAL_AND_STATE);
                    filter.getFilterCategories_OTM().addNewItem(restFilterCategory);
                } else if (category.isInternalLogicOr() && category.isInternalLogicOr() == Constants.DEFAULT_INTERNAL_AND_LOGIC) {
                    /*
                        If the internal "or" logic is specified, and the internal "or" logic is not the default value (i.e. Constants.DEFAULT_INTERNAL_AND_LOGIC is true),
                        then add a query parameter.
                     */
                    restFilterCategory.explicitSetState(CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE);
                    filter.getFilterCategories_OTM().addNewItem(restFilterCategory);
                }

                if (category.isExternalLogicAnd() && category.isExternalLogicAnd() != Constants.DEFAULT_EXTERNAL_AND_LOGIC) {
                    restFilterCategory.explicitSetState(CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE);
                    filter.getFilterCategories_OTM().addNewItem(restFilterCategory);
                } else if (category.isExternalLogicOr() && category.isExternalLogicOr() == Constants.DEFAULT_EXTERNAL_AND_LOGIC) {
                    restFilterCategory.explicitSetState(CommonFilterConstants.CATEGORY_EXTERNAL_OR_STATE);
                    filter.getFilterCategories_OTM().addNewItem(restFilterCategory);
                }

                for (final SearchUITag tag : category.getMyTags()) {
                    if (!processedIds.contains(tag.getTag().getItem().getId())) {
                        if (tag.getState() != TriStateSelectionState.NONE) {
                            final RESTFilterTagV1 filterTag = new RESTFilterTagV1();
                            filterTag.explicitSetTag(tag.getTag().getItem());
                            filterTag.explicitSetState(tag.getState() == TriStateSelectionState.SELECTED ? CommonFilterConstants.MATCH_TAG_STATE : CommonFilterConstants.NOT_MATCH_TAG_STATE);
                            filter.getFilterTags_OTM().addNewItem(filterTag);
                            processedIds.add(tag.getTag().getItem().getId());
                        }
                    }
                }
            }
        }
    }

    @Override
    public final String getSearchQuery(final boolean includeQueryPrefix) {

        final StringBuilder builder = new StringBuilder(includeQueryPrefix ? Constants.QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON : "");

        for (final SearchUIProject project : this.projects) {
            for (final SearchUICategory category : project.getCategories()) {

                /*
                    Add the parameters for the category logic.
                 */
                if (category.isInternalLogicAnd() && category.isInternalLogicAnd() != Constants.DEFAULT_INTERNAL_AND_LOGIC) {
                    /*
                        If the internal "and" logic is specified, and the internal "and" logic is not the default value (i.e. Constants.DEFAULT_INTERNAL_AND_LOGIC is false),
                        then add a query parameter.
                     */
                    builder.append(";").append(CommonFilterConstants.CATEORY_INTERNAL_LOGIC).append(category.getId()).append("=").append(Constants.AND_LOGIC_QUERY_STRING_VALUE);
                } else if (category.isInternalLogicOr() && category.isInternalLogicOr() == Constants.DEFAULT_INTERNAL_AND_LOGIC) {
                    /*
                        If the internal "or" logic is specified, and the internal "or" logic is not the default value (i.e. Constants.DEFAULT_INTERNAL_AND_LOGIC is true),
                        then add a query parameter.
                     */
                    builder.append(";").append(CommonFilterConstants.CATEORY_INTERNAL_LOGIC).append(category.getId()).append("=").append(Constants.OR_LOGIC_QUERY_STRING_VALUE);
                }

                if (category.isExternalLogicAnd() && category.isExternalLogicAnd() != Constants.DEFAULT_EXTERNAL_AND_LOGIC) {
                    builder.append(";").append(CommonFilterConstants.CATEORY_EXTERNAL_LOGIC).append(category.getId()).append("=").append(Constants.AND_LOGIC_QUERY_STRING_VALUE);
                } else if (category.isExternalLogicOr() && category.isExternalLogicOr() == Constants.DEFAULT_EXTERNAL_AND_LOGIC) {
                    builder.append(";").append(CommonFilterConstants.CATEORY_EXTERNAL_LOGIC).append(category.getId()).append("=").append(Constants.OR_LOGIC_QUERY_STRING_VALUE);
                }

                for (final SearchUITag tag : category.getMyTags()) {
                    if (tag.getState() != TriStateSelectionState.NONE) {
                        builder.append(";");

                        if (tag.getState() == TriStateSelectionState.SELECTED) {
                            builder.append(CommonFilterConstants.MATCH_TAG).append(tag.getTag().getItem().getId()).append("=").append(CommonFilterConstants.MATCH_TAG_STATE);
                        } else if (tag.getState() == TriStateSelectionState.UNSELECTED) {
                            builder.append(CommonFilterConstants.MATCH_TAG).append(tag.getTag().getItem().getId()).append("=").append(CommonFilterConstants.NOT_MATCH_TAG_STATE);
                        }
                    }
                }
            }
        }

        return builder.toString();
    }
}
