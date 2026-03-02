package freemind.modes.common;

import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.ListIterator;

@SuppressWarnings("serial")
@Slf4j
public class CommonToggleFoldedAction extends AbstractAction {

    private final ControllerAdapter modeController;

    public CommonToggleFoldedAction(ControllerAdapter controller) {
        super(controller.getText("toggle_folded"));
        this.modeController = controller;
    }

    public void actionPerformed(ActionEvent e) {
        toggleFolded();
    }

    public void toggleFolded() {
        toggleFolded(modeController.getSelecteds().listIterator());
    }

    public void toggleFolded(ListIterator listIterator) {
        boolean fold = getFoldingState(reset(listIterator));
        for (Iterator<MindMapNode> i = reset(listIterator); i.hasNext(); ) {
            MindMapNode node = i.next();
            modeController.setFolded(node, fold);
        }
    }

    public static ListIterator reset(ListIterator iterator) {
        while (iterator.hasPrevious()) {
            iterator.previous();
        }
        return iterator;
    }

    /**
     * Determines whether the nodes should be folded or unfolded depending on
     * their states. If not all nodes have the same folding status, the result
     * means folding
     *
     * @param iterator an iterator of MindMapNodes.
     * @return true, if the nodes should be folded.
     */
    public static boolean getFoldingState(ListIterator<MindMapNode> iterator) {
        /*
         * Retrieve the information whether all nodes have the same
         * folding state.
         */
        Boolean state = null;
        boolean allNodeHaveSameFoldedStatus = true;
        for (ListIterator<MindMapNode> it = iterator; it.hasNext(); ) {
            MindMapNode node = it.next();
            if (node.getChildCount() == 0) {
                // no folding state change for unfoldable nodes.
                continue;
            }
            if (state == null) {
                state = node.isFolded();
            } else {
                if (node.isFolded() != state) {
                    allNodeHaveSameFoldedStatus = false;
                    break;
                }
            }
        }
        /* if the folding state is ambiguous, the nodes are folded. */
        boolean fold = true;
        if (allNodeHaveSameFoldedStatus && state != null) {
            fold = !state;
        }
        return fold;
    }

}
