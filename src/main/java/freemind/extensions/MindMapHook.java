package freemind.extensions;

import freemind.modes.MapFeedback;

import java.util.Properties;

/**
 * This is the most general hook interface. It is a base class for all hooks.
 * {@link ModeControllerHook} for hooks that implement actions independent of nodes
 * {@link NodeHook} for hooks that implement actions belonging to nodes
 */
public interface MindMapHook {
    /**
     * @return The name of the hook. In the xml description, this is the unique
     * label.
     */
    String getName();

    void setName(String name);

    /**
     * @param properties the properties of the property file belonging to the hook are
     *                   passed.
     */
    void setProperties(Properties properties);

    /**
     * looks for a property in the plugin properties file, or from the localized resources.
     */
    String getResourceString(String property);

    void setController(MapFeedback controller);

    /**
     * If a base class is specified in the plugin declaration via a
     * plugin_registration entry with isPluginBase==true, this object is
     * returned here. You can use it to realize something like the state of a
     * plugin (eg. is the plugin switched on or off?).<br>
     * <p>
     * An example is the menu status of the encrypted nodes. If the node is not
     * encrypted, the encryption state cannot be toggled (see EncryptNode.java).<br>
     * <p>
     * Another example arises from the collaboration mode. The state (connected,
     * wait for second party, map sharing etc.) can be stored in the plugin
     * base.<br>
     * <p>
     * Remember, that it is most likely that you havn't specified the base class
     * and that you get NULL here.
     *
     * @return The object returned is of HookRegistration type but has to be
     * casted anyway.
     */
    Object getPluginBaseClass();

    /**
     * This indirection is necessary, as all stored PermanentNodeHooks are
     * created during the map's creation time but the registrations are
     * underdone on ModeController's startup method later.
     */
    interface PluginBaseClassSearcher {
        /**
         * @return the plugin base object {@link HookRegistration}.
         */
        Object getPluginBaseObject();
    }

    void setPluginBaseClass(PluginBaseClassSearcher baseClass);

    /* Hooks */

    /**
     * This method is also called, if the hook is created in the map.
     */
    void startupMapHook();

    /**
     * This method is also called, if the node, this hook belongs to, is removed
     * from the map.
     */
    void shutdownMapHook();

}
