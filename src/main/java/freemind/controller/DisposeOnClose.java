package freemind.controller;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

public class DisposeOnClose extends ComponentAdapter implements Serializable {
    public void componentHidden(ComponentEvent e) {
        Window w = (Window) e.getComponent();
        w.dispose();
    }
}
