package org.jboss.pressgangccms.client.local.ui.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.sort.SearchUINameSort;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;

import com.google.gwt.user.client.ui.TriStateSelectionState;

/**
 * The REST interface does not define a hierarchy or projects->categories->tags.
 * Instead, tags belong to both categories and projects, but the projects and
 * categories don't have any direct relationship.
 * 
 * When being viewed however tags are displayed in the
 * projects->categories->tags hierarchy. This class defines the top level
 * collection of projects.
 * 
 * @author Matthew Casperson
 */
public class SearchUIProjects
{
	private static final String TAG_PREFIX = "tag";
	private static final int TAG_INCLUDED = 1;
	private static final int TAG_EXCLUDED = 0;

	private final LinkedList<SearchUIProject> projects = new LinkedList<SearchUIProject>();

	public List<SearchUIProject> getProjects()
	{
		return projects;
	}

	public SearchUIProjects()
	{

	}

	public SearchUIProjects(final RESTTagCollectionV1 tags)
	{
		initialize(tags);
	}

	public void initialize(final RESTTagCollectionV1 tags)
	{
		if (tags == null)
			throw new NullPointerException("tags parameter cannot be null");
		if (tags.getItems() == null)
			throw new IllegalArgumentException("tags.getItems() cannot be null");

		for (final RESTTagV1 tag : tags.getItems())
		{
			if (tag.getProjects() == null)
				throw new IllegalArgumentException("tag.getProjects() cannot be null");
			if (tag.getProjects().getItems() == null)
				throw new IllegalArgumentException("tag.getProjects().getItems() cannot be null");

			/* Tags to be removed should not show up */
			if (!tag.getRemoveItem())
			{
				for (final RESTProjectV1 project : tag.getProjects().getItems())
				{
					final SearchUIProject searchUIProject = new SearchUIProject(project);
					if (!projects.contains(searchUIProject))
					{
						searchUIProject.populateCategories(project, tags);
						projects.add(searchUIProject);
					}
				}
			}
		}

		Collections.sort(projects, new SearchUINameSort());

		/*
		 * Add the common project to the start of the list. Do this after all
		 * the projects have been added, so it won't get confused with a project
		 * that might be called common.
		 */
		final SearchUIProject common = new SearchUIProject(PressGangCCMSUI.INSTANCE.Common());
		common.populateCategoriesWithoutProject(tags);
		if (common.getChildCount() != 0)
			projects.addFirst(common);
	}

	public String getRESTQueryString()
	{
		final StringBuilder builder = new StringBuilder("query");

		for (final SearchUIProject project : projects)
		{
			for (final SearchUICategory category : project.getCategories())
			{
				for (final SearchUITag tag : category.getMyTags())
				{
					if (tag.getState() != TriStateSelectionState.NONE)
					{
						builder.append(";");

						if (tag.getState() == TriStateSelectionState.SELECTED)
						{
							builder.append(TAG_PREFIX + tag.getTag().getId() + "=" + TAG_INCLUDED);
						}
						else if (tag.getState() == TriStateSelectionState.UNSELECTED)
						{
							builder.append(TAG_PREFIX + tag.getTag().getId() + "=" + TAG_EXCLUDED);
						}
					}
				}
			}
		}

		return builder.toString();
	}
}