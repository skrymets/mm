/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package accessories.plugins;

import accessories.plugins.ClonePasteAction.CloneProperties;
import accessories.plugins.ClonePasteAction.ClonePropertiesObserver;
import accessories.plugins.ClonePasteAction.Registration;
import freemind.extensions.PermanentNodeHook;
import freemind.main.Tools;
import freemind.main.XMLElement;
import freemind.model.MindMapNode;
import freemind.modes.ModeController.NodeLifetimeListener;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;

import java.awt.*;
import java.util.*;

public class ClonePlugin extends PermanentMindMapNodeHookAdapter implements
		NodeLifetimeListener, ClonePropertiesObserver {

	public static final String CLONE_ITSELF_FALSE = "false";
	public static final String CLONE_ITSELF_TRUE = "true";
	public static final String PLUGIN_LABEL = "accessories/plugins/ClonePlugin.properties";
	public static final String XML_STORAGE_CLONES = "CLONE_IDS";
	public static final String XML_STORAGE_CLONE_ID = "CLONE_ID";
	public static final String XML_STORAGE_CLONE_ITSELF = "CLONE_ITSELF";

	/**
	 * This is the master list. {@link ClonePlugin#mCloneNodes mCloneNodes} is
	 * derived from it. It contains id strings.
	 */
	private HashSet<String> mCloneNodeIds;
	/**
	 * Includes the original node. This is a cached list with the MindMapNodes
	 * belonging to the {@link ClonePlugin#mCloneNodeIds mCloneNodeIds}.
	 */
	private HashSet<MindMapNode> mCloneNodes;

	private String mCloneId;
	private boolean mDisabled = false;
	private Boolean mCloneItself = null;
	private CloneProperties mClonePropertiesHolder;

	public ClonePlugin() {
	}

	public void invoke(MindMapNode node) {
		super.invoke(node);
		registerPlugin();
	}

	public void addClone(MindMapNode cloneNode) {
		mCloneNodeIds.add(getMindMapController().getNodeID(cloneNode));
		clearCloneCache();
	}

	public void clearCloneCache() {
		mCloneNodes = new HashSet<>();
	}

	private void disablePlugin() {
		mDisabled = true;
		getMindMapController().getController().errorMessage(
				getMindMapController().getText("clone_plugin_impossible"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if (getHook(getNode()) != null) {
					removeHook();
				}
			}
		});
	}

	/**
	 * double add = remove.
	 * 
	 */
	protected void removeHook() {
		// first deactivate cloning for this node (otherwise, the deactivation will be cloned, too!)
		deregisterCloning();
		Vector<MindMapNode> selecteds = Tools.getVectorWithSingleElement(getNode());
		getMindMapController()
				.addHook(getNode(), selecteds, PLUGIN_LABEL, null);
	}

	public void save(XMLElement xml) {
		super.save(xml);
		HashMap<String, Object> values = new HashMap<>();
		values.put(XML_STORAGE_CLONES, getCloneIdsAsString());
		values.put(XML_STORAGE_CLONE_ID, mCloneId);
		String cloneItselfValue = getCloneItselfValue();
		values.put(XML_STORAGE_CLONE_ITSELF, cloneItselfValue);
		logger.trace("Saved mCloneItself to " + cloneItselfValue);
		saveNameValuePairs(values, xml);
		logger.trace("Saved clone plugin");
	}

	protected String getCloneItselfValue() {
		return mClonePropertiesHolder
				.isCloneItself() ? CLONE_ITSELF_TRUE
				: CLONE_ITSELF_FALSE;
	}

	public String getCloneIdsAsString() {
		StringBuffer cloneIds = new StringBuffer();
		for (String cloneId : mCloneNodeIds) {
			cloneIds.append(cloneId);
			cloneIds.append(",");
		}
		return cloneIds.toString();
	}

	public void loadFrom(XMLElement child) {
		super.loadFrom(child);
		mCloneNodes = null;
		mCloneNodeIds = new HashSet<>();
		HashMap<String, String> values = loadNameValuePairs(child);
		String cloneIds = (String) values.get(XML_STORAGE_CLONES);
		if (cloneIds != null) {
			StringTokenizer st = new StringTokenizer(cloneIds, ",");
			while (st.hasMoreTokens()) {
				String cloneId = st.nextToken();
				mCloneNodeIds.add(cloneId);
			}
		}
		mCloneId = (String) values.get(XML_STORAGE_CLONE_ID);
		if (values.containsKey(XML_STORAGE_CLONE_ITSELF)) {
			mCloneItself = Boolean.valueOf(Tools.safeEquals(CLONE_ITSELF_TRUE,
					values.get(XML_STORAGE_CLONE_ITSELF)));
		} else {
			mCloneItself = Boolean.FALSE;
		}
		logger.trace("Loaded mCloneItself to " + mCloneItself);
	}

	public void shutdownMapHook() {
		logger.trace("Shutdown of clones");
		deregisterPlugin();
		super.shutdownMapHook();
	}

	private void registerPlugin() {
		if (mDisabled)
			return;
		/*
		 * test for error cases: - orig is child of clone now - if clone is a
		 * child of clone, this is here not reachable, as the plugin remains
		 * active and is not newly invoked. Hmm, what to do?
		 */
		MindMapNode originalNode = getNode();
		HashSet<MindMapNode> cloneNodes = getCloneNodes();
		logger.trace("Invoke shadow class with orig: "
				+ printNodeId(originalNode) + " and clones "
				+ printNodeIds(cloneNodes));
		// check for error case that clones are descendant of one another.
		for (MindMapNode cloneNode : cloneNodes) {
			if (originalNode != null && originalNode.isDescendantOf(cloneNode)) {
				disablePlugin();
				return;
			}
		}
		getMindMapController().registerNodeLifetimeListener(this, false);
		Registration registration = getRegistration();
		if (mCloneId == null) {
			// hmm, it seems, that I am the first. Let's generate an id:
			mCloneId = registration.generateNewCloneId(null);
		}
		registration.registerClone(mCloneId, this);
		// the clone list contains itself, too.
		addClone(getNode());
		mClonePropertiesHolder = registration
				.getCloneProperties(mCloneId);
		if (mCloneItself != null) {
			mClonePropertiesHolder.setCloneItself(mCloneItself.booleanValue());
		}
		mClonePropertiesHolder.registerObserver(this);
	}

	protected Registration getRegistration() {
		return (Registration) getPluginBaseClass();
	}

	private void deregisterPlugin() {
		mClonePropertiesHolder.deregisterObserver(this);
		deregisterCloning();
		getMindMapController().deregisterNodeLifetimeListener(this);
		// remove icon
		getNode().setStateIcon(getName(), null);
		getMindMapController().nodeRefresh(getNode());
	}

	protected void deregisterCloning() {
		getRegistration().deregisterClone(mCloneId, this);
	}

	public void onCreateNodeHook(MindMapNode node) {
		HashSet<MindMapNode> cloneNodes = getCloneNodes();
		for (MindMapNode clone :cloneNodes) {
			for (MindMapNode clone2 : cloneNodes) {
				if (clone != clone2) {
					checkForChainError(clone, node, clone2);
				}
			}
		}
	}

	public void onPreDeleteNode(MindMapNode node) {
	}

	public void onPostDeleteNode(MindMapNode node, MindMapNode parent) {
	}

	/**
	 * @return a list of {@link MindMapNode}s including the original node!
	 */
	HashSet<MindMapNode> getCloneNodes() {
		// is list up to date?
		if (mCloneNodes != null) {
			for (MindMapNode cloneNode :mCloneNodes) {
				if (cloneNode.getParentNode() == null) {
					clearCloneCache();
				}
			}
		} else {
			clearCloneCache();
		}
		if (mCloneNodes.isEmpty()) {
			mCloneNodes.add(getNode());
			for (Iterator<String> it = mCloneNodeIds.iterator(); it.hasNext();) {
				String cloneId = it.next();
				try {
					mCloneNodes.add(getMindMapController().getNodeFromID(
							cloneId));
				} catch (IllegalArgumentException e) {
					// freemind.main.Resources.getInstance().logException(e);
					it.remove();
				}
			}
		}
		return mCloneNodes;
	}

	/**
	 * @param pCloneNode
	 * @return
	 */
	private String printNodeId(MindMapNode pCloneNode) {
		try {
			return getMindMapController().getNodeID(pCloneNode) + ": '"
					+ (pCloneNode.getShortText(getMindMapController())) + "'";
		} catch (Exception e) {
			return "NOT FOUND: '" + pCloneNode + "'";
		}
	}

	/**
	 * @param pTargets
	 * @return
	 */
	private String printNodeIds(Collection<MindMapNode> pTargets) {
		Vector<String> strings = new Vector<>();
		for (MindMapNode node : pTargets) {
			strings.add(printNodeId(node));
		}
		return "" + strings;
	}

	private void checkForChainError(MindMapNode originalNode, MindMapNode node,
			MindMapNode cloneNode) {
		if (cloneNode.isDescendantOfOrEqual(node)
				&& node.isDescendantOfOrEqual(originalNode)) {
			// orig -> .... -> node -> .. -> clone
			disablePlugin();
		}
	}

	public void removeClone(MindMapNode pCloneNode) {
		String nodeID = getMindMapController().getNodeID(pCloneNode);
		mCloneNodeIds.remove(nodeID);
		clearCloneCache();
		if (mCloneNodeIds.isEmpty()
				|| (mCloneNodeIds.size() == 1 && mCloneNodeIds
						.contains(getNodeId()))) {
			// remove myself
			logger.info("I'm the last clone " + nodeID);
			removeHook();
		}
	}

	public static ClonePlugin getHook(MindMapNode originalNode) {
		if (originalNode == null) {
			return null;
		}
		for (PermanentNodeHook hook :originalNode.getActivatedHooks()) {
			if (hook instanceof ClonePlugin) {
				ClonePlugin cloneHook = (ClonePlugin) hook;
				return cloneHook;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.extensions.PermanentNodeHookAdapter#processUnfinishedLinks()
	 */
	public void processUnfinishedLinks() {
		super.processUnfinishedLinks();
		if (mDisabled)
			return;
		HashSet<MindMapNode> cloneNodes = getCloneNodes();
		// activate other clones, if not already activated.
		for (MindMapNode cloneNode : cloneNodes) {
			ClonePlugin hook = getHook(cloneNode);
			if (hook == null && cloneNode != null) {
				// add hook to clone partner:
				Vector<MindMapNode> selecteds = Tools.getVectorWithSingleElement(cloneNode);
				// Transport the data to the plugin, as this method calls
				// invoke.
				Properties hookProperties = new Properties();
				hookProperties.setProperty(XML_STORAGE_CLONE_ID, mCloneId);
				hookProperties.setProperty(XML_STORAGE_CLONES,
						getCloneIdsAsString());
				hookProperties.setProperty(XML_STORAGE_CLONE_ITSELF, getCloneItselfValue());
				getMindMapController().addHook(cloneNode, selecteds,
						PLUGIN_LABEL, hookProperties);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see accessories.plugins.ClonePasteAction.ClonePropertiesObserver#
	 * propertiesChanged(accessories.plugins.ClonePasteAction.CloneProperties)
	 */
	public void propertiesChanged(CloneProperties pCloneProperties) {
		mCloneItself = Boolean.valueOf(pCloneProperties.isCloneItself());
	}

	public String getCloneId() {
		return mCloneId;
	}

}
