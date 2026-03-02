package freemind.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;

import javax.swing.Icon;

/**
 * Factory for loading SVG icons from the classpath using FlatLaf's SVG support.
 * SVG icons use {@code currentColor} so they adapt to FlatLaf light/dark themes.
 */
@Slf4j
public final class SvgIconFactory {

    private SvgIconFactory() {}

    /**
     * Loads an SVG icon from the classpath at the specified size.
     *
     * @param classpathResource path to the SVG resource on the classpath
     * @param width desired icon width in pixels
     * @param height desired icon height in pixels
     * @return the loaded Icon, or {@code null} if the resource was not found or loading failed
     */
    public static Icon loadSvgIcon(String classpathResource, int width, int height) {
        try {
            if (SvgIconFactory.class.getClassLoader().getResource(classpathResource) == null) {
                return null;
            }
            return new FlatSVGIcon(classpathResource, width, height);
        } catch (Exception e) {
            log.warn("Failed to load SVG icon: {}", classpathResource, e);
            return null;
        }
    }

    /**
     * Loads an SVG icon from the classpath at the default size of 24x24 pixels.
     *
     * @param classpathResource path to the SVG resource on the classpath
     * @return the loaded Icon, or {@code null} if the resource was not found or loading failed
     */
    public static Icon loadSvgIcon(String classpathResource) {
        return loadSvgIcon(classpathResource, 24, 24);
    }
}
