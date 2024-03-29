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
 * Created on 09.05.2004
 */

package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.generated.instance.PasteNodeAction;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PasteAction extends AbstractAction {

	private static org.slf4j.Logger logger;
	private final MindMapController mMindMapController;

	public PasteAction(MindMapController pMindMapController) {
		super(pMindMapController.getText("paste"), freemind.view.ImageFactory.getInstance().createIcon(
				pMindMapController.getResource("images/editpaste.png")));
		this.mMindMapController = pMindMapController;
		if (logger == null) {
			logger = mMindMapController.getFrame().getLogger(
					this.getClass().getName());
		}

		setEnabled(false);

	}

	public void actionPerformed(ActionEvent e) {
		Transferable clipboardContents = this.mMindMapController
				.getClipboardContents();
		MindMapNode selectedNode = this.mMindMapController.getSelected();
		this.mMindMapController.paste(clipboardContents, selectedNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
	public Class<PasteNodeAction> getDoActionClass() {
		return PasteNodeAction.class;
	}

}
