/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.Resources;
import freemind.main.XMLElement;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;

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
        List<MindIcon> icons = node.getIcons();
        for (ListIterator<MindIcon> i = icons.listIterator(); i.hasNext(); ) {
            MindIcon nextIcon = i.next();
            if (iconName.equals(nextIcon.getName())) return i.previousIndex();
        }
        return -1;

    }

    static public int iconLastIndex(MindMapNode node, String iconName) {
        List<MindIcon> icons = node.getIcons();
        ListIterator<MindIcon> i = icons.listIterator(icons.size());
        while (i.hasPrevious()) {
            MindIcon nextIcon = (MindIcon) i.previous();
            if (iconName.equals(nextIcon.getName())) return i.nextIndex();
        }
        return -1;

    }

    private static boolean isStateIconContained(MindMapNode node, String iconName) {
        Set<String> stateIcons = node.getStateIcons().keySet();
        for (String nextIcon : stateIcons) {
            if (iconName.equals(nextIcon)) return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public JComponent getListCellRendererComponent() {
        JCondition component = new JCondition();
        String text = Resources.getInstance().getResourceString("filter_icon")
                + ' '
                + Resources.getInstance().getResourceString("filter_not_contains")
                + ' ';
        component.add(new JLabel(text));
        component.add(MindIcon.factory(getIconName()).getRendererComponent());
        return component;
    }

    private String getIconName() {
        return iconName;
    }

    public void save(XMLElement element) {
        XMLElement child = new XMLElement();
        child.setName(NAME);
        child.setAttribute(ICON, iconName);
        element.addChild(child);
    }

    static Condition load(XMLElement element) {
        return new IconNotContainedCondition(
                element.getStringAttribute(ICON));
    }
}
