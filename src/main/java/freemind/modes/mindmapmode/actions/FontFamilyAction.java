package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

@SuppressWarnings("serial")
public class FontFamilyAction extends NodeGeneralAction {
    /**
     * This action is used for all fonts, which have to be set first.
     */
    private String actionFont;

    public FontFamilyAction(MindMapController modeController) {
        super(modeController, "font_family", null, (NodeActorXml) null);
        // default value:
        actionFont = modeController.getFrame().getProperty("defaultfont");
    }

    public void actionPerformed(String font) {
        this.actionFont = font;
        super.actionPerformed(null);
    }

    @Override
    protected ActionPair getActionPair(MindMapNodeModel pSelected) {
        return getMindMapController().getActorFactory().getFontFamilyActor().getActionPair(pSelected, actionFont);
    }
}
