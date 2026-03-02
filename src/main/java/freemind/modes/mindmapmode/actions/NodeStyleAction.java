package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import javax.swing.*;
import java.util.Objects;

@SuppressWarnings("serial")
public class NodeStyleAction extends NodeGeneralAction implements MenuItemSelectedListener {
    private final String mStyle;

    public NodeStyleAction(MindMapController controller, String style) {
        super(controller, style, null);
        this.mStyle = style;
    }

    @Override
    protected ActionPair getActionPair(MindMapNodeModel pSelected) {
        return getMindMapController().getActorFactory().getNodeStyleActor().getActionPair(pSelected, mStyle);
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        MindMapNode selected = modeController.getSelected();
        if (!selected.hasStyle())
            return false;
        return Objects.equals(mStyle, selected.getStyle());
    }
}
