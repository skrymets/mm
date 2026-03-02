/*
 * Created on 05.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.controller.actions.BoldNodeAction;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class BoldAction extends NodeGeneralAction implements
        MenuItemSelectedListener {
    public BoldAction(MindMapController modeController) {
        super(modeController, "bold", "images/Bold16.gif");
        setDoActionClass(BoldNodeAction.class);
    }

    public boolean isSelected(JMenuItem item, Action action) {
        return modeController.getSelected().isBold();
    }

}
