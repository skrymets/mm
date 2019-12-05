
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="mindmap_last_state_map_storage">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xml_action">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="mindmap_last_state_storage" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:int" use="optional" default="-1" name="last_focused_tab"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class MindmapLastStateMapStorage extends XmlAction
{
    private List<MindmapLastStateStorage> mindmapLastStateStorageList = new ArrayList<MindmapLastStateStorage>();
    private Integer lastFocusedTab;

    /** 
     * Get the list of 'mindmap_last_state_storage' element items.
     * 
     * @return list
     */
    public List<MindmapLastStateStorage> getMindmapLastStateStorageList() {
        return mindmapLastStateStorageList;
    }

    /** 
     * Set the list of 'mindmap_last_state_storage' element items.
     * 
     * @param list
     */
    public void setMindmapLastStateStorageList(
            List<MindmapLastStateStorage> list) {
        mindmapLastStateStorageList = list;
    }

    /** 
     * Get the number of 'mindmap_last_state_storage' element items.
     * @return count
     */
    public int sizeMindmapLastStateStorageList() {
        return mindmapLastStateStorageList.size();
    }

    /** 
     * Add a 'mindmap_last_state_storage' element item.
     * @param item
     */
    public void addMindmapLastStateStorage(MindmapLastStateStorage item) {
        mindmapLastStateStorageList.add(item);
    }

    /** 
     * Get 'mindmap_last_state_storage' element item by position.
     * @return item
     * @param index
     */
    public MindmapLastStateStorage getMindmapLastStateStorage(int index) {
        return mindmapLastStateStorageList.get(index);
    }

    /** 
     * Remove all 'mindmap_last_state_storage' element items.
     */
    public void clearMindmapLastStateStorageList() {
        mindmapLastStateStorageList.clear();
    }

    /** 
     * Get the 'last_focused_tab' attribute value.
     * 
     * @return value
     */
    public Integer getLastFocusedTab() {
        return lastFocusedTab;
    }

    /** 
     * Set the 'last_focused_tab' attribute value.
     * 
     * @param lastFocusedTab
     */
    public void setLastFocusedTab(Integer lastFocusedTab) {
        this.lastFocusedTab = lastFocusedTab;
    }
}
