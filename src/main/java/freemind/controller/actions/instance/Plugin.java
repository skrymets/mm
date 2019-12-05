
package freemind.controller.actions.instance;

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
public class Plugin
{
    private List<Choice> choiceList = new ArrayList<Choice>();
    private String label;

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
    public static class Choice
    {
        private int choiceListSelect = -1;
        /** 
         * ChoiceListSelect value when pluginClasspath is set
         */
        public static final int PLUGIN_CLASSPATH_CHOICE = 0;
        /** 
         * ChoiceListSelect value when pluginRegistration is set
         */
        public static final int PLUGIN_REGISTRATION_CHOICE = 1;
        /** 
         * ChoiceListSelect value when pluginAction is set
         */
        public static final int PLUGIN_ACTION_CHOICE = 2;
        /** 
         * ChoiceListSelect value when pluginStrings is set
         */
        public static final int PLUGIN_STRINGS_CHOICE = 3;
        private PluginClasspath pluginClasspath;
        private PluginRegistration pluginRegistration;
        private PluginAction pluginAction;
        private PluginStrings pluginStrings;

        private void setChoiceListSelect(int choice) {
            choiceListSelect = choice;
        }

        /** 
         * Clear the choice selection.
         */
        public void clearChoiceListSelect() {
            choiceListSelect = -1;
        }

        /** 
         * Get the current choice state.
         * @return state
         */
        public int stateChoiceListSelect() {
            return choiceListSelect;
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
            choiceListSelect = PLUGIN_CLASSPATH_CHOICE;
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
            choiceListSelect = PLUGIN_REGISTRATION_CHOICE;
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
            choiceListSelect = PLUGIN_ACTION_CHOICE;
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
            choiceListSelect = PLUGIN_STRINGS_CHOICE;
            this.pluginStrings = pluginStrings;
        }
    }
}
