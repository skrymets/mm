package freemind.controller.color;

import freemind.main.Resources;

import java.awt.*;

public class ColorPair {
    public ColorPair(Color color, String pname) {
        this.color = color;
        name = pname;
        displayName = null;
    }

    public ColorPair(Color color, String name, String displayName) {
        this.color = color;
        this.name = name;
        this.displayName = displayName;
    }

    void resolveDisplayName(Resources resources) {
        displayName = resources.getText("font_color_" + name);
    }

    public final Color color;
    public final String name;
    public String displayName;
}
