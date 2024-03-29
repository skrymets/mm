/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 02.05.2005
 */

package accessories.plugins.time;

import freemind.controller.MenuItemEnabledListener;
import freemind.extensions.HookRegistration;
import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.common.plugins.ReminderHookBase;
import freemind.modes.mindmapmode.actions.NodeHookAction;

import javax.swing.*;

/**
 * Enables the encrypt/decrypt menu item only if the map/node is encrypted.
 * 
 * @author foltin
 * 
 */
public class TimeManagementOrganizer implements HookRegistration,
		MenuItemEnabledListener {

	private final ModeController controller;
	private final org.slf4j.Logger logger;

	public TimeManagementOrganizer(ModeController controller, MindMap map) {
		this.controller = controller;
		logger = controller.getFrame().getLogger(this.getClass().getName());
	}

	public void register() {
	}

	public void deRegister() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.MenuItemEnabledListener#isEnabled(javax.swing.JMenuItem
	 * , javax.swing.Action)
	 */
	public boolean isEnabled(JMenuItem item, Action action) {
		if (action instanceof NodeHookAction) {
			String hookName = ((NodeHookAction) action).getHookName();
			if (hookName.equals("plugins/time/RemoveReminderHook.java")) {
				boolean visible = false;
				for (MindMapNode node : controller.getSelecteds()) {
					if (TimeManagementOrganizer.getHook(node) != null) {
						visible = true;
					}
				}
				item.setVisible(visible);
			}
		}
		return true;
	}

	/**
	 */
	public static ReminderHookBase getHook(MindMapNode node) {
		for (PermanentNodeHook element : node.getActivatedHooks()) {
			if (element instanceof ReminderHookBase) {
				return (ReminderHookBase) element;
			}
		}
		return null;
	}
}