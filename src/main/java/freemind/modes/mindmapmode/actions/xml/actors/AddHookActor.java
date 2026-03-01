/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.*;
import freemind.extensions.*;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.view.mindmapview.MapView;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.util.*;
import java.util.Map;

/**
 * @author foltin
 * {@code @date} 01.04.2014
 */
@Slf4j
public class AddHookActor extends XmlActorAdapter {

    public AddHookActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    private HookFactory getHookFactory() {
        return getExMapFeedback().getHookFactory();
    }

    private HookInstantiationMethod getInstantiationMethod(String hookName) {
        HookFactory factory = getHookFactory();
        return factory.getInstantiationMethod(hookName);
    }

    public void addHook(MindMapNode focussed, List<MindMapNode> selected, String hookName, Properties pHookProperties) {
        HookNodeAction doAction = createHookNodeAction(focussed, selected, hookName, pHookProperties);

        XmlAction undoAction = null;
        // this is the non-operation:
        undoAction = new CompoundAction();
        if (getInstantiationMethod(hookName).isPermanent()) {
            // double application = remove.
            undoAction = createHookNodeUndoAction(focussed, selected, hookName);
        }
        if (getInstantiationMethod(hookName).isUndoable()) {
            execute(new ActionPair(doAction, undoAction));
        } else {
            // direct invocation without undo and such stuff.
            invoke(focussed, selected, hookName, null);
        }
    }

    private XmlAction createHookNodeUndoAction(MindMapNode focussed, List<MindMapNode> selectedMindMapNodes, String hookName) {

        HookNodeAction hookNodeAction = createHookNodeAction(focussed, selectedMindMapNodes, hookName, null);
        CompoundAction.Choice hookNodeActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(hookNodeAction);

        CompoundAction undoAction = new CompoundAction();
        undoAction.addChoice(hookNodeActionChoice);

        HookInstantiationMethod instMethod = getInstantiationMethod(hookName);
        // get destination nodes
        Collection<MindMapNode> destinationNodes = instMethod.getDestinationNodes(getExMapFeedback(), focussed, selectedMindMapNodes);
        MindMapNode adaptedFocussedNode = instMethod.getCenterNode(getExMapFeedback(), focussed, selectedMindMapNodes);
        // test if hook already present
        if (!instMethod.isAlreadyPresent(hookName, adaptedFocussedNode)) {
            return undoAction;
        }

        // remove the hook:
        for (MindMapNode currentDestinationNode : destinationNodes) {
            // find the hook in the current node, if present:
            for (PermanentNodeHook hook : currentDestinationNode.getActivatedHooks()) {
                if (hook.getName().equals(hookName)) {
                    if (!(hook instanceof DontSaveMarker)) {
                        Document tempDoc = FreeMindXml.newDocument();
                        Element hookElement = tempDoc.createElement("hook");
                        tempDoc.appendChild(hookElement);
                        hook.save(tempDoc, hookElement);
                        List<Element> hookChildren = FreeMindXml.getChildElements(hookElement);
                        if (hookChildren.size() == 1) {
                            Element parameters = hookChildren.get(0);

                            if (Objects.equals(parameters.getTagName(), PermanentNodeHookAdapter.PARAMETERS)) {
                                // standard save mechanism
                                NamedNodeMap attrs = parameters.getAttributes();
                                for (int i = 0; i < attrs.getLength(); i++) {
                                    String name = attrs.item(i).getNodeName();
                                    NodeChildParameter nodeHookChild = new NodeChildParameter();
                                    nodeHookChild.setKey(name);
                                    nodeHookChild.setValue(parameters.getAttribute(name));
                                    hookNodeAction.addNodeChildParameter(nodeHookChild);
                                }

                            } else {
                                log.warn("Unusual save mechanism, implement me.");
                            }
                        } else {
                            log.warn("Unusual save mechanism, implement me.");
                        }
                    }
                    /*
                     * fc, 30.7.2004: we have to break. otherwise the
                     * collection is modified at two points (i.e., the
                     * collection is not valid anymore after removing one
                     * element). But this is no problem, as there exist only
                     * "once" plugins currently.
                     */
                    break;
                }
            }
        }
        return undoAction;
    }

    public HookNodeAction createHookNodeAction(MindMapNode focussed, List<MindMapNode> selectedMindMapNodes, String hookName, Properties pHookProperties) {

        HookNodeAction hookNodeAction = new HookNodeAction();
        hookNodeAction.setNode(getNodeID(focussed));
        hookNodeAction.setHookName(hookName);
        // selectedNodes list
        for (MindMapNode node : selectedMindMapNodes) {
            NodeListMember nodeListMember = new NodeListMember();
            nodeListMember.setNode(getNodeID(node));
            hookNodeAction.addNodeListMember(nodeListMember);
        }
        if (pHookProperties != null) {
            for (Map.Entry<Object, Object> entry : pHookProperties.entrySet()) {
                NodeChildParameter nodeChildParameter = new NodeChildParameter();
                nodeChildParameter.setKey((String) entry.getKey());
                nodeChildParameter.setValue((String) entry.getValue());
                hookNodeAction.addNodeChildParameter(nodeChildParameter);
            }
        }
        return hookNodeAction;
    }

