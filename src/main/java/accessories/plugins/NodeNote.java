package accessories.plugins;

import freemind.main.FreeMind;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class NodeNote extends MindMapNodeHookAdapter {

    public final static String NODE_NOTE_PLUGIN = "accessories/plugins/NodeNote.properties";

    public final static String EMPTY_EDITOR_STRING = "<html>\n  <head>\n\n  </head>\n  <body>\n    <p>\n      \n    </p>\n  </body>\n</html>\n";

    public final static String EMPTY_EDITOR_STRING_ALTERNATIVE = "<html>\n  <head>\n    \n  </head>\n  <body>\n    <p>\n      \n    </p>\n  </body>\n</html>\n";
    public final static String EMPTY_EDITOR_STRING_ALTERNATIVE2 = "<html>\n  <head>\n    \n    \n  </head>\n  <body>\n    <p>\n      \n    </p>\n  </body>\n</html>\n";

    public void startupMapHook() {
        super.startupMapHook();
        String foldingType = getResourceString("command");
        // get registration:
        log.info("processing command {}", foldingType);
        if (foldingType.equals("jump")) {
            // jump to the notes:
            getSplitPaneToScreen();
        } else {
            NodeNoteRegistration registration = getRegistration();
            // show hidden window:
            if (!registration.getSplitPaneVisible()) {
                // the window is currently hidden. show it:
                getSplitPaneToScreen();
            } else {
                // it is shown, hide it:
                registration.hideNotesPanel();
                setShowSplitPaneProperty(false);
                getMindMapController().obtainFocusForSelected();
            }

        }
    }

    private NodeNoteRegistration getRegistration() {
        NodeNoteRegistration registration = (NodeNoteRegistration) this
                .getPluginBaseClass();
        return registration;
    }

    private void getSplitPaneToScreen() {
        NodeNoteRegistration registration = getRegistration();
        if (!registration.getSplitPaneVisible()) {
            // the split pane isn't visible. show it.
            registration.showNotesPanel();
            setShowSplitPaneProperty(true);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .clearGlobalFocusOwner();
        NodeNoteRegistration.getHtmlEditorPanel(getMindMapController().getResources()).getMostRecentFocusOwner()
                .requestFocus();
    }

    private void setShowSplitPaneProperty(boolean pValue) {
        getMindMapController().setProperty(FreeMind.RESOURCES_SHOW_NOTE_PANE,
                pValue ? "true" : "false");
    }
}
