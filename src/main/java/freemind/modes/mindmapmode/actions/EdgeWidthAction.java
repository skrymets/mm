package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.model.EdgeAdapter;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import javax.swing.*;

@SuppressWarnings("serial")
public class EdgeWidthAction extends NodeGeneralAction implements MenuItemSelectedListener {
    private final int mWidth;

    public EdgeWidthAction(MindMapController controller, int width) {
        super(controller, null, null);
        this.mWidth = width;
        setName(getWidthTitle(controller, width));
    }

    @Override
    protected ActionPair getActionPair(MindMapNodeModel pSelected) {
        return getMindMapController().getActorFactory().getEdgeWidthActor().getActionPair(pSelected, mWidth);
    }

    private static String getWidthTitle(MindMapController controller, int width) {
        String returnValue;
        if (width == EdgeAdapter.WIDTH_PARENT) {
            returnValue = controller.getText("edge_width_parent");
        } else if (width == EdgeAdapter.WIDTH_THIN) {
            returnValue = controller.getText("edge_width_thin");
        } else {
            returnValue = Integer.toString(width);
        }
        return /* controller.getText("edge_width") + */returnValue;
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        int width = getMindMapController().getSelected().getEdge().getRealWidth();
        return width == mWidth;
    }

    public int getWidth() {
        return mWidth;
    }

}
