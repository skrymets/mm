package freemind.modes.mindmapmode.actions;

import freemind.main.HtmlTools;
import freemind.modes.mindmapmode.MindMapController;

@SuppressWarnings("serial")
public class UseRichFormattingAction extends NodeGeneralAction {
    public UseRichFormattingAction(final MindMapController modeController) {
        super(modeController, "use_rich_formatting", null,
                (map, selected) -> {
                    // modeController.getController().setProperty(
                    // "use_rich_text_in_new_long_nodes", "true");
                    String nodeText = selected.getText();
                    if (!HtmlTools.isHtmlNode(nodeText)) {
                        modeController.setNodeText(selected,
                                HtmlTools.plainToHTML(nodeText));
                    }
                });
    }

    // public boolean isEnabled() {
    // if (1 == 0) {
    // // Dan: The following code probably needs that the node context menu
    // // gets somehow refreshed
    // // whenever it pops. But I do not know how to ensure that.
    // for (ListIterator it = controller.getView().getSelecteds()
    // .listIterator(); it.hasNext();) {
    // NodeView selected = (NodeView) it.next();
    // String nodeText = selected.getModel().toString();
    // if (!nodeText.startsWith("<html>")) {
    // return true;
    // }
    // }
    // return false;
    // }
    // return true;
    // }

}
