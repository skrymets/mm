package freemind.extensions;

import freemind.controller.actions.*;
import freemind.frok.patches.JIBXGeneratedUtil;
import lombok.Getter;

import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * This is an information class that holds all outer properties of a hook, i.e.
 * all contents of the XML description file.
 * <p>
 * Don't use this class for anything except for the implementation of a
 * HookFactory.
 */
public class HookDescriptorPluginAction extends HookDescriptorBase {

    @Getter
    private final Properties properties = new Properties();
    public final List<String> menuPositions = new ArrayList<>();
    private final List<String> modes = new ArrayList<>();
    private final PluginAction pluginAction;

    public HookDescriptorPluginAction(String xmlPluginFile, Plugin pluginBase, PluginAction pluginAction) {
        super(pluginBase, xmlPluginFile);

        requireNonNull(pluginAction);
        this.pluginAction = pluginAction;
        if (this.pluginAction.getName() == null) {
            this.pluginAction.setName(pluginAction.getLabel());
        }

        List<Object> pluginActions = JIBXGeneratedUtil.listPluginActions(pluginAction);

        for (Object obj : pluginActions) {
            if (obj instanceof PluginMenu) {
                PluginMenu menu = (PluginMenu) obj;
                menuPositions.add(menu.getLocation());
            }
            if (obj instanceof PluginProperty) {
                PluginProperty property = (PluginProperty) obj;
                properties.put(property.getName(), property.getValue());
            }
            if (obj instanceof PluginMode) {
                PluginMode mode = (PluginMode) obj;
                modes.add(mode.getClassName());
            }
        }
    }

    public String toString() {
        return format("[HookDescriptor props=%s, menu positions=%s]", properties, menuPositions);
    }

    public HookInstantiationMethod getInstantiationMethod() {
        if (pluginAction.getInstanciation() != null) {
            HashMap<String, HookInstantiationMethod> allInstMethods = HookInstantiationMethod.getAllInstanciationMethods();
            for (String name : allInstMethods.keySet()) {
                if (pluginAction.getInstanciation().xmlValue().equalsIgnoreCase(name)) {
                    return allInstMethods.get(name);
                }
            }
        }
        // this is an error case?
        return HookInstantiationMethod.Other;
    }

    public Collection<String> getModes() {
        return modes;
    }

    public String getBaseClass() {
        return pluginAction.getBase();
    }

    public String getName() {
        return getFromResourceIfNecessary(pluginAction.getName());
    }

    public String getClassName() {
        return pluginAction.getClassName();
    }

    public String getDocumentation() {
        return getFromResourceIfNecessary(pluginAction.getDocumentation());
    }

    public String getIconPath() {
        return pluginAction.getIconPath();
    }

    public String getKeyStroke() {
        return getFromPropertiesIfNecessary(pluginAction.getKeyStroke());
    }

    /**
     * @return whether the plugin can be on/off and this should be
     * displayed in the menus.
     */
    public boolean isSelectable() {
        try {
            return pluginAction.getIsSelectable();
        } catch (Exception ignored) {
        }
        return false;
    }

}
