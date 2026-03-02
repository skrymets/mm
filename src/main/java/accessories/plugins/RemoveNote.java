package accessories.plugins;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.controller.MenuItemEnabledListener;
import freemind.extensions.HookRegistration;
import freemind.main.FreeMind;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

public class RemoveNote extends MindMapNodeHookAdapter {
    public RemoveNote() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        super.invoke(rootNode);
        int showResult = new OptionalDontShowMeAgainDialog(
                getMindMapController().getFrame().getJFrame(),
                getMindMapController().getSelectedView(),
                "really_remove_notes", "confirmation", getMindMapController(),
                new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                        getMindMapController().getController(),
                        FreeMind.RESOURCES_REMOVE_NOTES_WITHOUT_QUESTION),
                OptionalDontShowMeAgainDialog.ONLY_OK_SELECTION_IS_STORED)
                .show().getResult();
        if (showResult != JOptionPane.OK_OPTION) {
            return;
        }

        for (MindMapNode node : getMindMapController().getSelecteds()) {
            if (node.getNoteText() != null) {
                removeNote(node);
            }
        }
    }

    private void removeNote(MindMapNode node) {
        if (getMindMapController().getSelected() == node) {
            NodeNoteRegistration.getHtmlEditorPanel(getMindMapController().getResources()).setCurrentDocumentContent("");
        }
        getMindMapController().setNoteText(node, null);
    }

    @Slf4j
    public static class Registration implements HookRegistration, MenuItemEnabledListener {

        private final MindMapController controller;

        public Registration(ModeController controller, MindMap map) {
            this.controller = (MindMapController) controller;
        }

        public boolean isEnabled(JMenuItem pItem, Action pAction) {
            if (controller == null) {
                return false;
            }

            boolean foundNote = false;
            for (MindMapNode node : controller.getSelecteds()) {
                if (node.getNoteText() != null) {
                    foundNote = true;
                    break;
                }
            }
            return foundNote;
        }

        public void deRegister() {
        }

        public void register() {
        }
    }
}
