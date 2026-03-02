package freemind.modes.mindmapmode;

import freemind.controller.FreeMindPopupMenu;
import freemind.controller.StructuredMenuHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MindMapPopupMenu extends FreeMindPopupMenu {

    private static final String MINDMAPMODE_POPUP = "mindmapmode_popup/";

    private final MindMapController controller;

    public MindMapPopupMenu(MindMapController controller) {
        super();
        this.controller = controller;
    }

    public void update(StructuredMenuHolder holder) {
        this.removeAll();
        controller.createPatternSubMenu(holder, MINDMAPMODE_POPUP);
        controller.addIconsToMenu(holder, MINDMAPMODE_POPUP + "icons/");
        holder.updateMenus(this, MINDMAPMODE_POPUP);

    }

}
