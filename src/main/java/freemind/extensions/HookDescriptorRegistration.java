package freemind.extensions;

import freemind.controller.actions.Plugin;
import freemind.controller.actions.PluginMode;
import freemind.controller.actions.PluginRegistration;

import java.util.List;

public class HookDescriptorRegistration extends HookDescriptorBase {

    private final PluginRegistration mRegistration;

    public HookDescriptorRegistration(String xmlPluginFile,
                                      Plugin pluginBase, PluginRegistration pRegistration) {
        super(pluginBase, xmlPluginFile);
        mRegistration = pRegistration;
    }

    // public PluginRegistration getPluginRegistration() {
    // return mRegistration;
    // }

    public String getClassName() {
        return mRegistration.getClassName();
    }

    public boolean getIsPluginBase() {
        return mRegistration.getIsPluginBase() != null && mRegistration.getIsPluginBase();
    }

    public List<PluginMode> getListPluginModeList() {
        return mRegistration.getPluginModeList();
    }

}
