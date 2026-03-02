/*
 * Created on 28.03.2004
 *
 */
package accessories.plugins;

import freemind.controller.Controller;
import freemind.controller.MenuItemEnabledListener;
import freemind.extensions.HookRegistration;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.ModeController.NodeSelectionListener;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.NodeHookAction;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import freemind.view.MapModule;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NodeHistory extends MindMapNodeHookAdapter {

    /**
     * Of NodeHolder
     */
    private static final List<NodeHolder> sNodeVector = new ArrayList<>();

    private static int sCurrentPosition = 0;

    private static boolean sPreventRegistration = false;

    private static class NodeHolder {
        public final String mNodeId;

        public String mMapModuleName;

        public NodeHolder(MindMapNode pNode,
                          MindMapController pMindMapController) {
            mNodeId = pNode.getObjectId(pMindMapController);
            MapModule mapModule = pMindMapController.getMapModule();
            if (mapModule == null) {
                throw new IllegalArgumentException(
                        "MapModule not present to controller "
                                + pMindMapController);
            }
            mMapModuleName = mapModule.toString();
        }

        /**
         * @return null, if node not found.
         */
        public MindMapNode getNode(Controller pController) {
            ModeController modeController = getModeController(pController);
            if (modeController != null) {
                return modeController.getNodeFromID(mNodeId);
            }
            return null;
        }

        private ModeController getModeController(Controller pController) {
            ModeController modeController = null;
            MapModule mapModule = getMapModule(pController);
            if (mapModule != null) {
                modeController = mapModule.getModeController();
            }
            return modeController;
        }

        private MapModule getMapModule(Controller pController) {
            MapModule mapModule = null;
            Map<String, MapModule> mapModules = pController.getMapModuleManager().getMapModules();
            for (String mapModuleName : mapModules.keySet()) {
                if (mapModuleName != null
                        && mapModuleName.equals(mMapModuleName)) {
                    mapModule = mapModules.get(mapModuleName);
                    break;
                }
            }
            return mapModule;
        }

        public boolean isIdentical(MindMapNode pNode,
                                   MindMapController pMindMapController) {
            String id = pNode.getObjectId(pMindMapController);
            MapModule mapModule = pMindMapController.getMapModule();
            if (mapModule != null) {
                return id.equals(mNodeId);
            }
            return false;
        }

    }

    public static class Registration implements HookRegistration,
            NodeSelectionListener, MenuItemEnabledListener {

        private final MindMapController controller;

        public Registration(ModeController controller, MindMap map) {
            this.controller = (MindMapController) controller;
        }

        public void register() {
            controller.registerNodeSelectionListener(this, false);
        }

        public void deRegister() {
            controller.deregisterNodeSelectionListener(this);
        }

        public void onLostFocusNode(NodeView pNode) {
        }

        public void onFocusNode(NodeView pNode) {
            /*****************************************************************
              don't denote positions, if somebody navigates through them. *
             */
            if (!NodeHistory.sPreventRegistration) {
                // no duplicates:
                if (sCurrentPosition > 0
                        && sNodeVector.get(sCurrentPosition - 1)
                        .isIdentical(pNode.getModel(), controller)) {
                    // log.info("Avoid duplicate " + pNode + " at " +
                    // sCurrentPosition);
                    return;
                }
                if (sCurrentPosition != sNodeVector.size()) {
                    /*********************************************************
                       we change the selected in the middle of our vector.
                      Thus we remove all the coming nodes:
                     */
                    for (int i = sNodeVector.size() - 1; i >= sCurrentPosition; --i) {
                        sNodeVector.remove(i);
                    }
                }
                // log.info("Adding " + pNode + " at " + sCurrentPosition);
                sNodeVector.add(new NodeHolder(pNode.getModel(), controller));
                sCurrentPosition++;
                // only the last 100 nodes
                while (sNodeVector.size() > 100) {
                    sNodeVector.remove(0);
                    sCurrentPosition--;
                }
            }
        }

        public void onSaveNode(MindMapNode pNode) {
        }

        public void onUpdateNodeHook(MindMapNode pNode) {
        }

        public boolean isEnabled(JMenuItem pItem, Action pAction) {
            String hookName = ((NodeHookAction) pAction).getHookName();
            if ("accessories/plugins/NodeHistoryBack.properties"
                    .equals(hookName)) {
                // back is only enabled if there are already some nodes to go
                // back ;-)
                return sCurrentPosition > 1;
            } else {
                return sCurrentPosition < sNodeVector.size();
            }
        }

        public void onSelectionChange(NodeView pNode, boolean pIsSelected) {
        }

    }

    public NodeHistory() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        final MindMapController modeController = getMindMapController();
        String direction = getResourceString("direction");
        // log.info("Direction: " + direction);
        if ("back".equals(direction)) {
            if (sCurrentPosition > 1) {
                --sCurrentPosition;
            } else {
                return;
            }
        } else {
            if (sCurrentPosition < sNodeVector.size()) {
                ++sCurrentPosition;
            } else {
                return;
            }

        }
        if (sCurrentPosition == 0)
            return;
        // printVector();
        NodeHolder nodeHolder = sNodeVector
                .get(sCurrentPosition - 1);
        final Controller mainController = getController().getController();
        final MindMapNode toBeSelected = (nodeHolder).getNode(mainController);
        boolean changeModule = false;
        MapModule newModule = null;
        if (nodeHolder.getModeController(mainController) != getMindMapController()) {
            changeModule = true;
            newModule = nodeHolder.getMapModule(mainController);
            if (newModule == null) {
                // the map was apparently closed, we try with the next node.
                invoke(node);
                return;
            }
        }
        final boolean fChangeModule = changeModule;
        final MapModule fNewModule = newModule;
        log.trace("Selecting {} at pos {}", toBeSelected, sCurrentPosition);
        sPreventRegistration = true;
        /*********************************************************************
          as the selection is restored after invoke, we make this trick to
          change it.
         */
        EventQueue.invokeLater(() -> {
            ModeController c = modeController;
            if (fChangeModule) {
                boolean res = mainController.getMapModuleManager()
                        .changeToMapModule(fNewModule.toString());
                if (!res) {
                    log.warn("Can't change to map module {}", fNewModule);
                    sPreventRegistration = false;
                    return;
                }
                c = fNewModule.getModeController();
            }
            if (!toBeSelected.isRoot()) {
                c.setFolded(toBeSelected.getParentNode(), false);
            }
            NodeView nodeView = c.getNodeView(toBeSelected);
            if (nodeView != null) {
                c.select(nodeView);
                sPreventRegistration = false;
            }
        });
    }

    private void printVector() {
        StringBuilder sb = new StringBuilder("\n");
        int i = 0;
        for (NodeHolder holder : sNodeVector) {
            sb.append((sCurrentPosition - 1 == i) ? "==>" : "   ").append("Node pos ").append(i).append(" is ").append(holder.getNode(getMindMapController().getController()));
            sb.append("\n");
            i++;
        }
        log.info("{}\n", sb);
    }

}
