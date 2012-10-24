package org.jboss.pressgang.ccms.ui.client.local.mvp.events;

public class CreateTopicViewEvent extends ViewOpenEvent<ViewOpenEventHandler> {
    public static final Type<ViewOpenEventHandler> TYPE = new Type<ViewOpenEventHandler>();

    @Override
    public Type<ViewOpenEventHandler> getAssociatedType() {
        return TYPE;
    }
}