package org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic;


import com.google.gwt.core.client.GWT;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TranslatedTopicPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TranslatedTopicPresenter.TranslatedTopicPresenterDriver;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview.RESTTranslatedTopicV1BasicDetailsEditor;

/**
 * The view to display translated topics details. Unlike the other views, which are generalized
 * around the RESTBaseTopic class, translated topics and topics display slightly different details
 * (although this should probably be standardized).
 */
public class TranslatedTopicView extends BaseTemplateView implements TranslatedTopicPresenter.Display {

    /**
     * The GWT Editor Driver
     */
    private final TranslatedTopicPresenterDriver driver = GWT.create(TranslatedTopicPresenterDriver.class);

    public TranslatedTopicView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.Properties());

    }

    @Override
    public TranslatedTopicPresenterDriver getDriver() {
        return driver;
    }

    @Override
    public void display(final RESTTranslatedTopicV1 topic, final boolean readOnly) {
                /* SearchUIProjectsEditor is a grid */
        final RESTTranslatedTopicV1BasicDetailsEditor editor = new RESTTranslatedTopicV1BasicDetailsEditor(readOnly);
        /* Initialize the driver with the top-level editor */
        driver.initialize(editor);
        /* Copy the data in the object into the UI */
        driver.edit(topic);
        /* Add the projects */
        this.getPanel().setWidget(editor);
    }

}
