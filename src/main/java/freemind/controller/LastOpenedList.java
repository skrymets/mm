package freemind.controller;

import freemind.main.MindMapUtils;
import freemind.view.MapModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * This class manages a list of the maps that were opened last. It aims to
 * provide persistence for the last recent maps. Maps should be shown in the
 * format:"mode\:key",ie."Mindmap\:/home/joerg/freemind.mm"
 */
@Slf4j
public class LastOpenedList {
    private final Controller mController;
    private int maxEntries = 25; // is rewritten from property anyway
    /**
     * Contains Restore strings.
     */
    private final List<String> mlastOpenedList = new LinkedList<>();
    /**
     * Contains Restore string => map name (map.toString()).
     */
    private final Map<String, String> mRestorableToMapName = new HashMap<>();

    public LastOpenedList(Controller c, String restored) {
        this.mController = c;
        try {
            maxEntries = Integer.parseInt(c.getFrame().getProperty(
                    "last_opened_list_length"));
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        load(restored);
    }

    public void mapOpened(MapModule mapModule) {
        if (mapModule == null || mapModule.getModel() == null)
            return;
        String restoreString = mapModule.getModel().getRestorable();
        String name = mapModule.toString();
        add(restoreString, name);
    }

    /**
     * For testing purposes, this method is public
     */
    public void add(String restoreString, String name) {
        if (restoreString == null)
            return;
        mlastOpenedList.remove(restoreString);
        mlastOpenedList.add(0, restoreString);
        mRestorableToMapName.put(restoreString, name);

        while (mlastOpenedList.size() > maxEntries) {
            mlastOpenedList.remove(mlastOpenedList.size() - 1); // remove last elt
        }
    }

    void mapClosed(MapModule map) {
        // hash.remove(map.getModel().getRestoreable());
        // not needed
    }

    /**
     * fc, 8.8.2004: This method returns a string representation of this class.
     */
    public String save() {
        String str = "";
        for (ListIterator<String> it = listIterator(); it.hasNext(); ) {
            str = str.concat(it.next() + ";");
        }
        return str;
    }

    void load(String data) {
        // Take care that there are no ";" in restorable names!
        if (data != null) {
            StringTokenizer token = new StringTokenizer(data, ";");
            while (token.hasMoreTokens())
                mlastOpenedList.add(token.nextToken());
        }
    }

    public boolean open(String restoreable) throws
            IOException,
            URISyntaxException {
        boolean changedToMapModule = mController.getMapModuleManager()
                .tryToChangeToMapModule(
                        mRestorableToMapName.get(restoreable));
        if ((restoreable != null) && !(changedToMapModule)) {
            String mode = MindMapUtils.getModeFromRestorable(restoreable);
            String fileName = MindMapUtils.getFileNameFromRestorable(restoreable);
            if (mController.createNewMode(mode)) {
                mController.getMode().restore(fileName);
                return true;
            }
        }
        return false;
    }

    ListIterator<String> listIterator() {
        return mlastOpenedList.listIterator();
    }
}
