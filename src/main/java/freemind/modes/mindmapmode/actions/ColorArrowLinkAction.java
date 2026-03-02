package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.modes.mindmapmode.MindMapArrowLinkModel;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ColorArrowLinkAction extends MindmapAction {

    final MindMapArrowLinkModel arrowLink;

    private final MindMapController controller;

    public ColorArrowLinkAction(MindMapController controller,
                                MindMapArrowLinkModel arrowLink) {
        super("arrow_link_color", "images/Colors24.gif", controller);
        this.controller = controller;
        this.arrowLink = arrowLink;
    }

    public void actionPerformed(ActionEvent e) {
        Color selectedColor = arrowLink.getColor();
        Color color = Controller.showCommonJColorChooserDialog(controller
                        .getView().getSelectionService().getSelected(), (String) this.getValue(Action.NAME),
                selectedColor);
        if (color == null)
            return;
        controller.setArrowLinkColor(arrowLink, color);
    }

}
