package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class OpenURLAction extends AbstractAction {
    private final Controller controller;
    private final String url;

    public OpenURLAction(Controller controller, String description, String url) {
        super(description, ImageFactory.getInstance().createIcon(
                controller.getResource("images/Link.png")));
        this.controller = controller;
        this.url = url;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            controller.getFrame().openDocument(new URL(url));
        } catch (Exception ex) {
            controller.errorMessage(ex);
        }
    }
}
