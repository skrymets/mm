/*
 * Created on 15.05.2005
 *
 */
package freemind.controller.filter.condition;

import org.w3c.dom.Element;

import javax.swing.*;

public abstract class NodeCondition implements Condition {
    private JComponent renderer;
    private String description;

    protected NodeCondition() {
    }

    public JComponent getListCellRendererComponent() {
        if (renderer == null) {
            renderer = ConditionFactory.createCellRendererComponent(toString());
        }
        return renderer;
    }

    public String toString() {
        if (description == null) {
            description = createDesctiption();
        }
        return description;
    }

    abstract protected String createDesctiption();

    public void saveAttributes(Element child) {
    }

}
