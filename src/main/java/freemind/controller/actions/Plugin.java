
package freemind.controller.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class Choice -->
 *       &lt;/xs:choice>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="label"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin")
public class Plugin
{
    @XmlTransient
    private List<Choice> choiceList = new ArrayList<Choice>();

    @XmlElements({
        @XmlElement(name = "plugin_classpath", type = PluginClasspath.class),
        @XmlElement(name = "plugin_registration", type = PluginRegistration.class),
        @XmlElement(name = "plugin_action", type = PluginAction.class),
        @XmlElement(name = "plugin_strings", type = PluginStrings.class)
    })
    private List<Object> xmlElements;

    @XmlAttribute(name = "label")
    private String label;

    void afterUnmarshal(jakarta.xml.bind.Unmarshaller u, Object parent) {
        if (xmlElements != null && !xmlElements.isEmpty()) {
            choiceList = new ArrayList<>();
            for (Object obj : xmlElements) {
                Choice choice = new Choice();
                if (obj instanceof PluginClasspath) {
                    choice.setPluginClasspath((PluginClasspath) obj);
                } else if (obj instanceof PluginRegistration) {
                    choice.setPluginRegistration((PluginRegistration) obj);
                } else if (obj instanceof PluginAction) {
                    choice.setPluginAction((PluginAction) obj);
                } else if (obj instanceof PluginStrings) {
                    choice.setPluginStrings((PluginStrings) obj);
                }
                choiceList.add(choice);
            }
        }
    }

    boolean beforeMarshal(jakarta.xml.bind.Marshaller m) {
        if (choiceList != null && !choiceList.isEmpty()) {
            xmlElements = new ArrayList<>();
            for (Choice c : choiceList) {
                if (c.ifPluginClasspath()) xmlElements.add(c.getPluginClasspath());
                else if (c.ifPluginRegistration()) xmlElements.add(c.getPluginRegistration());
                else if (c.ifPluginAction()) xmlElements.add(c.getPluginAction());
                else if (c.ifPluginStrings()) xmlElements.add(c.getPluginStrings());
            }
        } else {
            xmlElements = null;
        }
        return true;
    }

    /**
     * Get the list of choice items.
     *
     * @return list
     */
    public List<Choice> getChoiceList() {
        return choiceList;
    }

    /**
     * Set the list of choice items.
     *
     * @param list
     */
    public void setChoiceList(List<Choice> list) {
        choiceList = list;
    }

    /**
     * Get the number of choice items.
     * @return count
     */
    public int sizeChoiceList() {
        return choiceList.size();
    }

    /**
     * Add a choice item.
     * @param item
     */
    public void addChoice(Choice item) {
        choiceList.add(item);
    }

    /**
     * Get choice item by position.
     * @return item
     * @param index
     */
    public Choice getChoice(int index) {
        return choiceList.get(index);
    }

    /**
     * Remove all choice items.
     */
    public void clearChoiceList() {
        choiceList.clear();
    }

    /**
     * Get the 'label' attribute value.
     *
     * @return value
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the 'label' attribute value.
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:choice xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:element ref="plugin_classpath"/>
     *   &lt;xs:element ref="plugin_registration"/>
     *   &lt;xs:element ref="plugin_action"/>
     *   &lt;xs:element ref="plugin_strings"/>
     * &lt;/xs:choice>
     * </pre>
     */
    @XmlType(name = "pluginChoice")
    public static class Choice
    {
        private int choiceListSelect = -1;
        private static final int PLUGIN_CLASSPATH_CHOICE = 0;
        private static final int PLUGIN_REGISTRATION_CHOICE = 1;
        private static final int PLUGIN_ACTION_CHOICE = 2;
        private static final int PLUGIN_STRINGS_CHOICE = 3;
        private PluginClasspath pluginClasspath;
        private PluginRegistration pluginRegistration;
        private PluginAction pluginAction;
        private PluginStrings pluginStrings;

        private void setChoiceListSelect(int choice) {
            if (choiceListSelect == -1) {
                choiceListSelect = choice;
            } else if (choiceListSelect != choice) {
                throw new IllegalStateException(
                        "Need to call clearChoiceListSelect() before changing existing choice");
            }
        }

        /**
         * Clear the choice selection.
         */
        public void clearChoiceListSelect() {
            choiceListSelect = -1;
        }

        /**
         * Check if PluginClasspath is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginClasspath() {
            return choiceListSelect == PLUGIN_CLASSPATH_CHOICE;
        }

        /**
         * Get the 'plugin_classpath' element value.
         *
         * @return value
         */
        public PluginClasspath getPluginClasspath() {
            return pluginClasspath;
        }

        /**
         * Set the 'plugin_classpath' element value.
         *
         * @param pluginClasspath
         */
        public void setPluginClasspath(PluginClasspath pluginClasspath) {
            setChoiceListSelect(PLUGIN_CLASSPATH_CHOICE);
            this.pluginClasspath = pluginClasspath;
        }

        /**
         * Check if PluginRegistration is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginRegistration() {
            return choiceListSelect == PLUGIN_REGISTRATION_CHOICE;
        }

        /**
         * Get the 'plugin_registration' element value.
         *
         * @return value
         */
        public PluginRegistration getPluginRegistration() {
            return pluginRegistration;
        }

        /**
         * Set the 'plugin_registration' element value.
         *
         * @param pluginRegistration
         */
        public void setPluginRegistration(PluginRegistration pluginRegistration) {
            setChoiceListSelect(PLUGIN_REGISTRATION_CHOICE);
            this.pluginRegistration = pluginRegistration;
        }

        /**
         * Check if PluginAction is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginAction() {
            return choiceListSelect == PLUGIN_ACTION_CHOICE;
        }

        /**
         * Get the 'plugin_action' element value.
         *
         * @return value
         */
        public PluginAction getPluginAction() {
            return pluginAction;
        }

        /**
         * Set the 'plugin_action' element value.
         *
         * @param pluginAction
         */
        public void setPluginAction(PluginAction pluginAction) {
            setChoiceListSelect(PLUGIN_ACTION_CHOICE);
            this.pluginAction = pluginAction;
        }

        /**
         * Check if PluginStrings is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginStrings() {
            return choiceListSelect == PLUGIN_STRINGS_CHOICE;
        }

        /**
         * Get the 'plugin_strings' element value.
         *
         * @return value
         */
        public PluginStrings getPluginStrings() {
            return pluginStrings;
        }

        /**
         * Set the 'plugin_strings' element value.
         *
         * @param pluginStrings
         */
        public void setPluginStrings(PluginStrings pluginStrings) {
            setChoiceListSelect(PLUGIN_STRINGS_CHOICE);
            this.pluginStrings = pluginStrings;
        }
    }
}
