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

public class ConditionNotSatisfiedDecorator implements Condition {

    static final String NAME = "negate_condition";
    private final Condition originalCondition;

    public ConditionNotSatisfiedDecorator(Condition originalCondition) {
        super();
        this.originalCondition = originalCondition;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        return !originalCondition.checkNode(null, node);
    }

    public JComponent getListCellRendererComponent() {
        JCondition component = new JCondition();
        final String not = SwingUtils.removeMnemonic(ConditionFactory.getResources()
                .getResourceString("filter_not"));
        String text = not + ' ';
        component.add(new JLabel(text));
        final JComponent renderer = originalCondition
                .getListCellRendererComponent();
        renderer.setOpaque(false);
        component.add(renderer);
        return component;
    }

    public void save(Document doc, Element parent) {
        Element child = doc.createElement(NAME);
        originalCondition.save(doc, child);
        parent.appendChild(child);
    }

    static Condition load(Element element) {
        final List<Element> children = FreeMindXml.getChildElements(element);
        Condition cond = FilterController.getConditionFactory().loadCondition(children.get(0));
        return new ConditionNotSatisfiedDecorator(cond);
    }

}
