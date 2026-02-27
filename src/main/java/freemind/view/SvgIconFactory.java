/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2015 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
