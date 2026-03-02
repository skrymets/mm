package accessories.plugins;

import freemind.controller.MenuItemEnabledListener;
import freemind.controller.actions.*;
import freemind.extensions.HookRegistration;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.Tools;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.NodeHookAction;
import freemind.modes.mindmapmode.actions.xml.ActionHandler;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This plugin stores the location of last edit taken place in order to jump to
 * it on keystroke.
 */
@Slf4j
public class JumpLastEditLocation extends MindMapNodeHookAdapter {

    public JumpLastEditLocation() {
    }

    public void invoke(MindMapNode pNode) {
        super.invoke(pNode);
        try {
            JumpLastEditLocationRegistration base = (JumpLastEditLocationRegistration) getPluginBaseClass();
            MindMapNode node = base.getLastEditLocation(pNode);
            if (node == null) {
                return;
            }
            log.trace("Selecting {} as last edit location.", node);
            getMindMapController().select(node, Collections.singletonList(node));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public static class JumpLastEditLocationRegistration implements HookRegistration, ActionHandler, MenuItemEnabledListener {

        private static final String PLUGIN_NAME = "accessories/plugins/JumpLastEditLocation.properties";

        private final MindMapController controller;

        private final List<String> mLastEditLocations = new ArrayList<>();

        public MindMapNode getLastEditLocation(MindMapNode pCurrentNode) {
            int size = mLastEditLocations.size();
            if (size == 0) {
                return null;
            }
            // search for the current node inside the vector:
            String id = controller.getNodeID(pCurrentNode);
            int index = mLastEditLocations.lastIndexOf(id);
            do {
                if (index < 0) {
                    // current node not present, we start with the last position:
                    index = size - 1;
                } else {
                    index = index - 1;
                    if (index < 0) {
                        index = 0;
                    }
                }
                id = mLastEditLocations.get(index);
                try {
                    pCurrentNode = controller.getNodeFromID(id);
                    return pCurrentNode;
                } catch (IllegalArgumentException e) {
                    // node not found, retry...
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            } while (index > 0);
            return null;
        }

        public JumpLastEditLocationRegistration(ModeController controller, MindMap map) {
            this.controller = (MindMapController) controller;
        }

        public void register() {
            controller.getActionRegistry().registerHandler(this);
        }

        public void deRegister() {
            controller.getActionRegistry().deregisterHandler(this);
        }

        public void executeAction(XmlAction action) {
            // detect format changes:
            detectFormatChanges(action);
        }

        private void detectFormatChanges(XmlAction doAction) {
            if (doAction instanceof CompoundAction) {
                CompoundAction compAction = (CompoundAction) doAction;
                List<XmlAction> xmlActions = JIBXGeneratedUtil.listXmlActions(compAction);
                for (XmlAction childAction : xmlActions) {
                    detectFormatChanges(childAction);
                }
            } else if ((doAction instanceof NodeAction)
                    && !(doAction instanceof FoldAction)) {
                // remove myself:
                if (doAction instanceof HookNodeAction) {
                    HookNodeAction hookAction = (HookNodeAction) doAction;
                    if (Objects.equals(hookAction.getHookName(), PLUGIN_NAME)) {
                        return;
                    }
                }
                String lastLocation = ((NodeAction) doAction).getNode();
                if (doAction instanceof NewNodeAction) {
                    NewNodeAction newNodeAction = (NewNodeAction) doAction;
                    lastLocation = newNodeAction.getNewId();
                }
                // prevent double entries
                if (!mLastEditLocations.isEmpty() && Objects.equals(lastLocation, mLastEditLocations.get(mLastEditLocations.size() - 1))) {
                    return;
                }
                mLastEditLocations.add(lastLocation);
                if (mLastEditLocations.size() > 10) {
                    mLastEditLocations.remove(0);
                }
                try {
                    log.trace("New last edit location: {} from {}", lastLocation, controller.marshall(doAction));
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                    log.warn("Not able to marshall the action {} as {}", doAction.getClass(), doAction);
                }
            }

        }

        public void startTransaction(String name) {
        }

        public void endTransaction(String name) {
        }

        public boolean isEnabled(JMenuItem pItem, Action pAction) {
            String hookName = ((NodeHookAction) pAction).getHookName();
            if (PLUGIN_NAME.equals(hookName)) {
                // back is only enabled if there are already some nodes to go
                // back ;-)
                return !mLastEditLocations.isEmpty();
            }
            return true;
        }
    }
}
