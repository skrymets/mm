package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.FontNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

public class FontFamilyActor extends XmlActorAdapter {

    public FontFamilyActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<FontNodeAction> getDoActionClass() {
        return FontNodeAction.class;
    }

    public void setFontFamily(MindMapNode node, String fontFamilyValue) {
        execute(getActionPair(node, fontFamilyValue));
    }

    public ActionPair getActionPair(MindMapNode node, String fontFamilyValue) {
        FontNodeAction fontFamilyAction = createFontNodeAction(node,
                fontFamilyValue);
        FontNodeAction undoFontFamilyAction = createFontNodeAction(node,
                node.getFontFamilyName());
        return new ActionPair(fontFamilyAction, undoFontFamilyAction);
    }

    private FontNodeAction createFontNodeAction(MindMapNode node,
                                                String fontValue) {
        FontNodeAction fontFamilyAction = new FontNodeAction();
        fontFamilyAction.setNode(getNodeID(node));
        fontFamilyAction.setFont(fontValue);
        return fontFamilyAction;

    }

    public void act(XmlAction action) {
        if (action instanceof FontNodeAction) {
            FontNodeAction fontFamilyAction = (FontNodeAction) action;
            NodeAdapter node = getNodeFromID(fontFamilyAction.getNode());
            String fontFamily = fontFamilyAction.getFont();
            if (!Objects.equals(node.getFontFamilyName(), fontFamily)) {
                node.establishOwnFont();
                node.setFont(getExMapFeedback().getFontThroughMap(
                        new Font(fontFamily, node.getFont().getStyle(), node
                                .getFont().getSize())));
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

}
