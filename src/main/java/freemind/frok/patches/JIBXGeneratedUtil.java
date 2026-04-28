package freemind.frok.patches;

import freemind.controller.actions.*;
import freemind.controller.actions.xml.operations.*;
import freemind.controller.actions.xml.plugins.*;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

public class JIBXGeneratedUtil {

    public static List<Object> listPluginChoice(Plugin plugin) {

        List<Object> pluginChoice = emptyIfNull(plugin.getChoiceList())
                .stream()
                .map((Plugin.Choice choice) -> {
                    Object ret = null;
                    if (choice.ifPluginAction()) {
                        ret = choice.getPluginAction();
                    } else if (choice.ifPluginStrings()) {
                        ret = choice.ifPluginStrings();
                    } else if (choice.ifPluginClasspath()) {
                        ret = choice.getPluginClasspath();
                    } else if (choice.ifPluginRegistration()) {
                        ret = choice.getPluginRegistration();
                    }
                    return ret;
                }).toList();
        return pluginChoice;

    }

    public static List<Object> listPluginActions(PluginAction action) {
        List<Object> pluginActions = emptyIfNull(action.getChoiceList())
                .stream()
                .map((PluginAction.Choice choice) -> {
                    Object ret = null;
                    if (choice.ifPluginMenu()) {
                        ret = choice.getPluginMenu();
                    } else if (choice.ifPluginMode()) {
                        ret = choice.getPluginMode();
                    } else if (choice.ifPluginProperty()) {
                        ret = choice.getPluginProperty();
                    }
                    return ret;
                }).toList();
        return pluginActions;
    }

