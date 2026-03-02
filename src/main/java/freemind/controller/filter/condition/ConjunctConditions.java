/*
 * Created on 08.05.2005
 *
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.controller.filter.FilterController;
import freemind.main.FreeMindXml;
import freemind.main.SwingUtils;
import freemind.model.MindMapNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ConjunctConditions implements Condition {

    static final String NAME = "conjunct_condition";

    private final List<Condition> conditions;

    public ConjunctConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        final boolean checkNegative = conditions.stream().anyMatch(condition -> !condition.checkNode(c, node));
        return !checkNegative;
    }

    public JComponent getListCellRendererComponent() {
        JCondition component = new JCondition();
        component.add(new JLabel("("));

        String text = format(" %s ", SwingUtils.removeMnemonic(ConditionFactory.getResources().getResourceString("filter_and")));

        conditions.forEach(condition -> {
            component.add(new JLabel(text));
            JComponent rendererComponent = condition.getListCellRendererComponent();
            rendererComponent.setOpaque(false);
            component.add(rendererComponent);
        });

        component.add(new JLabel(")"));
        return component;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        conditions.forEach(condition -> condition.save(doc, child));
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        final List<Condition> conditions = FreeMindXml.getChildElements(element).stream()
                .map(childEl -> FilterController.getConditionFactory().loadCondition(childEl))
                .collect(toList());

        return new ConjunctConditions(conditions);
    }
}
