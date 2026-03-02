package accessories.plugins;

import freemind.controller.actions.Pattern;
import freemind.model.MindMapNode;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import javax.swing.*;

public class FormatPaste extends MindMapNodeHookAdapter {
    private static Pattern pattern = null;

    public FormatPaste() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        String actionType = getResourceString("actionType");
        if (actionType.equals("copy_format")) {
            copyFormat(node);
        } else {
            pasteFormat(node);
        }
    }

    private void pasteFormat(MindMapNode node) {
        if (pattern == null) {
            JOptionPane.showMessageDialog(getMindMapController().getFrame()
                            .getContentPane(),
                    getResourceString("no_format_copy_before_format_paste"),
                    "" /* =Title */, JOptionPane.ERROR_MESSAGE);
            return;
        }
        getMindMapController().applyPattern(node, pattern);
    }

    private void copyFormat(MindMapNode node) {
        pattern = StylePatternFactory.createPatternFromNode(node);
    }

}
