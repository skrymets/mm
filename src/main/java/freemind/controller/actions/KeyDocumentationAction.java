package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.Tools;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

@Slf4j
public class KeyDocumentationAction extends AbstractAction {
    private final Controller controller;

    public KeyDocumentationAction(Controller controller) {
        super(controller.getResourceString("KeyDoc"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        String urlText = controller.getFrame().getResourceString("pdfKeyDocLocation");
        // if the current language does not provide its own translation,
        // POSTFIX_TRANSLATE_ME is appended:
        urlText = Tools.removeTranslateComment(urlText);
        try {
            URL url = urlText != null && urlText.startsWith(".") ? Controller.localDocumentationLinkConverter.convertLocalLink(urlText) : Tools.fileToUrl(new File(urlText));
            log.info("Opening key docs under {}", url);
            controller.getFrame().openDocument(url);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
