package org.jboss.pressgang.ccms.ui.client.local.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.TextAndImageButton;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;

public class ThreeTextAndImageButton extends TextAndImageButton {
    private String text2;
    private String text3;
    protected final Element div2 = DOM.createElement("div");
    protected final Element div3 = DOM.createElement("div");

    /**
     * Create the UI element and sets the CSS styles.
     */
    public ThreeTextAndImageButton() {
        super(CSSConstants.Common.CUSTOM_BUTTON_TEXT_BOLD);

        int row = 1;
        div2.setAttribute("class", CSSConstants.Common.CUSTOM_BUTTON_TEXT);
        DOM.insertChild(getElement(), div2, row);

        div3.setAttribute("class", CSSConstants.Common.CUSTOM_BUTTON_TEXT);
        DOM.insertChild(getElement(), div3, ++row);
    }

    /**
     * @param text The text to appear on the second line of the button
     */
    public void setText2(final String text) {
        this.text2 = text;
        div2.setInnerText(text);
    }

    /**
     * @return The text that appears on the second line of the button
     */
    public String getText2() {
        return this.text2;
    }

    /**
     * @param text The text to appear on the third line of the button
     */
    public void setText3(final String text) {
        this.text3 = text;
        div3.setInnerText(text);
    }

    /**
     * @return The text that appears on the third line of the button
     */
    public String getText3() {
        return this.text3;
    }
}
