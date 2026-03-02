package freemind.modes.browsemode;

import freemind.main.EncryptionUtils.SingleDesEncrypter;
import freemind.model.MindMap;
import freemind.model.NodeAdapter;
import freemind.modes.MapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.ModeController;
import freemind.modes.common.dialogs.EnterPasswordDialog;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

@Slf4j
public class EncryptedBrowseNode extends BrowseNodeModel {

    private static ImageIcon encryptedIcon;

    private static ImageIcon decryptedIcon;

    private String encryptedContent;

    private boolean isDecrypted = false;

    private final MapFeedback mMapFeedback;

    public EncryptedBrowseNode(MapFeedback pMapFeedback) {
        this(null, pMapFeedback);
    }

    public EncryptedBrowseNode(Object userObject, MapFeedback pMapFeedback) {
        super(userObject, pMapFeedback.getMap());
        this.mMapFeedback = pMapFeedback;
        if (encryptedIcon == null) {
            encryptedIcon = MindIcon.factory("encrypted").getIcon();
        }
        if (decryptedIcon == null) {
            decryptedIcon = MindIcon.factory("decrypted").getIcon();
        }
        updateIcon();
    }

    public void updateIcon() {
        setStateIcon("encryptedNode", (isDecrypted) ? decryptedIcon
                : encryptedIcon);
    }

    public void setFolded(boolean folded) {
        if (isDecrypted || folded) {
            super.setFolded(folded);
            return;
        }
        // get password:
        final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(null,
                mMapFeedback::getResourceString, false);
        pwdDialog.setModal(true);
        pwdDialog.setVisible(true);
        if (pwdDialog.getResult() == EnterPasswordDialog.CANCEL) {
            return;
        }
        SingleDesEncrypter encrypter = new SingleDesEncrypter(
                pwdDialog.getPassword());
        // Decrypt
        String decrypted = encrypter.decrypt(encryptedContent);
        if (decrypted == null)
            return;
        HashMap<String, NodeAdapter> IDToTarget = new HashMap<>();
        String[] childs = decrypted.split(ModeController.NODESEPARATOR);
        // and now? paste it:
        for (int i = childs.length - 1; i >= 0; i--) {
            String string = childs[i];
            log.trace("Decrypted '{}'.", string);
            // if the encrypted node is empty, we skip the insert.
            if (string.isEmpty())
                continue;
            try {
                NodeAdapter node = (NodeAdapter) getMap()
                        .createNodeTreeFromXml(new StringReader(string), IDToTarget);
                // now, the import is finished. We can inform others about
                // the new nodes:
                MindMap model = mMapFeedback.getMap();
                model.insertNodeInto(node, this, this.getChildCount());
                mMapFeedback.invokeHooksRecursively(node, model);
                super.setFolded(folded);
                mMapFeedback.nodeChanged(this);
                //mMapFeedback.nodeStructureChanged(this);
                isDecrypted = true;
                updateIcon();
            } catch (RuntimeException | IOException e) {
                log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
    }

    public void setAdditionalInfo(String info) {
        encryptedContent = info;
        isDecrypted = false;
    }

}
