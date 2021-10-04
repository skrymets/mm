/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
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
package plugins.map;


import freemind.modes.MindMapNode;

@SuppressWarnings("serial")
public class MapMarkerLocation extends MapMarkerBase {

    private final MapNodePositionHolder mNodePositionHolder;

    /**
     * @param pNodePositionHolder
     * @param pMapDialog
     */
    public MapMarkerLocation(MapNodePositionHolder pNodePositionHolder, MapDialog pMapDialog) {
        super(pMapDialog);
        mNodePositionHolder = pNodePositionHolder;
        update();
    }

    /**
     * Either start or when something changes on the node, this method is called.
     */
    public void update() {
        MindMapNode node = mNodePositionHolder.getNode();
        setText(node.getText());
        setForeground(node.getColor());
        setSize(getPreferredSize());
    }

    public double getLat() {
        return mNodePositionHolder.getPosition().getLat();
    }

    public double getLon() {
        return mNodePositionHolder.getPosition().getLon();
    }

    public String toString() {
        return "MapMarkerLocation for node "
                + mNodePositionHolder.getNode().getText() + " at " + getLat()
                + " " + getLon();
    }

    public MapNodePositionHolder getNodePositionHolder() {
        return mNodePositionHolder;
    }

    @Override
    public void setLat(double pLat) {
        mNodePositionHolder.getPosition().setLat(pLat);
    }

    @Override
    public void setLon(double pLon) {
        mNodePositionHolder.getPosition().setLon(pLon);
    }


}
