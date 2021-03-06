/*
 * Copyright (C) 2017 skrymets
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package freemind.frok.patches;

import freemind.controller.actions.generated.instance.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author skrymets
 */
public class JIBXGeneratedUtil {

    public static List<Object> listPluginChoice(Plugin plugin) {

        List<Object> pluginChoice = plugin
                .getChoiceList()
                .stream()
                .map((Plugin.Choice choice) -> {
                    Object ret = null;
                    switch (choice.stateChoiceListSelect()) {
                        case Plugin.Choice.PLUGIN_CLASSPATH_CHOICE:
                            ret = choice.getPluginClasspath();
                            break;
                        case Plugin.Choice.PLUGIN_REGISTRATION_CHOICE:
                            ret = choice.getPluginRegistration();
                            break;
                        case Plugin.Choice.PLUGIN_STRINGS_CHOICE:
                            ret = choice.ifPluginStrings();
                            break;
                        case Plugin.Choice.PLUGIN_ACTION_CHOICE:
                            ret = choice.getPluginAction();
                            break;
                    }
                    return ret;
                }).collect(Collectors.toList());
        return pluginChoice;

    }

    public static List<Object> listPluginActions(PluginAction action) {
        List<Object> pluginActions = action
                .getChoiceList()
                .stream()
                .map((PluginAction.Choice choice) -> {
                    Object ret = null;
                    switch (choice.stateChoiceListSelect()) {
                        case PluginAction.Choice.PLUGIN_MENU_CHOICE:
                            ret = choice.getPluginMenu();
                            break;
                        case PluginAction.Choice.PLUGIN_MODE_CHOICE:
                            ret = choice.getPluginMode();
                            break;
                        case PluginAction.Choice.PLUGIN_PROPERTY_CHOICE:
                            ret = choice.getPluginProperty();
                            break;
                    }
                    return ret;
                }).collect(Collectors.toList());
        return pluginActions;
    }

