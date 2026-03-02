/*
 * Created on 10.03.2004
 *
 */
package accessories.plugins;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Iterator;

@Slf4j
public class CreationModificationPlugin extends PermanentMindMapNodeHookAdapter {

    private String tooltipFormat;

    public CreationModificationPlugin() {
        super();
    }

    private void setStyle(MindMapNode node) {
        Object[] messageArguments = {
                node.getHistoryInformation().getCreatedAt(),
                node.getHistoryInformation().getLastModifiedAt()};
        if (tooltipFormat == null) {
            tooltipFormat = getResourceString("tooltip_format");
        }
        MessageFormat formatter = new MessageFormat(tooltipFormat);
        String message = formatter.format(messageArguments);
        setToolTip(node, getName(), message);
        log.trace("{}Tooltip for {} with parent {} is {}", this, node, node.getParentNode(), message);
    }

    public void shutdownMapHook() {
        removeToolTipRecursively(getNode());
        super.shutdownMapHook();
    }

    private void removeToolTipRecursively(MindMapNode node) {
        setToolTip(node, getName(), null);
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            removeToolTipRecursively(child);
        }
    }

    public void onUpdateChildrenHook(MindMapNode updatedNode) {
        super.onUpdateChildrenHook(updatedNode);
        setStyleRecursive(updatedNode);
    }

    public void onUpdateNodeHook() {
        super.onUpdateNodeHook();
        setStyle(getNode());
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        setStyleRecursive(node);
    }

    private void setStyleRecursive(MindMapNode node) {
        log.trace("setStyle {}", node);
        setStyle(node);
        // recurse:
        for (Iterator<MindMapNode> i = node.childrenFolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            setStyleRecursive(child);
        }
    }

    public void onAddChildren(MindMapNode pAddedChild) {
        setStyleRecursive(pAddedChild);
    }

    public void onNewChild(MindMapNode pNewChildNode) {
        setStyleRecursive(pNewChildNode);
    }

}
