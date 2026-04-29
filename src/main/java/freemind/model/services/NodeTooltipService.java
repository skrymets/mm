package freemind.model.services;

import freemind.main.FreeMindCommon;
import freemind.main.Resources;
import freemind.main.UrlTools;
import freemind.model.NodeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Owns the per-node tooltip map and the thumbnail-cache logic that synthesizes a preview entry
 * for `.mm` links. Extracted from {@link NodeAdapter} so its file-I/O and URL machinery is
 * isolated from the rest of the model.
 */
@Slf4j
public class NodeTooltipService {

    private static final String TOOLTIP_PREVIEW_KEY = "preview";

    private final NodeAdapter node;
    private TreeMap<String, String> toolTip = null; // lazy

    public NodeTooltipService(NodeAdapter node) {
        this.node = node;
    }

    public SortedMap<String, String> getToolTip() {
        boolean toolTipChanged = false;
        TreeMap<String, String> result = toolTip;
        if (result == null)
            result = new TreeMap<>();
        // add preview to other map, if appropriate:
        String link = node.getLink();
        // replace jump mark
        if (link != null && link.matches(".*\\" + FreeMindCommon.FREEMIND_FILE_EXTENSION + "(#.*)?")) {
            link = link.replaceFirst("#.*?$", "");
        }
        if (link != null && link.endsWith(FreeMindCommon.FREEMIND_FILE_EXTENSION)) {
            String linkHtmlPart = "alt=\"" + link + "\"";
            boolean addIt = true;
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                String prev = result.get(TOOLTIP_PREVIEW_KEY);
                if (prev != null && prev.contains(linkHtmlPart)) {
                    addIt = false;
                }
            }
            if (addIt) {
                try {
                    File mmFile = UrlTools.urlToFile(new URL(node.getMap().getURL(), link));
                    String thumbnailFileName = Resources.get().createThumbnailFileName(mmFile);
                    if (new File(thumbnailFileName).exists()) {
                        URL thumbUrl = UrlTools.fileToUrl(new File(thumbnailFileName));
                        String imgHtml = "<img src=\"" + thumbUrl + "\" " + linkHtmlPart + "/>";
                        log.info("Adding new tooltip: {}", imgHtml);
                        result.put(TOOLTIP_PREVIEW_KEY, imgHtml);
                        toolTipChanged = true;
                    }
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } else {
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                result.remove(TOOLTIP_PREVIEW_KEY);
                toolTipChanged = true;
            }
        }
        if (toolTipChanged) {
            if (result.isEmpty()) {
                toolTip = null;
            } else {
                toolTip = result;
            }
        }
        return Collections.unmodifiableSortedMap(result);
    }

    public void setToolTip(String key, String string) {
        if (toolTip == null) {
            toolTip = new TreeMap<>();
        }
        if (string == null) {
            toolTip.remove(key);
            if (toolTip.isEmpty())
                toolTip = null;
        } else {
            toolTip.put(key, string);
        }
    }
}
