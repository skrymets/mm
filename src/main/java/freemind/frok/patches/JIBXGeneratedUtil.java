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
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author skrymets
 */
public class JIBXGeneratedUtil {

    public static List<Object> listPluginChoice(Plugin plugin) {

        List<Object> pluginChoice = plugin
                .getChoiceList()
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
                }).collect(toList());
        return pluginChoice;

    }

    public static List<Object> listPluginActions(PluginAction action) {
        List<Object> pluginActions = action
                .getChoiceList()
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
                }).collect(toList());
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
                .collect(toList());
        return xmlActions;
    }

}
