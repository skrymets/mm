package freemind.modes.filemode;

import javax.swing.*;

public class FileToolBar extends JToolBar {

    public FileToolBar(FileController controller) {
        this.setRollover(true);

        JButton button;

        button = add(controller.newMap);
        button.setText("");

        button = add(controller.center);
    }
}
