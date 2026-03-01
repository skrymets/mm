/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package freemind.modes.mindmapmode;

import freemind.main.SwingUtils;

import org.apache.commons.lang3.SystemUtils;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.common.XmlBindingTools;
import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.*;
import freemind.extensions.*;
import freemind.extensions.HookFactory.RegistrationContainer;
import freemind.main.*;
import freemind.model.*;
import freemind.model.MindMap.MapSourceChangedObserver;
import freemind.modes.*;
import freemind.modes.attributes.Attribute;
import freemind.modes.common.CommonNodeKeyListener;
import freemind.modes.common.GotoLinkNodeAction;
import freemind.modes.common.actions.FindAction;
import freemind.modes.common.actions.FindAction.FindNextAction;
import freemind.modes.common.actions.NewMapAction;
import freemind.modes.common.listeners.CommonNodeMouseMotionListener;
import freemind.modes.mindmapmode.actions.*;
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction.RemoveNodeBackgroundColorAction;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActionRegistry;
import freemind.modes.mindmapmode.actions.xml.DefaultActionHandler;
import freemind.modes.mindmapmode.actions.xml.UndoActionHandler;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;
import freemind.modes.mindmapmode.listeners.MindMapMouseMotionManager;
import freemind.modes.mindmapmode.listeners.MindMapNodeDropListener;
import freemind.modes.mindmapmode.listeners.MindMapNodeMotionListener;
import freemind.modes.mindmapmode.services.*;
import freemind.view.MapModule;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("serial")
@Slf4j
public class MindMapController extends ControllerAdapter implements ExtendedMapFeedback, MapSourceChangedObserver {

    public static final String REGEXP_FOR_NUMBERS_IN_STRINGS = "([+\\-]?\\d*[.,]?\\d+)\\b";

    public interface MindMapControllerPlugin {
    }

    // for MouseEventHandlers
    private final HashSet<MouseWheelEventHandler> mRegisteredMouseWheelEventHandler = new HashSet<>();

    private final ActionRegistry actionFactory;
    private final ArrayList<HookAction> hookActions = new ArrayList<>();
    // Mode mode;
    private MindMapPopupMenu popupmenu;
    // private JToolBar toolbar;
    private MindMapToolBar toolbar;
    private Clipboard clipboard = null;
    private Clipboard selection = null;

    @Setter
    private HookFactory nodeHookFactory;

    public ApplyPatternAction[] patterns = new ApplyPatternAction[0]; // Make
    // sure
    // it is
    // initialized
    public Action newMap = new NewMapAction(this);
    public Action open = new OpenAction(this);
    public final Action save = new SaveAction();
    public final Action saveAs = new SaveAsAction();
    public final Action exportToHTML = new ExportToHTMLAction(this);
    public final Action exportBranchToHTML = new ExportBranchToHTMLAction(this);

    public final Action editLong = new EditLongAction(this);
    public final Action newSibling = new NewSiblingAction(this);
    public final Action newPreviousSibling = new NewPreviousSiblingAction(this);
    public final Action setLinkByFileChooser = new SetLinkByFileChooserAction(this);
    public final Action setImageByFileChooser = new SetImageByFileChooserAction(this);
    public final Action followLink = new FollowLinkAction(this);
    public Action openLinkDirectory = new OpenLinkDirectoryAction(this);
    public final Action exportBranch = new ExportBranchAction(this);
    public final Action importBranch = new ImportBranchAction(this);
    public final Action importLinkedBranch = new ImportLinkedBranchAction(this);
    public final Action importLinkedBranchWithoutRoot = new ImportLinkedBranchWithoutRootAction(this);

    public Action propertyAction = null;

    public final Action increaseNodeFont = new NodeGeneralAction(this, "increase_node_font_size", null, (map, node) -> increaseFontSize(node, 1));
    public final Action decreaseNodeFont = new NodeGeneralAction(this, "decrease_node_font_size", null, (map, node) -> increaseFontSize(node, -1));

    public UndoAction undo = null;
    public RedoAction redo = null;
    public CopyAction copy = null;
    public Action copySingle = null;
    public CutAction cut = null;
    public PasteAction paste = null;
    public PasteAsPlainTextAction pasteAsPlainText = null;
    public BoldAction bold = null;
    public StrikethroughAction strikethrough = null;
    public ItalicAction italic = null;
    public UnderlinedAction underlined = null;
    public FontSizeAction fontSize = null;
    public FontFamilyAction fontFamily = null;
    public NodeColorAction nodeColor = null;
    public EditAction edit = null;
    public NewChildAction newChild = null;
    public DeleteChildAction deleteChild = null;
    public ToggleFoldedAction toggleFolded = null;
    public ToggleChildrenFoldedAction toggleChildrenFolded = null;
    public UseRichFormattingAction useRichFormatting = null;
    public UsePlainTextAction usePlainText = null;
    public NodeUpAction nodeUp = null;
    public NodeDownAction nodeDown = null;
    public EdgeColorAction edgeColor = null;
    public EdgeWidthAction EdgeWidth_WIDTH_PARENT = null;
    public EdgeWidthAction EdgeWidth_WIDTH_THIN = null;
    public EdgeWidthAction EdgeWidth_1 = null;
    public EdgeWidthAction EdgeWidth_2 = null;
    public EdgeWidthAction EdgeWidth_4 = null;
    public EdgeWidthAction EdgeWidth_8 = null;
    public EdgeWidthAction[] edgeWidths = null;
    public EdgeStyleAction EdgeStyle_linear = null;
    public EdgeStyleAction EdgeStyle_bezier = null;
    public EdgeStyleAction EdgeStyle_sharp_linear = null;
    public EdgeStyleAction EdgeStyle_sharp_bezier = null;
    public EdgeStyleAction[] edgeStyles = null;
    public NodeColorBlendAction nodeColorBlend = null;
    public NodeStyleAction fork = null;
    public NodeStyleAction bubble = null;
    public CloudAction cloud = null;
    public freemind.modes.mindmapmode.actions.CloudColorAction cloudColor = null;
    public AddArrowLinkAction addArrowLinkAction = null;
    public RemoveArrowLinkAction removeArrowLinkAction = null;
    public ColorArrowLinkAction colorArrowLinkAction = null;
    public ChangeArrowsInArrowLinkAction changeArrowsInArrowLinkAction = null;
    public NodeBackgroundColorAction nodeBackgroundColor = null;
    public RemoveNodeBackgroundColorAction removeNodeBackgroundColor = null;

