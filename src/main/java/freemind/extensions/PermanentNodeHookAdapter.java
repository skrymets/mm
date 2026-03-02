/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.extensions;

import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;

/**
 * Simple, straight forward implementation of PermanentNodeHook
 * with some support for saving and loading
 */
@Slf4j
public class PermanentNodeHookAdapter extends NodeHookAdapter implements PermanentNodeHook {

    public PermanentNodeHookAdapter() {
        super();
    }

    public void shutdownMapHook() {
        log.trace("shutdownMapHook");
        setNode(null);
        setMap(null);
        super.shutdownMapHook();
    }

    public void onUpdateNodeHook() {
        log.trace("onUpdateNodeHook");
    }

    public void onUpdateChildrenHook(MindMapNode updatedNode) {
        log.trace("onUpdateChildrenHook");
    }

    public void onAddChild(MindMapNode newChildNode) {
        log.trace("onAddChild");
    }

    public void onNewChild(MindMapNode newChildNode) {
        log.trace("onNewChild");
    }

    public void onRemoveChild(MindMapNode oldChildNode) {
        log.trace("onRemoveChild");
    }

    public void save(Document doc, Element xml) {
        String saveName = getName();
        // saveName=saveName.replace(File.separatorChar, '/');
        xml.setAttribute("name", saveName);
    }

    public void loadFrom(Element child) {
    }

    public void onFocusNode(NodeView nodeView) {
        log.trace("onSelectHook");

    }

    public void onLostFocusNode(NodeView nodeView) {
        log.trace("onDeselectHook");
    }

    public void onAddChildren(MindMapNode addedChild) {
        log.trace("onAddChildren");
    }

    public static final String PARAMETERS = "Parameters";

    protected HashMap<String, String> loadNameValuePairs(Element xml) {
        HashMap<String, String> result = new HashMap<>();
        List<Element> children = FreeMindXml.getChildElements(xml);
        if (children.isEmpty()) {
            return result;
        }
        Element child = children.get(0);
        if (PARAMETERS.equals(child.getTagName())) {
            NamedNodeMap attrs = child.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                String name = attrs.item(i).getNodeName();
                result.put(name, child.getAttribute(name));
            }
        }
        return result;
    }

    protected void saveNameValuePairs(HashMap<String, Object> nameValuePairs, Document doc, Element xml) {
        if (!nameValuePairs.isEmpty()) {
            Element child = doc.createElement(PARAMETERS);
            for (String key : nameValuePairs.keySet()) {
                Object value = nameValuePairs.get(key);
                child.setAttribute(key, value.toString());
            }
            xml.appendChild(child);
        }
    }

    public void onRemoveChildren(MindMapNode oldChildNode, MindMapNode oldDad) {
        log.trace("onRemoveChildren");
    }

    public void onViewCreatedHook(NodeView nodeView) {
    }

    public void onViewRemovedHook(NodeView nodeView) {
    }

    protected void setToolTip(String key, String value) {
        setToolTip(getNode(), key, value);
    }

    protected void setToolTip(MindMapNode node, String key, String value) {
        getController().setToolTip(node, key, value);
    }

    protected void executeTransaction(final ActionPair pair) {
    }

    public void registerFilter() {
    }

    public void deregisterFilter() {
    }

    public ActionPair filterAction(ActionPair pPair) {
        return null;
    }

    public void processUnfinishedLinks() {
    }

    public void saveHtml(Writer pFileout) {
    }

}
