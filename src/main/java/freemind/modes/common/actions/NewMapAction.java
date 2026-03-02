/*
 * Created on 05.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.modes.common.actions;

import freemind.modes.ModeController;

import javax.swing.*;
import java.awt.event.ActionEvent;

// //////////
// Actions
// /////////

@SuppressWarnings("serial")
public class NewMapAction extends AbstractAction {
    private final ModeController modeController;

    public NewMapAction(ModeController modeController) {
        super(modeController.getText("new"), freemind.view.ImageFactory.getInstance().createIcon(
                modeController.getResource("images/filenew.png")));
        this.modeController = modeController;
        // Workaround to get the images loaded in jar file.
        // they have to be added to jar manually with full path from root
        // I really don't like this, but it's a bug of java
    }

    public void actionPerformed(ActionEvent e) {
        modeController.newMap();
    }
}