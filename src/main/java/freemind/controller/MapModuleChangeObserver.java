package freemind.controller;

import freemind.modes.Mode;
import freemind.view.MapModule;

public interface MapModuleChangeObserver {
    /**
     * The params may be null to indicate the there was no previous map, or
     * that the last map is closed now.
     */
    boolean isMapModuleChangeAllowed(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode);

    void beforeMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode);

    void afterMapClose(MapModule oldMapModule, Mode oldMode);

    void afterMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode);

    /**
     * To enable/disable the previous/next map actions.
     */
    void numberOfOpenMapInformation(int number, int pIndex);
}
