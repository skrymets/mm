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


package freemind.modes.mindmapmode;

import freemind.controller.Controller;
import freemind.main.XMLParseException;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Log4j2
public class MindMapMode extends Mode {

    private Controller controller;
    private MindMapController mindMapController;
    private boolean isRunning = false;

    public MindMapMode() {
    }

    public void init(Controller controller) {
        this.controller = controller;
        mindMapController = (MindMapController) createModeController();
    }

    public ModeController createModeController() {
        return new MindMapController(this);
    }

    public String toString() {
        return "MindMap";
    }

    /**
     * Called whenever this mode is chosen in the program. (updates Actions etc.)
     */
    public void activate() {
        if (isRunning) {
            controller.getMapModuleManager().changeToMapOfMode(this);
        } else {
            isRunning = true;
        }
    }

    public void restore(String restorable) throws XMLParseException, IOException, URISyntaxException {
        getDefaultModeController().load(new File(restorable));
    }

    public Controller getController() {
        return controller;
    }

    public ModeController getDefaultModeController() {
        return mindMapController;
    }

}
