package freemind.modes;

import freemind.controller.Controller;
import java.io.IOException;

public abstract class Mode {

    public abstract void init(Controller c);

    public abstract String toString();

    public abstract void activate();

    public abstract void restore(String restorable) throws IOException;

    /**
     * Creates a new mode controller and returns it.
     */
    public abstract ModeController createModeController();

    /**
     * This modeController is only used, when no map is opened.
     */
    public abstract ModeController getDefaultModeController();

    public abstract Controller getController();

    public String toLocalizedString() {
        return getController().getResourceString("mode_" + this);
    }
}
