package freemind.view;

import freemind.main.Resources;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class ImageFactory {
    private static ImageFactory mInstance = null;
    private static Resources resources;

    public static void init(Resources res) {
        resources = res;
    }

    public static ImageFactory getInstance() {
        if (mInstance == null) {
            mInstance = new ImageFactory();
        }
        return mInstance;
    }

    public ImageIcon createIcon(URL pUrl) {
        if (SwingUtils.getScalingFactorPlain() == 100) {
            return createUnscaledIcon(pUrl);
        }

        ScalableImageIcon icon = new ScalableImageIcon(pUrl);
        icon.setScale(SwingUtils.getScalingFactor());
        return icon;
    }

    /**
     * All icons directly displayed in the mindmap view are scaled by the zoom.
     */
    public ImageIcon createUnscaledIcon(URL pResource) {
        return new ImageIcon(pResource);
    }

    /**
     * Tries to load an SVG version of the icon first; falls back to the PNG at the given URL.
     * The SVG is looked up by converting {@code images/foo.png} to {@code images/svg/foo.svg}.
     *
     * @param pngUrl URL of the original PNG icon
     * @return an SVG Icon if available, otherwise the PNG ImageIcon
     */
    public Icon createIconWithSvgFallback(URL pngUrl) {
        if (pngUrl != null) {
            String path = pngUrl.getPath();
            int lastSlash = path.lastIndexOf('/');
            String filename = (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;
            String baseName = filename.contains(".") ? filename.substring(0, filename.lastIndexOf('.')) : filename;
            String svgPath = "images/svg/" + baseName + ".svg";
            Icon svgIcon = SvgIconFactory.loadSvgIcon(svgPath);
            if (svgIcon != null) {
                return svgIcon;
            }
        }
        // Fall back to PNG
        return createIcon(pngUrl);
    }

    public ImageIcon createIcon(String pFilePath) {
        return createIcon(pFilePath, resources);
    }

    public ImageIcon createIcon(String pFilePath, Resources resources) {
        if (SwingUtils.getScalingFactorPlain() == 200) {
            // test for existence  of a scaled icon:
            if (pFilePath.endsWith(".png")) {
                try {
                    URL url = resources.getResource(pFilePath.replaceAll(".png$", "_32.png"));
                    URLConnection connection = url.openConnection();
                    if (connection.getContentLength() > 0) {
                        return createUnscaledIcon(url);
                    }
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
        return createIcon(resources.getResource(pFilePath));
    }
}
