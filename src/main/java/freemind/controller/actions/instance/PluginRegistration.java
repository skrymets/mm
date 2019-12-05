
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_registration">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="plugin_mode" minOccurs="1" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="class_name"/>
 *     &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="isPluginBase"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PluginRegistration
{
    private List<PluginMode> pluginModeList = new ArrayList<PluginMode>();
    private String className;
    private Boolean isPluginBase;

    /** 
     * Get the list of 'plugin_mode' element items.
     * 
     * @return list
     */
    public List<PluginMode> getPluginModeList() {
        return pluginModeList;
    }

    /** 
     * Set the list of 'plugin_mode' element items.
     * 
     * @param list
     */
    public void setPluginModeList(List<PluginMode> list) {
        pluginModeList = list;
    }

    /** 
     * Get the number of 'plugin_mode' element items.
     * @return count
     */
    public int sizePluginModeList() {
        return pluginModeList.size();
    }

    /** 
     * Add a 'plugin_mode' element item.
     * @param item
     */
    public void addPluginMode(PluginMode item) {
        pluginModeList.add(item);
    }

    /** 
     * Get 'plugin_mode' element item by position.
     * @return item
     * @param index
     */
    public PluginMode getPluginMode(int index) {
        return pluginModeList.get(index);
    }

    /** 
     * Remove all 'plugin_mode' element items.
     */
    public void clearPluginModeList() {
        pluginModeList.clear();
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
     * Get the 'isPluginBase' attribute value.
     * 
     * @return value
     */
    public Boolean getIsPluginBase() {
        return isPluginBase;
    }

    /** 
     * Set the 'isPluginBase' attribute value.
     * 
     * @param isPluginBase
     */
    public void setIsPluginBase(Boolean isPluginBase) {
        this.isPluginBase = isPluginBase;
    }
}
