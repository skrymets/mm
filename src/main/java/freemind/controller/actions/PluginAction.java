
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin_action")
public class PluginAction
{
    private List<Choice> choiceList = new ArrayList<Choice>();
    @XmlAttribute(name = "label")
    private String label;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "base")
    private String base;
    @XmlAttribute(name = "class_name")
    private String className;
    @XmlAttribute(name = "documentation")
    private String documentation;
    @XmlAttribute(name = "icon_path")
    private String iconPath;
    @XmlAttribute(name = "key_stroke")
    private String keyStroke;
    @XmlAttribute(name = "instanciation")
    private Instanciation instanciation;
    @XmlAttribute(name = "isSelectable")
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
     */
    public void addChoice(Choice item) {
        choiceList.add(item);
    }

    /**
     * Get choice item by position.
     * @return item
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
        private static final int PLUGIN_MODE_CHOICE = 0;
        private static final int PLUGIN_MENU_CHOICE = 1;
        private static final int PLUGIN_PROPERTY_CHOICE = 2;
        private PluginMode pluginMode;
        private PluginMenu pluginMenu;
        private PluginProperty pluginProperty;

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
         */
        public void setPluginMode(PluginMode pluginMode) {
            setChoiceListSelect(PLUGIN_MODE_CHOICE);
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
         */
        public void setPluginMenu(PluginMenu pluginMenu) {
            setChoiceListSelect(PLUGIN_MENU_CHOICE);
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
         */
        public void setPluginProperty(PluginProperty pluginProperty) {
            setChoiceListSelect(PLUGIN_PROPERTY_CHOICE);
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