    public IconAction unknownIconAction = null;
    public RemoveIconAction removeLastIconAction = null;
    public RemoveAllIconsAction removeAllIconsAction = null;
    public SetLinkByTextFieldAction setLinkByTextField = null;
    public AddLocalLinkAction addLocalLinkAction = null;
    public GotoLinkNodeAction gotoLinkNodeAction = null;
    public JoinNodesAction joinNodes = null;
    public MoveNodeAction moveNodeAction = null;
    public ImportExplorerFavoritesAction importExplorerFavorites = null;
    public ImportFolderStructureAction importFolderStructure = null;

    public FindAction find = null;
    public FindNextAction findNext = null;
    public SearchAction searchAllMaps = null;
    public NodeHookAction nodeHookAction = null;
    public RevertAction revertAction = null;
    public SelectBranchAction selectBranchAction = null;
    public SelectAllAction selectAllAction = null;

    // Extension Actions
    public final ArrayList<IconAction> iconActions = new ArrayList<>(); // fc

    final FileFilter filefilter;

    private MenuStructure mMenuStructure;
    private List<HookRegistration> mRegistrations;
    private List<Pattern> mPatternsList = new ArrayList<>();
    private long mGetEventIfChangedAfterThisTimeInMillies = 0;

    // Extracted services
    private MenuConfigService menuConfigService;
    private FileManagementService fileManagementService;
    private SelectionService selectionService;
    private TextOperationService textOperationService;

    public MindMapController(Mode mode) {
        super(mode);
        filefilter = new MindMapFilter(getResources());
        nodeHookFactory = new MindMapHookFactory(getResources());
        // create action factory:
        actionFactory = new ActionRegistry();
        // create node information timer and actions. They don't fire, until called to do so.
        mNodeInformationTimerAction = new NodeInformationTimerAction(this);
        mNodeInformationTimer = new Timer(100, mNodeInformationTimerAction);
        mNodeInformationTimer.setRepeats(false);

        init();
    }

    protected void init() {
        log.info("createXmlActions");
        mActorFactory = new XmlActorFactory(this);

        // Initialize extracted services
        iconService = new IconService(mActorFactory);
        linkService = new LinkService(mActorFactory);
        nodeFormattingService = new NodeFormattingService(mActorFactory);
        treeStructureService = new TreeStructureService(mActorFactory);
        clipboardService = new ClipboardService(mActorFactory, this);
        editingService = new EditingService(mActorFactory);
        hookService = new HookService(mActorFactory);
        attributeService = new AttributeService(mActorFactory);
        fileManagementService = new FileManagementService(this);
        selectionService = new SelectionService(this);
        textOperationService = new TextOperationService(this);
        menuConfigService = new MenuConfigService(this);

        log.info("createIconActions");
        // create standard actions:
        createStandardActions();
        // icon actions:
        createIconActions();

        log.info("createNodeHookActions");
        // node hook actions:
        createNodeHookActions();

        log.info("mindmap_menus");
        // load menus:
        try (InputStream in = this.getFrame().getResource("mindmap_menus.xml").openStream()) {
            mMenuStructure = updateMenusFromXml(in);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }

        log.info("MindMapPopupMenu");
        popupmenu = new MindMapPopupMenu(this);

        log.info("MindMapToolBar");
        toolbar = new MindMapToolBar(this);

        mRegistrations = new ArrayList<>();

        propertyAction = getController().propertyAction;
    }