    public static CompoundAction.Choice choiceFromXmlActions(XmlAction action) {

        var choice = new CompoundAction.Choice();
        if (action instanceof CompoundAction a) {
            choice.setCompoundAction(a);
        } else if (action instanceof SelectNodeAction a) {
            choice.setSelectNodeAction(a);
        } else if (action instanceof CutNodeAction a) {
            choice.setCutNodeAction(a);
        } else if (action instanceof PasteNodeAction a) {
            choice.setPasteNodeAction(a);
        } else if (action instanceof UndoPasteNodeAction a) {
            choice.setUndoPasteNodeAction(a);
        } else if (action instanceof RevertXmlAction a) {
            choice.setRevertXmlAction(a);
        } else if (action instanceof BoldNodeAction a) {
            choice.setBoldNodeAction(a);
        } else if (action instanceof StrikethroughNodeAction a) {
            choice.setStrikethroughNodeAction(a);
        } else if (action instanceof ItalicNodeAction a) {
            choice.setItalicNodeAction(a);
        } else if (action instanceof UnderlinedNodeAction a) {
            choice.setUnderlinedNodeAction(a);
        } else if (action instanceof FontSizeNodeAction a) {
            choice.setFontSizeNodeAction(a);
        } else if (action instanceof FontNodeAction a) {
            choice.setFontNodeAction(a);
        } else if (action instanceof NodeColorFormatAction a) {
            choice.setNodeColorFormatAction(a);
        } else if (action instanceof NodeBackgroundColorFormatAction a) {
            choice.setNodeBackgroundColorFormatAction(a);
        } else if (action instanceof NodeStyleFormatAction a) {
            choice.setNodeStyleFormatAction(a);
        } else if (action instanceof EdgeColorFormatAction a) {
            choice.setEdgeColorFormatAction(a);
        } else if (action instanceof EdgeWidthFormatAction a) {
            choice.setEdgeWidthFormatAction(a);
        } else if (action instanceof EdgeStyleFormatAction a) {
            choice.setEdgeStyleFormatAction(a);
        } else if (action instanceof DeleteNodeAction a) {
            choice.setDeleteNodeAction(a);
        } else if (action instanceof EditNodeAction a) {
            choice.setEditNodeAction(a);
        } else if (action instanceof NewNodeAction a) {
            choice.setNewNodeAction(a);
        } else if (action instanceof FoldAction a) {
            choice.setFoldAction(a);
        } else if (action instanceof MoveNodesAction a) {
            choice.setMoveNodesAction(a);
        } else if (action instanceof HookNodeAction a) {
            choice.setHookNodeAction(a);
        } else if (action instanceof AddIconAction a) {
            choice.setAddIconAction(a);
        } else if (action instanceof RemoveIconXmlAction a) {
            choice.setRemoveIconXmlAction(a);
        } else if (action instanceof RemoveAllIconsXmlAction a) {
            choice.setRemoveAllIconsXmlAction(a);
        } else if (action instanceof MoveNodeXmlAction a) {
            choice.setMoveNodeXmlAction(a);
        } else if (action instanceof AddCloudXmlAction a) {
            choice.setAddCloudXmlAction(a);
        } else if (action instanceof CloudColorXmlAction a) {
            choice.setCloudColorXmlAction(a);
        } else if (action instanceof AddArrowLinkXmlAction a) {
            choice.setAddArrowLinkXmlAction(a);
        } else if (action instanceof AddLinkXmlAction a) {
            choice.setAddLinkXmlAction(a);
        } else if (action instanceof RemoveArrowLinkXmlAction a) {
            choice.setRemoveArrowLinkXmlAction(a);
        } else if (action instanceof ArrowLinkColorXmlAction a) {
            choice.setArrowLinkColorXmlAction(a);
        } else if (action instanceof ArrowLinkArrowXmlAction a) {
            choice.setArrowLinkArrowXmlAction(a);
        } else if (action instanceof ArrowLinkPointXmlAction a) {
            choice.setArrowLinkPointXmlAction(a);
        } else if (action instanceof SetAttributeAction a) {
            choice.setSetAttributeAction(a);
        } else if (action instanceof InsertAttributeAction a) {
            choice.setInsertAttributeAction(a);
        } else if (action instanceof AddAttributeAction a) {
            choice.setAddAttributeAction(a);
        } else if (action instanceof RemoveAttributeAction a) {
            choice.setRemoveAttributeAction(a);
        } else if (action instanceof EditNoteToNodeAction a) {
            choice.setEditNoteToNodeAction(a);
        } else if (action instanceof PlaceNodeXmlAction a) {
            choice.setPlaceNodeXmlAction(a);
        }

        return choice;

    }