    public void act(XmlAction action) {
        if (action instanceof HookNodeAction) {
            HookNodeAction hookNodeAction = (HookNodeAction) action;
            MindMapNode selected = getNodeFromID(hookNodeAction.getNode());
            List<MindMapNode> selectedMindMapNodes = new ArrayList<>();
            for (NodeListMember node : hookNodeAction.getNodeListMemberList()) {
                selectedMindMapNodes.add(getNodeFromID(node.getNode()));
            }
            // reconstruct child-xml as DOM:
            Document tempDoc = FreeMindXml.newDocument();
            Element xmlParent = tempDoc.createElement(hookNodeAction.getHookName());
            tempDoc.appendChild(xmlParent);
            Element child = tempDoc.createElement(PermanentNodeHookAdapter.PARAMETERS);
            xmlParent.appendChild(child);
            for (NodeChildParameter childParameter : hookNodeAction.getNodeChildParameterList()) {
                child.setAttribute(childParameter.getKey(), childParameter.getValue());
            }
            invoke(selected, selectedMindMapNodes, hookNodeAction.getHookName(), xmlParent);
        }
    }

    public Class<HookNodeAction> getDoActionClass() {
        return HookNodeAction.class;
    }

    public void removeHook(MindMapNode pFocussed, List<MindMapNode> pSelecteds, String pHookName) {
        HookNodeAction undoAction = createHookNodeAction(pFocussed, pSelecteds, pHookName, null);

        XmlAction doAction = null;
        // this is the non operation:
        doAction = new CompoundAction();
        if (getInstantiationMethod(pHookName).isPermanent()) {
            // double application = remove.
            doAction = createHookNodeUndoAction(pFocussed, pSelecteds, pHookName);
        }
        execute(new ActionPair(undoAction, doAction));
    }

    private void invoke(MindMapNode focussed, List<MindMapNode> selecteds, String hookName, Element pXmlParent) {
        log.trace("invoke(selecteds) called.");
        HookInstantiationMethod instMethod = getInstantiationMethod(hookName);
        // get destination nodes
        Collection<MindMapNode> destinationNodes = instMethod.getDestinationNodes(getExMapFeedback(), focussed, selecteds);
        MindMapNode adaptedFocussedNode = instMethod.getCenterNode(getExMapFeedback(), focussed, selecteds);
        // test if hook already present
        if (instMethod.isAlreadyPresent(hookName, adaptedFocussedNode)) {
            // remove the hook:
            for (MindMapNode currentDestinationNode : destinationNodes) {
                // find the hook ini the current node, if present:
                for (PermanentNodeHook hook : currentDestinationNode.getActivatedHooks()) {
                    if (hook.getName().equals(hookName)) {
                        currentDestinationNode.removeHook(hook);
                        getExMapFeedback().nodeChanged(currentDestinationNode);
                        /*
                         * fc, 30.7.2004: we have to break. otherwise the
                         * collection is modified at two points (i.e., the
                         * collection is not valid anymore after removing one
                         * element). But this is no problem, as there exist only
                         * "once" plugins currently.
                         */
                        break;
                    }
                }
            }
        } else {
            // add the hook
            for (MindMapNode currentDestinationNode : destinationNodes) {
                NodeHook hook = getExMapFeedback().createNodeHook(hookName, currentDestinationNode);
                log.trace("created hook {}", hookName);
                // set parameters, if present
                if (pXmlParent != null && hook instanceof PermanentNodeHook) {
                    ((PermanentNodeHook) hook).loadFrom(pXmlParent);
                }
                // call invoke.
                currentDestinationNode.invokeHook(hook);
                if (hook instanceof PermanentNodeHook) {
                    PermanentNodeHook permHook = (PermanentNodeHook) hook;
                    log.trace("This is a permanent hook {}", hookName);
                    // the focused receives the focus:
                    if (currentDestinationNode == adaptedFocussedNode) {
                        permHook.onFocusNode(getNodeView(currentDestinationNode));
                    }
                    // using this method, the map is dirty now. This is
                    // important to
                    // guarantee, that the hooks are saved.
                    getExMapFeedback().nodeChanged(currentDestinationNode);
                }
            }
            finishInvocation(focussed, selecteds, adaptedFocussedNode,
                    destinationNodes);
        }
    }

    /**
     */
    private NodeView getNodeView(MindMapNode pNode) {
        return getViewAbstraction().getNodeView(pNode);
    }

    protected MapView getViewAbstraction() {
        MapView viewAbstraction = getExMapFeedback().getViewAbstraction();
        if (viewAbstraction == null) {
            throw new IllegalArgumentException("View abstraction not available.");
        }
        return viewAbstraction;
    }

    /**
     * @param focussed            The real focussed node
     * @param selecteds           The list of selected nodes
     * @param adaptedFocussedNode The calculated focussed node (if the hook specifies, that the
     *                            hook should apply to root, then this is the root node).
     * @param destinationNodes    The calculated list of selected nodes (see last)
     */
    private void finishInvocation(MindMapNode focussed, List<MindMapNode> selecteds, MindMapNode adaptedFocussedNode, Collection<MindMapNode> destinationNodes) {
        // restore selection only, if nothing selected.
        if (getViewAbstraction().getSelecteds().isEmpty()) {
            // select all destination nodes:
            getExMapFeedback().select(focussed, selecteds);
        }
    }

}
