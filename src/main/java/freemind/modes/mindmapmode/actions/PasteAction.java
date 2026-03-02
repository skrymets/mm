package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.PasteNodeAction;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
@Slf4j
public class PasteAction extends AbstractAction {

    private final MindMapController mMindMapController;

    public PasteAction(MindMapController pMindMapController) {
        super(pMindMapController.getText("paste"),
                freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(pMindMapController.getResource("images/editpaste.png")));
        this.mMindMapController = pMindMapController;

        setEnabled(false);

    }

    public void actionPerformed(ActionEvent e) {
        Transferable clipboardContents = this.mMindMapController
                .getClipboardContents();
        MindMapNode selectedNode = this.mMindMapController.getSelected();
        this.mMindMapController.paste(clipboardContents, selectedNode);
    }

    public Class<PasteNodeAction> getDoActionClass() {
        return PasteNodeAction.class;
    }

}
