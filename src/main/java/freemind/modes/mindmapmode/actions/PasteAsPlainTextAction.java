package freemind.modes.mindmapmode.actions;

import freemind.main.HtmlTools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

@SuppressWarnings("serial")
@Slf4j
public class PasteAsPlainTextAction extends AbstractAction {

    private final MindMapController mMindMapController;

    public PasteAsPlainTextAction(MindMapController pMindMapController) {
        super(pMindMapController.getText("paste_as_plain_text"), null);
        this.mMindMapController = pMindMapController;

        setEnabled(false);
    }

    public void actionPerformed(ActionEvent pArg0) {
        Transferable clipboardContents = mMindMapController
                .getClipboardContents();
        // test for plain text support
        if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String plainText = (String) clipboardContents.getTransferData(DataFlavor.stringFlavor);
                // sometimes these (for XML illegal) characters occur
                plainText = HtmlTools.makeValidXml(plainText);
                log.info("Pasting string {}", plainText);
                // paste.
                MindMapNode selected = mMindMapController.getSelected();
                MindMapNode newNode = mMindMapController.addNewNode(selected, selected.getChildCount(), selected.isLeft());
                mMindMapController.setNodeText(newNode, plainText);
            } catch (UnsupportedFlavorException | IOException e) {
                log.error(e.getLocalizedMessage(), e);

            }
        } else {
            // not supported message.
            log.warn("String flavor not supported for transferable {}", clipboardContents);
        }
    }

}
