/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.extensions;

import freemind.main.XMLElement;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.view.mindmapview.NodeView;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Simple, straight forward implementation of PermanentNodeHook
 * with some support for saving and loading
 *
 * @author foltin
 */
@Log4j2
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

    public void save(XMLElement xml) {
        String saveName = getName();
        // saveName=saveName.replace(File.separatorChar, '/');
        xml.setAttribute("name", saveName);
    }

    public void loadFrom(XMLElement child) {
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

    protected HashMap<String, String> loadNameValuePairs(XMLElement xml) {
        HashMap<String, String> result = new HashMap<>();
        if (xml.getChildren().isEmpty()) {
            return result;
        }
        XMLElement child = (XMLElement) xml.getChildren().get(0);
        if (child != null && PARAMETERS.equals(child.getName())) {
            for (Iterator<String> i = child.enumerateAttributeNames(); i.hasNext(); ) {
                String name = i.next();
                result.put(name, child.getStringAttribute(name));
            }
        }
        return result;
    }

    protected void saveNameValuePairs(HashMap<String, Object> nameValuePairs, XMLElement xml) {
        if (!nameValuePairs.isEmpty()) {
            XMLElement child = new XMLElement();
            child.setName(PARAMETERS);
            for (String key : nameValuePairs.keySet()) {
                Object value = nameValuePairs.get(key);
                child.setAttribute(key, value);
            }
            xml.addChild(child);
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

    protected void executeTransaction(final ActionPair pair)
            throws InterruptedException, InvocationTargetException {
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

    public void saveHtml(Writer pFileout) throws IOException {
    }


}
