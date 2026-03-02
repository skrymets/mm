package freemind.modes.filemode;

import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.extensions.HookFactory;
import freemind.model.MapAdapter;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.common.actions.NewMapAction;
import freemind.modes.viewmodes.ViewControllerAdapter;
import freemind.view.mindmapview.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

@SuppressWarnings("serial")
public class FileController extends ViewControllerAdapter {

    final Action newMap = new NewMapAction(this);
    final Action center = new CenterAction();
    final Action openPath = new OpenPathAction();

    private final JPopupMenu popupmenu = new FilePopupMenu(this);

    public FileController(Mode mode) {
        super(mode);
    }

    public JToolBar getModeToolBar() {
        return ((FileMode) getMode()).getToolbar();
    }

    public MapAdapter newModel(ModeController modeController) {
        FileMapModel model = new FileMapModel(getFrame(), modeController);
        modeController.setModel(model);
        return model;
    }

    public MindMapNode newNode(Object userObject, MindMap map) {
        return new FileNodeModel((File) userObject, map);
    }

    public JPopupMenu getPopupMenu() {
        return this.popupmenu;
    }

    // -----------------------------------------------------------------------------------

    // Private
    //

    private class CenterAction extends AbstractAction {
        CenterAction() {
            super(getController().getResourceString("center"));
        }

        public void actionPerformed(ActionEvent e) {
            if (getSelected() != null) {
                MindMap map = new FileMapModel(
                        ((FileNodeModel) getSelected()).getFile(), getFrame(),
                        /*
                         * DON'T COPY THIS, AS THIS IS A BAD HACK! The
                         * Constructor needs a new instance of a modecontroller.
                         */
                        FileController.this);
                newMap(map, FileController.this);
            }
        }
    }

    private class OpenPathAction extends AbstractAction {
        OpenPathAction() {
            super(getController().getResourceString("open"));
        }

        public void actionPerformed(ActionEvent e) {
            String inputValue = JOptionPane.showInputDialog(getController()
                    .getView().getSelectionService().getSelected(), getText("open"), "");
            if (inputValue != null) {
                File newCenter = new File(inputValue);
                if (newCenter.exists()) { // and is a folder
                    MindMap map = new FileMapModel(newCenter, getFrame(),
                            /*
                             * DON'T COPY THIS, AS THIS IS A BAD HACK! The Constructor
                             * needs a new instance of a modecontroller.
                             */
                            FileController.this);
                    newMap(map, FileController.this);
                }
            }
        }
    }

    public void updateMenus(StructuredMenuHolder holder) {
        add(holder, MenuBar.EDIT_MENU + "/find", find, "keystroke_find");
        add(holder, MenuBar.EDIT_MENU + "/findNext", findNext,
                "keystroke_find_next");
        add(holder, MenuBar.EDIT_MENU + "/openPath", openPath, null);
    }

    public HookFactory getHookFactory() {
        throw new IllegalArgumentException("Not implemented yet.");
    }

    public void plainClick(MouseEvent e) {
        /* perform action only if one selected node. */
        if (getSelecteds().size() != 1)
            return;
        final MainView component = (MainView) e.getComponent();
        if (component.isInFollowLinkRegion(e.getX())) {
            loadURL();
        } else {
            MindMapNode node = (component).getNodeView().getModel();
            toggleFolded(node);
        }
    }

    private void toggleFolded(MindMapNode node) {
        if (node.hasChildren() && !node.isRoot()) {
            setFolded(node, !node.isFolded());
        }
    }

    @Override
    protected void loadInternally(URL pUrl, MapAdapter pModel) {
        // empty on purpose.
    }

    @Override
    public void out(String pFormat) {
        getFrame().setStatusText(pFormat);
    }

}
