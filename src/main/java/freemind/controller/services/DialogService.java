/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package freemind.controller.services;

import freemind.controller.Closer;
import freemind.controller.ColorTracker;
import freemind.controller.DisposeOnClose;

import javax.swing.*;
import java.awt.*;

/**
 * Provides static color chooser dialogs and common message dialog utilities.
 */
public class DialogService {

    private static final String FREEMIND = "FreeMind";
    private static final JColorChooser colorChooser = new JColorChooser();

    private DialogService() {
        // static utility class
    }

    /**
     * Returns the shared JColorChooser instance to preserve recent colors.
     */
    public static JColorChooser getCommonJColorChooser() {
        return colorChooser;
    }

    /**
     * Shows a modal color chooser dialog using the shared JColorChooser.
     *
     * @param component    the parent component
     * @param title        the dialog title
     * @param initialColor the initially selected color
     * @return the selected color, or null if the user cancelled
     */
    public static Color showCommonJColorChooserDialog(Component component, String title, Color initialColor) throws HeadlessException {
        final JColorChooser pane = getCommonJColorChooser();
        pane.setColor(initialColor);

        ColorTracker ok = new ColorTracker(pane);
        JDialog dialog = JColorChooser.createDialog(component, title, true, pane, ok, null);
        dialog.addWindowListener(new Closer());
        dialog.addComponentListener(new DisposeOnClose());

        dialog.setVisible(true);
        // blocks until user brings dialog down...

        return ok.getColor();
    }

    /**
     * Shows an error message dialog.
     *
     * @param parentComponent the parent component for the dialog
     * @param message         the message to display
     * @param undefinedError  fallback message if message is null
     */
    public static void errorMessage(Component parentComponent, Object message, String undefinedError) {
        String myMessage;
        if (message != null) {
            myMessage = message.toString();
        } else {
            myMessage = undefinedError;
            if (myMessage == null) {
                myMessage = "Undefined error";
            }
        }
        JOptionPane.showMessageDialog(parentComponent, myMessage, FREEMIND, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an error message dialog on the given component.
     */
    public static void errorMessage(JComponent component, Object message) {
        JOptionPane.showMessageDialog(component, message.toString(), FREEMIND, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an information message dialog.
     */
    public static void informationMessage(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message.toString(), FREEMIND, JOptionPane.INFORMATION_MESSAGE);
    }
}
