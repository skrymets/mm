/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.extensions;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.NodeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Writer;

/**
 * Basic interface for all node hooks that are permanent.
 * Thus, there are methods that inform the plugin about changes on the node, it is stick to.
 * Moreover, methods for loading and saving the hook are present.
 */
public interface PermanentNodeHook extends NodeHook {

    void onFocusNode(NodeView nodeView);

    void onLostFocusNode(NodeView nodeView);

    /**
     * Fired after node is getting visible (is unfolded after having been folded).
     */
    void onViewCreatedHook(NodeView nodeView);

    /**
     * Fired after a node is getting invisible (folded).
     */
    void onViewRemovedHook(NodeView nodeView);

    /**
     * If the node I belong to is changed, I get this notification.
     */
    void onUpdateNodeHook();

    /**
     * Is called if the addedChildNode is inserted as a direct child of the
     * node, this hook is attached to. The cases in which this method is called
     * contain new nodes, paste, move, etc.
     * <p>
     * Ah, don't call propagate in this method, as paste introduces nodes with
     * the hook and you'll have them twice, ... see onNewChild
     */
    void onAddChild(MindMapNode addedChildNode);

    /**
     * Is only called, if a new nodes is inserted as a child. Remark: In this
     * case onAddChild is called too and moreover *before* this method. see
     * onAddChild.
     */
    void onNewChild(MindMapNode newChildNode);

    /**
     * This method is called, if a child is added to me or to any of my
     * children. (See onUpdateChildrenHook)
     */
    void onAddChildren(MindMapNode addedChild);

    void onRemoveChild(MindMapNode oldChildNode);

    /**
     * This method is called, if a child is removed to me or to any of my
     * children. (See onUpdateChildrenHook)
     *
     */
    void onRemoveChildren(MindMapNode oldChildNode, MindMapNode oldDad);

    /**
     * If any of my children is updated, I get this notification.
     */
    void onUpdateChildrenHook(MindMapNode updatedNode);

    void save(Document doc, Element hookElement);

    void loadFrom(Element child);

    /**
     * Can be used to adjust some things after a paste action. (Currently it is used for clones).
     */
    void processUnfinishedLinks();

    /**
     * Can be used to contribute to the standard html export.
     *
     */
    void saveHtml(Writer pFileout);
}
