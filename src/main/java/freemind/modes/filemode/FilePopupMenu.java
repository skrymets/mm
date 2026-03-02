package freemind.modes.filemode;

import javax.swing.*;

public class FilePopupMenu extends JPopupMenu {

    private final FileController c;

    protected void add(Action action, String keystroke) {
        JMenuItem item = add(action);
        item.setAccelerator(KeyStroke.getKeyStroke(c.getFrame()
                .getAdjustableProperty(keystroke)));
    }

    public FilePopupMenu(FileController c) {
        this.c = c;

        // Node menu
        this.add(c.center);
        this.addSeparator();
        this.add(c.find, "keystroke_find");
        this.add(c.findNext, "keystroke_find_next");

    }
}
