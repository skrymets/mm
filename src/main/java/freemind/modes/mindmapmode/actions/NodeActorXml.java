/*
 * Created on 05.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.modes.mindmapmode.actions;

import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActorXml;

public interface NodeActorXml extends ActorXml {
    /**
     * Returns the action pair to set the node @param selected to the current values.
     *
     */
    ActionPair apply(MindMap model, MindMapNode selected);

}
