package accessories.plugins;

import freemind.main.FreeMind;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Shows or hides the attribute table belonging to each node.
 */

@Slf4j
public class NodeAttributeTable extends MindMapNodeHookAdapter {

    public void startupMapHook() {
        super.startupMapHook();
        String foldingType = getResourceString("command");
        // get registration:
        log.info("processing command {}", foldingType);
        if (foldingType.equals("jump")) {
            // jump to the notes:
            getSplitPaneToScreen();
        } else {
            NodeAttributeTableRegistration registration = getRegistration();
            // show hidden window:
            if (!registration.getSplitPaneVisible()) {
                // the window is currently hidden. show it:
                getSplitPaneToScreen();
            } else {
                // it is shown, hide it:
                registration.hideAttributeTablePanel();
                setShowSplitPaneProperty(false);
                getMindMapController().obtainFocusForSelected();
            }
        }
    }

    private NodeAttributeTableRegistration getRegistration() {
        NodeAttributeTableRegistration registration = (NodeAttributeTableRegistration) this
                .getPluginBaseClass();
        return registration;
    }

    private void getSplitPaneToScreen() {
        NodeAttributeTableRegistration registration = getRegistration();
        if (!registration.getSplitPaneVisible()) {
            // the split pane isn't visible. show it.
            registration.showAttributeTablePanel();
            setShowSplitPaneProperty(true);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .clearGlobalFocusOwner();
        // focus table.
        registration.focusAttributeTable();
    }

    private void setShowSplitPaneProperty(boolean pValue) {
        getMindMapController().setProperty(FreeMind.RESOURCES_SHOW_ATTRIBUTE_PANE,
                pValue ? "true" : "false");
    }
}
