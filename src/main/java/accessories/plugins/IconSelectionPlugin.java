/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package accessories.plugins;

import freemind.main.FreeMind;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.model.MindMapNode;
import freemind.modes.IconInformation;
import freemind.modes.common.dialogs.IconSelectionPopupDialog;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.IconAction;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class IconSelectionPlugin extends MindMapNodeHookAdapter {

    public IconSelectionPlugin() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        // we dont need node.
        NodeView focussed = getController().getSelectedView();
        MindMapController controller = getMindMapController();
        List<IconAction> iconActions = controller.getActions().iconActions;
        ArrayList<IconInformation> actions = new ArrayList<>(iconActions);
        actions.add(controller.getActions().removeLastIconAction);
        actions.add(controller.getActions().removeAllIconsAction);

        FreeMind frame = (FreeMind) getController().getFrame();
        IconSelectionPopupDialog selectionDialog = new IconSelectionPopupDialog(frame.getJFrame(), actions, frame);

        final MapView mapView = controller.getView();
        mapView.getScrollService().scrollNodeToVisible(focussed, 0);
        selectionDialog.pack();
        SwingUtils.setDialogLocationRelativeTo(selectionDialog, focussed);
        selectionDialog.setModal(true);
        selectionDialog.setVisible(true);
        // process result:
        int result = selectionDialog.getResult();
        if (result >= 0) {
            Action action = (Action) actions.get(result);
            action.actionPerformed(new ActionEvent(action, 0, "icon",
                    selectionDialog.getModifiers()));
        }
    }

    //    // public void invoke(MindMapNode node) {
    // setNode(node);
    // node.addIcon(icon);
    // nodeChanged(node);
    // }
}
