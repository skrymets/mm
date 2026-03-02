package freemind.modes.browsemode;

import freemind.modes.ControllerAdapter;
import freemind.modes.common.dialogs.PersistentEditableComboBox;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.net.URL;

@Slf4j
public class BrowseToolBar extends JToolBar {

    public static final String BROWSE_URL_STORAGE_KEY = "browse_url_storage";

    private final ControllerAdapter c;
    private PersistentEditableComboBox urlfield = null;

    public BrowseToolBar(ControllerAdapter controller) {

        this.c = controller;
        this.setRollover(true);
        this.add(controller.getController().showFilterToolbarAction);
        urlfield = new PersistentEditableComboBox(controller,
                BROWSE_URL_STORAGE_KEY);

        urlfield.addActionListener(e -> {
            String urlText = urlfield.getText();
            if ("".equals(urlText)
                    || e.getActionCommand().equals("comboBoxEdited"))
                return;
            try {
                c.load(new URL(urlText));
            } catch (Exception e1) {
                log.error(e1.getLocalizedMessage(), e1);
                c.getController().errorMessage(e1);
            }
        });

        add(new JLabel("URL:"));
        add(urlfield);
    }

    void setURLField(String text) {
        urlfield.setText(text);
    }
}
