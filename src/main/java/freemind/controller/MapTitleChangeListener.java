package freemind.controller;

import freemind.model.MindMap;
import freemind.view.MapModule;

/**
 * You can register yourself to this listener at the main controller.
 */
public interface MapTitleChangeListener {
    void setMapTitle(String pNewMapTitle, MapModule pMapModule, MindMap pModel);
}
