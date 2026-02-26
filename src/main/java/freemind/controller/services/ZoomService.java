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

import freemind.controller.ZoomListener;

import java.util.HashSet;

/**
 * Manages zoom level values and notifies registered listeners when the zoom changes.
 */
public class ZoomService {

    private static final float[] ZOOM_VALUES = {0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 3.0f, 4.0f};

    private final HashSet<ZoomListener> zoomListenerSet = new HashSet<>();

    public void registerZoomListener(ZoomListener listener) {
        zoomListenerSet.add(listener);
    }

    public void unregisterZoomListener(ZoomListener listener) {
        zoomListenerSet.remove(listener);
    }

    /**
     * Notifies all registered zoom listeners of a zoom value change.
     *
     * @param zoomValue the new zoom value (e.g. 1.0 for 100%)
     */
    public void changeZoomValueProperty(float zoomValue) {
        for (ZoomListener listener : zoomListenerSet) {
            listener.setZoom(zoomValue);
        }
    }

    /**
     * Returns the available zoom levels as display strings (e.g. "100%").
     */
    public String[] getZooms() {
        String[] zooms = new String[ZOOM_VALUES.length];
        for (int i = 0; i < ZOOM_VALUES.length; i++) {
            zooms[i] = (int) (ZOOM_VALUES[i] * 100f) + "%";
        }
        return zooms;
    }

    /**
     * Returns the static array of zoom float values.
     */
    public static float[] getZoomValues() {
        return ZOOM_VALUES;
    }
}
