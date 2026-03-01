/*
 * FreeMind - A Program for creating and viewing MindmapsCopyright (C) 2000-2015
 * Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and
 * others.
 *
 * See COPYING for Details
 *
 * This program is free software; you can redistribute it and/ormodify it under
 * the terms of the GNU General Public Licenseas published by the Free Software
 * Foundation; either version 2of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty ofMERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See theGNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public Licensealong with
 * this program; if not, write to the Free SoftwareFoundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.controller.color;

import freemind.main.Resources;
import freemind.main.Tools;
import freemind.main.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


@SuppressWarnings("serial")
public class JColorCombo extends JComboBox<ColorPair> {


    public static class ColorIcon extends ImageIcon {

        private static final int ICON_SIZE = (int) (SwingUtils.getScalingFactor() * 16);

        public ColorIcon(Color pColor) {
            super(new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB));
            BufferedImage mImage = (BufferedImage) getImage();
            Graphics g = mImage.getGraphics();
            g.setColor(pColor);
            g.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
            g.dispose();
        }


    }

    public JColorCombo() {
        ColorPair[] colorList = sColorList;
        for (ColorPair colorPair : colorList) {
            addItem(colorPair);
        }
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        setRenderer(renderer);
        setMaximumRowCount(20);
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public static class ComboBoxRenderer extends JLabel implements ListCellRenderer<ColorPair> {
        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends ColorPair> list, ColorPair value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ColorPair pair = value;
            ImageIcon icon = new ColorIcon(pair.color);
            setIcon(icon);
            setText(pair.displayName);

            return this;
        }
    }

    public static void main(String[] s) {
        JFrame frame = new JFrame("JColorChooser");
        JColorCombo colorChooser = new JColorCombo();

        frame.getContentPane().add(colorChooser);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static final ColorPair[] sColorList = new ColorPair[]{
            // taken from http://wiki.selfhtml.org/wiki/Grafik/Farbpaletten#Farbnamen
            // default 16bit colors
            new ColorPair(new Color(0x000000), "black", Resources.getInstance()),
            new ColorPair(new Color(0x808080), "gray", Resources.getInstance()),
            new ColorPair(new Color(0x800000), "maroon", Resources.getInstance()),
            new ColorPair(new Color(0xFF0000), "red", Resources.getInstance()),

            new ColorPair(new Color(0x008000), "green", Resources.getInstance()),
            new ColorPair(new Color(0x00FF00), "lime", Resources.getInstance()),

            new ColorPair(new Color(0x808000), "olive", Resources.getInstance()),
            new ColorPair(new Color(0xFFFF00), "yellow", Resources.getInstance()),

            new ColorPair(new Color(0x000080), "navy", Resources.getInstance()),
            new ColorPair(new Color(0x0000FF), "blue", Resources.getInstance()),

            new ColorPair(new Color(0x800080), "purple", Resources.getInstance()),
            new ColorPair(new Color(0xFF00FF), "fuchsia", Resources.getInstance()),
            new ColorPair(new Color(0x008080), "teal", Resources.getInstance()),
            new ColorPair(new Color(0x00FFFF), "aqua", Resources.getInstance()),

            new ColorPair(new Color(0xC0C0C0), "silver", Resources.getInstance()),
            new ColorPair(new Color(0xFFFFFF), "white", Resources.getInstance()),

            // automatic layout colors:
            new ColorPair(new Color(0x0033ff), "level1", Resources.getInstance()),
            new ColorPair(new Color(0x00b439), "level2", Resources.getInstance()),
            new ColorPair(new Color(0x990000), "level3", Resources.getInstance()),
            new ColorPair(new Color(0x111111), "level4", Resources.getInstance()),


            // netscape colors
            new ColorPair(new Color(0xFFC0CB), "pink", Resources.getInstance()),
            new ColorPair(new Color(0xFFB6C1), "lightpink", Resources.getInstance()),
            new ColorPair(new Color(0xFF69B4), "hotpink", Resources.getInstance()),
            new ColorPair(new Color(0xFF1493), "deeppink", Resources.getInstance()),
            new ColorPair(new Color(0xDB7093), "palevioletred", Resources.getInstance()),
            new ColorPair(new Color(0xC71585), "mediumvioletred", Resources.getInstance()),
            new ColorPair(new Color(0xFFA07A), "lightsalmon", Resources.getInstance()),
            new ColorPair(new Color(0xFA8072), "salmon", Resources.getInstance()),
            new ColorPair(new Color(0xE9967A), "darksalmon", Resources.getInstance()),
            new ColorPair(new Color(0xF08080), "lightcoral", Resources.getInstance()),
            new ColorPair(new Color(0xCD5C5C), "indianred", Resources.getInstance()),
            new ColorPair(new Color(0xDC143C), "crimson", Resources.getInstance()),
            new ColorPair(new Color(0xB22222), "firebrick", Resources.getInstance()),
            new ColorPair(new Color(0x8B0000), "darkred", Resources.getInstance()),
            new ColorPair(new Color(0xFF0000), "red", Resources.getInstance()),
            new ColorPair(new Color(0xFF4500), "orangered", Resources.getInstance()),
            new ColorPair(new Color(0xFF6347), "tomato", Resources.getInstance()),
            new ColorPair(new Color(0xFF7F50), "coral", Resources.getInstance()),
            new ColorPair(new Color(0xFF8C00), "darkorange", Resources.getInstance()),
            new ColorPair(new Color(0xFFA500), "orange", Resources.getInstance()),
            new ColorPair(new Color(0xFFFF00), "yellow", Resources.getInstance()),
            new ColorPair(new Color(0xFFFFE0), "lightyellow", Resources.getInstance()),
            new ColorPair(new Color(0xFFFACD), "lemonchiffon", Resources.getInstance()),
            new ColorPair(new Color(0xFFEFD5), "papayawhip", Resources.getInstance()),
            new ColorPair(new Color(0xFFE4B5), "moccasin", Resources.getInstance()),
            new ColorPair(new Color(0xFFDAB9), "peachpuff", Resources.getInstance()),
            new ColorPair(new Color(0xEEE8AA), "palegoldenrod", Resources.getInstance()),
            new ColorPair(new Color(0xF0E68C), "khaki", Resources.getInstance()),
            new ColorPair(new Color(0xBDB76B), "darkkhaki", Resources.getInstance()),
            new ColorPair(new Color(0xFFD700), "gold", Resources.getInstance()),
            new ColorPair(new Color(0xFFF8DC), "cornsilk", Resources.getInstance()),
            new ColorPair(new Color(0xFFEBCD), "blanchedalmond", Resources.getInstance()),
            new ColorPair(new Color(0xFFE4C4), "bisque", Resources.getInstance()),
            new ColorPair(new Color(0xFFDEAD), "navajowhite", Resources.getInstance()),
            new ColorPair(new Color(0xF5DEB3), "wheat", Resources.getInstance()),
            new ColorPair(new Color(0xDEB887), "burlywood", Resources.getInstance()),
            new ColorPair(new Color(0xD2B48C), "tan", Resources.getInstance()),
            new ColorPair(new Color(0xBC8F8F), "rosybrown", Resources.getInstance()),
            new ColorPair(new Color(0xF4A460), "sandybrown", Resources.getInstance()),
            new ColorPair(new Color(0xDAA520), "goldenrod", Resources.getInstance()),
            new ColorPair(new Color(0xB8860B), "darkgoldenrod", Resources.getInstance()),
            new ColorPair(new Color(0xCD853F), "peru", Resources.getInstance()),
            new ColorPair(new Color(0xD2691E), "chocolate", Resources.getInstance()),
            new ColorPair(new Color(0x8B4513), "saddlebrown", Resources.getInstance()),
            new ColorPair(new Color(0xA0522D), "sienna", Resources.getInstance()),
            new ColorPair(new Color(0xA52A2A), "brown", Resources.getInstance()),
            new ColorPair(new Color(0x800000), "maroon", Resources.getInstance()),
            new ColorPair(new Color(0x556B2F), "darkolivegreen", Resources.getInstance()),
            new ColorPair(new Color(0x808000), "olive", Resources.getInstance()),
            new ColorPair(new Color(0x6B8E23), "olivedrab", Resources.getInstance()),
            new ColorPair(new Color(0x9ACD32), "yellowgreen", Resources.getInstance()),
            new ColorPair(new Color(0x32CD32), "limegreen", Resources.getInstance()),
            new ColorPair(new Color(0x00FF00), "lime", Resources.getInstance()),
            new ColorPair(new Color(0x7CFC00), "lawngreen", Resources.getInstance()),
            new ColorPair(new Color(0x7FFF00), "chartreuse", Resources.getInstance()),
            new ColorPair(new Color(0xADFF2F), "greenyellow", Resources.getInstance()),
            new ColorPair(new Color(0x00FF7F), "springgreen", Resources.getInstance()),
            new ColorPair(new Color(0x00FA9A), "mediumspringgreen", Resources.getInstance()),
            new ColorPair(new Color(0x90EE90), "lightgreen", Resources.getInstance()),
            new ColorPair(new Color(0x98FB98), "palegreen", Resources.getInstance()),
            new ColorPair(new Color(0x8FBC8F), "darkseagreen", Resources.getInstance()),
            new ColorPair(new Color(0x3CB371), "mediumseagreen", Resources.getInstance()),
            new ColorPair(new Color(0x2E8B57), "seagreen", Resources.getInstance()),
            new ColorPair(new Color(0x228B22), "forestgreen", Resources.getInstance()),
            new ColorPair(new Color(0x008000), "green", Resources.getInstance()),
            new ColorPair(new Color(0x006400), "darkgreen", Resources.getInstance()),
            new ColorPair(new Color(0x66CDAA), "mediumaquamarine", Resources.getInstance()),
            new ColorPair(new Color(0x00FFFF), "aqua", Resources.getInstance()),
            new ColorPair(new Color(0x00FFFF), "cyan", Resources.getInstance()),
            new ColorPair(new Color(0xE0FFFF), "lightcyan", Resources.getInstance()),
            new ColorPair(new Color(0xAFEEEE), "paleturquoise", Resources.getInstance()),
            new ColorPair(new Color(0x7FFFD4), "aquamarine", Resources.getInstance()),
            new ColorPair(new Color(0x40E0D0), "turquoise", Resources.getInstance()),
            new ColorPair(new Color(0x48D1CC), "mediumturquoise", Resources.getInstance()),
            new ColorPair(new Color(0x00CED1), "darkturquoise", Resources.getInstance()),
            new ColorPair(new Color(0x20B2AA), "lightseagreen", Resources.getInstance()),
            new ColorPair(new Color(0x5F9EA0), "cadetblue", Resources.getInstance()),
            new ColorPair(new Color(0x008B8B), "darkcyan", Resources.getInstance()),
            new ColorPair(new Color(0x008080), "teal", Resources.getInstance()),
            new ColorPair(new Color(0xB0C4DE), "lightsteelblue", Resources.getInstance()),
            new ColorPair(new Color(0xB0E0E6), "powderblue", Resources.getInstance()),
            new ColorPair(new Color(0xADD8E6), "lightblue", Resources.getInstance()),
            new ColorPair(new Color(0x87CEEB), "skyblue", Resources.getInstance()),
            new ColorPair(new Color(0x87CEFA), "lightskyblue", Resources.getInstance()),
            new ColorPair(new Color(0x00BFFF), "deepskyblue", Resources.getInstance()),
            new ColorPair(new Color(0x1E90FF), "dodgerblue", Resources.getInstance()),
            new ColorPair(new Color(0x6495ED), "cornflowerblue", Resources.getInstance()),
            new ColorPair(new Color(0x4682B4), "steelblue", Resources.getInstance()),
            new ColorPair(new Color(0x4169E1), "royalblue", Resources.getInstance()),
            new ColorPair(new Color(0x0000FF), "blue", Resources.getInstance()),
            new ColorPair(new Color(0x0000CD), "mediumblue", Resources.getInstance()),
            new ColorPair(new Color(0x00008B), "darkblue", Resources.getInstance()),
            new ColorPair(new Color(0x000080), "navy", Resources.getInstance()),
            new ColorPair(new Color(0x191970), "midnightblue", Resources.getInstance()),
            new ColorPair(new Color(0xE6E6FA), "lavender", Resources.getInstance()),
            new ColorPair(new Color(0xD8BFD8), "thistle", Resources.getInstance()),
            new ColorPair(new Color(0xDDA0DD), "plum", Resources.getInstance()),
            new ColorPair(new Color(0xEE82EE), "violet", Resources.getInstance()),
            new ColorPair(new Color(0xDA70D6), "orchid", Resources.getInstance()),
            new ColorPair(new Color(0xFF00FF), "fuchsia", Resources.getInstance()),
            new ColorPair(new Color(0xFF00FF), "magenta", Resources.getInstance()),
            new ColorPair(new Color(0xBA55D3), "mediumorchid", Resources.getInstance()),
            new ColorPair(new Color(0x9370DB), "mediumpurple", Resources.getInstance()),
            new ColorPair(new Color(0x8A2BE2), "blueviolet", Resources.getInstance()),
            new ColorPair(new Color(0x9400D3), "darkviolet", Resources.getInstance()),
            new ColorPair(new Color(0x9932CC), "darkorchid", Resources.getInstance()),
            new ColorPair(new Color(0x8B008B), "darkmagenta", Resources.getInstance()),
            new ColorPair(new Color(0x800080), "purple", Resources.getInstance()),
            new ColorPair(new Color(0x4B0082), "indigo", Resources.getInstance()),
            new ColorPair(new Color(0x483D8B), "darkslateblue", Resources.getInstance()),
            new ColorPair(new Color(0x6A5ACD), "slateblue", Resources.getInstance()),
            new ColorPair(new Color(0x7B68EE), "mediumslateblue", Resources.getInstance()),
            new ColorPair(new Color(0xFFFFFF), "white", Resources.getInstance()),
            new ColorPair(new Color(0xFFFAFA), "snow", Resources.getInstance()),
            new ColorPair(new Color(0xF0FFF0), "honeydew", Resources.getInstance()),
            new ColorPair(new Color(0xF5FFFA), "mintcream", Resources.getInstance()),
            new ColorPair(new Color(0xF0FFFF), "azure", Resources.getInstance()),
            new ColorPair(new Color(0xF0F8FF), "aliceblue", Resources.getInstance()),
            new ColorPair(new Color(0xF8F8FF), "ghostwhite", Resources.getInstance()),
            new ColorPair(new Color(0xF5F5F5), "whitesmoke", Resources.getInstance()),
            new ColorPair(new Color(0xFFF5EE), "seashell", Resources.getInstance()),
            new ColorPair(new Color(0xF5F5DC), "beige", Resources.getInstance()),
            new ColorPair(new Color(0xFDF5E6), "oldlace", Resources.getInstance()),
            new ColorPair(new Color(0xFFFAF0), "floralwhite", Resources.getInstance()),
            new ColorPair(new Color(0xFFFFF0), "ivory", Resources.getInstance()),
            new ColorPair(new Color(0xFAEBD7), "antiquewhite", Resources.getInstance()),
            new ColorPair(new Color(0xFAF0E6), "linen", Resources.getInstance()),
            new ColorPair(new Color(0xFFF0F5), "lavenderblush", Resources.getInstance()),
            new ColorPair(new Color(0xFFE4E1), "mistyrose", Resources.getInstance()),
            new ColorPair(new Color(0xDCDCDC), "gainsboro", Resources.getInstance()),
            new ColorPair(new Color(0xD3D3D3), "lightgray", Resources.getInstance()),
            new ColorPair(new Color(0xC0C0C0), "silver", Resources.getInstance()),
            new ColorPair(new Color(0xA9A9A9), "darkgray", Resources.getInstance()),
            new ColorPair(new Color(0x808080), "gray", Resources.getInstance()),
            new ColorPair(new Color(0x696969), "dimgray", Resources.getInstance()),
            new ColorPair(new Color(0x778899), "lightslategray", Resources.getInstance()),
            new ColorPair(new Color(0x708090), "slategray", Resources.getInstance()),
            new ColorPair(new Color(0x2F4F4F), "darkslategray", Resources.getInstance()),
            new ColorPair(new Color(0x000000), "black", Resources.getInstance()),
    };

}
