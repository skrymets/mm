package freemind.modes.browsemode;

import freemind.controller.MenuBar;
import freemind.controller.MenuItemEnabledListener;
import freemind.controller.StructuredMenuHolder;
import freemind.extensions.HookFactory;
import freemind.main.Tools;
import freemind.model.*;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.common.GotoLinkNodeAction;
import freemind.modes.common.plugins.MapNodePositionHolderBase;
import freemind.modes.common.plugins.NodeNoteBase;
import freemind.modes.viewmodes.ViewControllerAdapter;
import freemind.view.mindmapview.MainView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import static freemind.modes.common.plugins.MapNodePositionHolderBase.*;
import static java.lang.String.format;

@SuppressWarnings("serial")

@Slf4j
public class BrowseController extends ViewControllerAdapter {

    private final JPopupMenu popupmenu;
    private final JToolBar toolbar;

    final Action followLink;

    Action nodeUp;

    Action nodeDown;

    private final HookFactory mBrowseHookFactory;
    private ImageIcon noteIcon;
    public final FollowMapLink followMapLink;

    @Slf4j
    public static class FollowMapLink extends AbstractAction implements MenuItemEnabledListener {

        private final ViewControllerAdapter modeController;

        public FollowMapLink(ViewControllerAdapter controller) {
            super(controller.getText("follow_map_link"), getMapLocationIcon());
            this.modeController = controller;
        }

        public void actionPerformed(ActionEvent e) {

            MapNodePositionHolderBase hook = getHook();
            if (hook != null) {
                String[] barePositions = hook.getBarePosition();
                try {
                    // GRR, this is doubled code :-(
                    HashMap<String, String> tileSources = new HashMap<>();
                    tileSources.put(TILE_SOURCE_MAPNIK, SHORT_MAPNIK);
                    tileSources.put(TILE_SOURCE_CYCLE_MAP, SHORT_CYCLE_MAP);
                    tileSources.put(TILE_SOURCE_TRANSPORT_MAP, SHORT_TRANSPORT_MAP);
                    tileSources.put(TILE_SOURCE_MAP_QUEST_OPEN_MAP, SHORT_MAP_QUEST_OPEN_MAP);

                    String link = format("https://www.openstreetmap.org/?mlat=%s&mlon=%s&lat=%s&lon=%s&zoom=%s&layers=%s",
                            barePositions[0],
                            barePositions[1],
                            barePositions[2],
                            barePositions[3],
                            barePositions[4],
                            tileSources.get(barePositions[5]));
                    log.trace("Try to open link {}", link);
                    modeController.getFrame().openDocument(new URL(link));
                } catch (Exception e1) {
                    log.error(e1.getLocalizedMessage(), e1);
                }
            }
        }

        protected MapNodePositionHolderBase getHook() {
            MindMapNode selected = modeController.getSelected();
            MapNodePositionHolderBase hook = getBaseHook(selected);
            return hook;
        }

        public boolean isEnabled(JMenuItem pItem, Action pAction) {
            return getHook() != null;
        }

    }

    public BrowseController(Mode mode) {
        super(mode);
        mBrowseHookFactory = new BrowseHookFactory();
        // Daniel: Actions are initialized here and not above because of
        // some error it would produce. Not studied in more detail.
        followLink = new FollowLinkAction();

        followMapLink = new FollowMapLink(this);
        popupmenu = new BrowsePopupMenu(this);
        toolbar = new BrowseToolBar(this);
        setAllActions(false);
        // for displaying notes.
        registerNodeSelectionListener(new NodeNoteViewer(this), false);
    }

    public void startupController() {
        super.startupController();
        invokeHooksRecursively(getRootNode(), getMap());
    }

    protected void restoreMapsLastState(ModeController pNewModeController,
                                        MapAdapter pModel) {
        // intentionally do nothing.
    }