    public static CompoundAction.Choice choiceFromXmlActions(XmlAction action) {

        CompoundAction.Choice choice = new CompoundAction.Choice();
        if (action instanceof CompoundAction) {
            choice.setCompoundAction((CompoundAction) action);
        } else if (action instanceof SelectNodeAction) {
            choice.setSelectNodeAction((SelectNodeAction) action);
        } else if (action instanceof CutNodeAction) {
            choice.setCutNodeAction((CutNodeAction) action);
        } else if (action instanceof PasteNodeAction) {
            choice.setPasteNodeAction((PasteNodeAction) action);
        } else if (action instanceof UndoPasteNodeAction) {
            choice.setUndoPasteNodeAction((UndoPasteNodeAction) action);
        } else if (action instanceof RevertXmlAction) {
            choice.setRevertXmlAction((RevertXmlAction) action);
        } else if (action instanceof BoldNodeAction) {
            choice.setBoldNodeAction((BoldNodeAction) action);
        } else if (action instanceof StrikethroughNodeAction) {
            choice.setStrikethroughNodeAction((StrikethroughNodeAction) action);
        } else if (action instanceof ItalicNodeAction) {
            choice.setItalicNodeAction((ItalicNodeAction) action);
        } else if (action instanceof UnderlinedNodeAction) {
            choice.setUnderlinedNodeAction((UnderlinedNodeAction) action);
        } else if (action instanceof FontSizeNodeAction) {
            choice.setFontSizeNodeAction((FontSizeNodeAction) action);
        } else if (action instanceof FontNodeAction) {
            choice.setFontNodeAction((FontNodeAction) action);
        } else if (action instanceof NodeColorFormatAction) {
            choice.setNodeColorFormatAction((NodeColorFormatAction) action);
        } else if (action instanceof NodeBackgroundColorFormatAction) {
            choice.setNodeBackgroundColorFormatAction((NodeBackgroundColorFormatAction) action);
        } else if (action instanceof NodeStyleFormatAction) {
            choice.setNodeStyleFormatAction((NodeStyleFormatAction) action);
        } else if (action instanceof EdgeColorFormatAction) {
            choice.setEdgeColorFormatAction((EdgeColorFormatAction) action);
        } else if (action instanceof EdgeWidthFormatAction) {
            choice.setEdgeWidthFormatAction((EdgeWidthFormatAction) action);
        } else if (action instanceof EdgeStyleFormatAction) {
            choice.setEdgeStyleFormatAction((EdgeStyleFormatAction) action);
        } else if (action instanceof DeleteNodeAction) {
            choice.setDeleteNodeAction((DeleteNodeAction) action);
        } else if (action instanceof EditNodeAction) {
            choice.setEditNodeAction((EditNodeAction) action);
        } else if (action instanceof NewNodeAction) {
            choice.setNewNodeAction((NewNodeAction) action);
        } else if (action instanceof FoldAction) {
            choice.setFoldAction((FoldAction) action);
        } else if (action instanceof MoveNodesAction) {
            choice.setMoveNodesAction((MoveNodesAction) action);
        } else if (action instanceof HookNodeAction) {
            choice.setHookNodeAction((HookNodeAction) action);
        } else if (action instanceof AddIconAction) {
            choice.setAddIconAction((AddIconAction) action);
        } else if (action instanceof RemoveIconXmlAction) {
            choice.setRemoveIconXmlAction((RemoveIconXmlAction) action);
        } else if (action instanceof RemoveAllIconsXmlAction) {
            choice.setRemoveAllIconsXmlAction((RemoveAllIconsXmlAction) action);
        } else if (action instanceof MoveNodeXmlAction) {
            choice.setMoveNodeXmlAction((MoveNodeXmlAction) action);
        } else if (action instanceof AddCloudXmlAction) {
            choice.setAddCloudXmlAction((AddCloudXmlAction) action);
        } else if (action instanceof CloudColorXmlAction) {
            choice.setCloudColorXmlAction((CloudColorXmlAction) action);
        } else if (action instanceof AddArrowLinkXmlAction) {
            choice.setAddArrowLinkXmlAction((AddArrowLinkXmlAction) action);
        } else if (action instanceof AddLinkXmlAction) {
            choice.setAddLinkXmlAction((AddLinkXmlAction) action);
        } else if (action instanceof RemoveArrowLinkXmlAction) {
            choice.setRemoveArrowLinkXmlAction((RemoveArrowLinkXmlAction) action);
        } else if (action instanceof ArrowLinkColorXmlAction) {
            choice.setArrowLinkColorXmlAction((ArrowLinkColorXmlAction) action);
        } else if (action instanceof ArrowLinkArrowXmlAction) {
            choice.setArrowLinkArrowXmlAction((ArrowLinkArrowXmlAction) action);
        } else if (action instanceof ArrowLinkPointXmlAction) {
            choice.setArrowLinkPointXmlAction((ArrowLinkPointXmlAction) action);
        } else if (action instanceof SetAttributeAction) {
            choice.setSetAttributeAction((SetAttributeAction) action);
        } else if (action instanceof InsertAttributeAction) {
            choice.setInsertAttributeAction((InsertAttributeAction) action);
        } else if (action instanceof AddAttributeAction) {
            choice.setAddAttributeAction((AddAttributeAction) action);
        } else if (action instanceof RemoveAttributeAction) {
            choice.setRemoveAttributeAction((RemoveAttributeAction) action);
        } else if (action instanceof EditNoteToNodeAction) {
            choice.setEditNoteToNodeAction((EditNoteToNodeAction) action);
        } else if (action instanceof PlaceNodeXmlAction) {
            choice.setPlaceNodeXmlAction((PlaceNodeXmlAction) action);
        } 

        return choice;

    }

