package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.contentspec;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.PushButton;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTextContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BasePopulatedEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.EditorSettingsDialog;
import org.jboss.pressgang.ccms.ui.client.local.preferences.Preferences;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.contentspec.RESTTextContentSpecV1TextEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Displays the text of a content spec.
 */
@Dependent
public class ContentSpecPresenter extends BaseTemplatePresenter {

    public static final String HISTORY_TOKEN = "ContentSpecTextEditor";

    @Nullable
    private Integer contentSpecId;

    @Inject
    private Display display;

    @Override
    public void parseToken(@NotNull final String historyToken) {
        try {
            contentSpecId = Integer.parseInt(removeHistoryToken(historyToken, HISTORY_TOKEN));
        } catch (@NotNull final Exception ex) {
            // bad history token. silently fail
            contentSpecId = null;
        }
    }

    @Override
    public void bindExtended() {
        bindEditorSettingsButtons();
    }

    @Override
    protected void go() {
        bindExtended();
    }

    @Override
    public void close() {
    }

    @NotNull
    public Display getDisplay() {
        return display;
    }

    public void loadEditorSettings() {
        display.getEditorSettingsDialog().getLineWrap().setValue(display.getEditor().getUserWrapMode());
        display.getEditorSettingsDialog().getShowInvisibles().setValue(display.getEditor().getShowInvisibles());

        final String theme = Preferences.INSTANCE.getString(Preferences.EDITOR_THEME, Constants.DEFAULT_THEME);
        display.getEditor().setThemeByName(theme);
        for (int i = 0; i < display.getEditorSettingsDialog().getThemes().getItemCount(); ++i) {
            if (display.getEditorSettingsDialog().getThemes().getValue(i) == theme) {
                display.getEditorSettingsDialog().getThemes().setSelectedIndex(i);
                break;
            }
        }

        final String fontSize = Preferences.INSTANCE.getString(Preferences.EDITOR_FONT_SIZE, Constants.DEFAULT_FONT_SIZE);
        display.getEditor().setFontSize(fontSize);
        for (int i = 0; i < display.getEditorSettingsDialog().getFontSizes().getItemCount(); ++i) {
            if (display.getEditorSettingsDialog().getFontSizes().getValue(i) == fontSize) {
                display.getEditorSettingsDialog().getFontSizes().setSelectedIndex(i);
                break;
            }
        }

        final String font = Preferences.INSTANCE.getString(Preferences.EDITOR_FONT, Constants.DEFAULT_MONOSPACED_FONT);
        display.getEditor().setFontFamily(font);
        for (int i = 0; i < display.getEditorSettingsDialog().getFonts().getItemCount(); ++i) {
            if (display.getEditorSettingsDialog().getFonts().getValue(i).equals(font)) {
                display.getEditorSettingsDialog().getFonts().setSelectedIndex(i);
                break;
            }
        }

        final boolean wrap = Preferences.INSTANCE.getBoolean(Preferences.LINE_WRAPPING, true);
        display.getEditor().setUseWrapMode(wrap);
        display.getEditorSettingsDialog().getLineWrap().setValue(wrap);

        final boolean hidden = Preferences.INSTANCE.getBoolean(Preferences.SHOW_HIDDEN_CHARACTERS, false);
        display.getEditor().setShowInvisibles(hidden);
        display.getEditorSettingsDialog().getShowInvisibles().setValue(hidden);

        final boolean behaviours = Preferences.INSTANCE.getBoolean(Preferences.BEHAVIOURS, true);
        getDisplay().getEditor().setBehavioursEnabled(behaviours);
        getDisplay().getEditorSettingsDialog().getBehaviours().setValue(behaviours);

        final boolean autoComplete = Preferences.INSTANCE.getBoolean(Preferences.AUTO_COMPLETE, true);
        getDisplay().getEditor().setAutoCompleteEnabled(autoComplete);
        getDisplay().getEditorSettingsDialog().getAutoComplete().setValue(autoComplete);
    }

    /**
     * Bind the button clicks for the ACE editor buttons
     */
    protected void bindEditorSettingsButtons() {
        getDisplay().getEditorSettingsDialog().getBehaviours().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(@NotNull final ValueChangeEvent<Boolean> event) {
                getDisplay().getEditor().setBehavioursEnabled(event.getValue());
                Preferences.INSTANCE.saveSetting(Preferences.BEHAVIOURS, event.getValue());
            }
        });

        getDisplay().getEditorSettingsDialog().getAutoComplete().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(@NotNull final ValueChangeEvent<Boolean> event) {
                getDisplay().getEditor().setAutoCompleteEnabled(event.getValue());
                Preferences.INSTANCE.saveSetting(Preferences.AUTO_COMPLETE, event.getValue());
            }
        });

        display.getEditorSettingsDialog().getLineWrap().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(@NotNull final ValueChangeEvent<Boolean> event) {
                display.getEditor().setUseWrapMode(event.getValue());
                Preferences.INSTANCE.saveSetting(Preferences.LINE_WRAPPING, event.getValue());
            }
        });

        display.getEditorSettingsDialog().getShowInvisibles().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(@NotNull final ValueChangeEvent<Boolean> event) {
                display.getEditor().setShowInvisibles(event.getValue());
                Preferences.INSTANCE.saveSetting(Preferences.SHOW_HIDDEN_CHARACTERS, event.getValue());
            }
        });

        display.getEditorSettingsDialog().getThemes().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(@NotNull final ChangeEvent event) {
                if (display.getEditor() != null && display.getEditorSettingsDialog().getThemes().getSelectedIndex() != -1) {
                    final String theme = display.getEditorSettingsDialog().getThemes().getValue(
                            display.getEditorSettingsDialog().getThemes().getSelectedIndex());
                    display.getEditor().setThemeByName(theme);
                    Preferences.INSTANCE.saveSetting(Preferences.EDITOR_THEME, theme);
                }
            }
        });

        display.getEditorSettingsDialog().getFontSizes().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (display.getEditor() != null && display.getEditorSettingsDialog().getFontSizes().getSelectedIndex() != -1) {
                    final String fontSize = display.getEditorSettingsDialog().getFontSizes().getValue(
                            display.getEditorSettingsDialog().getFontSizes().getSelectedIndex());
                    display.getEditor().setFontSize(fontSize);
                    Preferences.INSTANCE.saveSetting(Preferences.EDITOR_FONT_SIZE, fontSize);
                }
            }
        });

        display.getEditorSettingsDialog().getFonts().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (display.getEditor() != null && display.getEditorSettingsDialog().getFonts().getSelectedIndex() != -1) {
                    final String font = display.getEditorSettingsDialog().getFonts().getValue(
                            display.getEditorSettingsDialog().getFonts().getSelectedIndex());
                    display.getEditor().setFontFamily(font);
                    Preferences.INSTANCE.saveSetting(Preferences.EDITOR_FONT, font);
                }
            }
        });
    }

    // Empty interface declaration, similar to UiBinder
    public interface ContentSpecTextPresenterDriver extends SimpleBeanEditorDriver<RESTTextContentSpecV1, RESTTextContentSpecV1TextEditor> {
    }

    public interface Display extends BasePopulatedEditorViewInterface<RESTTextContentSpecV1, RESTTextContentSpecV1,
            RESTTextContentSpecV1TextEditor> {
        @NotNull
        AceEditor getEditor();

        @NotNull
        EditorSettingsDialog getEditorSettingsDialog();

        @NotNull
        PushButton getEditorSettings();

        void display(@NotNull final RESTTextContentSpecV1 contentSpec, final boolean readOnly);
    }
}