    public MapAdapter newModel(ModeController newModeController) {
        BrowseMapModel model = new BrowseMapModel(null, newModeController);
        newModeController.setModel(model);
        return model;
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
            if (!node.hasChildren()) {
                // the emulate the plain click.
                doubleClick(e);
                return;
            }
            toggleFolded.toggleFolded(getSelecteds().listIterator());
        }

    }

    public void doubleClick() {
        /* If the link exists, follow the link; toggle folded otherwise */
        if (getSelected().getLink() == null) {
            toggleFolded.toggleFolded();
        } else {
            loadURL();
        }
    }

    // public void anotherNodeSelected(MindMapNode n) {
    // super.anotherNodeSelected(n);
    // if(n.isRoot()){
    // return;
    // }
    // //Presentation:
    // setFolded(n, false);
    // foldOthers(n);
    // }
    //
    //
    // private void foldOthers(MindMapNode n) {
    // if(n.isRoot()){
    // return;
    // }
    // MindMapNode parent = n.getParentNode();
    // for (Iterator iter = parent.childrenUnfolded(); iter.hasNext();) {
    // MindMapNode element = (MindMapNode) iter.next();
    // if(element != n){
    // setFolded(element, true);
    // }
    // }
    // foldOthers(parent);
    // }

    public MindMapNode newNode(Object userObject, MindMap map) {
        return new BrowseNodeModel(userObject, map);
    }

    public JPopupMenu getPopupMenu() {
        return popupmenu;
    }

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    public JPopupMenu getPopupForModel(java.lang.Object obj) {
        if (obj instanceof BrowseArrowLinkModel) {
            // yes, this is a link.
            BrowseArrowLinkModel link = (BrowseArrowLinkModel) obj;
            JPopupMenu arrowLinkPopup = new JPopupMenu();

            arrowLinkPopup.add(getGotoLinkNodeAction(link.getSource()));
            arrowLinkPopup.add(getGotoLinkNodeAction(link.getTarget()));

            arrowLinkPopup.addSeparator();
            // add all links from target and from source:
            HashSet<MindMapNode> nodeAlreadyVisited = new HashSet<>();
            nodeAlreadyVisited.add(link.getSource());
            nodeAlreadyVisited.add(link.getTarget());
            List<MindMapLink> links = getModel().getLinkRegistry().getAllLinks(link.getSource());
            links.addAll(getModel().getLinkRegistry().getAllLinks(link.getTarget()));
            for (MindMapLink mindMapLink : links) {
                BrowseArrowLinkModel foreign_link = (BrowseArrowLinkModel) mindMapLink;
                if (nodeAlreadyVisited.add(foreign_link.getTarget())) {
                    arrowLinkPopup.add(getGotoLinkNodeAction(foreign_link
                            .getTarget()));
                }
                if (nodeAlreadyVisited.add(foreign_link.getSource())) {
                    arrowLinkPopup.add(getGotoLinkNodeAction(foreign_link
                            .getSource()));
                }
            }
            return arrowLinkPopup;
        }
        return null;
    }

    private GotoLinkNodeAction getGotoLinkNodeAction(MindMapNode destination) {
        return new GotoLinkNodeAction(this, destination);
    }

    public JToolBar getModeToolBar() {
        return getToolBar();
    }

    BrowseToolBar getToolBar() {
        return (BrowseToolBar) toolbar;
    }

    // public void loadURL(String relative) {
    // // copy from mind map controller:
    // if (relative.startsWith("#")) {
    // // inner map link, fc, 12.10.2004
    // String target = relative.substring(1);
    // try {
    // MindMapNode node = getNodeFromID(target);
    // centerNode(node);
    // return;
    // } catch (Exception e) {
    // // bad luck.
    // getFrame().out(Tools.expandPlaceholders(getText("link_not_found"),
    // target));
    // return;
    // }
    // }
    //
    // URL absolute = null;
    // try {
    // absolute = new URL(getMap().getURL(), relative);
    // getFrame().out(absolute.toString());
    // } catch (MalformedURLException ex) {
    // freemind.main.Resources.getInstance().logExecption(ex);
    // getController().errorMessage(
    // getText("url_error") + " " + ex.getMessage());
    // // getFrame().err(getText("url_error"));
    // return;
    // }
    //
    // String type = Tools.getExtension(absolute.getFile());
    // try {
    // if
    // (type.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT))
    // {
    // getFrame().setWaitingCursor(true);
    // load(absolute);
    // } else {
    // getFrame().openDocument(absolute);
    // }
    // } catch (Exception ex) {
    // getController().errorMessage(getText("url_load_error") + absolute);
    // freemind.main.Resources.getInstance().logExecption( ex);
    // // for some reason, this exception is thrown anytime...
    // } finally {
    // getFrame().setWaitingCursor(false);
    // }
    //
    // }

    public ModeController load(URL url) throws IOException,
            URISyntaxException {
        ModeController newModeController = (ModeController) super.load(url);
        // decorator pattern.
        ((BrowseToolBar) newModeController.getModeToolBar()).setURLField(url
                .toString());
        return newModeController;
    }

    public ModeController load(File pFile) {
        try {
            ModeController newModeController = (ModeController) super.load(pFile);
            // decorator pattern.
            ((BrowseToolBar) newModeController.getModeToolBar()).setURLField(Tools
                    .fileToUrl(pFile).toString());
            return newModeController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void newMap(MindMap mapModel, ModeController modeController) {
        setNoteIcon(mapModel.getRootNode());
        super.newMap(mapModel, modeController);
    }

    private void setNoteIcon(MindMapNode node) {
        String noteText = node.getNoteText();
        if (noteText != null && !noteText.isEmpty()) {
            // icon
            if (noteIcon == null) {
                noteIcon = freemind.view.ImageFactory.getInstance().createUnscaledIcon(getController().getResource(
                        "images/knotes.png"));
            }
            node.setStateIcon(NodeNoteBase.NODE_NOTE_ICON, noteIcon);
        }
        ListIterator<MindMapNode> children = node.childrenUnfolded();
        while (children.hasNext()) {
            setNoteIcon(children.next());
        }

    }

    /**
     * Enabled/Disabled all actions that are dependent on whether there is a map
     * open or not.
     */
    protected void setAllActions(boolean enabled) {
        super.setAllActions(enabled);
        toggleFolded.setEnabled(enabled);
        toggleChildrenFolded.setEnabled(enabled);
        followLink.setEnabled(enabled);
    }

    // ////////
    // Actions
    // ///////

    private class FollowLinkAction extends AbstractAction {
        FollowLinkAction() {
            super(getText("follow_link"));
        }

        public void actionPerformed(ActionEvent e) {
            loadURL();
        }
    }

    public void updateMenus(StructuredMenuHolder holder) {
        add(holder, MenuBar.EDIT_MENU + "/find/find", find, "keystroke_find");
        add(holder, MenuBar.EDIT_MENU + "/find/findNext", findNext,
                "keystroke_find_next");
        add(holder, MenuBar.EDIT_MENU + "/find/followLink", followLink,
                "keystroke_follow_link");
        holder.addSeparator(MenuBar.EDIT_MENU + "/find");
        add(holder, MenuBar.EDIT_MENU + "/find/toggleFolded", toggleFolded,
                "keystroke_toggle_folded");
        add(holder, MenuBar.EDIT_MENU + "/find/toggleChildrenFolded",
                toggleChildrenFolded, "keystroke_toggle_children_folded");
    }

    public HookFactory getHookFactory() {
        return mBrowseHookFactory;
    }

    @Override
    protected void loadInternally(URL url, MapAdapter pModel)
            throws IOException {
        ((BrowseMapModel) pModel).setURL(url);
        BrowseNodeModel root = loadTree(url);
        if (root != null) {
            pModel.setRoot(root);
        } else {
            // System.err.println("Err:"+root.toString());
            throw new IOException();
        }
    }

    BrowseNodeModel loadTree(URL url) {
        BrowseNodeModel root = null;

        InputStreamReader urlStreamReader = null;

        try {
            urlStreamReader = new InputStreamReader(url.openStream());
        } catch (IOException ex) {
            final String message = format("Could not open URL %s.", url);
            getFrame().getController().errorMessage(message);
            log.error(message, ex);
            return null;
        }

        try {
            HashMap<String, NodeAdapter> IDToTarget = new HashMap<>();
            root = (BrowseNodeModel) getMap().createNodeTreeFromXml(urlStreamReader, IDToTarget);
            urlStreamReader.close();
            return root;
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    @Override
    public void out(String pFormat) {
    }

}
