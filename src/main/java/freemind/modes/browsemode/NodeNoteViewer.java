package freemind.modes.browsemode;

import freemind.controller.SplitComponentType;
import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import freemind.modes.ModeController.NodeSelectionListener;
import freemind.modes.common.plugins.NodeNoteBase;
import freemind.view.mindmapview.NodeView;

import javax.swing.*;
import java.awt.*;

public class NodeNoteViewer extends NodeNoteBase implements
        NodeSelectionListener {
    private JComponent noteScrollPane;

    private JLabel noteViewer;

    private final ControllerAdapter mBrowseController;

    private static ImageIcon noteIcon = null;

    public NodeNoteViewer(ControllerAdapter pBrowseController) {
        mBrowseController = pBrowseController;
    }

    protected JComponent getNoteViewerComponent(String text) {
        if (noteViewer == null) {
            noteViewer = new JLabel();
            noteViewer.setBackground(Color.WHITE);
            noteViewer.setVerticalAlignment(JLabel.TOP);
            noteViewer.setOpaque(true);
            noteScrollPane = new JScrollPane(noteViewer);
            noteScrollPane.setPreferredSize(new Dimension(1, 200));
        }
        return noteScrollPane;
    }

    public void onLostFocusNode(NodeView pNode) {
        mBrowseController.getController().removeSplitPane(SplitComponentType.NOTE_PANEL);
    }

    public void onFocusNode(NodeView pNode) {
        String noteText = pNode.getModel().getNoteText();
        if (noteText != null && !noteText.isEmpty()) {
            // logger.info("Panel added");
            mBrowseController.getController().insertComponentIntoSplitPane(
                    getNoteViewerComponent(noteText), SplitComponentType.NOTE_PANEL);
            noteViewer.setText(noteText != null ? noteText : "");
        }
    }

    public void onSaveNode(MindMapNode pNode) {
    }

    public void onUpdateNodeHook(MindMapNode pNode) {
        setStateIcon(pNode, true);
    }

    /**
     * Copied from NodeNoteRegistration.
     */
    protected void setStateIcon(MindMapNode node, boolean enabled) {
        // icon
        if (noteIcon == null) {
            noteIcon = freemind.view.ImageFactory.getInstance().createUnscaledIcon(
                    mBrowseController.getResource("images/knotes.png"));
        }
        node.setStateIcon(NODE_NOTE_ICON, (enabled) ? noteIcon : null);
    }

    public void onSelectionChange(NodeView pNode, boolean pIsSelected) {
    }

}
