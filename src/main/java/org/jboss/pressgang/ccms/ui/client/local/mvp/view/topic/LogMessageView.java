package org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic;

import com.google.gwt.user.client.ui.*;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.LogMessageInterface;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.UIUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * The dialog box that presents the list of locales for the user to select from.
 *
 * @author Matthew Casperson
 */
public class LogMessageView extends DialogBox implements LogMessageInterface {

    /**
     * Used to group the radio buttons
     */
    private static final String CHANGE_TYPE_GROUP = "ChangeType";

    private final FlexTable layout = new FlexTable();
    private final RadioButton minorChange = new RadioButton(CHANGE_TYPE_GROUP, "");
    private final RadioButton majorChange = new RadioButton(CHANGE_TYPE_GROUP, "");
    private final TextArea message = new TextArea();
    private final TextBox username = new TextBox();
    private final PushButton ok = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.OK());
    private final PushButton cancel = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Cancel());

    @NotNull
    @Override
    public TextArea getMessage() {
        return message;
    }

    @NotNull
    @Override
    public RadioButton getMajorChange() {
        return majorChange;
    }

    @NotNull
    @Override
    public RadioButton getMinorChange() {
        return minorChange;
    }

    @NotNull
    @Override
    public DialogBox getDialogBox() {
        return this;
    }

    @Override
    public PushButton getCancel() {
        return cancel;
    }

    @Override
    public PushButton getOk() {
        return ok;
    }

    @NotNull
    @Override
    public TextBox getUsername() {
        return username;
    }

    public LogMessageView() {
        message.getElement().setId(Constants.MESSAGE_SAVE_DIALOG);
        username.getElement().setId(Constants.USERNAME_SAVE_DIALOG);
        minorChange.getElement().setId(Constants.MINOR_CHANGE_SAVE_DIALOG);
        majorChange.getElement().setId(Constants.MAJOR_CHANGE_SAVE_DIALOG);
        cancel.getElement().setId(Constants.CANCEL_SAVE_DIALOG);
        ok.getElement().setId(Constants.OK_SAVE_DIALOG);

        this.setGlassEnabled(true);
        this.setText(PressGangCCMSUI.INSTANCE.SaveLog());

        int row = 0;
        layout.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.Message()));
        layout.setWidget(row, 1, message);

        ++row;
        layout.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.Username()));
        layout.setWidget(row, 1, username);

        ++row;
        layout.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.MinorChange()));
        layout.setWidget(row, 1, minorChange);

        ++row;
        layout.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.MajorChange()));
        layout.setWidget(row, 1, majorChange);

        final HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.addStyleName(CSSConstants.Common.DIALOG_BOX_OK_CANCEL_PANEL);
        buttonPanel.add(cancel);
        buttonPanel.add(ok);

        ++row;
        layout.setWidget(row, 0, buttonPanel);
        layout.getFlexCellFormatter().setColSpan(row, 0, 2);

        this.add(layout);

        reset();
    }

    @Override
    public void reset() {
        this.message.setText("");
        this.minorChange.setValue(true);
    }

    @Override
    public void show() {
        super.show();
        message.setFocus(true);
    }
}