/*
 * FreeMind - a program for creating and viewing mindmaps
 * Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * See COPYING for details
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.main;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;

/**
 * Utility methods for converting between {@link Point} objects and XML
 * semicolon-delimited strings, and for Swing coordinate conversions.
 */
public final class PointUtils {

    private PointUtils() {
        // utility class
    }

    /**
     * Converts a {@link Point} to a semicolon-delimited string (e.g. {@code "10;20"}).
     *
     * @param col the point to convert, may be {@code null}
     * @return the string representation, or {@code null} if the input is {@code null}
     */
    public static String PointToXml(Point col) {
        if (col == null) {
            return null;
        }
        return col.x + ";" + col.y;
    }

    /**
     * Parses a semicolon-delimited string (e.g. {@code "10;20"}) into a {@link Point}.
     * Also handles the legacy {@code java.awt.Point[x=10,y=20]} format.
     *
     * @param string the string to parse, may be {@code null}
     * @return the parsed point, or {@code null} if the input is {@code null}
     * @throws IllegalArgumentException if the string does not contain exactly two numbers
     */
    public static Point xmlToPoint(String string) {
        if (string == null) {
            return null;
        }
        // fc, 3.11.2004: bug fix for alpha release of FM
        if (string.startsWith("java.awt.Point")) {
            string = string.replaceAll(
                    "java\\.awt\\.Point\\[x=(-*[0-9]*),y=(-*[0-9]*)\\]", "$1;$2");
        }

        String[] parts = string.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "A point must consist of two numbers (and not: '" + string + "').");
        }

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Point(x, y);
    }

    /**
     * Converts a point from the coordinate system of component {@code c} to the
     * coordinate system of an ancestor component {@code destination}.
     * The point is modified in place.
     *
     * @param c           the source component
     * @param p           the point to convert (modified in place)
     * @param destination the ancestor component
     * @return the same point instance, now in the destination coordinate system
     */
    public static Point convertPointToAncestor(Component c, Point p,
                                               Component destination) {
        int x, y;
        while (c != destination) {
            x = c.getX();
            y = c.getY();

            p.x += x;
            p.y += y;

            c = c.getParent();
        }
        return p;
    }

    /**
     * Converts a point from the coordinate system of an ancestor component
     * {@code source} to the coordinate system of component {@code c}.
     * The point is modified in place.
     *
     * @param source the ancestor component
     * @param p      the point to convert (modified in place)
     * @param c      the target component
     */
    public static void convertPointFromAncestor(Component source, Point p,
                                                Component c) {
        int x, y;
        while (c != source) {
            x = c.getX();
            y = c.getY();

            p.x -= x;
            p.y -= y;

            c = c.getParent();
        }
    }

    /**
     * Converts a point from the coordinate system of component {@code source}
     * to the coordinate system of the nearest ancestor of the given class.
     * The point is modified in place.
     *
     * @param source        the source component
     * @param point         the point to convert (modified in place)
     * @param ancestorClass the class of the ancestor to convert to
     */
    public static void convertPointToAncestor(Component source, Point point,
                                              Class<?> ancestorClass) {
        Component destination = SwingUtilities.getAncestorOfClass(
                ancestorClass, source);
        convertPointToAncestor(source, point, destination);
    }
}
