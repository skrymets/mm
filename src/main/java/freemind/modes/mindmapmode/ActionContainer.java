package freemind.modes.mindmapmode;

import freemind.modes.NodeDownAction;
import freemind.modes.common.actions.FindAction;
import freemind.modes.common.actions.FindAction.FindNextAction;
import freemind.modes.common.GotoLinkNodeAction;
import freemind.modes.mindmapmode.actions.*;
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction.RemoveNodeBackgroundColorAction;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Holds all public action fields previously scattered across MindMapController.
 * This is a plain data container — actions are created and assigned by
 * MindMapController during initialization.
 */
public class ActionContainer {

    // Pattern actions
    public ApplyPatternAction[] patterns = new ApplyPatternAction[0];

    // File actions
    public Action newMap;
    public Action open;
    public Action save;
    public Action saveAs;
    public Action exportToHTML;
    public Action exportBranchToHTML;
    public Action exportBranch;
    public Action importBranch;
    public Action importLinkedBranch;
    public Action importLinkedBranchWithoutRoot;
    public Action propertyAction;
    public RevertAction revertAction;

    // Edit actions
    public Action editLong;
    public Action newSibling;
    public Action newPreviousSibling;
    public EditAction edit;
    public NewChildAction newChild;
    public DeleteChildAction deleteChild;
    public UndoAction undo;
    public RedoAction redo;
    public CutAction cut;
    public CopyAction copy;
    public Action copySingle;
    public PasteAction paste;
    public PasteAsPlainTextAction pasteAsPlainText;
    public JoinNodesAction joinNodes;
    public NodeUpAction nodeUp;
    public NodeDownAction nodeDown;
    public ToggleFoldedAction toggleFolded;
    public ToggleChildrenFoldedAction toggleChildrenFolded;
    public SelectBranchAction selectBranchAction;
    public SelectAllAction selectAllAction;

    // Formatting actions
    public BoldAction bold;
    public StrikethroughAction strikethrough;
    public ItalicAction italic;
    public UnderlinedAction underlined;
    public FontSizeAction fontSize;
    public FontFamilyAction fontFamily;
    public UseRichFormattingAction useRichFormatting;
    public UsePlainTextAction usePlainText;
    public Action increaseNodeFont;
    public Action decreaseNodeFont;

    // Color actions
    public NodeColorAction nodeColor;
    public NodeColorBlendAction nodeColorBlend;
    public NodeBackgroundColorAction nodeBackgroundColor;
    public RemoveNodeBackgroundColorAction removeNodeBackgroundColor;
    public EdgeColorAction edgeColor;

    // Edge actions
    public EdgeWidthAction EdgeWidth_WIDTH_PARENT;
    public EdgeWidthAction EdgeWidth_WIDTH_THIN;
    public EdgeWidthAction EdgeWidth_1;
    public EdgeWidthAction EdgeWidth_2;
    public EdgeWidthAction EdgeWidth_4;
    public EdgeWidthAction EdgeWidth_8;
    public EdgeWidthAction[] edgeWidths;
    public EdgeStyleAction EdgeStyle_linear;
    public EdgeStyleAction EdgeStyle_bezier;
    public EdgeStyleAction EdgeStyle_sharp_linear;
    public EdgeStyleAction EdgeStyle_sharp_bezier;
    public EdgeStyleAction[] edgeStyles;

    // Node style actions
    public NodeStyleAction fork;
    public NodeStyleAction bubble;
    public CloudAction cloud;
    public CloudColorAction cloudColor;

    // Link actions
    public Action setLinkByFileChooser;
    public Action setImageByFileChooser;
    public Action followLink;
    public Action openLinkDirectory;
    public SetLinkByTextFieldAction setLinkByTextField;
    public AddLocalLinkAction addLocalLinkAction;
    public GotoLinkNodeAction gotoLinkNodeAction;
    public AddArrowLinkAction addArrowLinkAction;
    public RemoveArrowLinkAction removeArrowLinkAction;
    public ColorArrowLinkAction colorArrowLinkAction;
    public ChangeArrowsInArrowLinkAction changeArrowsInArrowLinkAction;

    // Icon actions
    public IconAction unknownIconAction;
    public RemoveIconAction removeLastIconAction;
    public RemoveAllIconsAction removeAllIconsAction;
    public final ArrayList<IconAction> iconActions = new ArrayList<>();

    // Search actions
    public FindAction find;
    public FindNextAction findNext;
    public SearchAction searchAllMaps;

    // Hook/node actions
    public NodeHookAction nodeHookAction;
    public MoveNodeAction moveNodeAction;
    public ImportExplorerFavoritesAction importExplorerFavorites;
    public ImportFolderStructureAction importFolderStructure;

    // Hook actions list
    public final ArrayList<HookAction> hookActions = new ArrayList<>();
}
