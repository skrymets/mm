package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.controller.actions.AddCloudXmlAction;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class CloudAction extends NodeGeneralAction implements MenuItemSelectedListener {

    public CloudAction(MindMapController controller) {
        super(controller, "cloud", "images/Cloud24.gif");
        setDoActionClass(getDoActionClass());
    }

    public Class<AddCloudXmlAction> getDoActionClass() {
        return AddCloudXmlAction.class;
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        return modeController.getSelected().getCloud() != null;
    }
}
