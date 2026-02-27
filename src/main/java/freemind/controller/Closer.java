package freemind.controller;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

public class Closer extends WindowAdapter implements Serializable {
    public void windowClosing(WindowEvent e) {
        Window w = e.getWindow();
        w.setVisible(false);
    }
}
