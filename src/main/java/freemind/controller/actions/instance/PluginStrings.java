
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_strings">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="plugin_string" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="language"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PluginStrings
{
    private List<PluginString> pluginStringList = new ArrayList<PluginString>();
    private String language;

    /** 
     * Get the list of 'plugin_string' element items.
     * 
     * @return list
     */
    public List<PluginString> getPluginStringList() {
        return pluginStringList;
    }

    /** 
     * Set the list of 'plugin_string' element items.
     * 
     * @param list
     */
    public void setPluginStringList(List<PluginString> list) {
        pluginStringList = list;
    }

    /** 
     * Get the number of 'plugin_string' element items.
     * @return count
     */
    public int sizePluginStringList() {
        return pluginStringList.size();
    }

    /** 
     * Add a 'plugin_string' element item.
     * @param item
     */
    public void addPluginString(PluginString item) {
        pluginStringList.add(item);
    }

    /** 
     * Get 'plugin_string' element item by position.
     * @return item
     * @param index
     */
    public PluginString getPluginString(int index) {
        return pluginStringList.get(index);
    }

    /** 
     * Remove all 'plugin_string' element items.
     */
    public void clearPluginStringList() {
        pluginStringList.clear();
    }

    /** 
     * Get the 'language' attribute value.
     * 
     * @return value
     */
    public String getLanguage() {
        return language;
    }

    /** 
     * Set the 'language' attribute value.
     * 
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
