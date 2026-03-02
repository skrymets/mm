/*
 * Created on 19.04.2004
 *
 */
package accessories.plugins;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortNodes extends MindMapNodeHookAdapter {

    private static final class NodeTextComparator implements Comparator<MindMapNode> {
        private boolean mNegative = false;

        public int compare(MindMapNode node1, MindMapNode node2) {

            String nodeText1 = node1.getPlainTextContent();
            String nodeText2 = node2.getPlainTextContent();
            int retValue = nodeText1.compareToIgnoreCase(nodeText2);
            if (mNegative) {
                return -retValue;
            }
            return retValue;

        }

        public void setNegative() {
            mNegative = true;
        }
    }

    public SortNodes() {
        super();
    }

    public void invoke(MindMapNode node) {
        // we want to sort the children of the node:
        // put in all children of the node
        List<MindMapNode> sortVector = new ArrayList<>(node.getChildren());
        NodeTextComparator comparator = new NodeTextComparator();
        MindMapNode last = null;
        boolean isOrdered = true;
        for (MindMapNode listNode : sortVector) {
            if (last != null) {
                if (comparator.compare(listNode, last) < 0) {
                    isOrdered = false;
                    break;
                }
            }
            last = listNode;
        }
        if (isOrdered) {
            comparator.setNegative();
        }
        sortVector.sort(comparator);
        // now, as it is sorted. we cut the children
        for (MindMapNode child : sortVector) {
            List<MindMapNode> childList = Collections.singletonList(child);
            Transferable cut = getMindMapController().cut(childList);
            // paste directly again causes that the node is added as the last
            // one.
            getMindMapController().paste(cut, node);
        }
        getController().select(node, Collections.singletonList(node));
        obtainFocusForSelected();

    }

}
