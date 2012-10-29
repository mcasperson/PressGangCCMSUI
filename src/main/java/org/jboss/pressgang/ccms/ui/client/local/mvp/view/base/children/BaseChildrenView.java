package org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.children;

import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBasePrimaryEntityV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.ui.UIUtilities;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.VerticalPanel;

abstract public class BaseChildrenView<T extends RESTBasePrimaryEntityV1, U extends RESTBaseCollectionItemV1> extends BaseTemplateView implements BaseChildrenViewInterface<T, U> {
    /** A reference to the tag that this view will be modifying */
    private T originalEntity;

    private final VerticalPanel possibleChildrenResultsPanel = new VerticalPanel();
    private final SimplePager possibleChildrenPager = UIUtilities.createSimplePager();
    private final CellTable<U> possibleChildrenResults = UIUtilities.<U> createCellTable();
    private EnhancedAsyncDataProvider<U> possibleChildrenProvider;

    @Override
    public T getOriginalEntity() {
        return originalEntity;
    }

    @Override
    public void setOriginalEntity(final T originalEntity) {
        this.originalEntity = originalEntity;
    }

    @Override
    public VerticalPanel getPossibleChildrenResultsPanel() {
        return possibleChildrenResultsPanel;
    }

    @Override
    public SimplePager getPossibleChildrenPager() {
        return possibleChildrenPager;
    }

    @Override
    public CellTable<U> getPossibleChildrenResults() {
        return possibleChildrenResults;
    }

    @Override
    public EnhancedAsyncDataProvider<U> getPossibleChildrenProvider() {
        return possibleChildrenProvider;
    }

    @Override
    public void setPossibleChildrenProvider(final EnhancedAsyncDataProvider<U> possibleChildrenProvider) {
        this.possibleChildrenProvider = possibleChildrenProvider;
        possibleChildrenProvider.addDataDisplay(possibleChildrenResults);
    }
    
    @Override
    public void initialize(final T originalEntity, final boolean readOnly) {
        this.originalEntity = originalEntity;
    }
    
    public BaseChildrenView(final String applicationName, final String pageName) {
        super(applicationName, pageName);
        
        possibleChildrenPager.setDisplay(possibleChildrenResults);

        possibleChildrenResultsPanel.add(possibleChildrenResults);
        possibleChildrenResultsPanel.add(possibleChildrenPager);

        this.getPanel().setWidget(possibleChildrenResultsPanel);
    }
}
