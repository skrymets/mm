package freemind.modes;

import javax.swing.*;

public interface IconInformation {
    String getDescription();

    ImageIcon getIcon();

    String getKeystrokeResourceName();

    KeyStroke getKeyStroke();
}
