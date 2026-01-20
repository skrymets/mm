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
 * Created on 08.05.2005
 *
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.controller.filter.FilterController;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.main.XMLElement;
import freemind.model.MindMapNode;

import javax.swing.*;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author dimitri 08.05.2005
 */
public class DisjunctConditions implements Condition {

    static final String NAME = "disjunct_condition";
    private final List<Condition> conditions;

    public DisjunctConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        final boolean checkNegative = conditions.stream().anyMatch(condition -> !condition.checkNode(c, node));
        return !checkNegative;
    }

    public JComponent getListCellRendererComponent() {
        JCondition component = new JCondition();
        component.add(new JLabel("("));

        String text = format(" %s ", Tools.removeMnemonic(Resources.getInstance().getResourceString("filter_or")));

        conditions.stream().forEachOrdered(condition -> {
            component.add(new JLabel(text));
            JComponent rendererComponent = condition.getListCellRendererComponent();
            rendererComponent.setOpaque(false);
            component.add(rendererComponent);
        });

        component.add(new JLabel(")"));
        return component;
    }

    public void save(XMLElement element) {
        XMLElement child = new XMLElement();
        child.setName(NAME);
        conditions.forEach(condition -> condition.save(child));
        element.addChild(child);
    }

    static Condition load(XMLElement element) {
        final List<Condition> conditions = element.getChildren().stream()
                .map(xmlElement -> FilterController.getConditionFactory().loadCondition(xmlElement))
                .collect(toList());

        return new DisjunctConditions(conditions);
    }
}
