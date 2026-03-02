package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.EncryptedMindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
@Slf4j
public class NewChildAction extends MindmapAction {
    private final MindMapController c;

    public NewChildAction(MindMapController modeController) {
        super("new_child", "images/idea.png", modeController);
        this.c = modeController;
    }

    MindMapController getModeController() {
        return c;
    }

    public void actionPerformed(ActionEvent e) {
        this.c.addNew(c.getSelected(), MindMapController.NEW_CHILD, null);
    }

    public MindMapNode addNew(final MindMapNode target, int newNodeMode,
                              final KeyEvent e) {
        final MindMapNode targetNode = target;
        MindMapNode newNode = null;

        switch (newNodeMode) {
            case MindMapController.NEW_SIBLING_BEFORE:
            case MindMapController.NEW_SIBLING_BEHIND: {
                if (!targetNode.isRoot()) {
                    MindMapNode parent = targetNode.getParentNode();
                    int childPosition = parent.getChildPosition(targetNode);
                    if (newNodeMode == MindMapController.NEW_SIBLING_BEHIND) {
                        childPosition++;
                    }
                    newNode = getModeController().addNewNode(parent, childPosition, targetNode.isLeft());
                    final NodeView nodeView = getModeController().getNodeView(newNode);
                    getModeController().select(nodeView);
                    getModeController().getActions().edit.edit(nodeView, getModeController().getNodeView(target), e, true, false,
                            false);
                    break;
                } else {
                    // fc, 21.8.07: we don't do anything here and get a new child
                    // instead.
                    newNodeMode = MindMapController.NEW_CHILD;
                    // @fallthrough
                }
            }

            case MindMapController.NEW_CHILD:
            case MindMapController.NEW_CHILD_WITHOUT_FOCUS: {
                if (targetNode instanceof EncryptedMindMapNode && !((EncryptedMindMapNode) targetNode).isAccessible())
                    break;
                final boolean parentFolded = targetNode.isFolded();
                if (parentFolded) {
                    getModeController().setFolded(targetNode, false);
                }
                int position = c.getProperty("placenewbranches")
                        .equals("last") ? targetNode.getChildCount() : 0;
                newNode = addNewNode(targetNode, position);
                final NodeView nodeView = getModeController().getNodeView(newNode);
                if (newNodeMode == MindMapController.NEW_CHILD) {
                    getModeController().select(nodeView);
                }
                getModeController().getActions().edit.edit(nodeView, getModeController().getNodeView(target), e, true, parentFolded,
                        false);
                break;
            }
        }
        return newNode;
    }

    protected MindMapNode addNewNode(MindMapNode parent, int index) {
        return getModeController().addNewNode(parent, index, parent.isNewChildLeft());
    }

}
