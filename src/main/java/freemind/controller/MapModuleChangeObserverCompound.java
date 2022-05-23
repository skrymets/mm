package freemind.controller;

import freemind.modes.Mode;
import freemind.view.MapModule;

import java.util.ArrayList;
import java.util.HashSet;

public class MapModuleChangeObserverCompound implements MapModuleChangeObserver {

    private final HashSet<MapModuleChangeObserver> listeners = new HashSet<>();

    public void addListener(MapModuleChangeObserver listener) {
        listeners.add(listener);
    }

    public void removeListener(MapModuleChangeObserver listener) {
        listeners.remove(listener);
    }

    public boolean isMapModuleChangeAllowed(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        boolean returnValue = true;
        for (MapModuleChangeObserver observer : new ArrayList<>(listeners)) {
            returnValue = observer.isMapModuleChangeAllowed(oldMapModule, oldMode, newMapModule, newMode);
            if (!returnValue) {
                break;
            }
        }
        return returnValue;
    }

    public void beforeMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        for (MapModuleChangeObserver observer : new ArrayList<>(listeners)) {
            observer.beforeMapModuleChange(oldMapModule, oldMode,
                    newMapModule, newMode);
        }
    }

    public void afterMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        for (MapModuleChangeObserver observer : new ArrayList<>(listeners)) {
            observer.afterMapModuleChange(oldMapModule, oldMode, newMapModule, newMode);
        }
    }

    public void numberOfOpenMapInformation(int number, int pIndex) {
        for (MapModuleChangeObserver observer : new ArrayList<>(listeners)) {
            observer.numberOfOpenMapInformation(number, pIndex);
        }
    }

    public void afterMapClose(MapModule pOldMapModule, Mode pOldMode) {
        for (MapModuleChangeObserver observer : new ArrayList<>(listeners)) {
            observer.afterMapClose(pOldMapModule, pOldMode);
        }
    }
}
