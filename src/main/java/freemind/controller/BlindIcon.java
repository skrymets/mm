package freemind.controller;

import javax.swing.*;
import java.awt.*;

public class BlindIcon implements Icon {

    private final int length;

    public BlindIcon(int length) {
        this.length = length;
    }

    public int getIconHeight() {
        return length;
    }

    public int getIconWidth() {
        return length;
    }

    public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
    }

}
