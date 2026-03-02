package freemind.extensions;

import freemind.controller.actions.Plugin;
import freemind.model.MindMapNode;

import java.util.Collection;
import java.util.List;

public interface HookFactory {

    class RegistrationContainer {
        public Class hookRegistrationClass;

        public boolean isPluginBase;

        public Plugin correspondingPlugin;

        public RegistrationContainer() {
        }
    }

    List<String> getPossibleNodeHooks();

    Collection<String> getPossibleModeControllerHooks();

    ModeControllerHook createModeControllerHook(String hookName);

    /**
     * Do not call this method directly. Call ModeController.createNodeHook instead.
     */
    NodeHook createNodeHook(String hookName);

    /**
     * @return null if not present, the hook otherwise.
     */
    PermanentNodeHook getHookInNode(MindMapNode node, String hookName);

    /**
     * @return returns a list of menu position strings for the StructuredMenuHolder.
     */
    List<String> getHookMenuPositions(String hookName);

    HookInstantiationMethod getInstantiationMethod(String hookName);

    /**
     * Each Plugin can have a list of HookRegistrations that are called after
     * the corresponding mode is enabled. (Like singletons.) One of these can
     * operate as the pluginBase that is accessible to every normal
     * plugin_action via the getPluginBaseClass method.
     *
     * @return A list of RegistrationContainer elements. The field
     * hookRegistrationClass of RegistrationContainer is a class that is
     * (probably) of HookRegistration type. You have to register every
     * registration via the registerRegistrationContainer method when
     * instanciated (this is typically done in the ModeController).
     */
    List<RegistrationContainer> getRegistrations();

    /**
     * See getRegistrations. The registration makes sense for the factory, as
     * the factory observes every object creation. <br>
     * Moreover, the factory can tell other hooks it creates, who is its base
     * plugin.
     */
    void registerRegistrationContainer(HookFactory.RegistrationContainer container, HookRegistration registrationObject);

    void deregisterAllRegistrationContainer();

    /**
     * A plugin base class is a common registration class of multiple plugins.
     * It is useful to embrace several related plugins (example: EncryptedNote
     * -> Registration).
     *
     * @return the base class if declared and successfully instanciated or NULL.
     */
    Object getPluginBaseClass(String hookName);

}
