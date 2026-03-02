package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

@SuppressWarnings("serial")
public class FontSizeAction extends NodeGeneralAction {

    /**
     * This action is used for all sizes, which have to be set first.
     */
    private String actionSize;

    public FontSizeAction(MindMapController modeController) {
        super(modeController, "font_size", null, (NodeActorXml) null);
        // default value:
        actionSize = modeController.getFrame().getProperty("defaultfontsize");
    }

    public void actionPerformed(String size) {
        this.actionSize = size;
        super.actionPerformed(null);
    }

    @Override
    protected ActionPair getActionPair(MindMapNodeModel pSelected) {
        return getMindMapController().getActorFactory().getFontSizeActor().getActionPair(pSelected, actionSize);
    }
}
