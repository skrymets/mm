package freemind.modes.browsemode;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class BrowsePopupMenu extends JPopupMenu implements PopupMenuListener {

    private final BrowseController c;

    protected void add(Action action, String keystroke) {
        JMenuItem item = add(action);
        item.setAccelerator(KeyStroke.getKeyStroke(c.getFrame()
                .getAdjustableProperty(keystroke)));
    }

    public BrowsePopupMenu(BrowseController c) {
        this.c = c;
        add(c.find, "keystroke_find");
        add(c.findNext, "keystroke_find_next");
        add(c.followLink, "keystroke_follow_link");

        addSeparator();

        add(c.toggleFolded, "keystroke_toggle_folded");
        add(c.toggleChildrenFolded, "keystroke_toggle_children_folded");
        addSeparator();
        add(c.followMapLink, "keystroke_follow_map_link");
        addPopupMenuListener(this);
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent pE) {
        c.followMapLink.setEnabled(c.followMapLink.isEnabled(null, null));
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent pE) {
    }

    public void popupMenuCanceled(PopupMenuEvent pE) {
    }

}
