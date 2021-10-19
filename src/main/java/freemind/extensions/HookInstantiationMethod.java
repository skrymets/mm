/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 22.07.2004
 */
/*$Id: HookInstantiationMethod.java,v 1.1.4.1.16.3 2007/06/05 20:53:30 dpolivaev Exp $*/
package freemind.extensions;

import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;

import java.util.*;

public class HookInstantiationMethod {
    private interface DestinationNodesGetter {
        Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds);

        MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds);
    }

    private static class DefaultDestinationNodesGetter implements DestinationNodesGetter {
        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return selecteds;
        }

        public MindMapNode getCenterNode(MapFeedback controller,
                                         MindMapNode focussed, List<MindMapNode> selecteds) {
            return focussed;
        }
    }

    private static class RootDestinationNodesGetter implements DestinationNodesGetter {
        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            Vector<MindMapNode> returnValue = new Vector<MindMapNode>();
            returnValue.add(controller.getMap().getRootNode());
            return returnValue;
        }

        public MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return controller.getMap().getRootNode();
        }
    }

    private static class AllDestinationNodesGetter implements
            DestinationNodesGetter {
        private void addChilds(MindMapNode node, Collection<MindMapNode> allNodeCollection) {
            allNodeCollection.add(node);
            for (Iterator<MindMapNode> i = node.childrenFolded(); i.hasNext(); ) {
                MindMapNode child = i.next();
                addChilds(child, allNodeCollection);
            }
        }

        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            Vector<MindMapNode> returnValue = new Vector<MindMapNode>();
            addChilds(controller.getMap().getRootNode(), returnValue);
            return returnValue;
        }

        public MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return focussed;
        }

    }

    private final boolean isSingleton;
    private final DestinationNodesGetter getter;
    private final boolean isPermanent;
    private final boolean isUndoable;

    public boolean isSingleton() {
        return isSingleton;
    }

    /**
     * @return Returns the isPermanent.
     */
    public boolean isPermanent() {
        return isPermanent;
    }

    private HookInstantiationMethod(boolean isPermanent, boolean isSingleton,
                                    DestinationNodesGetter getter, boolean isUndoable) {
        this.isPermanent = isPermanent;
        this.isSingleton = isSingleton;
        this.getter = getter;
        this.isUndoable = isUndoable;
    }

    static final public HookInstantiationMethod Once = new HookInstantiationMethod(
            true, true, new DefaultDestinationNodesGetter(), true);
    /**
     * The hook should only be added/removed to the root node.
     */
    static final public HookInstantiationMethod OnceForRoot = new HookInstantiationMethod(
            true, true, new RootDestinationNodesGetter(), true);
    /**
     * Each (or none) node should have the hook.
     */
    static final public HookInstantiationMethod OnceForAllNodes = new HookInstantiationMethod(
            true, true, new AllDestinationNodesGetter(), true);
    /**
     * This is for MindMapHooks in general. Here, no undo- or redoaction are
     * performed, the undo information is given by the actions the hook
     * performs.
     */
    static final public HookInstantiationMethod Other = new HookInstantiationMethod(
            false, false, new DefaultDestinationNodesGetter(), false);
    /**
     * This is for MindMapHooks that wish to be applied to root, whereevery they
     * are called from. Here, no undo- or redoaction are performed, the undo
     * information is given by the actions the hook performs.
     */
    static final public HookInstantiationMethod ApplyToRoot = new HookInstantiationMethod(
            false, false, new RootDestinationNodesGetter(), false);

    static public HashMap<String, HookInstantiationMethod> getAllInstanciationMethods() {
        HashMap<String, HookInstantiationMethod> res = new HashMap<>();
        res.put("Once", Once);
        res.put("OnceForRoot", OnceForRoot);
        res.put("OnceForAllNodes", OnceForAllNodes);
        res.put("Other", Other);
        res.put("ApplyToRoot", ApplyToRoot);
        return res;
    }

    /**
     *
     */
    public Collection<MindMapNode> getDestinationNodes(MapFeedback controller,
                                                       MindMapNode focussed, List<MindMapNode> selecteds) {
        return getter.getDestinationNodes(controller, focussed, selecteds);
    }

    /**
     *
     */
    public boolean isAlreadyPresent(String hookName, MindMapNode focussed) {
        for (Iterator<PermanentNodeHook> i = focussed.getActivatedHooks().iterator(); i.hasNext(); ) {
            PermanentNodeHook hook = i.next();
            if (hookName.equals(hook.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public MindMapNode getCenterNode(MapFeedback controller,
                                     MindMapNode focussed, List<MindMapNode> selecteds) {
        return getter.getCenterNode(controller, focussed, selecteds);
    }

    /**
     *
     */
    public boolean isUndoable() {
        return isUndoable;
    }
}
