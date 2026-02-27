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

import java.awt.Color;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

/**
 * Utility methods for converting between {@link Color} objects and XML hex
 * color strings (e.g. {@code "#ff0000"}).
 */
public final class ColorUtils {

    private ColorUtils() {
        // utility class
    }

    public static String colorToXml(Color col) {
        if (col == null) {
            return null;
        }

        String red = toHexString(col.getRed());
        if (col.getRed() < 16) {
            red = "0" + red;
        }
        String green = toHexString(col.getGreen());
        if (col.getGreen() < 16) {
            green = "0" + green;
        }
        String blue = toHexString(col.getBlue());
        if (col.getBlue() < 16) {
            blue = "0" + blue;
        }
        return "#" + red + green + blue;
    }

    public static Color xmlToColor(String xmlColor) {
        String string = deleteWhitespace(defaultString(xmlColor));
        if (string.length() == 7) {
            int red = parseInt(string.substring(1, 3), 16);
            int green = parseInt(string.substring(3, 5), 16);
            int blue = parseInt(string.substring(5, 7), 16);
            return new Color(red, green, blue);
        } else {
            throw new IllegalArgumentException("No xml color given by '" + string + "'.");
        }
    }
}
