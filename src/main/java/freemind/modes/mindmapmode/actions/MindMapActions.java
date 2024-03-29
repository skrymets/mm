/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
 * Created on 05.05.2004
 */

package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.generated.instance.Pattern;
import freemind.extensions.NodeHook;
import freemind.model.MindMap;
import freemind.model.MindMapLink;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import freemind.modes.MindMapArrowLink;
import freemind.modes.attributes.Attribute;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Properties;

/**
 * This is the central method interface of actions that can be undertaken on
 * nodes. Whenever you want to change the mindmap choose one of these actions as
 * they do proper redisplay, inform others about the actions, the actions are
 * all undoable etc.etc.
 * 
 * All these methods do redisplay, because they are offered from the
 * MindMapController for use.
 * 
 * @author foltin see MindMapController
 * */
public interface MindMapActions {
	public static final int NEW_CHILD_WITHOUT_FOCUS = 1; // old model of
															// insertion
	public static final int NEW_CHILD = 2;
	public static final int NEW_SIBLING_BEHIND = 3;
	public static final int NEW_SIBLING_BEFORE = 4;

	/**
	 * The following modes are present: public final int NEW_CHILD_WITHOUT_FOCUS
	 * = 1; // old model of insertion public final int NEW_CHILD = 2; public
	 * final int NEW_SIBLING_BEHIND = 3; public final int NEW_SIBLING_BEFORE =
	 * 4; see MindMapController
	 * */
	public void edit(KeyEvent e, boolean addNew, boolean editLong);

	public void setNodeText(MindMapNode selected, String newText);

	public void setNoteText(MindMapNode selected, String newText);

	/**
	 * Another variant of addNew. If the index of the new node as a child of
	 * parent is known, this method is easier to use. Moreover, it does not
	 * automatically start an editor.
	 * @param index 0-based index of the new node. Use getChildCount or -1 to
	 * add a new node at the bottom.
	 * @param newNodeIsLeft
	 *            here, normally parent.isLeft() or null is used.
	 * @return returns the new node.
	 */
	MindMapNode addNewNode(MindMapNode parent, int index, boolean newNodeIsLeft);

	public void deleteNode(MindMapNode selectedNode);

	public Transferable cut();

	/**
	 * @param nodeList
	 *            a list of MindMapNode elements
	 * @return the result of the cut operation.
	 */
	public Transferable cut(List<MindMapNode> nodeList);

	/**
	 * moves selected and selecteds (if they are child of the same parent and
	 * adjacent) in the direction specified (up = -1, down = 1).
	 * */
	void moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction);

	/**
	 */
	void setFolded(MindMapNode node, boolean folded);

	/**
	 * Switches the folding state of all selected nodes. In fact, it determines
	 * one action (fold or unfold) and applies this action to every selected
	 * node.
	 */
	void toggleFolded();

	public void setBold(MindMapNode node, boolean bolded);

	public void setStrikethrough(MindMapNode node, boolean strikethrough);
	
	public void setItalic(MindMapNode node, boolean isItalic);

	public void setNodeColor(MindMapNode node, Color color);

	public void setNodeBackgroundColor(MindMapNode node, Color color);

	public void blendNodeColor(MindMapNode node);

	public void setFontFamily(MindMapNode node, String fontFamily);

	public void setFontSize(MindMapNode node, String fontSizeValue);

	/**
	 * This method is nice, but how to get a MindIcon ? see
	 * freemind.modes.MindIcon.factory(String)
	 */
	public void addIcon(MindMapNode node, MindIcon icon);

	public int removeLastIcon(MindMapNode node);

	public void removeAllIcons(MindMapNode node);

	public void applyPattern(MindMapNode node, Pattern pattern);

	public void setNodeStyle(MindMapNode node, String style);

	public void setEdgeColor(MindMapNode node, Color color);

	/** The widths range from -1 (for equal to parent) to 0 (thin), 1, 2, 4, 8. */
	public void setEdgeWidth(MindMapNode node, int width);

	public void setEdgeStyle(MindMapNode node, String style);

	public void setCloud(MindMapNode node, boolean enable);

	public void setCloudColor(MindMapNode node, Color color);

	// public void setCloudWidth(MindMapNode node, int width);
	// public void setCloudStyle(MindMapNode node, String style);
	/**
	 * Source holds the MindMapArrowLinkModel and points to the id placed in
	 * target.
	 */
	public void addLink(MindMapNode source, MindMapNode target);

	public void removeReference(MindMapLink arrowLink);

	public void changeArrowsOfArrowLink(MindMapArrowLink arrowLink,
			boolean hasStartArrow, boolean hasEndArrow);

	public void setArrowLinkColor(MindMapLink arrowLink, Color color);

	public void setArrowLinkEndPoints(MindMapArrowLink link, Point startPoint,
			Point endPoint);

	/**
	 * Adds a textual hyperlink to a node (e.g. http:/freemind.sourceforge.net)
	 */
	public void setLink(MindMapNode node, String link);

	/**
	 * @param t the content to be pasted
	 * @param target destination node
	 * @param asSibling true: the past will be a direct sibling of target, otherwise it will become a child
	 * @param isLeft
	 *            determines, whether or not the node is placed on the left or
	 *            right.
	 * @return true, if successfully.
	 */
	public boolean paste(Transferable t, MindMapNode target, boolean asSibling,
			boolean isLeft);

	public void paste(MindMapNode node, MindMapNode parent);

	// hooks, fc 28.2.2004:
	public void addHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName, Properties pHookProperties);

	public void removeHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName);

	/**
	 * This is the only way to instanciate new Hooks. THEY HAVE TO BE INVOKED
	 * AFTERWARDS! The hook is equipped with the map and controller information.
	 * Furthermore, the hook is added to the node, if it is an instance of the
	 * PermanentNodeHook. If the hook policy specifies, that only one instance
	 * may exist per node, it returns this instance if it already exists.
	 * */
	NodeHook createNodeHook(String hookName, MindMapNode node);
	
	void invokeHooksRecursively(MindMapNode node, MindMap map);

	// end hooks

	/**
	 * Moves the node to a new position.
	 */
	public void moveNodePosition(MindMapNode node, int vGap, int hGap,
			int shiftY);

	void setAttribute(MindMapNode node, int pPosition, Attribute pAttribute);
	
	/**
	 * Inserts a new attribute at a given place of the attributes table.
	 * To insert an attribute as the last item, {link {@link #addAttribute(MindMapNode, Attribute)} instead.
	 */
	void insertAttribute(MindMapNode node, int pPosition, Attribute pAttribute);

	/**
	 * Inserts a new attribute at the end of the attributes table.
	 * 
	 * @param node
	 *            to which the attribute is added
	 * @param pAttribute
	 *            itself
	 * @return the index of the new attribute.
	 */
	int addAttribute(MindMapNode node, Attribute pAttribute);

	/**
	 * Removes the attribute at the given position
	 * 
	 * @param pPosition
	 *            the position to delete.
	 * */
	void removeAttribute(MindMapNode node, int pPosition);


}
