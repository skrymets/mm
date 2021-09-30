/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

package freemind.modes;

import freemind.controller.*;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.extensions.PermanentNodeHookSubstituteUnknown;
import freemind.main.Resources;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.ViewFeedback;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author foltin
 * @date 07.02.2014
 */
public abstract class MapFeedbackAdapter implements MapFeedback, ViewFeedback {

	private HashMap<String, Font> fontMap = new HashMap<String, Font>();
	protected static org.slf4j.Logger logger = null;

	/**
	 * 
	 */
	public MapFeedbackAdapter() {
		if (logger == null) {
			logger = freemind.main.Resources.getInstance().getLogger(
					this.getClass().getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.MapFeedback#fireNodePreDeleteEvent(freemind.model.MindMapNode
	 * )
	 */
	@Override
	public void fireNodePreDeleteEvent(MindMapNode pNode) {

	}
	
	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#fireNodePostDeleteEvent(freemind.model.MindMapNode, freemind.model.MindMapNode)
	 */
	@Override
	public void fireNodePostDeleteEvent(MindMapNode pNode, MindMapNode pParent) {
		
	}

	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#fireRecursiveNodeCreateEvent(freemind.model.MindMapNode)
	 */
	@Override
	public void fireRecursiveNodeCreateEvent(MindMapNode pNode) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.MapFeedback#firePreSaveEvent(freemind.model.MindMapNode)
	 */
	@Override
	public void firePreSaveEvent(MindMapNode pNode) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#nodeChanged(freemind.model.MindMapNode)
	 */
	@Override
	public void nodeChanged(MindMapNode pNode) {
		if(getMap() != null) {
			getMap().setSaved(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#nodeRefresh(freemind.model.MindMapNode)
	 */
	@Override
	public void nodeRefresh(MindMapNode pNode) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#paste(freemind.model.MindMapNode,
	 * freemind.model.MindMapNode)
	 */
	@Override
	public void paste(MindMapNode pNode, MindMapNode pParent) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#getResourceString(java.lang.String)
	 */
	@Override
	public String getResourceString(String pTextId) {
		return Resources.getInstance().getResourceString(pTextId);
	}


	/* (non-Javadoc)
	 * @see freemind.model.MindMap.MapFeedback#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String pResourceId) {
		return Resources.getInstance().getProperty(pResourceId);
	}
	
	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#getIntProperty(java.lang.String, int)
	 */
	@Override
	public int getIntProperty(String pKey, int pDefaultValue) {
		return Resources.getInstance().getIntProperty(pKey, pDefaultValue);
	}

	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(String pProperty, String pValue) {
		Resources.getInstance().getProperties().setProperty(pProperty, pValue);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#out(java.lang.String)
	 */
	@Override
	public void out(String pFormat) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#getDefaultFont()
	 */
	@Override
	public Font getDefaultFont() {

		return null;
	}

	@Override
	public Font getFontThroughMap(Font font) {
		if (!fontMap.containsKey(font.toString())) {
			fontMap.put(font.toString(), font);
		}
		return (Font) fontMap.get(font.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MapFeedback#createNodeHook(java.lang.String,
	 * freemind.model.MindMapNode)
	 */
	@Override
	public NodeHook createNodeHook(String pLoadName, MindMapNode pNode) {
		PermanentNodeHookSubstituteUnknown hook = new PermanentNodeHookSubstituteUnknown(pLoadName);
		hook.setMap(getMap());
		pNode.addHook(hook);
		return hook;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.MapFeedback#invokeHooksRecursively(freemind.model.MindMapNode
	 * , freemind.model.MindMap)
	 */
	@Override
	public void invokeHooksRecursively(MindMapNode pNode, MindMap pModel) {
		for (Iterator i = pNode.childrenUnfolded(); i.hasNext();) {
			NodeAdapter child = (NodeAdapter) i.next();
			invokeHooksRecursively(child, getMap());
		}
		for (PermanentNodeHook hook : pNode.getHooks()) {
			hook.setController(this);
			hook.setMap(getMap());
			pNode.invokeHook(hook);
		}

	}

	@Override
	public void changeSelection(NodeView pNode, boolean pIsSelected) {
		
		
	}

	@Override
	public void onLostFocusNode(NodeView pNode) {
		
		
	}

	@Override
	public void onFocusNode(NodeView pNode) {
		
		
	}

	@Override
	public void setFolded(MindMapNode pModel, boolean pB) {
		
	}

	@Override
	public void onViewCreatedHook(NodeView pNewView) {
		
		
	}

	@Override
	public void onViewRemovedHook(NodeView pNodeView) {
		
		
	}

	@Override
	public NodeMouseMotionListener getNodeMouseMotionListener() {
		
		return null;
	}

	@Override
	public NodeMotionListener getNodeMotionListener() {
		
		return null;
	}

	@Override
	public NodeKeyListener getNodeKeyListener() {
		
		return null;
	}

	@Override
	public NodeDragListener getNodeDragListener() {
		
		return null;
	}

	@Override
	public NodeDropListener getNodeDropListener() {
		
		return null;
	}

	@Override
	public MapMouseMotionListener getMapMouseMotionListener() {
		
		return null;
	}

	@Override
	public MapMouseWheelListener getMapMouseWheelListener() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#getViewFeedback()
	 */
	@Override
	public ViewFeedback getViewFeedback() {
		return this;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#getViewAbstraction()
	 */
	@Override
	public ViewAbstraction getViewAbstraction() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see freemind.view.mindmapview.ViewFeedback#select(freemind.view.mindmapview.NodeView)
	 */
	public void select(NodeView node) {
	}

	/* (non-Javadoc)
	 * @see freemind.view.mindmapview.ViewFeedback#getNodeView(freemind.model.MindMapNode)
	 */
	public NodeView getNodeView(MindMapNode node) {
		return null;
	}
	
	/**
	 * This class sortes nodes by ascending depth of their paths to root. This
	 * is useful to assure that children are cutted <b>before </b> their
	 * fathers!!!.
	 * 
	 * Moreover, it sorts nodes with the same depth according to their position
	 * relative to each other.
	 */
	protected class NodesDepthComparator implements Comparator<MindMapNode> {
		public NodesDepthComparator() {
		}

		/* the < relation. */
		public int compare(MindMapNode n1, MindMapNode n2) {
			Object[] path1 = getMap().getPathToRoot(n1);
			Object[] path2 = getMap().getPathToRoot(n2);
			int depth = path1.length - path2.length;
			if (depth > 0)
				return -1;
			if (depth < 0)
				return 1;
			if (n1.isRoot()) // if n1 is root, n2 is root, too ;)
				return 0;
			return n1.getParentNode().getChildPosition(n1)
					- n2.getParentNode().getChildPosition(n2);
		}
	}

	public void sortNodesByDepth(List<MindMapNode> inPlaceList) {
		Collections.sort(inPlaceList, new NodesDepthComparator());
		logger.trace("Sort result: " + inPlaceList);
	}

	@Override
	public void registerMouseWheelEventHandler(MouseWheelEventHandler pHandler) {
	}

	@Override
	public void deRegisterMouseWheelEventHandler(MouseWheelEventHandler pHandler) {
	}


	
}
