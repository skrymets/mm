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
 * Created on 26.07.2004
 */

package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemEnabledListener;
import freemind.controller.MenuItemSelectedListener;
import freemind.extensions.HookFactory;
import freemind.extensions.HookInstantiationMethod;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
@Log4j2
public class NodeHookAction extends MindmapAction implements HookAction, MenuItemEnabledListener, MenuItemSelectedListener {
    String _hookName;
    MindMapController mMindMapController;

    public MindMapController getController() {
        return mMindMapController;
    }

    public NodeHookAction(String hookName, MindMapController controller) {
        super(hookName, (Icon) null, controller);
        this._hookName = hookName;
        this.mMindMapController = controller;
    }

    public void actionPerformed(ActionEvent arg0) {
        // check, which method of invocation:
        //
        mMindMapController.getFrame().setWaitingCursor(true);
        invoke(mMindMapController.getSelected(), mMindMapController.getSelecteds());
        mMindMapController.getFrame().setWaitingCursor(false);
    }

    public void invoke(MindMapNode focussed, List<MindMapNode> selecteds) {
        mMindMapController.addHook(focussed, selecteds, _hookName, null);
    }

    private HookInstantiationMethod getInstanciationMethod(String hookName) {
        HookFactory factory = getHookFactory();
        // determine instanciation method
        HookInstantiationMethod instMethod = factory.getInstantiationMethod(hookName);
        return instMethod;
    }

    private HookFactory getHookFactory() {
        HookFactory factory = mMindMapController.getHookFactory();
        return factory;
    }

    public boolean isEnabled(JMenuItem item, Action action) {
        if (!super.isEnabled(item, action) || mMindMapController.getView() == null) {
            return false;
        }
        HookFactory factory = getHookFactory();
        Object baseClass = factory.getPluginBaseClass(_hookName);
        if (baseClass != null) {
            if (baseClass instanceof MenuItemEnabledListener) {
                MenuItemEnabledListener listener = (MenuItemEnabledListener) baseClass;
                return listener.isEnabled(item, action);
            }
        }
        return true;
    }

    public String getHookName() {
        return _hookName;
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        // test if plugin has its own method:
        HookFactory factory = getHookFactory();
        Object baseClass = factory.getPluginBaseClass(_hookName);
        if (baseClass != null) {
            if (baseClass instanceof MenuItemSelectedListener) {
                MenuItemSelectedListener listener = (MenuItemSelectedListener) baseClass;
                return listener.isSelected(pCheckItem, pAction);
            }
        }
        MindMapNode focussed = mMindMapController.getSelected();
        List<MindMapNode> selecteds = mMindMapController.getSelecteds();
        HookInstantiationMethod instMethod = getInstanciationMethod(_hookName);
        // get destination nodes
        instMethod.getDestinationNodes(mMindMapController, focussed, selecteds);
        MindMapNode adaptedFocussedNode = instMethod.getCenterNode(mMindMapController, focussed, selecteds);
        // test if hook already present
        return instMethod.isAlreadyPresent(_hookName, adaptedFocussedNode);

    }

}
