package freemind.modes.browsemode;

import freemind.extensions.*;
import freemind.modes.common.plugins.MapNodePositionHolderBase;
import freemind.modes.common.plugins.ReminderHookBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BrowseHookFactory extends HookFactoryAdapter {

    public BrowseHookFactory() {
        super();
    }

    public List<String> getPossibleNodeHooks() {
        return new ArrayList<>();
    }

    public List<String> getPossibleModeControllerHooks() {
        return new ArrayList<>();
    }

    public ModeControllerHook createModeControllerHook(String hookName) {
        return null;
    }

    public NodeHook createNodeHook(String hookName) {
        // System.out.println("create node hook:"+hookName);
        NodeHook hook;
        if (hookName.equals(ReminderHookBase.PLUGIN_LABEL)) {
            hook = new BrowseReminderHook();
        } else if (hookName.equals(MapNodePositionHolderBase.NODE_MAP_HOOK_NAME)) {
            hook = new MapNodePositionHolderBase();
        } else {
            hook = new PermanentNodeHookSubstituteUnknown(hookName);
        }
        // decorate hook.
        hook.setProperties(new Properties());
        hook.setName(hookName);
        hook.setPluginBaseClass(null);
        return hook;
    }

    public List<String> getHookMenuPositions(String hookName) {
        return null;
    }

    public HookInstantiationMethod getInstantiationMethod(String hookName) {
        return null;
    }

    public List<RegistrationContainer> getRegistrations() {
        return null;
    }

    public Object getPluginBaseClass(String hookName) {
        return null;
    }

}
