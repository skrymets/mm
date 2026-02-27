package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
@Slf4j
public class OpenLinkDirectoryAction extends LinkActionBase {

    public OpenLinkDirectoryAction(MindMapController controller) {
        super("open_link_directory", controller);
    }

    public void actionPerformed(ActionEvent event) {
        String link = "";
        for (MindMapNode selNode : getMindMapController().getSelecteds()) {
            link = selNode.getLink();
            if (link != null) {
                // as link is an URL, '/' is the only correct one.
                final int i = link.lastIndexOf('/');
                if (i >= 0) {
                    link = link.substring(0, i + 1);
                }
                log.info("Opening link for directory {}", link);
                getMindMapController().loadURL(link);
            }
        }
    }
}
