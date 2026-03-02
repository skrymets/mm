package freemind.modes.mindmapmode.hooks;

import freemind.extensions.ModeControllerHookAdapter;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class MindMapHookAdapter extends ModeControllerHookAdapter {

    public MindMapHookAdapter() {
        super();
    }

    public MindMapController getMindMapController() {
        return (MindMapController) getController();
    }

    public JMenuItem addAccelerator(JMenuItem menuItem, String key) {
        String keyProp = getMindMapController().getFrame().getProperty(key);
        if (keyProp == null) {
            log.warn("Keystroke to {} not found.", key);
        }
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyProp);
        menuItem.setAccelerator(keyStroke);
        menuItem.getAction().putValue(Action.ACCELERATOR_KEY, keyStroke);
        return menuItem;
    }

}
