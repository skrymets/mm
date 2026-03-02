/*
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter;

import freemind.controller.Controller;
import freemind.controller.MapModuleChangeObserver;
import freemind.model.Filter;
import freemind.controller.filter.condition.Condition;
import freemind.controller.filter.condition.ConditionFactory;
import freemind.controller.filter.condition.ConditionRenderer;
import freemind.controller.filter.condition.NoFilteringCondition;
import freemind.main.FreeMindXml;

import freemind.model.MindMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import freemind.modes.MindIcon;
import freemind.modes.Mode;
import freemind.modes.common.plugins.NodeNoteBase;
import freemind.view.MapModule;
import lombok.Getter;

import javax.swing.*;
import java.io.*;

public class FilterController implements MapModuleChangeObserver {
    private final Controller c;
    private FilterToolbar filterToolbar;
    @Getter
    private DefaultComboBoxModel<Condition> filterConditionModel;
    static private ConditionRenderer conditionRenderer = null;
    static private ConditionFactory conditionFactory;
    @Getter
    private MindMap map;
    static final String FREEMIND_FILTER_EXTENSION_WITHOUT_DOT = "mmfilter";
    private static Filter inactiveFilter;

    public FilterController(Controller c) {
        this.c = c;
        c.getMapModuleManager().addListener(this);
    }

    ConditionRenderer getConditionRenderer() {
        if (conditionRenderer == null)
            conditionRenderer = new ConditionRenderer();
        return conditionRenderer;
    }

    public FilterToolbar getFilterToolbar() {
        if (filterToolbar == null) {
            ConditionFactory.init(c.getResources());
            filterToolbar = new FilterToolbar(c);
            filterConditionModel = (DefaultComboBoxModel<Condition>) filterToolbar.getFilterConditionModel();

            // FIXME state icons should be created on order to make possible
            // their use in the filter component.
            // It should not happen here.
            MindIcon.factory("AttributeExist", freemind.view.ImageFactory.getInstance().createIcon(
                    c.getResource("images/showAttributes.gif")));
            MindIcon.factory(NodeNoteBase.NODE_NOTE_ICON, freemind.view.ImageFactory.getInstance().createIcon(
                    c.getResource("images/knotes.png")));
            MindIcon.factory("encrypted");
            MindIcon.factory("decrypted");

            filterToolbar.initConditions();
        }
        return filterToolbar;
    }

    public void showFilterToolbar(boolean show) {
        if (show == isVisible())
            return;
        getFilterToolbar().setVisible(show);
        final Filter filter = getMap().getFilter();
        if (show) {
            filter.applyFilter(c);
        } else {
            createTransparentFilter().applyFilter(c);
        }
        refreshMap();
    }

    public boolean isVisible() {
        return getFilterToolbar().isVisible();
    }

    void refreshMap() {
        c.getModeController().refreshMap();
    }

    static public ConditionFactory getConditionFactory() {
        if (conditionFactory == null)
            conditionFactory = new ConditionFactory();
        return conditionFactory;
    }

    public boolean isMapModuleChangeAllowed(MapModule oldMapModule,
                                            Mode oldMode, MapModule newMapModule, Mode newMode) {
        return true;
    }

    public void beforeMapModuleChange(MapModule oldMapModule, Mode oldMode,
                                      MapModule newMapModule, Mode newMode) {
    }

    public void afterMapClose(MapModule pOldMapModule, Mode pOldMode) {
    }

    public void afterMapModuleChange(MapModule oldMapModule, Mode oldMode,
                                     MapModule newMapModule, Mode newMode) {
        MindMap newMap = newMapModule != null ? newMapModule.getModel() : null;
        FilterComposerDialog fd = getFilterToolbar().getFilterDialog();
        if (fd != null) {
            fd.mapChanged(newMap);
        }
        map = newMap;
        getFilterToolbar().mapChanged(newMap);
    }

    public void numberOfOpenMapInformation(int number, int pIndex) {
    }

    private static Filter createTransparentFilter() {
        if (inactiveFilter == null)
            inactiveFilter = new DefaultFilter(
                    NoFilteringCondition.createCondition(), true, false);
        return inactiveFilter;

    }

    public void saveConditions() {
        if (filterToolbar != null) {
            filterToolbar.saveConditions();
        }
    }

    public void setFilterConditionModel(DefaultComboBoxModel<Condition> filterConditionModel) {
        this.filterConditionModel = filterConditionModel;
        filterToolbar.setFilterConditionModel(filterConditionModel);
    }

    void saveConditions(DefaultComboBoxModel<Condition> filterConditionModel, String pathToFilterFile) throws IOException {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("filter_conditions");
        doc.appendChild(root);
        for (int i = 0; i < filterConditionModel.getSize(); i++) {
            Condition cond = filterConditionModel.getElementAt(i);
            cond.save(doc, root);
        }
        try (Writer writer = new FileWriter(pathToFilterFile)) {
            FreeMindXml.write(doc, writer);
        }
    }

    void loadConditions(DefaultComboBoxModel<Condition> filterConditionModel, String pathToFilterFile) throws IOException {
        filterConditionModel.removeAllElements();

        Document doc;
        try (Reader reader = new FileReader(pathToFilterFile)) {
            doc = FreeMindXml.parse(reader);
        }

        Element root = doc.getDocumentElement();
        for (Element condition : FreeMindXml.getChildElements(root)) {
            filterConditionModel.addElement(FilterController.getConditionFactory().loadCondition(condition));
        }
    }
}
