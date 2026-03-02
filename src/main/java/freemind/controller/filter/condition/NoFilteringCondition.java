/*
 * Created on 18.04.2006
 * Created by Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;

public class NoFilteringCondition implements Condition {

    private static String description;
    private static JComponent renderer;
    private static NoFilteringCondition condition;

    private NoFilteringCondition() {
        super();
    }

    public String toString() {
        if (description == null) {
            description = ConditionFactory.getResources().getResourceString(
                    "filter_no_filtering");
        }
        return description;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        return true;
    }

    public JComponent getListCellRendererComponent() {
        if (renderer == null) {
            renderer = new JLabel(description);
        }
        return renderer;
    }

    public static Condition createCondition() {
        if (condition == null) {
            condition = new NoFilteringCondition();
        }
        return condition;
    }

    public void save(Document doc, Element parent) {
    }

}
