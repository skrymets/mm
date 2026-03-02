package freemind.modes;

import freemind.controller.MenuItemEnabledListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import static javax.swing.Action.NAME;

/**
 * Common class for all actions that are disabled, when no map is open.
 */
@SuppressWarnings("serial")
@Slf4j
public abstract class FreemindAction extends AbstractAction implements MenuItemEnabledListener {

    private final ControllerAdapter pControllerAdapter;

    /**
     * @param title is a fixed title (no translation is done via resources)
     */
    protected FreemindAction(String title, Icon icon, ControllerAdapter controllerAdapter) {
        super(title, icon);
        this.pControllerAdapter = controllerAdapter;
    }

    /**
     * @param title Title is a resource.
     */
    protected FreemindAction(String title, ControllerAdapter controllerAdapter) {
        this(title, (String) null, controllerAdapter);
    }

    /**
     * @param title    Title is a resource.
     * @param iconPath is a path to an icon.
     */
    protected FreemindAction(String title, String iconPath, final ControllerAdapter controllerAdapter) {
        this(controllerAdapter.getText(title),
                (iconPath == null)
                        ? null
                        : freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(controllerAdapter.getResource(iconPath)),
                controllerAdapter);
    }

    public ControllerAdapter getControllerAdapter() {
        return pControllerAdapter;
    }

    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        boolean result = pControllerAdapter != null && pControllerAdapter.getMap() != null;
        log.trace("isEnabled {}={} from {}", pAction.getValue(NAME), result, pControllerAdapter);
        return result;
    }

}
