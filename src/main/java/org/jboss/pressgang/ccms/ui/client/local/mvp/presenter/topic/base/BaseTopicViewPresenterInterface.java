package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.base;

import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.PresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.BaseTopicViewInterface;

public interface BaseTopicViewPresenterInterface extends PresenterInterface {
    /**
     * Get and display the entity
     * 
     * @param entityId The entity primary key
     * @param display the display used to display the wait message
     */
    void getEntity(final Integer entityId, final BaseTopicViewInterface display);
}
