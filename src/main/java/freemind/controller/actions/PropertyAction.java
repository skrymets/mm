package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.FreeMind;
import freemind.main.SwingUtils;
import freemind.preferences.layout.OptionPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyAction extends AbstractAction {

    private final Controller controller;

    public PropertyAction(Controller controller) {
        super(controller.getResourceString("property_dialog"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent arg0) {
        JDialog dialog = new JDialog(controller.getFrame().getJFrame(), true /* modal */);
        dialog.setResizable(true);
        dialog.setUndecorated(false);
        final OptionPanel options = new OptionPanel((FreeMind) controller.getFrame(),
                dialog, props -> {
            List<String> sortedKeys = new ArrayList<>(props.stringPropertyNames());
            Collections.sort(sortedKeys);
            boolean propertiesChanged = false;
            for (String key : sortedKeys) {
                // save only changed keys:
                String newProperty = props.getProperty(key);
                propertiesChanged = propertiesChanged
                        || !newProperty.equals(controller
                        .getProperty(key));
                controller.setProperty(key, newProperty);
            }

            if (propertiesChanged) {
                controller.getFrame().saveProperties(false);
                // Apply L&F change immediately
                FreeMind fm = (FreeMind) controller.getFrame();
                fm.updateLookAndFeel();
                SwingUtilities.updateComponentTreeUI(fm);
                fm.pack();
            }
        });
        options.buildPanel();
        options.setProperties();
        dialog.setTitle("Freemind Properties");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                options.closeWindow();
            }
        });
        Action action = new AbstractAction() {

            public void actionPerformed(ActionEvent arg0) {
                options.closeWindow();
            }
        };
        SwingUtils.addEscapeActionToDialog(dialog, action);
        dialog.setVisible(true);

    }

}
