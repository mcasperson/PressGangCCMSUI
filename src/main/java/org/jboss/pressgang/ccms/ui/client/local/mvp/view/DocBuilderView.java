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

package org.jboss.pressgang.ccms.ui.client.local.mvp.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Frame;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.ui.client.local.callbacks.AllServerDetailsCallback;
import org.jboss.pressgang.ccms.ui.client.local.callbacks.ServerDetailsCallback;
import org.jboss.pressgang.ccms.ui.client.local.callbacks.ServerSettingsCallback;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.DocBuilderPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.server.ServerDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DocBuilderView extends BaseTemplateView implements DocBuilderPresenter.Display {

    @NotNull
    private final Frame iFrame;

    public DocBuilderView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.DocBuilder());
        iFrame = new Frame();
        iFrame.setWidth("100%");
        iFrame.setHeight("100%");
        iFrame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.getPanel().add(iFrame);

        // Hide the action bar since it's not needed
        getTopActionGrandParentPanel().removeFromParent();
    }

    @Override
    protected void initialiseShortcuts() {
        super.initialiseShortcuts();
    }

    public void display(@Nullable final Integer id, @NotNull final String url) {
        String myUrl = url;

        if (id != null) {
            myUrl += "/" + id;
        }

        iFrame.setUrl(myUrl);
    }
}
