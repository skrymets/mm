package freemind.modes.common.plugins;

import freemind.extensions.PermanentNodeHook;
import freemind.extensions.PermanentNodeHookAdapter;
import freemind.main.Resources;
import freemind.model.MindMapNode;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * This base class is free of openstreetmap and similar classes.
 * Thus, it doesn't know much about its position.
 */

@Slf4j
public class MapNodePositionHolderBase extends PermanentNodeHookAdapter {

    public static final String NODE_MAP_HOOK_NAME = "plugins/map/MapNodePositionHolder.properties";
    public static final String NODE_MAP_LOCATION_ICON = "node_map_location_icon";
    protected static final String XML_STORAGE_POS_LON = "XML_STORAGE_POS_LON";
    protected static final String XML_STORAGE_POS_LAT = "XML_STORAGE_POS_LAT";
    protected static final String XML_STORAGE_MAP_LON = "XML_STORAGE_MAP_LON";
    protected static final String XML_STORAGE_MAP_LAT = "XML_STORAGE_MAP_LAT";
    protected static final String XML_STORAGE_ZOOM = "XML_STORAGE_ZOOM";
    protected static final String XML_STORAGE_TILE_SOURCE = "XML_STORAGE_TILE_SOURCE";
    protected static final String XML_STORAGE_MAP_TOOLTIP_LOCATION = "XML_STORAGE_MAP_TOOLTIP_LOCATION";
    protected static final String NODE_MAP_SHOW_TOOLTIP = "node_map_show_tooltip";
    public static final String TILE_SOURCE_MAP_QUEST_OPEN_MAP = "plugins.map.FreeMindMapController.MapQuestOpenMap";
    public static final String TILE_SOURCE_TRANSPORT_MAP = "plugins.map.FreeMindMapController.TransportMap";
    public static final String TILE_SOURCE_CYCLE_MAP = "org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource$CycleMap";
    public static final String TILE_SOURCE_MAPNIK = "org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource$Mapnik";
    public static final String SHORT_MAP_QUEST_OPEN_MAP = "Q";
    public static final String SHORT_TRANSPORT_MAP = "T";
    public static final String SHORT_CYCLE_MAP = "C";
    public static final String SHORT_MAPNIK = "M";
    public static ImageIcon sMapLocationIcon;

    protected String mTooltipLocation = null;
    protected File mTooltipFile = null;
    private HashMap<String, String> mValues;

    public static ImageIcon getMapLocationIcon() {
        // icon
        if (sMapLocationIcon == null) {
            sMapLocationIcon = freemind.view.ImageFactory.getInstance().createUnscaledIcon(Resources.get().getResource("images/map_location.png"));
        }
        return sMapLocationIcon;
    }

    protected void setStateIcon(MindMapNode node, boolean enabled) {
        node.setStateIcon(NODE_MAP_LOCATION_ICON, (enabled) ? getMapLocationIcon() : null);
    }

    public MapNodePositionHolderBase() {
        super();
    }

    public void shutdownMapHook() {
        setStateIcon(getNode(), false);
        hideTooltip();
        super.shutdownMapHook();
    }

    public void invoke(MindMapNode pNode) {
        super.invoke(pNode);
        setStateIcon(pNode, true);
        showTooltip();
    }

    public void showTooltip() {
        if (isTooltipDesired()) {
            if (mTooltipLocation != null) {
                setTooltip();
            }
        }
    }

    protected boolean isTooltipDesired() {
        return getController().getResources().getBoolProperty(NODE_MAP_SHOW_TOOLTIP) && !Objects.equals(mTooltipLocation, "false");
    }

    public void loadFrom(Element pChild) {
        super.loadFrom(pChild);
        mValues = loadNameValuePairs(pChild);
        // if no value stored, the get method returns null.
        mTooltipLocation = mValues.get(XML_STORAGE_MAP_TOOLTIP_LOCATION);
    }

    public void setTooltip() {
        String imageHtml = getImageHtml();
        setToolTip(NODE_MAP_HOOK_NAME, imageHtml);
    }

    public String getImageHtml() {
        String imageTag = "<img src=\"file:./" + mTooltipLocation + "\"/>";
        String imageHtml = "<html><body>" + imageTag + "</body></html>";
        log.trace("Tooltip at {}", imageTag);
        return imageHtml;
    }

    protected void hideTooltip() {
        setToolTip(NODE_MAP_HOOK_NAME, null);
    }

    public static MapNodePositionHolderBase getBaseHook(MindMapNode node) {
        for (PermanentNodeHook element : node.getActivatedHooks()) {
            if (element instanceof MapNodePositionHolderBase) {
                return (MapNodePositionHolderBase) element;
            }
        }
        return null;
    }

    public String[] getBarePosition() {
        return new String[]{mValues.get(XML_STORAGE_POS_LAT), mValues.get(XML_STORAGE_POS_LON), mValues.get(XML_STORAGE_MAP_LAT), mValues.get(XML_STORAGE_MAP_LON), mValues.get(XML_STORAGE_ZOOM), mValues.get(XML_STORAGE_TILE_SOURCE)};
    }

}