    /**
     * Convert a Choice wrapper to its contained XmlAction (for JAXB serialization).
     */
    public static XmlAction choiceToXmlAction(CompoundAction.Choice choice) {
        if (choice.ifCompoundAction()) return choice.getCompoundAction();
        if (choice.ifSelectNodeAction()) return choice.getSelectNodeAction();
        if (choice.ifCutNodeAction()) return choice.getCutNodeAction();
        if (choice.ifPasteNodeAction()) return choice.getPasteNodeAction();
        if (choice.ifUndoPasteNodeAction()) return choice.getUndoPasteNodeAction();
        if (choice.ifRevertXmlAction()) return choice.getRevertXmlAction();
        if (choice.ifBoldNodeAction()) return choice.getBoldNodeAction();
        if (choice.ifStrikethroughNodeAction()) return choice.getStrikethroughNodeAction();
        if (choice.ifItalicNodeAction()) return choice.getItalicNodeAction();
        if (choice.ifUnderlinedNodeAction()) return choice.getUnderlinedNodeAction();
        if (choice.ifFontSizeNodeAction()) return choice.getFontSizeNodeAction();
        if (choice.ifFontNodeAction()) return choice.getFontNodeAction();
        if (choice.ifNodeColorFormatAction()) return choice.getNodeColorFormatAction();
        if (choice.ifNodeBackgroundColorFormatAction()) return choice.getNodeBackgroundColorFormatAction();
        if (choice.ifNodeStyleFormatAction()) return choice.getNodeStyleFormatAction();
        if (choice.ifEdgeColorFormatAction()) return choice.getEdgeColorFormatAction();
        if (choice.ifEdgeWidthFormatAction()) return choice.getEdgeWidthFormatAction();
        if (choice.ifEdgeStyleFormatAction()) return choice.getEdgeStyleFormatAction();
        if (choice.ifDeleteNodeAction()) return choice.getDeleteNodeAction();
        if (choice.ifEditNodeAction()) return choice.getEditNodeAction();
        if (choice.ifNewNodeAction()) return choice.getNewNodeAction();
        if (choice.ifFoldAction()) return choice.getFoldAction();
        if (choice.ifMoveNodesAction()) return choice.getMoveNodesAction();
        if (choice.ifHookNodeAction()) return choice.getHookNodeAction();
        if (choice.ifAddIconAction()) return choice.getAddIconAction();
        if (choice.ifRemoveIconXmlAction()) return choice.getRemoveIconXmlAction();
        if (choice.ifRemoveAllIconsXmlAction()) return choice.getRemoveAllIconsXmlAction();
        if (choice.ifMoveNodeXmlAction()) return choice.getMoveNodeXmlAction();
        if (choice.ifAddCloudXmlAction()) return choice.getAddCloudXmlAction();
        if (choice.ifCloudColorXmlAction()) return choice.getCloudColorXmlAction();
        if (choice.ifAddArrowLinkXmlAction()) return choice.getAddArrowLinkXmlAction();
        if (choice.ifAddLinkXmlAction()) return choice.getAddLinkXmlAction();
        if (choice.ifRemoveArrowLinkXmlAction()) return choice.getRemoveArrowLinkXmlAction();
        if (choice.ifArrowLinkColorXmlAction()) return choice.getArrowLinkColorXmlAction();
        if (choice.ifArrowLinkArrowXmlAction()) return choice.getArrowLinkArrowXmlAction();
        if (choice.ifArrowLinkPointXmlAction()) return choice.getArrowLinkPointXmlAction();
        if (choice.ifSetAttributeAction()) return choice.getSetAttributeAction();
        if (choice.ifInsertAttributeAction()) return choice.getInsertAttributeAction();
        if (choice.ifAddAttributeAction()) return choice.getAddAttributeAction();
        if (choice.ifRemoveAttributeAction()) return choice.getRemoveAttributeAction();
        if (choice.ifEditNoteToNodeAction()) return choice.getEditNoteToNodeAction();
        if (choice.ifPlaceNodeXmlAction()) return choice.getPlaceNodeXmlAction();
        return null;
    }

    /**
     * Convert an XmlAction to a Choice wrapper (for JAXB deserialization).
     * Delegates to existing choiceFromXmlActions method.
     */
    public static CompoundAction.Choice xmlActionToChoice(XmlAction action) {
        return choiceFromXmlActions(action);
    }

