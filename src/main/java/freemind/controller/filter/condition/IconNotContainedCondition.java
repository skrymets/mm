/*
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.main.MindMapUtils;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class IconNotContainedCondition implements Condition {
    static final String ICON = "icon";
    static final String NAME = "icon_not_contained_condition";
    private final String iconName;

    public IconNotContainedCondition(String iconName) {
        this.iconName = iconName;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        return iconFirstIndex(node, iconName) == -1 && !isStateIconContained(node, iconName);
    }

    static public int iconFirstIndex(MindMapNode node, String iconName) {
        return MindMapUtils.iconFirstIndex(node, iconName);
    }

    static public int iconLastIndex(MindMapNode node, String iconName) {
        return MindMapUtils.iconLastIndex(node, iconName);
    }

    private static boolean isStateIconContained(MindMapNode node, String iconName) {
        Set<String> stateIcons = node.getStateIcons().keySet();
        for (String nextIcon : stateIcons) {
            if (iconName.equals(nextIcon)) return true;
        }
        return false;
    }

    public JComponent getListCellRendererComponent() {
        JCondition component = new JCondition();
        String text = ConditionFactory.getResources().getResourceString("filter_icon")
                + ' '
                + ConditionFactory.getResources().getResourceString("filter_not_contains")
                + ' ';
        component.add(new JLabel(text));
        component.add(MindIcon.factory(getIconName()).getRendererComponent());
        return component;
    }

    private String getIconName() {
        return iconName;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        child.setAttribute(ICON, iconName);
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        return new IconNotContainedCondition(
                FreeMindXml.getStringAttribute(element, ICON));
    }
}
