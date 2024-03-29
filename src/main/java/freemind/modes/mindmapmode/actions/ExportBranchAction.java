/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2006  Christian Foltin <christianfoltin@users.sourceforge.net>
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
/*$Id: ExportBranchAction.java,v 1.1.2.8 2008/11/12 21:44:34 christianfoltin Exp $*/

package freemind.modes.mindmapmode.actions;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapMapModel;
import freemind.modes.mindmapmode.MindMapNodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;

/** */
@SuppressWarnings("serial")
public class ExportBranchAction extends MindmapAction {
	private final MindMapController mMindMapController;

	public ExportBranchAction(MindMapController pMindMapController) {
		super("export_branch_new", pMindMapController);
		mMindMapController = pMindMapController;
	}

	public void actionPerformed(ActionEvent e) {
		MindMapNodeModel node = (MindMapNodeModel) mMindMapController
				.getSelected();

		// if something is wrong, abort.
		if (mMindMapController.getMap() == null || node == null
				|| node.isRoot()) {
			mMindMapController.getFrame().err("Could not export branch.");
			return;
		}
		// If the current map is not saved yet, save it first.
		if (mMindMapController.getMap().getFile() == null) {
			mMindMapController.getFrame().out(
					"You must save the current map first!");
			if(!mMindMapController.save()) {
				return;
			}
		}

		// Open FileChooser to choose in which file the exported
		// branch should be stored
		FreeMindFileDialog chooser = mMindMapController.getFileChooser();
		chooser.setSelectedFile(new File(Tools.getFileNameProposal(node)
				+ freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION));
		int returnVal = chooser.showSaveDialog(mMindMapController
				.getSelectedView());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File chosenFile = chooser.getSelectedFile();
			// Force the extension to be .mm
			String ext = Tools.getExtension(chosenFile.getName());
			if (!ext.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT)) {
				chosenFile = new File(
						chosenFile.getParent(),
						chosenFile.getName()
								+ freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION);
			}
			try {
				Tools.fileToUrl(chosenFile);
			} catch (MalformedURLException ex) {
				JOptionPane.showMessageDialog(mMindMapController.getView(),
						"couldn't create valid URL!");
				return;
			}
			// Confirm overwrite if file exists.
			if (chosenFile.exists()) { // If file exists, ask before
										// overwriting.
				int overwriteMap = JOptionPane.showConfirmDialog(
						mMindMapController.getView(),
						mMindMapController.getText("map_already_exists"),
						"FreeMind", JOptionPane.YES_NO_OPTION);
				if (overwriteMap != JOptionPane.YES_OPTION) {
					return;
				}
			}

			/*
			 * Now make a copy from the node, remove the node from the map and
			 * create a new Map with the node as root, store the new Map, add
			 * the copy of the node to the parent, and set a link from the copy
			 * to the new Map.
			 */
			MindMapNodeModel parent = (MindMapNodeModel) node.getParentNode();
			// set a link from the new root to the old map
			String linkToNewMapString = Tools.fileToRelativeUrlString(
					mMindMapController.getModel().getFile(), chosenFile);
			mMindMapController.setLink(node, linkToNewMapString);
			int nodePosition = parent.getChildPosition(node);
			mMindMapController.deleteNode(node);
			// save node:
			node.setParent(null);
			// unfold node
			node.setFolded(false);
			// construct new controller:
			final ModeController newModeController = mMindMapController.getMode()
					.createModeController();
			MindMapMapModel newMap = new MindMapMapModel(node,
					newModeController);
			newModeController.setModel(newMap);
			try {
				newMap.save(chosenFile);
			} catch (Exception e1) {
				freemind.main.Resources.getInstance().logException(e1);
				// roll back:
				mMindMapController.insertNodeInto(node, parent);
				
				String message = Tools.expandPlaceholders(mMindMapController.getText("save_failed"),
						chosenFile.getName());
				mMindMapController.getController().errorMessage(message);
				return;
			}
			// new node instead:
			final MindMapNode newNode = mMindMapController.addNewNode(parent,
					nodePosition, node.isLeft());
			// TODO: Keep formatting of node.
			mMindMapController.setNodeText(newNode, node.getText());

			final String linkString = Tools.fileToRelativeUrlString(chosenFile, mMindMapController.getModel().getFile());
			mMindMapController.setLink(newNode, linkString);
			mMindMapController.newMap(newMap, newModeController);
			// old map should not be saved automatically!!
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// save new map again to create thumbnail...
						newModeController.setSaved(false);
						newModeController.save();
						// set link again to refresh thumbnail
						mMindMapController.setLink(newNode, linkString);
					} catch (Exception e2) {
						freemind.main.Resources.getInstance().logException(e2);
					}
				}
			});
		}
	}

}

// private static org.slf4j.Logger logger =
// freemind.main.Resources.getInstance().getLogger(ExportBranchAction.class.getName());
