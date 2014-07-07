package org.jboss.pressgang.ccms.ui.client.local.ui;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUICategory;
import org.jetbrains.annotations.NotNull;

/**
 * This is a custom button that can be databound to a SearchUIProject.CategorySummary object. This button is used to display a
 * project selection button.
 *
 * @author Matthew Casperson
 */
public final class FourTextAndImageButtonSearchUICategoryEditor extends FourTextAndImageButton implements
        IsEditor<LeafValueEditor<SearchUICategory.TagSummary>> {
    private SearchUICategory.TagSummary details;

    @NotNull
    @Override
    public LeafValueEditor<SearchUICategory.TagSummary> asEditor() {
        return new LeafValueEditor<SearchUICategory.TagSummary>() {
            @Override
            public void setValue(@NotNull final SearchUICategory.TagSummary value) {
                details = value;

                setText(value.getName());
                setText2(PressGangCCMSUI.INSTANCE.TagCount() + ": " + value.getTagCount());
                setText3(PressGangCCMSUI.INSTANCE.Included() + ": " + value.getIncludedTags());
                setText4(PressGangCCMSUI.INSTANCE.Excluded() + ": " + value.getExcludedTags());

                div3.setAttribute("class", value.getIncludedTags() != 0 ? CSSConstants.Common.CUSTOM_BUTTON_TAGS_INCLUDED + ""
                        : CSSConstants.Common.CUSTOM_BUTTON_TEXT);
                div4.setAttribute("class", value.getExcludedTags() != 0 ? CSSConstants.Common.CUSTOM_BUTTON_TAGS_EXCLUDED + ""
                        : CSSConstants.Common.CUSTOM_BUTTON_TEXT);

            }

            @Override
            public SearchUICategory.TagSummary getValue() {
                return details;
            }
        };
    }
}
