package freemind.controller;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class ColorTracker implements ActionListener, Serializable {
    final JColorChooser chooser;
    @Getter
    Color color;

    public ColorTracker(JColorChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        color = chooser.getColor();
    }
}
