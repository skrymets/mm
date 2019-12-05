
package freemind.controller.actions.generated.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="time_window_configuration_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="window_configuration_storage">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="time_window_column_setting" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="true" name="view_folded_nodes"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class TimeWindowConfigurationStorage extends WindowConfigurationStorage
{
    private List<TimeWindowColumnSetting> timeWindowColumnSettingList = new ArrayList<TimeWindowColumnSetting>();
    private Boolean viewFoldedNodes;

    /** 
     * Get the list of 'time_window_column_setting' element items.
     * 
     * @return list
     */
    public List<TimeWindowColumnSetting> getTimeWindowColumnSettingList() {
        return timeWindowColumnSettingList;
    }

    /** 
     * Set the list of 'time_window_column_setting' element items.
     * 
     * @param list
     */
    public void setTimeWindowColumnSettingList(
            List<TimeWindowColumnSetting> list) {
        timeWindowColumnSettingList = list;
    }

    /** 
     * Get the number of 'time_window_column_setting' element items.
     * @return count
     */
    public int sizeTimeWindowColumnSettingList() {
        return timeWindowColumnSettingList.size();
    }

    /** 
     * Add a 'time_window_column_setting' element item.
     * @param item
     */
    public void addTimeWindowColumnSetting(TimeWindowColumnSetting item) {
        timeWindowColumnSettingList.add(item);
    }

    /** 
     * Get 'time_window_column_setting' element item by position.
     * @return item
     * @param index
     */
    public TimeWindowColumnSetting getTimeWindowColumnSetting(int index) {
        return timeWindowColumnSettingList.get(index);
    }

    /** 
     * Remove all 'time_window_column_setting' element items.
     */
    public void clearTimeWindowColumnSettingList() {
        timeWindowColumnSettingList.clear();
    }

    /** 
     * Get the 'view_folded_nodes' attribute value.
     * 
     * @return value
     */
    public Boolean getViewFoldedNodes() {
        return viewFoldedNodes;
    }

    /** 
     * Set the 'view_folded_nodes' attribute value.
     * 
     * @param viewFoldedNodes
     */
    public void setViewFoldedNodes(Boolean viewFoldedNodes) {
        this.viewFoldedNodes = viewFoldedNodes;
    }
}
