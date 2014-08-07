/*
 * Copyright 2011-2014 Red Hat, Inc.
 *
 * This file is part of PressGang CCMS.
 *
 * PressGang CCMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PressGang CCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PressGang CCMS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.ui.client.local.callbacks.ServerSettingsCallback;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.FailOverRESTCallDatabase;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCallBack;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Dependent
public class DocBuilderPresenter extends BaseTemplatePresenter implements BaseTemplatePresenterInterface {

    public static final String HISTORY_TOKEN = "DocBuilderView";

    public interface Display extends BaseTemplateViewInterface {
        void display(@Nullable final Integer id, @NotNull final String url);
    }

    @Inject
    private Display display;

    private String queryString;
    private Integer id;

    @Override
    public Display getDisplay() {
        return display;
    }

    @Override
    public void go() {
        display.setViewShown(true);
        bindExtended();
    }

    @Override
    public void close() {
        GWTUtilities.setBrowserWindowTitle(PressGangCCMSUI.INSTANCE.PressGangCCMS());
    }

    @Override
    public void bindExtended() {
        getServerSettings(new ServerSettingsCallback() {
              @Override
              public void serverSettingsLoaded(@NotNull RESTServerSettingsV1 serverSettings) {
                  final String url = serverSettings.getDocBuilderUrl() == null ? Constants.DOCBUILDER_SERVER : serverSettings.getDocBuilderUrl();
                  display.display(id, url);
              }
          });



        loadAdditionalDisplayedItemData(id);
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

    protected void loadAdditionalDisplayedItemData(final Integer id) {
        // Make the window title display the id of the content spec
        if (id != null) {
            /*
            Run an additional query to get the title of the spec
            */
            getFailOverRESTCall().performRESTCall(
                FailOverRESTCallDatabase.getCSNodesWithFromQuery("query;" +
                        CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR + "=" + CommonConstants.CS_NODE_META_DATA + ";" +
                        CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR + "=" + CommonConstants.CS_TITLE_TITLE
                        + ";" +
                        CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR + "=" + id),
                new RESTCallBack<RESTCSNodeCollectionV1>() {
                    @Override
                    public void success(@NotNull final RESTCSNodeCollectionV1 retValue) {
                        checkArgument(retValue.getItems() != null, "The returned collection should have expanded items");

                        /*
                        The query may return title and subtitle
                        */
                        for (final RESTCSNodeCollectionItemV1 node : retValue.getItems()) {
                            if (node.getItem().getTitle().equalsIgnoreCase(CommonConstants.CS_TITLE_TITLE)) {
                                GWTUtilities.setBrowserWindowTitle(node.getItem().getAdditionalText() + " - " +
                                        PressGangCCMSUI.INSTANCE.PressGangCCMS());
                                break;
                            }
                        }
                    }
                }
            );
        }
    }
}
