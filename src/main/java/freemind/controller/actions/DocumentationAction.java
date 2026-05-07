package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.MessageTools;
import freemind.main.UrlTools;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class DocumentationAction extends AbstractAction {
    private final Controller controller;

    public DocumentationAction(Controller controller) {
        super(controller.getResourceString("documentation"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        try {
            String map = controller.getFrame().getResourceString("browsemode_initial_map");
            // if the current language does not provide its own translation,
            // POSTFIX_TRANSLATE_ME is appended:
            map = MessageTools.removeTranslateComment(map);
            final URL endUrl = map != null && map.startsWith(".")
                    ? Controller.localDocumentationLinkConverter.convertLocalLink(map)
                    : UrlTools.fileToUrl(new File(map));
            SwingUtilities.invokeLater(() -> {
                try {
                    controller.getModeController().load(endUrl);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            });
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
