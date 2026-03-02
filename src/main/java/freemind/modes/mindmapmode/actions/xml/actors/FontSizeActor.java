package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.FontSizeNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.util.Objects;

public class FontSizeActor extends XmlActorAdapter {

    public FontSizeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<FontSizeNodeAction> getDoActionClass() {
        return FontSizeNodeAction.class;
    }

    public void setFontSize(MindMapNode node, String fontSizeValue) {
        if (Objects.equals(fontSizeValue, node.getFontSize())) {
            return;
        }
        execute(getActionPair(node, fontSizeValue));

    }

    public ActionPair getActionPair(MindMapNode node, String fontSizeValue) {
        FontSizeNodeAction fontSizeAction = createFontSizeNodeAction(node,
                fontSizeValue);
        FontSizeNodeAction undoFontSizeAction = createFontSizeNodeAction(node,
                node.getFontSize());
        return new ActionPair(fontSizeAction, undoFontSizeAction);
    }

    private FontSizeNodeAction createFontSizeNodeAction(MindMapNode node,
                                                        String fontSizeValue) {
        FontSizeNodeAction fontSizeAction = new FontSizeNodeAction();
        fontSizeAction.setNode(getNodeID(node));
        fontSizeAction.setSize(fontSizeValue);
        return fontSizeAction;

    }

    public void act(XmlAction action) {
        if (action instanceof FontSizeNodeAction) {
            FontSizeNodeAction fontSizeAction = (FontSizeNodeAction) action;
            MindMapNode node = getNodeFromID(fontSizeAction.getNode());
            try {
                int size = Integer.valueOf(fontSizeAction.getSize()).intValue();
                if (!node.getFontSize().equals(fontSizeAction.getSize())) {
                    node.setFontSize(size);
                    getExMapFeedback().nodeChanged(node);
                }
            } catch (NumberFormatException e) {
            }
        }
    }

}
