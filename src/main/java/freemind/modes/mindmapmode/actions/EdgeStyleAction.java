package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import javax.swing.*;
import java.util.Objects;

public class EdgeStyleAction extends NodeGeneralAction implements MenuItemSelectedListener {
    private final String mStyle;

    public EdgeStyleAction(MindMapController controller, String style) {
        super(controller, null, null);
        setName(/* controller.getText("edge_style") + */controller.getText(style));
        this.mStyle = style;
    }

    @Override
    protected ActionPair getActionPair(MindMapNodeModel pSelected) {
        return getMindMapController().getActorFactory().getEdgeStyleActor().getActionPair(pSelected, mStyle);
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        String style = getMindMapController().getSelected().getEdge().getStyle();
        return Objects.equals(style, mStyle);
    }
}