    public static List<XmlAction> listXmlActions(CompoundAction pAction) {
        List<XmlAction> xmlActions = emptyIfNull(pAction.getChoiceList())
                .stream().
                map((CompoundAction.Choice choice) -> {
                    if (choice.ifCompoundAction()) {
                        return choice.getCompoundAction();
                    } else if (choice.ifSelectNodeAction()) {
                        return choice.getSelectNodeAction();
                    } else if (choice.ifCutNodeAction()) {
                        return choice.getCutNodeAction();
                    } else if (choice.ifPasteNodeAction()) {
                        return choice.getPasteNodeAction();
                    } else if (choice.ifUndoPasteNodeAction()) {
                        return choice.getUndoPasteNodeAction();
                    } else if (choice.ifRevertXmlAction()) {
                        return choice.getRevertXmlAction();
                    } else if (choice.ifBoldNodeAction()) {
                        return choice.getBoldNodeAction();
                    } else if (choice.ifStrikethroughNodeAction()) {
                        return choice.getStrikethroughNodeAction();
                    } else if (choice.ifItalicNodeAction()) {
                        return choice.getItalicNodeAction();
                    } else if (choice.ifUnderlinedNodeAction()) {
                        return choice.getUnderlinedNodeAction();
                    } else if (choice.ifFontSizeNodeAction()) {
                        return choice.getFontSizeNodeAction();
                    } else if (choice.ifFontNodeAction()) {
                        return choice.getFontNodeAction();
                    } else if (choice.ifNodeColorFormatAction()) {
                        return choice.getNodeColorFormatAction();
                    } else if (choice.ifNodeBackgroundColorFormatAction()) {
                        return choice.getNodeBackgroundColorFormatAction();
                    } else if (choice.ifNodeStyleFormatAction()) {
                        return choice.getNodeStyleFormatAction();
                    } else if (choice.ifEdgeColorFormatAction()) {
                        return choice.getEdgeColorFormatAction();
                    } else if (choice.ifEdgeWidthFormatAction()) {
                        return choice.getEdgeWidthFormatAction();
                    } else if (choice.ifEdgeStyleFormatAction()) {
                        return choice.getEdgeStyleFormatAction();
                    } else if (choice.ifDeleteNodeAction()) {
                        return choice.getDeleteNodeAction();
                    } else if (choice.ifEditNodeAction()) {
                        return choice.getEditNodeAction();
                    } else if (choice.ifNewNodeAction()) {
                        return choice.getNewNodeAction();
                    } else if (choice.ifFoldAction()) {
                        return choice.getFoldAction();
                    } else if (choice.ifMoveNodesAction()) {
                        return choice.getMoveNodesAction();
                    } else if (choice.ifHookNodeAction()) {
                        return choice.getHookNodeAction();
                    } else if (choice.ifAddIconAction()) {
                        return choice.getAddIconAction();
                    } else if (choice.ifRemoveIconXmlAction()) {
                        return choice.getRemoveIconXmlAction();
                    } else if (choice.ifRemoveAllIconsXmlAction()) {
                        return choice.getRemoveAllIconsXmlAction();
                    } else if (choice.ifMoveNodeXmlAction()) {
                        return choice.getMoveNodeXmlAction();
                    } else if (choice.ifAddCloudXmlAction()) {
                        return choice.getAddCloudXmlAction();
                    } else if (choice.ifCloudColorXmlAction()) {
                        return choice.getCloudColorXmlAction();
                    } else if (choice.ifAddArrowLinkXmlAction()) {
                        return choice.getAddArrowLinkXmlAction();
                    } else if (choice.ifAddLinkXmlAction()) {
                        return choice.getAddLinkXmlAction();
                    } else if (choice.ifRemoveArrowLinkXmlAction()) {
                        return choice.getRemoveArrowLinkXmlAction();
                    } else if (choice.ifArrowLinkColorXmlAction()) {
                        return choice.getArrowLinkColorXmlAction();
                    } else if (choice.ifArrowLinkArrowXmlAction()) {
                        return choice.getArrowLinkArrowXmlAction();
                    } else if (choice.ifArrowLinkPointXmlAction()) {
                        return choice.getArrowLinkPointXmlAction();
                    } else if (choice.ifSetAttributeAction()) {
                        return choice.getSetAttributeAction();
                    } else if (choice.ifInsertAttributeAction()) {
                        return choice.getInsertAttributeAction();
                    } else if (choice.ifAddAttributeAction()) {
                        return choice.getAddAttributeAction();
                    } else if (choice.ifRemoveAttributeAction()) {
                        return choice.getRemoveAttributeAction();
                    } else if (choice.ifEditNoteToNodeAction()) {
                        return choice.getEditNoteToNodeAction();
                    } else if (choice.ifPlaceNodeXmlAction()) {
                        return choice.getPlaceNodeXmlAction();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
        return xmlActions;
    }

}
