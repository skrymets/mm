/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package accessories.plugins;

import accessories.plugins.dialogs.ChooseFormatPopupDialog;
import freemind.controller.actions.Pattern;
import freemind.model.MindMapNode;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import java.util.List;

public class ApplyFormatPlugin extends MindMapNodeHookAdapter {

    public ApplyFormatPlugin() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        // we dont need node.
        MindMapNode focussed = getController().getSelected();
        List<MindMapNode> selected = getController().getSelecteds();
        Pattern nodePattern = StylePatternFactory.createPatternFromSelected(
                focussed, selected);
        ChooseFormatPopupDialog formatDialog = new ChooseFormatPopupDialog(
                getController().getFrame().getJFrame(), getMindMapController(),
                "accessories/plugins/ApplyFormatPlugin.dialog.title",
                nodePattern, focussed);
        formatDialog.setModal(true);
        formatDialog.setVisible(true);
        // process result:
        if (formatDialog.getResult() == ChooseFormatPopupDialog.OK) {
            Pattern pattern = formatDialog.getPattern();
            for (MindMapNode node : selected) {
                getMindMapController().applyPattern(node, pattern);
            }
        }
    }

}
