package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemEnabledListener;
import freemind.controller.MenuItemSelectedListener;
import freemind.extensions.HookFactory;
import freemind.extensions.HookInstantiationMethod;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
@Slf4j
public class NodeHookAction extends MindmapAction implements HookAction, MenuItemEnabledListener, MenuItemSelectedListener {
    final String _hookName;
    final MindMapController mMindMapController;

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
