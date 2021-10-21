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


package freemind.view;

import freemind.model.MindMap;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is the key to one Model/View bundle which represents one map.
 */

@Getter
@Setter
public class MapModule {
    private final MindMap model;
    private final MapView view;
    private final Mode mode;
    private final ModeController modeController;

    /**
     * Contains an extension if a map with same file name is already opened.
     */
    private String displayName;
    private String name;

    private static AtomicInteger unnamedMapsNumber = new AtomicInteger(1); // used to give unique names to maps

    public MapModule(MindMap model, MapView view, Mode mode, ModeController modeController) {
        this.model = model;
        this.view = view;
        this.mode = mode;
        this.modeController = modeController;
        modeController.setView(view);
    }

    /**
     * Returns the String that is used to identify this map. Important: If the
     * String is changed, other component (ie Controller) must be notified.
     */
    public String toString() {
        if (name == null) {
            rename();
        }
        return name;
    }

    public void rename() {
        name = getModel().toString() == null
                ? mode.getController().getFrame().getResourceString("mindmap") + unnamedMapsNumber.incrementAndGet()
                : getModel().toString();
    }

}
