package freemind.modes.mindmapmode.actions;

import freemind.main.HtmlTools;
import freemind.modes.mindmapmode.MindMapController;

@SuppressWarnings("serial")
public class UsePlainTextAction extends NodeGeneralAction {

    public UsePlainTextAction(final MindMapController modeController) {
        super(modeController, "use_plain_text", null,
                (map, selected) -> {
                    // modeController.getController().setProperty(
                    // "use_rich_text_in_new_long_nodes", "false");
                    String nodeText = selected.getText();
                    if (HtmlTools.isHtmlNode(nodeText)) {
                        modeController.setNodeText(selected,
                                HtmlTools.htmlToPlain(nodeText));
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
    // if (nodeText.startsWith("<html>")) {
    // return true;
    // }
    // }
    // return false;
    // }
    // return true;
    // }

}
