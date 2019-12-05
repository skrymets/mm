
package freemind.controller.actions.generated.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_action">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class Choice -->
 *       &lt;/xs:choice>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="label"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="name"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="base"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="class_name"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="documentation"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="icon_path"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="key_stroke"/>
 *     &lt;xs:attribute use="optional" default="Once" name="instanciation">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class Instanciation -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="isSelectable"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PluginAction
{
    private List<Choice> choiceList = new ArrayList<Choice>();
    private String label;
    private String name;
    private String base;
    private String className;
    private String documentation;
    private String iconPath;
    private String keyStroke;
    private Instanciation instanciation;
    private Boolean isSelectable;

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
     * Get the 'name' attribute value.
     * 
     * @return value
     */
    public String getName() {
        return name;
    }

    /** 
     * Set the 'name' attribute value.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * Get the 'base' attribute value.
     * 
     * @return value
     */
    public String getBase() {
        return base;
    }

    /** 
     * Set the 'base' attribute value.
     * 
     * @param base
     */
    public void setBase(String base) {
        this.base = base;
    }

    /** 
     * Get the 'class_name' attribute value.
     * 
     * @return value
     */
    public String getClassName() {
        return className;
    }

    /** 
     * Set the 'class_name' attribute value.
     * 
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /** 
     * Get the 'documentation' attribute value.
     * 
     * @return value
     */
    public String getDocumentation() {
        return documentation;
    }

    /** 
     * Set the 'documentation' attribute value.
     * 
     * @param documentation
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /** 
     * Get the 'icon_path' attribute value.
     * 
     * @return value
     */
    public String getIconPath() {
        return iconPath;
    }

    /** 
     * Set the 'icon_path' attribute value.
     * 
     * @param iconPath
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    /** 
     * Get the 'key_stroke' attribute value.
     * 
     * @return value
     */
    public String getKeyStroke() {
        return keyStroke;
    }

    /** 
     * Set the 'key_stroke' attribute value.
     * 
     * @param keyStroke
     */
    public void setKeyStroke(String keyStroke) {
        this.keyStroke = keyStroke;
    }

    /** 
     * Get the 'instanciation' attribute value.
     * 
     * @return value
     */
    public Instanciation getInstanciation() {
        return instanciation;
    }

    /** 
     * Set the 'instanciation' attribute value.
     * 
     * @param instanciation
     */
    public void setInstanciation(Instanciation instanciation) {
        this.instanciation = instanciation;
    }

    /** 
     * Get the 'isSelectable' attribute value.
     * 
     * @return value
     */
    public Boolean getIsSelectable() {
        return isSelectable;
    }

    /** 
     * Set the 'isSelectable' attribute value.
     * 
     * @param isSelectable
     */
    public void setIsSelectable(Boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
    /** 
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:choice xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:element ref="plugin_mode"/>
     *   &lt;xs:element ref="plugin_menu"/>
     *   &lt;xs:element ref="plugin_property"/>
     * &lt;/xs:choice>
     * </pre>
     */
    public static class Choice
    {
        private int choiceListSelect = -1;
        /** 
         * ChoiceListSelect value when pluginMode is set
         */
        public static final int PLUGIN_MODE_CHOICE = 0;
        /** 
         * ChoiceListSelect value when pluginMenu is set
         */
        public static final int PLUGIN_MENU_CHOICE = 1;
        /** 
         * ChoiceListSelect value when pluginProperty is set
         */
        public static final int PLUGIN_PROPERTY_CHOICE = 2;
        private PluginMode pluginMode;
        private PluginMenu pluginMenu;
        private PluginProperty pluginProperty;

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
         * Check if PluginMode is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginMode() {
            return choiceListSelect == PLUGIN_MODE_CHOICE;
        }

        /** 
         * Get the 'plugin_mode' element value.
         * 
         * @return value
         */
        public PluginMode getPluginMode() {
            return pluginMode;
        }

        /** 
         * Set the 'plugin_mode' element value.
         * 
         * @param pluginMode
         */
        public void setPluginMode(PluginMode pluginMode) {
            choiceListSelect = PLUGIN_MODE_CHOICE;
            this.pluginMode = pluginMode;
        }

        /** 
         * Check if PluginMenu is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginMenu() {
            return choiceListSelect == PLUGIN_MENU_CHOICE;
        }

        /** 
         * Get the 'plugin_menu' element value.
         * 
         * @return value
         */
        public PluginMenu getPluginMenu() {
            return pluginMenu;
        }

        /** 
         * Set the 'plugin_menu' element value.
         * 
         * @param pluginMenu
         */
        public void setPluginMenu(PluginMenu pluginMenu) {
            choiceListSelect = PLUGIN_MENU_CHOICE;
            this.pluginMenu = pluginMenu;
        }

        /** 
         * Check if PluginProperty is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPluginProperty() {
            return choiceListSelect == PLUGIN_PROPERTY_CHOICE;
        }

        /** 
         * Get the 'plugin_property' element value.
         * 
         * @return value
         */
        public PluginProperty getPluginProperty() {
            return pluginProperty;
        }

        /** 
         * Set the 'plugin_property' element value.
         * 
         * @param pluginProperty
         */
        public void setPluginProperty(PluginProperty pluginProperty) {
            choiceListSelect = PLUGIN_PROPERTY_CHOICE;
            this.pluginProperty = pluginProperty;
        }
    }
    /** 
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="Once"/>
     *     &lt;xs:enumeration value="OnceForRoot"/>
     *     &lt;xs:enumeration value="OnceForAllNodes"/>
     *     &lt;xs:enumeration value="Other"/>
     *     &lt;xs:enumeration value="ApplyToRoot"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum Instanciation {
        ONCE("Once"), ONCE_FOR_ROOT("OnceForRoot"), ONCE_FOR_ALL_NODES(
                "OnceForAllNodes"), OTHER("Other"), APPLY_TO_ROOT("ApplyToRoot");
        private final String value;

        private Instanciation(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static Instanciation convert(String value) {
            for (Instanciation inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
}
