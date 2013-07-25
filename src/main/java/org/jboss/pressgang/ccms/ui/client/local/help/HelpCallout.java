package org.jboss.pressgang.ccms.ui.client.local.help;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.*;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.resources.images.ImageResources;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.UIUtilities;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkState;

/**
 * A callout used by the help overlay
 */
public class HelpCallout extends FlexTable {

    private final Frame iFrame = new Frame();
    private final HTML arrow = new HTML();
    private final PushButton edit = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Edit());
    private final PushButton close = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Close());
    private final Image spinner = new Image(ImageResources.INSTANCE.spinner());
    private final SimplePanel contentParent = new SimplePanel();
    private final SimplePanel hiddenPanel = new SimplePanel();
    private JavaScriptObject listener;


    /**
     * @param helpData The help data that will be used to build this callout.
     */
    public HelpCallout(@NotNull final HelpData helpData) {

        this.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(@NotNull final AttachEvent event) {
                if (isAttached() && listener == null) {
                    createEventListener();
                    addEventListener();
                } else if (!isAttached() && listener != null) {
                    removeListener();
                }
            }
        });

        this.addStyleName(CSSConstants.HelpOverlay.HELP_CALLOUT);

        iFrame.addStyleName(CSSConstants.HelpOverlay.HELP_CALLOUT_IFRAME);
        contentParent.addStyleName(CSSConstants.HelpOverlay.HELP_CALLOUT_CONTENT_PARENT);
        spinner.addStyleName(CSSConstants.HelpOverlay.HELP_CALLOUT_SPINNER);

        final HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.addStyleName(CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT);
        horizontalPanel.setSpacing(2);
        horizontalPanel.add(edit);
        horizontalPanel.add(close);

        if (helpData.getDirection() == 0) {
            this.setWidget(0, 1, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.LEFT_ARROW);
            this.setWidget(0, 0, arrow);

            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

            this.setWidget(1, 1, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 1, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);

        } else if (helpData.getDirection() == 1) {
            this.setWidget(1, 0, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.TOP_ARROW);
            this.setWidget(0, 0, arrow);

            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

            this.setWidget(2, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(2, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);
        } else if (helpData.getDirection() == 2) {
            this.setWidget(0, 0, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.RIGHT_ARROW);
            this.setWidget(0, 1, arrow);

            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);
        }  else if (helpData.getDirection() == 3) {
            this.setWidget(0, 0, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.RIGHT_ARROW);
            this.setWidget(0, 1, arrow);

            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setRowSpan(0, 1, 2);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);


        }  else if (helpData.getDirection() == 4) {
            this.setWidget(0, 0, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.RIGHT_ARROW);
            this.setWidget(1, 1, arrow);

            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_BOTTOM);
            this.getFlexCellFormatter().setRowSpan(0, 1, 2);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);


        } else if (helpData.getDirection() == 5) {
            this.setWidget(0, 0, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.BOTTOM_ARROW);
            this.setWidget(2, 0, arrow);

            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(2, 0, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);
        }  else if (helpData.getDirection() == 6) {
            this.setWidget(0, 1, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.LEFT_ARROW);
            this.setWidget(0, 0, arrow);

            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setRowSpan(0, 0, 2);
            this.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);


        }  else if (helpData.getDirection() == 7) {
            this.setWidget(0, 1, contentParent);
            arrow.addStyleName(CSSConstants.HelpOverlay.LEFT_ARROW);
            this.setWidget(0, 0, arrow);

            this.getFlexCellFormatter().addStyleName(0, 1, CSSConstants.HelpOverlay.CONTENT_CELL);
            this.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.HelpOverlay.ARROW_CELL);
            this.getFlexCellFormatter().setRowSpan(0, 0, 2);
            this.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

            this.setWidget(1, 0, horizontalPanel);
            this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
            this.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.HelpOverlay.CLOSE_AND_EDIT_BUTTONS_PARENT_CELL);
        }

        contentParent.setWidget(spinner);

        /*
            The iframe needs to be attached to the dom to load the XML, but it doesn't
            have to be visible.
         */
        hiddenPanel.setVisible(false);
        this.setWidget(this.getRowCount(), 0, hiddenPanel);
        hiddenPanel.setWidget(iFrame);
    }

    /**
     * Swap the spinner for the iframe.
     */
    private void displayIFrame() {
        contentParent.setWidget(iFrame);
    }

    /**
     * The listener hold a reference to this, which will prevent it from being reclaimed by the GC.
     * So here we remove the listener.
     */
    public native void removeListener() /*-{
        if (this.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::listener != null) {
            $wnd.removeEventListener('message', this.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::listener);
            this.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::listener = null;
        }
    }-*/;

    private native void createEventListener() /*-{
        this.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::listener =
            function (me) {
                return function displayAfterLoaded(event) {
                    // Make sure the iframe sending the data is from an expected source
                    var server = @org.jboss.pressgang.ccms.ui.client.local.server.ServerDetails::getSavedServer()();
                    var serverHost = server.@org.jboss.pressgang.ccms.ui.client.local.server.ServerDetails::getRestUrl()();

                    try {
                        var eventObject = JSON.parse(event.data);

                        if (eventObject.event == 'loaded' && serverHost.indexOf(event.origin) == 0) {
                            me.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::displayIFrame()();
                        }
                    } catch (exception) {
                        // The events just used to be 'loaded', so fall back to that if the JSON parsing failed
                        if (event.data == 'loaded' && serverHost.indexOf(event.origin) == 0) {
                            me.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::displayIFrame()();
                        }
                    }
                };
            }(this);
    }-*/;

    private native void addEventListener() /*-{
        $wnd.addEventListener('message', this.@org.jboss.pressgang.ccms.ui.client.local.help.HelpCallout::listener);
    }-*/;

    public Frame getiFrame() {
        return iFrame;
    }

    public HTML getArrow() {
        return arrow;
    }

    public PushButton getEdit() {
        return edit;
    }

    public PushButton getClose() {
        return close;
    }
}
