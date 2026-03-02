/*
 * Created on 18.04.2006
 * Created by Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.NodeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;

public class SelectedViewCondition implements Condition {

    private static String description;
    private static JComponent renderer;
    private static Condition condition;

    public SelectedViewCondition() {
        super();
    }

    public String toString() {
        if (description == null) {
            description = ConditionFactory.getResources().getResourceString(
                    "filter_selected_node_view");
        }
        return description;
    }

    public boolean checkNode(Controller c, MindMapNode node) {
        NodeView viewer = c.getModeController().getNodeView(node);
        return viewer != null && viewer.getMap().getSelectionService().isSelected(viewer);
    }

    public JComponent getListCellRendererComponent() {
        if (renderer == null) {
            renderer = ConditionFactory
                    .createCellRendererComponent(description);
        }
        return renderer;
    }

    public static Condition CreateCondition() {
        if (condition == null) {
            condition = new SelectedViewCondition();
        }
        return condition;
    }

    public void save(Document doc, Element parent) {
    }
}