    private void createStandardActions() {
        // prepare undo:
        undo = new UndoAction(this);
        redo = new RedoAction(this);
        // register default action handler:
        // the executor must be the first here, because it is executed last then.
        getActionRegistry().registerHandler(new DefaultActionHandler(getActionRegistry()));
        getActionRegistry().registerUndoHandler(new UndoActionHandler(this, undo, redo));
        // debug:
        // getActionFactory().registerHandler(new freemind.modes.mindmapmode.actions.xml.PrintActionHandler(this));

        cut = new CutAction(this);
        paste = new PasteAction(this);
        pasteAsPlainText = new PasteAsPlainTextAction(this);
        copy = new CopyAction(this);
        copySingle = new CopySingleAction(this);
        bold = new BoldAction(this);
        strikethrough = new StrikethroughAction(this);
        italic = new ItalicAction(this);
        underlined = new UnderlinedAction(this);
        fontSize = new FontSizeAction(this);
        fontFamily = new FontFamilyAction(this);
        edit = new EditAction(this);
        useRichFormatting = new UseRichFormattingAction(this);
        usePlainText = new UsePlainTextAction(this);
        newChild = new NewChildAction(this);
        deleteChild = new DeleteChildAction(this);
        toggleFolded = new ToggleFoldedAction(this);
        toggleChildrenFolded = new ToggleChildrenFoldedAction(this);
        nodeUp = new NodeUpAction(this);
        nodeDown = new NodeDownAction(this);
        edgeColor = new EdgeColorAction(this);
        nodeColor = new NodeColorAction(this);
        nodeColorBlend = new NodeColorBlendAction(this);
        fork = new NodeStyleAction(this, MindMapNode.STYLE_FORK);
        bubble = new NodeStyleAction(this, MindMapNode.STYLE_BUBBLE);
        // this is an unknown icon and thus corrected by mindicon:
        removeLastIconAction = new RemoveIconAction(this);
        // this action handles the xml stuff: (undo etc.)
        unknownIconAction = new IconAction(this, MindIcon.factory(MindIcon.getAllIconNames().get(0)), removeLastIconAction);
        removeLastIconAction.setIconAction(unknownIconAction);
        removeAllIconsAction = new RemoveAllIconsAction(this, unknownIconAction);
        // load pattern actions:
        loadPatternActions();

        EdgeWidth_WIDTH_PARENT = new EdgeWidthAction(this, EdgeAdapter.WIDTH_PARENT);
        EdgeWidth_WIDTH_THIN = new EdgeWidthAction(this, EdgeAdapter.WIDTH_THIN);
        EdgeWidth_1 = new EdgeWidthAction(this, 1);
        EdgeWidth_2 = new EdgeWidthAction(this, 2);
        EdgeWidth_4 = new EdgeWidthAction(this, 4);
        EdgeWidth_8 = new EdgeWidthAction(this, 8);
        edgeWidths = new EdgeWidthAction[]{EdgeWidth_WIDTH_PARENT, EdgeWidth_WIDTH_THIN, EdgeWidth_1, EdgeWidth_2, EdgeWidth_4, EdgeWidth_8};

        EdgeStyle_linear = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_LINEAR);
        EdgeStyle_bezier = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_BEZIER);
        EdgeStyle_sharp_linear = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_SHARP_LINEAR);
        EdgeStyle_sharp_bezier = new EdgeStyleAction(this, EdgeAdapter.EDGESTYLE_SHARP_BEZIER);
        edgeStyles = new EdgeStyleAction[]{EdgeStyle_linear, EdgeStyle_bezier, EdgeStyle_sharp_linear, EdgeStyle_sharp_bezier};

        cloud = new CloudAction(this);
        cloudColor = new CloudColorAction(this);
        addArrowLinkAction = new AddArrowLinkAction(this);
        removeArrowLinkAction = new RemoveArrowLinkAction(this, null);
        colorArrowLinkAction = new ColorArrowLinkAction(this, null);
        changeArrowsInArrowLinkAction = new ChangeArrowsInArrowLinkAction(this, "none", null, null, true, true);
        nodeBackgroundColor = new NodeBackgroundColorAction(this);
        removeNodeBackgroundColor = new RemoveNodeBackgroundColorAction(this);
        setLinkByTextField = new SetLinkByTextFieldAction(this);
        addLocalLinkAction = new AddLocalLinkAction(this);
        gotoLinkNodeAction = new GotoLinkNodeAction(this, null);
        moveNodeAction = new MoveNodeAction(this);
        joinNodes = new JoinNodesAction(this);
        importExplorerFavorites = new ImportExplorerFavoritesAction(this);
        importFolderStructure = new ImportFolderStructureAction(this);
        find = new FindAction(this);
        findNext = new FindNextAction(this, find);
        searchAllMaps = new SearchAction(this);
        nodeHookAction = new NodeHookAction("no_title", this);
        revertAction = new RevertAction(this);
        selectBranchAction = new SelectBranchAction(this);
        selectAllAction = new SelectAllAction(this);
    }

    /**
     * Tries to load the user patterns and proposes an update to the new format,
     * if they are old fashioned (this is determined by having an exception
     * while reading the pattern file).
     */
    private void loadPatternActions() {
        try {
            loadPatterns(getPatternsXML());
        } catch (Exception ex) {
            log.error("Patterns not loaded", ex);
//            // repair old patterns:
//            String repairTitle = "Repair patterns";
//            File patternsFile = getFrame().getPatternsXML();
//            int result = JOptionPane.showConfirmDialog(null,
//                    "<html>The pattern file format has changed, <br>"
//                            + "and it seems, that your pattern file<br>"
//                            + "'"
//                            + patternsFile.getAbsolutePath()
//                            + "'<br> is formatted in the old way. <br>"
//                            + "Should I try to repair the pattern file <br>"
//                            + "(otherwise, you should update it by hand or delete it)?",
//                    repairTitle, JOptionPane.YES_NO_OPTION);
//            if (result == JOptionPane.YES_OPTION) {
//                // try xslt script:
//                boolean success = false;
//                try {
//                    loadPatterns(Tools.getUpdateReader(Tools.getReaderFromFile(patternsFile), "patterns_updater.xslt"));
//                    // save patterns directly:
//                    StylePatternFactory.savePatterns(new FileWriter(patternsFile), mPatternsList);
//                    success = true;
//                } catch (Exception e) {
//                    log.error(e.getLocalizedMessage(), e);
//                }
//                if (success) {
//                    JOptionPane.showMessageDialog(null,
//                            "Successfully repaired the pattern file.",
//                            repairTitle, JOptionPane.PLAIN_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(null,
//                            "An error occured repairing the pattern file.",
//                            repairTitle, JOptionPane.WARNING_MESSAGE);
//                }
//            }
        }
    }

    public String getPatternsXML() {
        return getFrame().getPatternsXML();
    }

    /**
     * @return the patternsList
     */
    public List<Pattern> getPatternsList() {
        return mPatternsList;
    }

    public boolean isUndoAction() {
        return undo.isUndoAction() || redo.isUndoAction();
    }

    protected void loadInternally(URL url, MapAdapter model) throws URISyntaxException, IOException {
        fileManagementService.loadInternally(url, model);
    }

    public MindMapNode loadTree(final File pFile) throws IOException {
        return fileManagementService.loadTree(pFile);
    }

    public MindMapNode loadTree(Tools.ReaderCreator pReaderCreator) throws IOException {
        return fileManagementService.loadTree(pReaderCreator);
    }

    /**
     * Creates the patterns actions (saved in array patterns), and the pure
     * patterns list (saved in mPatternsList).
     *
     */
    public void loadPatterns(String patternsXML) throws Exception {
        createPatterns(StylePatternFactory.loadPatterns(patternsXML, getResources()));
    }

    private void createPatterns(List<Pattern> patternsList) {
        mPatternsList = patternsList;
        patterns = new ApplyPatternAction[patternsList.size()];
        for (int i = 0; i < patterns.length; i++) {
            Pattern actualPattern = patternsList.get(i);
            patterns[i] = new ApplyPatternAction(this, actualPattern);

            // search icons for patterns:
            PatternIcon patternIcon = actualPattern.getPatternIcon();
            if (patternIcon != null && patternIcon.getValue() != null) {
                patterns[i].putValue(Action.SMALL_ICON, MindIcon.factory(patternIcon.getValue()).getIcon());
            }
        }
    }

    /**
     * This method is called after and before a change of the map module. Use it
     * to perform the actions that cannot be performed at creation time.
     */
    public void startupController() {
        super.startupController();
        getToolBar().startup();
        HookFactory hookFactory = getHookFactory();
        List<RegistrationContainer> pluginRegistrations = hookFactory.getRegistrations();
        log.trace("mScheduledActions are executed: {}", pluginRegistrations.size());
        for (RegistrationContainer container : pluginRegistrations) {
            // call constructor:
            try {
                Class registrationClass = container.hookRegistrationClass;
                Constructor hookConstructor = registrationClass.getConstructor(ModeController.class, MindMap.class);
                HookRegistration registrationInstance = (HookRegistration) hookConstructor.newInstance(new Object[]{this, getMap()});
                // register the instance to enable basePlugins.
                hookFactory.registerRegistrationContainer(container, registrationInstance);
                registrationInstance.register();
                mRegistrations.add(registrationInstance);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        invokeHooksRecursively(getRootNode(), getMap());

        // register mouse motion handler:
        getMapMouseMotionListener().register(new MindMapMouseMotionManager(this));
        getNodeDropListener().register(new MindMapNodeDropListener(this));
        getNodeKeyListener().register(new CommonNodeKeyListener(this, MindMapController.this::edit));
        getNodeMotionListener().register(new MindMapNodeMotionListener(this));
        getNodeMouseMotionListener().register(new CommonNodeMouseMotionListener(this));
        getMap().registerMapSourceChangedObserver(this, mGetEventIfChangedAfterThisTimeInMillies);
    }

    public void shutdownController() {
        super.shutdownController();
        for (HookRegistration registrationInstance : mRegistrations) {
            registrationInstance.deRegister();
        }
        getHookFactory().deregisterAllRegistrationContainer();
        mRegistrations.clear();
        // deregister motion handler
        getMapMouseMotionListener().deregister();
        getNodeDropListener().deregister();
        getNodeKeyListener().deregister();
        getNodeMotionListener().deregister();
        getNodeMouseMotionListener().deregister();
        mGetEventIfChangedAfterThisTimeInMillies = getMap().deregisterMapSourceChangedObserver(this);
        getToolBar().shutdown();

    }

    public MapAdapter newModel(ModeController modeController) {
        MindMapMapModel model = new MindMapMapModel(modeController);
        modeController.setModel(model);
        return model;
    }

    private void createIconActions() {
        List<String> iconNames = MindIcon.getAllIconNames();
        File iconDir = new File(getResources().getFreemindDirectory(), "icons");
        if (iconDir.exists()) {
            String[] userIconArray = iconDir.list((dir, name) -> name.matches(".*\\.png"));
            if (userIconArray != null) {
                for (String s : userIconArray) {
                    String iconName = s;
                    iconName = iconName.substring(0, iconName.length() - 4);
                    if (iconName.isEmpty()) {
                        continue;
                    }
                    iconNames.add(iconName);
                }
            }
        }
        for (String iconName : iconNames) {
            MindIcon myIcon = MindIcon.factory(iconName);
            IconAction myAction = new IconAction(this, myIcon, removeLastIconAction);
            iconActions.add(myAction);
        }
    }

    private void createNodeHookActions() {
        MindMapHookFactory factory = (MindMapHookFactory) getHookFactory();
        List<String> nodeHookNames = factory.getPossibleNodeHooks();
        for (String hookName : nodeHookNames) {
            // create hook action.
            NodeHookAction action = new NodeHookAction(hookName, this);
            hookActions.add(action);
        }
        List<String> modeControllerHookNames = factory.getPossibleModeControllerHooks();
        for (String hookName : modeControllerHookNames) {
            MindMapControllerHookAction action = new MindMapControllerHookAction(hookName, this);
            hookActions.add(action);
        }
    }

    public FileFilter getFileFilter() {
        return filefilter;
    }

    public void nodeChanged(MindMapNode n) {
        super.nodeChanged(n);
        final MapModule mapModule = getController().getMapModule();
        // only for the selected node (fc, 2.5.2004)
        if (mapModule != null && n == mapModule.getModeController().getSelected()) {
            updateToolbar(n);
            updateNodeInformation();
        }
    }

    @Override
    public void nodeStyleChanged(MindMapNode node) {
        nodeChanged(node);
        final ListIterator<MindMapNode> childrenFolded = node.childrenFolded();
        while (childrenFolded.hasNext()) {
            MindMapNode child = childrenFolded.next();
            if (!(child.hasStyle() && child.getEdge().hasStyle())) {
                nodeStyleChanged(child);
            }
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.modes.ControllerAdapter#onFocusNode(freemind.view.mindmapview
     * .NodeView)
     */
    public void onFocusNode(NodeView pNode) {
        super.onFocusNode(pNode);
        MindMapNode model = pNode.getModel();
        updateToolbar(model);
        updateNodeInformation();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.modes.ControllerAdapter#onLostFocusNode(freemind.view.mindmapview
     * .NodeView)
     */
    public void onLostFocusNode(NodeView pNode) {
        super.onLostFocusNode(pNode);
        updateNodeInformation();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.modes.ControllerAdapter#changeSelection(freemind.view.mindmapview
     * .NodeView, boolean)
     */
    public void changeSelection(NodeView pNode, boolean pIsSelected) {
        super.changeSelection(pNode, pIsSelected);
        updateNodeInformation();
    }

    /**
     * Updates the status line.
     */
    private void updateNodeInformation() {
        mNodeInformationTimer.stop();
        if (mNodeInformationTimerAction.isRunning()) {
            mNodeInformationTimerAction.interrupt();
        }
        mNodeInformationTimer.start();
    }

    protected void updateToolbar(MindMapNode n) {
        toolbar.selectFontSize(n.getFontSize());
        toolbar.selectFontName(n.getFontFamilyName());
        toolbar.selectColor(n.getColor());
    }

    // fc, 14.12.2004: changes, such that different models can be used:
    private NewNodeCreator myNewNodeCreator = null;
    /**
     * A general list of MindMapControllerPlugin s. Members need to be tested
     * for the right class and casted to be applied.
     */
    private final HashSet<MindMapControllerPlugin> mPlugins = new HashSet<>();
    private final Timer mNodeInformationTimer;
    private final NodeInformationTimerAction mNodeInformationTimerAction;
    private XmlActorFactory mActorFactory;

    // Extracted services for reduced coupling
    private IconService iconService;
    private LinkService linkService;
    private NodeFormattingService nodeFormattingService;
    private TreeStructureService treeStructureService;
    private ClipboardService clipboardService;
    private EditingService editingService;
    private HookService hookService;
    private AttributeService attributeService;

    public interface NewNodeCreator {

        MindMapNode createNode(Object userObject, MindMap map);
    }

    public static class DefaultMindMapNodeCreator implements NewNodeCreator {

        public MindMapNode createNode(Object userObject, MindMap map) {
            return new MindMapNodeModel(userObject, map);
        }

    }

    public void setNewNodeCreator(NewNodeCreator creator) {
        myNewNodeCreator = creator;
    }

    public MindMapNode newNode(Object userObject, MindMap map) {
        // singleton default:
        if (myNewNodeCreator == null) {
            myNewNodeCreator = new DefaultMindMapNodeCreator();
        }
        return myNewNodeCreator.createNode(userObject, map);
    }

    // fc, 14.12.2004: end "different models" change
    // get/set methods

    public void updateMenus(StructuredMenuHolder holder) {
        menuConfigService.updateMenus(holder);
    }

    public void addIconsToMenu(StructuredMenuHolder holder, String iconMenuString) {
        menuConfigService.addIconsToMenu(holder, iconMenuString);
    }

    public void createPatternSubMenu(StructuredMenuHolder holder, String formatMenuString) {
        menuConfigService.createPatternSubMenu(holder, formatMenuString);
    }

    public MenuStructure updateMenusFromXml(InputStream in) {
        return menuConfigService.updateMenusFromXml(in);
    }

    public void processMenuCategory(StructuredMenuHolder holder, List<Object> list, String category) {
        menuConfigService.processMenuCategory(holder, list, category);
    }

    public MenuStructure getMenuStructure() {
        return mMenuStructure;
    }

    public List<HookAction> getHookActions() {
        return hookActions;
    }

    public MindMapPopupMenu getPopupMenuInternal() {
        return popupmenu;
    }

    public JMenuItem addMenuItem(StructuredMenuHolder holder, String category, Action action, String keystroke) {
        return add(holder, category, action, keystroke);
    }

    public JMenuItem addCheckBoxMenuItem(StructuredMenuHolder holder, String category, Action action, String keystroke) {
        return addCheckBox(holder, category, action, keystroke);
    }

    public JMenuItem addRadioMenuItem(StructuredMenuHolder holder, String category, Action action, String keystroke, boolean isSelected) {
        return addRadioItem(holder, category, action, keystroke, isSelected);
    }

    public JPopupMenu getPopupMenu() {
        return popupmenu;
    }

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    public JPopupMenu getPopupForModel(java.lang.Object obj) {
        if (obj instanceof MindMapArrowLinkModel) {
            // yes, this is a link.
            MindMapArrowLinkModel link = (MindMapArrowLinkModel) obj;
            JPopupMenu arrowLinkPopup = new JPopupMenu();
            // block the screen while showing popup.
            arrowLinkPopup.addPopupMenuListener(this.popupListenerSingleton);
            removeArrowLinkAction.setArrowLink(link);
            arrowLinkPopup.add(new RemoveArrowLinkAction(this, link));
            arrowLinkPopup.add(new ColorArrowLinkAction(this, link));
            arrowLinkPopup.addSeparator();
            /* The arrow state as radio buttons: */
            JRadioButtonMenuItem itemnn = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction(this, "none", "images/arrow-mode-none.png", link, false, false));
            JRadioButtonMenuItem itemnt = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction(this, "forward", "images/arrow-mode-forward.png", link, false, true));
            JRadioButtonMenuItem itemtn = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction(this, "backward", "images/arrow-mode-backward.png", link, true, false));
            JRadioButtonMenuItem itemtt = new JRadioButtonMenuItem(new ChangeArrowsInArrowLinkAction(this, "both", "images/arrow-mode-both.png", link, true, true));
            itemnn.setText(null);
            itemnt.setText(null);
            itemtn.setText(null);
            itemtt.setText(null);
            arrowLinkPopup.add(itemnn);
            arrowLinkPopup.add(itemnt);
            arrowLinkPopup.add(itemtn);
            arrowLinkPopup.add(itemtt);
            // select the right one:
            boolean a = !link.getStartArrow().equals("None");
            boolean b = !link.getEndArrow().equals("None");
            itemtt.setSelected(a && b);
            itemnt.setSelected(!a && b);
            itemtn.setSelected(a && !b);
            itemnn.setSelected(!a && !b);

            arrowLinkPopup.addSeparator();

            arrowLinkPopup.add(new GotoLinkNodeAction(this, link.getSource()));
            arrowLinkPopup.add(new GotoLinkNodeAction(this, link.getTarget()));

            arrowLinkPopup.addSeparator();
            // add all links from target and from source:
            HashSet<MindMapNode> NodeAlreadyVisited = new HashSet<>();
            NodeAlreadyVisited.add(link.getSource());
            NodeAlreadyVisited.add(link.getTarget());
            List<MindMapLink> links = getMindMapMapModel().getLinkRegistry().getAllLinks(link.getSource());
            links.addAll(getMindMapMapModel().getLinkRegistry().getAllLinks(link.getTarget()));
            for (MindMapLink mindMapLink : links) {
                MindMapArrowLinkModel foreign_link = (MindMapArrowLinkModel) mindMapLink;
                if (NodeAlreadyVisited.add(foreign_link.getTarget())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(this, foreign_link.getTarget()));
                }
                if (NodeAlreadyVisited.add(foreign_link.getSource())) {
                    arrowLinkPopup.add(new GotoLinkNodeAction(this, foreign_link.getSource()));
                }
            }
            return arrowLinkPopup;
        }
        return null;
    }

    // convenience methods
    public MindMapMapModel getMindMapMapModel() {
        return (MindMapMapModel) getMap();
    }

    public JToolBar getModeToolBar() {
        return getToolBar();
    }

    public MindMapToolBar getToolBar() {
        return toolbar;
    }

    /**
     * Enabled/Disabled all actions that are dependent on whether there is a map
     * open or not.
     */
    protected void setAllActions(boolean enabled) {
        log.trace("setAllActions:{}", enabled);
        super.setAllActions(enabled);
        // own actions
        increaseNodeFont.setEnabled(enabled);
        decreaseNodeFont.setEnabled(enabled);
        exportBranch.setEnabled(enabled);
        exportBranchToHTML.setEnabled(enabled);
        editLong.setEnabled(enabled);
        newSibling.setEnabled(enabled);
        newPreviousSibling.setEnabled(enabled);
        setLinkByFileChooser.setEnabled(enabled);
        setImageByFileChooser.setEnabled(enabled);
        followLink.setEnabled(enabled);
        for (IconAction iconAction : iconActions) {
            iconAction.setEnabled(enabled);
        }
        save.setEnabled(enabled);
        saveAs.setEnabled(enabled);
        getToolBar().setAllActions(enabled);
        exportBranch.setEnabled(enabled);
        exportToHTML.setEnabled(enabled);
        importBranch.setEnabled(enabled);
        importLinkedBranch.setEnabled(enabled);
        importLinkedBranchWithoutRoot.setEnabled(enabled);
        // hooks:
        for (HookAction hookAction : hookActions) {
            ((Action) hookAction).setEnabled(enabled);
        }
        cut.setEnabled(enabled);
        copy.setEnabled(enabled);
        copySingle.setEnabled(enabled);
        paste.setEnabled(enabled);
        pasteAsPlainText.setEnabled(enabled);
        undo.setEnabled(enabled);
        redo.setEnabled(enabled);
        edit.setEnabled(enabled);
        newChild.setEnabled(enabled);
        toggleFolded.setEnabled(enabled);
        toggleChildrenFolded.setEnabled(enabled);
        setLinkByTextField.setEnabled(enabled);
        italic.setEnabled(enabled);
        bold.setEnabled(enabled);
        strikethrough.setEnabled(enabled);
        find.setEnabled(enabled);
        findNext.setEnabled(enabled);
        searchAllMaps.setEnabled(enabled);
        addArrowLinkAction.setEnabled(enabled);
        addLocalLinkAction.setEnabled(enabled);
        nodeColorBlend.setEnabled(enabled);
        nodeUp.setEnabled(enabled);
        nodeBackgroundColor.setEnabled(enabled);
        nodeDown.setEnabled(enabled);
        importExplorerFavorites.setEnabled(enabled);
        importFolderStructure.setEnabled(enabled);
        joinNodes.setEnabled(enabled);
        deleteChild.setEnabled(enabled);
        cloud.setEnabled(enabled);
        cloudColor.setEnabled(enabled);
        // normalFont.setEnabled(enabled);
        nodeColor.setEnabled(enabled);
        edgeColor.setEnabled(enabled);
        removeLastIconAction.setEnabled(enabled);
        removeAllIconsAction.setEnabled(enabled);
        selectAllAction.setEnabled(enabled);
        selectBranchAction.setEnabled(enabled);
        removeNodeBackgroundColor.setEnabled(enabled);
        moveNodeAction.setEnabled(enabled);
        revertAction.setEnabled(enabled);
        for (EdgeWidthAction edgeWidth : edgeWidths) {
            edgeWidth.setEnabled(enabled);
        }
        fork.setEnabled(enabled);
        bubble.setEnabled(enabled);
        for (EdgeStyleAction edgeStyle : edgeStyles) {
            edgeStyle.setEnabled(enabled);
        }
        for (ApplyPatternAction pattern : patterns) {
            pattern.setEnabled(enabled);
        }
        useRichFormatting.setEnabled(enabled);
        usePlainText.setEnabled(enabled);
    }

    //
    // Actions
    // _______________________________________________________________________________

    public void setBold(MindMapNode node, boolean bolded) {
        nodeFormattingService.setBold(node, bolded);
    }

    public void setStrikethrough(MindMapNode node, boolean strikethrough) {
        nodeFormattingService.setStrikethrough(node, strikethrough);
    }

    public void setItalic(MindMapNode node, boolean isItalic) {
        nodeFormattingService.setItalic(node, isItalic);
    }

    public void setCloud(MindMapNode node, boolean enable) {
        nodeFormattingService.setCloud(node, enable);
    }

    public void setCloudColor(MindMapNode node, Color color) {
        nodeFormattingService.setCloudColor(node, color);
    }

    // Node editing
    public void setFontSize(MindMapNode node, String fontSizeValue) {
        nodeFormattingService.setFontSize(node, fontSizeValue);
    }

    public void increaseFontSize(MindMapNode node, int increment) {
        nodeFormattingService.increaseFontSize(node, increment);
    }

    public void setFontFamily(MindMapNode node, String fontFamilyValue) {
        nodeFormattingService.setFontFamily(node, fontFamilyValue);
    }

    public void setNodeColor(MindMapNode node, Color color) {
        nodeFormattingService.setNodeColor(node, color);
    }

    public void setNodeBackgroundColor(MindMapNode node, Color color) {
        nodeFormattingService.setNodeBackgroundColor(node, color);
    }

    public void blendNodeColor(MindMapNode node) {
        Color mapColor = getView().getBackground();
        Color nodeColor = node.getColor();
        if (nodeColor == null) {
            nodeColor = MapView.standardNodeTextColor;
        }
        setNodeColor(node, new Color((3 * mapColor.getRed() + nodeColor.getRed()) / 4, (3 * mapColor.getGreen() + nodeColor.getGreen()) / 4, (3 * mapColor.getBlue() + nodeColor.getBlue()) / 4));
    }

    public void setEdgeColor(MindMapNode node, Color color) {
        nodeFormattingService.setEdgeColor(node, color);
    }

    public void applyPattern(MindMapNode node, String patternName) {
        for (ApplyPatternAction patternAction : patterns) {
            if (patternAction.getPattern().getName().equals(patternName)) {
                StylePatternFactory.applyPattern(node, patternAction.getPattern(), getPatternsList(), getPlugins(), this);
                break;
            }
        }
    }

    public void applyPattern(MindMapNode node, Pattern pattern) {
        StylePatternFactory.applyPattern(node, pattern, getPatternsList(), getPlugins(), this);
    }

    public void addIcon(MindMapNode node, MindIcon icon) {
        iconService.addIcon(node, icon);
    }

    public void removeAllIcons(MindMapNode node) {
        iconService.removeAllIcons(node);
    }

    public int removeLastIcon(MindMapNode node) {
        return iconService.removeLastIcon(node);
    }

    /**
     *
     */
    public void addLink(MindMapNode source, MindMapNode target) {
        linkService.addLink(source, target);
    }

    public void removeReference(MindMapLink arrowLink) {
        linkService.removeReference(arrowLink);
    }

    public void setArrowLinkColor(MindMapLink arrowLink, Color color) {
        linkService.setArrowLinkColor(arrowLink, color);
    }

    /**
     *
     */
    public void changeArrowsOfArrowLink(MindMapArrowLink arrowLink, boolean hasStartArrow, boolean hasEndArrow) {
        linkService.changeArrowsOfArrowLink(arrowLink, hasStartArrow, hasEndArrow);
    }

    public void setArrowLinkEndPoints(MindMapArrowLink link, Point startPoint, Point endPoint) {
        linkService.setArrowLinkEndPoints(link, startPoint, endPoint);
    }

    public void setLink(MindMapNode node, String link) {
        linkService.setLink(node, link);
    }

    // edit begins with home/end or typing (PN 6.2)
    public void edit(KeyEvent e, boolean addNew, boolean editLong) {
        edit.edit(e, addNew, editLong);
    }

    public void setNodeText(MindMapNode selected, String newText) {
        editingService.setNodeText(selected, newText);
    }

    /**
     *
     */
    public void setEdgeWidth(MindMapNode node, int width) {
        nodeFormattingService.setEdgeWidth(node, width);
    }

    /**
     *
     */
    public void setEdgeStyle(MindMapNode node, String style) {
        nodeFormattingService.setEdgeStyle(node, style);
    }

    /**
     *
     */
    public void setNodeStyle(MindMapNode node, String style) {
        nodeFormattingService.setNodeStyle(node, style);
    }

    @Override
    public Transferable copy(MindMapNode node, boolean saveInvisible) {
        return clipboardService.copy(node, saveInvisible);
    }

    public Transferable cut() {
        return cut(getView().getSelectedNodesSortedByY());
    }

    public Transferable cut(List<MindMapNode> nodeList) {
        return clipboardService.cut(nodeList);
    }

    public void paste(Transferable t, MindMapNode parent) {
        clipboardService.paste(t, parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.modes.mindmapmode.actions.MindMapActions#paste(java.awt.datatransfer
     * .Transferable, freemind.model.MindMapNode, boolean, boolean)
     */
    public boolean paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
        return clipboardService.paste(t, target, asSibling, isLeft);
    }

    public void paste(MindMapNode node, MindMapNode parent) {
        clipboardService.paste(node, parent);
    }

    public MindMapNode addNew(final MindMapNode target, final int newNodeMode, final KeyEvent e) {
        edit.stopEditing();
        return newChild.addNew(target, newNodeMode, e);
    }

    public MindMapNode addNewNode(MindMapNode parent, int index, boolean newNodeIsLeft) {
        return treeStructureService.addNewNode(parent, index, newNodeIsLeft);
    }

    public void deleteNode(MindMapNode selectedNode) {
        treeStructureService.deleteNode(selectedNode);
    }

    public void toggleFolded() {
        treeStructureService.toggleFolded(getSelecteds().listIterator());
    }

    public void setFolded(MindMapNode node, boolean folded) {
        treeStructureService.setFolded(node, folded);
    }

    public void moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction) {
        treeStructureService.moveNodes(selected, selecteds, direction);
    }

    public void joinNodes(MindMapNode selectedNode, List<MindMapNode> selectedNodes) {
        joinNodes.joinNodes(selectedNode, selectedNodes);
    }

    public void setLinkByFileChooser() {
        fileManagementService.setLinkByFileChooser();
    }

    public void setImageByFileChooser() {
        fileManagementService.setImageByFileChooser();
    }

    protected String getLinkByFileChooser(FileFilter fileFilter) {
        return fileManagementService.getLinkByFileChooser(fileFilter);
    }

    public void loadURL(String relative) {
        if (getMap().getFile() == null) {
            getFrame().setStatusText("You must save the current map first!");
            boolean result = save();
            // canceled??
            if (!result) {
                return;
            }
        }
        super.loadURL(relative);
    }

    public void addHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName, Properties pHookProperties) {
        hookService.addHook(focussed, selecteds, hookName, pHookProperties);
    }

    public void removeHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName) {
        hookService.removeHook(focussed, selecteds, hookName);
    }

    public void moveNodePosition(MindMapNode node, int parentVGap, int hGap, int shiftY) {
        getActorFactory().getMoveNodeActor().moveNodeTo(node, parentVGap, hGap, shiftY);
    }

    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    public void plainClick(MouseEvent e) {
        selectionService.plainClick(e);
    }

    @Override
    public HookFactory getHookFactory() {
        return nodeHookFactory;
    }

    public NodeHook createNodeHook(String hookName, MindMapNode node) {
        HookFactory hookFactory = getHookFactory();
        NodeHook hook = hookFactory.createNodeHook(hookName);
        hook.setController(this);
        hook.setMap(getMap());

        if (hook instanceof PermanentNodeHook) {
            PermanentNodeHook permHook = (PermanentNodeHook) hook;
            if (hookFactory.getInstantiationMethod(hookName).isSingleton()) {
                // search for already instantiated hooks of this type:
                PermanentNodeHook otherHook = hookFactory.getHookInNode(node, hookName);
                if (otherHook != null) {
                    return otherHook;
                }
            }
            node.addHook(permHook);
        }
        return hook;
    }

    public void invokeHook(ModeControllerHook hook) {
        hookService.invokeHook(hook, this);
    }

    public ActionRegistry getActionRegistry() {
        return actionFactory;
    }

    public XmlActorFactory getActorFactory() {
        return mActorFactory;
    }

    static public void saveHTML(MindMapNodeModel rootNodeOfBranch, File file) throws IOException {
        BufferedWriter fileout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        MindMapHTMLWriter htmlWriter = new MindMapHTMLWriter(fileout, Resources.getInstance());
        htmlWriter.saveHTML(rootNodeOfBranch);
    }

    static public void saveHTML(List<MindMapNodeModel> mindMapNodes, Writer fileout) throws IOException {
        MindMapHTMLWriter htmlWriter = new MindMapHTMLWriter(fileout, Resources.getInstance());
        htmlWriter.saveHTML(mindMapNodes);
    }

    /**
     *
     */
    public void splitNode(MindMapNode node, int caretPosition, String newText) {
        textOperationService.splitNode(node, caretPosition, newText);
    }

    protected void updateNode(MindMapNode node) {
        super.updateNode(node);
        recursiveCallUpdateHooks(node, node /*
         * self
         * update
         */);
    }

    /**
     *
     */
    private void recursiveCallUpdateHooks(MindMapNode node, MindMapNode changedNode) {
        // Tell any node hooks that the node is changed:
        if (node instanceof MindMapNode) {
            for (PermanentNodeHook hook : node.getActivatedHooks()) {
                if ((!isUndoAction()) || hook instanceof UndoEventReceiver) {
                    if (node == changedNode) {
                        hook.onUpdateNodeHook();
                    } else {
                        hook.onUpdateChildrenHook(changedNode);
                    }
                }
            }
        }
        if (!node.isRoot() && node.getParentNode() != null) {
            recursiveCallUpdateHooks(node.getParentNode(), changedNode);
        }
    }

    public void doubleClick(MouseEvent e) {
        selectionService.doubleClick(e);
    }

    public boolean extendSelection(MouseEvent e) {
        return selectionService.extendSelection(e);
    }

    public void registerMouseWheelEventHandler(MouseWheelEventHandler handler) {
        log.trace("Registered   MouseWheelEventHandler {}", handler);
        mRegisteredMouseWheelEventHandler.add(handler);
    }

    public void deRegisterMouseWheelEventHandler(MouseWheelEventHandler handler) {
        log.trace("Deregistered MouseWheelEventHandler {}", handler);
        mRegisteredMouseWheelEventHandler.remove(handler);
    }

    public Set<MouseWheelEventHandler> getRegisteredMouseWheelEventHandler() {
        return Collections.unmodifiableSet(mRegisteredMouseWheelEventHandler);

    }

    public String marshall(XmlAction action) {
        return Tools.marshall(action);
    }

    public XmlAction unMarshall(String inputString) {
        return Tools.unMarshall(inputString);
    }

    public void storeDialogPositions(JDialog dialog, WindowConfigurationStorage pStorage, String window_preference_storage_property) {
        XmlBindingTools.getInstance().storeDialogPositions(getController(), dialog, pStorage, window_preference_storage_property);
    }

    public WindowConfigurationStorage decorateDialog(JDialog dialog, String window_preference_storage_property) {
        return XmlBindingTools.getInstance().decorateDialog(getController(), dialog, window_preference_storage_property);
    }

    public void insertNodeInto(MindMapNode newNode, MindMapNode parent, int index) {
        setSaved(false);
        getMap().insertNodeInto(newNode, parent, index);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.model.MindMap#insertNodeInto(javax.swing.tree.MutableTreeNode,
     * javax.swing.tree.MutableTreeNode)
     */
    public void insertNodeInto(MindMapNode newChild, MindMapNode parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    public void removeNodeFromParent(MindMapNode selectedNode) {
        setSaved(false);
        // first deselect, and then remove.
        NodeView nodeView = getView().getNodeView(selectedNode);
        getView().deselect(nodeView);
        getModel().removeNodeFromParent(selectedNode);
    }

    public void repaintMap() {
        getView().repaint();
    }

    /**
     * Erases all content of the node as text, colors, fonts, etc.
     */
    public void clearNodeContents(MindMapNode pNode) {
        Pattern erasePattern = StylePatternFactory.getRemoveAllPattern();
        erasePattern.setPatternNodeText(new PatternNodeText());
        applyPattern(pNode, erasePattern);
        setNoteText(pNode, null);
    }

    public void registerPlugin(MindMapControllerPlugin pPlugin) {
        mPlugins.add(pPlugin);
    }

    public void deregisterPlugin(MindMapControllerPlugin pPlugin) {
        mPlugins.remove(pPlugin);
    }

    public Set<MindMapControllerPlugin> getPlugins() {
        return Collections.unmodifiableSet(mPlugins);
    }

    public Transferable getClipboardContents() {
        getClipboard();
        return clipboard.getContents(this);
    }

    protected void getClipboard() {
        if (clipboard == null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            selection = toolkit.getSystemSelection();
            clipboard = toolkit.getSystemClipboard();
        }
    }

    public void setClipboardContents(Transferable t) {
        getClipboard();
        clipboard.setContents(t, null);
        if (selection != null) {
            selection.setContents(t, null);
        }
    }

    public boolean mapSourceChanged(MindMap pMap) throws Exception {
        // ask the user, if he wants to reload the map.
        MapSourceChangeDialog runnable = new MapSourceChangeDialog(this);
        SwingUtils.invokeAndWait(runnable);
        return runnable.getReturnValue();
    }

    /**
     * Creates and invokes a ModeControllerHook.
     */
    public void createModeControllerHook(String pHookName) {
        HookFactory hookFactory = getHookFactory();
        // two different invocation methods:single or selecteds
        ModeControllerHook hook = hookFactory.createModeControllerHook(pHookName);
        hook.setController(this);
        invokeHook(hook);
    }

    /**
     * Delegate method to Controller. Must be called after cut.s
     */
    public void obtainFocusForSelected() {
        getController().obtainFocusForSelected();

    }

    public boolean doTransaction(String pName, ActionPair pPair) {
        return actionFactory.doTransaction(pName, pPair);
    }

    @Override
    public void setAttribute(MindMapNode pNode, int pPosition, Attribute pAttribute) {
        attributeService.setAttribute(pNode, pPosition, pAttribute);
    }

    @Override
    public void insertAttribute(MindMapNode pNode, int pPosition, Attribute pAttribute) {
        attributeService.insertAttribute(pNode, pPosition, pAttribute);
    }

    @Override
    public int addAttribute(MindMapNode pNode, Attribute pAttribute) {
        return attributeService.addAttribute(pNode, pAttribute);
    }

    @Override
    public void removeAttribute(MindMapNode pNode, int pPosition) {
        attributeService.removeAttribute(pNode, pPosition);
    }

    @Override
    public void out(String pFormat) {
        getFrame().setStatusText(pFormat);
    }

    @Override
    public void close(boolean pForce) {
        getController().close(pForce);
    }

    @Override
    public void setNoteText(MindMapNode pSelected, String pNewText) {
        editingService.setNoteText(pSelected, pNewText);
    }

}
