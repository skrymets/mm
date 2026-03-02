package freemind.modes;

import freemind.controller.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * This class creates all the modes that are available. To add your own mode,
 * simply import it, and create it in getAllModes() (just do the same whats done
 * with MindMapMode). Thats all!
 */
@Slf4j
public class ModesCreator {

    private final Controller controller;

    /**
     * Contains translated mode name => Mode instances
     */
    private Map<String, Mode> mCreatedModes;

    /**
     * Contains a name translation. Mode name => Class Name
     */
    private Map<String, String> modesTranslation;

    public ModesCreator(Controller controller) {
        this.controller = controller;
    }

    public Set<String> getAllModes() {

        if (mCreatedModes == null) {
            mCreatedModes = new TreeMap<>();
            modesTranslation = new HashMap<>();
            String modestring = controller.getFrame().getProperty("modes_since_0_8_0");

            StringTokenizer tokens = new StringTokenizer(modestring, ",");

            while (tokens.hasMoreTokens()) {
                String modename = tokens.nextToken();
                String modeAlias = tokens.nextToken();
                mCreatedModes.put(modename, null);
                modesTranslation.put(modeAlias, modename);
            }
            log.info("Modes:{}", mCreatedModes.keySet());
        }
        return modesTranslation.keySet();
    }

    /**
     * Creates a new ModeController.
     */
    public Mode getMode(String modeAlias) {
        getAllModes();

        if (!modesTranslation.containsKey(modeAlias)) {
            throw new IllegalArgumentException("Unknown mode " + modeAlias);
        }

        String modeName = modesTranslation.get(modeAlias);
        if (mCreatedModes.get(modeName) == null) {
            try {
                Mode mode = null;
                mode = (Mode) Class.forName(modeName).newInstance();
                log.info("Initializing mode {}", modeAlias);
                mode.init(controller);
                log.info("Done: Initializing mode {}", modeAlias);
                mCreatedModes.put(modeName, mode);
            } catch (Exception e) {
                log.error("Mode {} could not be loaded.", modeName, e);
            }
        }
        return mCreatedModes.get(modeName);
    }

}
