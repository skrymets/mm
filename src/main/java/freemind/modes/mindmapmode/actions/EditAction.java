/*
 * Created on 05.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.modes.mindmapmode.actions;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.main.FreeMind;
import freemind.main.HtmlTools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.*;
import freemind.view.mindmapview.EditNodeBase.EditControl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

//
//Node editing
//

@SuppressWarnings("serial")
public class EditAction extends MindmapAction {
    private static final Pattern HTML_HEAD = Pattern.compile(
            "\\s*<head>.*</head>", Pattern.DOTALL);
    private final MindMapController mMindMapController;
    private EditNodeBase mCurrentEditDialog = null;

    public EditAction(MindMapController modeController) {
        super("edit_node", modeController);
        this.mMindMapController = modeController;
    }

    public void actionPerformed(ActionEvent arg0) {
        this.mMindMapController.edit(null, false, false);
    }

    // edit begins with home/end or typing (PN 6.2)
    public void edit(KeyEvent e, boolean addNew, boolean editLong) {
        NodeView selectedNodeView = mMindMapController.getView().getSelectionService().getSelected();
        if (selectedNodeView != null) {
            if (e == null || !addNew) {
                edit(selectedNodeView, selectedNodeView, e, false, false,
                        editLong);
            } else if (!mMindMapController.isBlocked()) {
                mMindMapController.addNew(mMindMapController.getSelected(),
                        MindMapController.NEW_SIBLING_BEHIND, e);
            }
            if (e != null) {
                e.consume();
            }
        }
    }

    public void edit(final NodeView node, final NodeView prevSelected,
                     final KeyEvent firstEvent, final boolean isNewNode,
                     final boolean parentFolded, final boolean editLong) {
        if (node == null) {
            return;
        }
        final MapView map = node.getMap();
        map.validate();
        map.invalidate();

        stopEditing();
        // EditNodeBase.closeEdit();
        mMindMapController.setBlocked(true); // locally "modal" stated

        String text = node.getModel().toString();
        String htmlEditingOption = mMindMapController.getController()
                .getProperty("html_editing_option");

        boolean isHtmlNode = HtmlTools.isHtmlNode(text);
        boolean isLongNode = node.getIsLong();

        // do we need a decision if plain or HTML editing?
        String useRichTextInNewLongNodes = (isHtmlNode) ? "true" : "false";
        // if the node is not already html, we ask if rich text or plain text
        // edit.
        if (!isHtmlNode && !isLongNode && editLong) {
            // ask user:
            int showResult = new OptionalDontShowMeAgainDialog(
                    mMindMapController.getFrame().getJFrame(),
                    mMindMapController.getSelectedView(),
                    "edit.edit_rich_text",
                    "edit.decision",
                    mMindMapController,
                    new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                            mMindMapController.getController(),
                            FreeMind.RESOURCES_REMIND_USE_RICH_TEXT_IN_NEW_LONG_NODES),
                    OptionalDontShowMeAgainDialog.BOTH_OK_AND_CANCEL_OPTIONS_ARE_STORED)
                    .show().getResult();
            useRichTextInNewLongNodes = (showResult == JOptionPane.OK_OPTION) ? "true"
                    : "false";
        }
        // useRichTextInNewLongNodes =
        // c.getController().getProperty("use_rich_text_in_new_long_nodes");
        boolean editHtml = isHtmlNode || (editLong && Objects.equals(useRichTextInNewLongNodes, "true"));
        boolean editInternalWysiwyg = editHtml && Objects.equals(htmlEditingOption, "internal-wysiwyg");
        boolean editExternal = editHtml && Objects.equals(htmlEditingOption, "external");

        if (editHtml && !isHtmlNode) {
            text = HtmlTools.plainToHTML(text);
        }
        if (editInternalWysiwyg) {
            EditNodeWYSIWYG editNodeWYSIWYG = new EditNodeWYSIWYG(node, text,
                    firstEvent, mMindMapController,
                    new EditControl() {
                        public void cancel() {
                            mMindMapController.setBlocked(false);
                            mCurrentEditDialog = null;
                            mMindMapController.getController()
                                    .obtainFocusForSelected();
                        }

                        public void ok(String newText) {
                            setHtmlText(node, newText);
                            cancel();
                        }

                        public void split(String newText, int position) {
                            mMindMapController.getTextOperationService().splitNode(node.getModel(),
                                    position, newText);
                            cancel();
                        }
                    }); // focus fix
            mCurrentEditDialog = editNodeWYSIWYG;
            editNodeWYSIWYG.show();
            return;
        }

        if (editExternal) {
            EditNodeExternalApplication editNodeExternalApplication = new EditNodeExternalApplication(
                    node, text, firstEvent, mMindMapController,
                    new EditControl() {
                        public void cancel() {
                            mMindMapController.setBlocked(false);
                            mCurrentEditDialog = null;
                            mMindMapController.getController()
                                    .obtainFocusForSelected();
                        }

                        public void ok(String newText) {
                            setHtmlText(node, newText);
                            cancel();
                        }

                        public void split(String newText, int position) {
                            mMindMapController.getTextOperationService().splitNode(node.getModel(),
                                    position, newText);
                            cancel();
                        }
                    }); // focus fix
            mCurrentEditDialog = editNodeExternalApplication;
            editNodeExternalApplication.show();
            // We come here before quitting the editor window.
            return;
        }

        if (isLongNode || editLong) {
            EditNodeDialog nodeEditDialog = new EditNodeDialog(node, text,
                    firstEvent, mMindMapController,
                    new EditControl() {

                        public void cancel() {
                            mMindMapController.setBlocked(false);
                            mCurrentEditDialog = null;
                            mMindMapController.getController()
                                    .obtainFocusForSelected(); // focus fix
                        }

                        public void ok(String newText) {
                            mMindMapController.setNodeText(node.getModel(), newText);
                            cancel();
                        }

                        public void split(String newText, int position) {
                            mMindMapController.getTextOperationService().splitNode(node.getModel(),
                                    position, newText);
                            cancel();
                        }
                    });
            mCurrentEditDialog = nodeEditDialog;
            nodeEditDialog.show();
            return;
        }
        // inline editing:
        EditNodeTextField textfield = new EditNodeTextField(node, text,
                firstEvent, mMindMapController, new EditControl() {

            public void cancel() {
                if (isNewNode) { // delete also the node and set focus
                    // to the parent
                    mMindMapController.getView()
                            .getSelectionService().selectAsTheOnlyOneSelected(node);
                    List<MindMapNode> nodeList = new ArrayList<>();
                    nodeList.add(node.getModel());
                    mMindMapController.cut(nodeList);
                    mMindMapController.select(prevSelected);
                    // include max level for navigation
                    if (parentFolded) {
                        mMindMapController.setFolded(
                                prevSelected.getModel(), true);
                    }
                }
                endEdit();
            }

            public void ok(String newText) {
                mMindMapController.setNodeText(node.getModel(), newText);
                endEdit();
            }

            private void endEdit() {
                mMindMapController.obtainFocusForSelected();
                mMindMapController.setBlocked(false);
                mCurrentEditDialog = null;
            }

            public void split(String newText, int position) {
            }
        });
        mCurrentEditDialog = textfield;
        textfield.show();

    }

    public void stopEditing() {
        if (mCurrentEditDialog != null) {
            // there was previous editing.
            mCurrentEditDialog.closeEdit();
            mCurrentEditDialog = null;
        }
    }

    private void setHtmlText(final NodeView node, String newText) {
        final String body = HTML_HEAD.matcher(newText).replaceFirst("");
        mMindMapController.setNodeText(node.getModel(), body);
    }
}
