package freemind.controller;

import freemind.model.MindMap;
import freemind.view.MapModule;

/**
 * You can register yourself as a contributor to the title at the main
 * controller.
 */
public interface MapTitleContributor {
    /**
     * @param pOldTitle  The current title
     * @param pMapModule
     * @param pModel
     * @return The current title can be changed or something can be added,
     * but it must be returned as a whole.
     */
    String getMapTitle(String pOldTitle, MapModule pMapModule, MindMap pModel);
}
