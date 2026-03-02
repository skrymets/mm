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
