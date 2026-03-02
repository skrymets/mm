package freemind.modes.common;

import freemind.model.MindMapNode;
import freemind.modes.ModeController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Follow a graphical link (AKA connector) action.
 */
@SuppressWarnings("serial")
public class GotoLinkNodeAction extends AbstractAction {
    final MindMapNode source;

    private final ModeController controller;

    public GotoLinkNodeAction(ModeController controller, MindMapNode source) {
        super(controller.getText("goto_link_node_action"), freemind.view.ImageFactory.getInstance().createIcon(
                controller.getResource("images/Link.png")));
        this.controller = controller;
        // only display a reasonable part of the string. the rest is available
        // via the short description (tooltip).
        this.source = source;
        // source is for the controllerAdapter == null,
        if (source != null) {
            String adaptedText = source.getShortText(controller);
            putValue(Action.NAME, controller.getText("follow_graphical_link")
                    + adaptedText);
            putValue(Action.SHORT_DESCRIPTION, source.toString());
        }
    }

    public void actionPerformed(ActionEvent e) {
        controller.centerNode(source);
    }

}