    public static List<XmlAction> listXmlActions(CompoundAction pAction) {
        List<XmlAction> xmlActions = pAction.getChoiceList()
                .stream().
                map((CompoundAction.Choice choice) -> {
                    switch (choice.stateChoiceListSelect()) {
                        case CompoundAction.Choice.COMPOUND_ACTION_CHOICE:
                            return choice.getCompoundAction();

                        case CompoundAction.Choice.SELECT_NODE_ACTION_CHOICE:
                            return choice.getSelectNodeAction();

                        case CompoundAction.Choice.CUT_NODE_ACTION_CHOICE:
                            return choice.getCutNodeAction();

                        case CompoundAction.Choice.PASTE_NODE_ACTION_CHOICE:
                            return choice.getPasteNodeAction();

                        case CompoundAction.Choice.UNDO_PASTE_NODE_ACTION_CHOICE:
                            return choice.getUndoPasteNodeAction();

                        case CompoundAction.Choice.REVERT_XML_ACTION_CHOICE:
                            return choice.getRevertXmlAction();

                        case CompoundAction.Choice.BOLD_NODE_ACTION_CHOICE:
                            return choice.getBoldNodeAction();

                        case CompoundAction.Choice.STRIKETHROUGH_NODE_ACTION_CHOICE:
                            return choice.getStrikethroughNodeAction();

                        case CompoundAction.Choice.ITALIC_NODE_ACTION_CHOICE:
                            return choice.getItalicNodeAction();

                        case CompoundAction.Choice.UNDERLINED_NODE_ACTION_CHOICE:
                            return choice.getUnderlinedNodeAction();

                        case CompoundAction.Choice.FONT_SIZE_NODE_ACTION_CHOICE:
                            return choice.getFontSizeNodeAction();

                        case CompoundAction.Choice.FONT_NODE_ACTION_CHOICE:
                            return choice.getFontNodeAction();

                        case CompoundAction.Choice.NODE_COLOR_FORMAT_ACTION_CHOICE:
                            return choice.getNodeColorFormatAction();

                        case CompoundAction.Choice.NODE_BACKGROUND_COLOR_FORMAT_ACTION_CHOICE:
                            return choice.getNodeBackgroundColorFormatAction();

                        case CompoundAction.Choice.NODE_STYLE_FORMAT_ACTION_CHOICE:
                            return choice.getNodeStyleFormatAction();

                        case CompoundAction.Choice.EDGE_COLOR_FORMAT_ACTION_CHOICE:
                            return choice.getEdgeColorFormatAction();

                        case CompoundAction.Choice.EDGE_WIDTH_FORMAT_ACTION_CHOICE:
                            return choice.getEdgeWidthFormatAction();

                        case CompoundAction.Choice.EDGE_STYLE_FORMAT_ACTION_CHOICE:
                            return choice.getEdgeStyleFormatAction();

                        case CompoundAction.Choice.DELETE_NODE_ACTION_CHOICE:
                            return choice.getDeleteNodeAction();

                        case CompoundAction.Choice.EDIT_NODE_ACTION_CHOICE:
                            return choice.getEditNodeAction();

                        case CompoundAction.Choice.NEW_NODE_ACTION_CHOICE:
                            return choice.getNewNodeAction();

                        case CompoundAction.Choice.FOLD_ACTION_CHOICE:
                            return choice.getFoldAction();

                        case CompoundAction.Choice.MOVE_NODES_ACTION_CHOICE:
                            return choice.getMoveNodesAction();

                        case CompoundAction.Choice.HOOK_NODE_ACTION_CHOICE:
                            return choice.getHookNodeAction();

                        case CompoundAction.Choice.ADD_ICON_ACTION_CHOICE:
                            return choice.getAddIconAction();

                        case CompoundAction.Choice.REMOVE_ICON_XML_ACTION_CHOICE:
                            return choice.getRemoveIconXmlAction();

                        case CompoundAction.Choice.REMOVE_ALL_ICONS_XML_ACTION_CHOICE:
                            return choice.getRemoveAllIconsXmlAction();

                        case CompoundAction.Choice.MOVE_NODE_XML_ACTION_CHOICE:
                            return choice.getMoveNodeXmlAction();

                        case CompoundAction.Choice.ADD_CLOUD_XML_ACTION_CHOICE:
                            return choice.getAddCloudXmlAction();

                        case CompoundAction.Choice.CLOUD_COLOR_XML_ACTION_CHOICE:
                            return choice.getCloudColorXmlAction();

                        case CompoundAction.Choice.ADD_ARROW_LINK_XML_ACTION_CHOICE:
                            return choice.getAddArrowLinkXmlAction();

                        case CompoundAction.Choice.ADD_LINK_XML_ACTION_CHOICE:
                            return choice.getAddLinkXmlAction();

                        case CompoundAction.Choice.REMOVE_ARROW_LINK_XML_ACTION_CHOICE:
                            return choice.getRemoveArrowLinkXmlAction();

                        case CompoundAction.Choice.ARROW_LINK_COLOR_XML_ACTION_CHOICE:
                            return choice.getArrowLinkColorXmlAction();

                        case CompoundAction.Choice.ARROW_LINK_ARROW_XML_ACTION_CHOICE:
                            return choice.getArrowLinkArrowXmlAction();

                        case CompoundAction.Choice.ARROW_LINK_POINT_XML_ACTION_CHOICE:
                            return choice.getArrowLinkPointXmlAction();

                        case CompoundAction.Choice.SET_ATTRIBUTE_ACTION_CHOICE:
                            return choice.getSetAttributeAction();

                        case CompoundAction.Choice.INSERT_ATTRIBUTE_ACTION_CHOICE:
                            return choice.getInsertAttributeAction();

                        case CompoundAction.Choice.ADD_ATTRIBUTE_ACTION_CHOICE:
                            return choice.getAddAttributeAction();

                        case CompoundAction.Choice.REMOVE_ATTRIBUTE_ACTION_CHOICE:
                            return choice.getRemoveAttributeAction();

                        case CompoundAction.Choice.EDIT_NOTE_TO_NODE_ACTION_CHOICE:
                            return choice.getEditNoteToNodeAction();
                        case CompoundAction.Choice.PLACE_NODE_XML_ACTION_CHOICE:
                            return choice.getPlaceNodeXmlAction();
                        default:
                            throw new AssertionError();
                    }
                })
                .collect(Collectors.toList());
        return xmlActions;
    }

}
