package freemind.extensions;

import freemind.model.MindMapNode;

import java.util.HashMap;

public abstract class HookFactoryAdapter implements HookFactory {

    /**
     * Contains PluginType -> Object (baseClass) relations.
     */
    protected HashMap<String, HookRegistration> allRegistrationInstances;

    protected HookFactoryAdapter() {
        super();
    }

    /**
     * @return null if not present, the hook otherwise.
     */
    public PermanentNodeHook getHookInNode(MindMapNode node, String hookName) {
        // search for already instanciated hooks of this type:
        for (PermanentNodeHook otherHook : node.getActivatedHooks()) {
            if (otherHook.getName().equals(hookName)) {
                // there is already one instance.
                return otherHook;
            }
        }
        return null;
    }

    /**
     * See getRegistrations. The registration makes sense for the factory, as
     * the factory observes every object creation. <br>
     * Moreover, the factory can tell other hooks it creates, who is its base
     * plugin.
     */
    public void registerRegistrationContainer(
            HookFactory.RegistrationContainer container,
            HookRegistration registrationObject) {
        // registration only for pluginBases.
        if (container.isPluginBase) {
            allRegistrationInstances.put(
                    container.correspondingPlugin.getLabel(),
                    registrationObject);
        }
    }

    public void deregisterAllRegistrationContainer() {
        allRegistrationInstances.clear();
    }

}
