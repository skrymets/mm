
package freemind.controller.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="mindmap_last_state_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="node_list_member" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:long" use="required" name="last_changed"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="tab_index"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="restorable_name"/>
 *         &lt;xs:attribute type="xs:float" use="required" name="last_zoom"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="x"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="y"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="last_selected"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mindmap_last_state_storage")
public class MindmapLastStateStorage extends XmlAction
{
    private List<NodeListMember> nodeListMemberList = new ArrayList<NodeListMember>();
    @XmlAttribute(name = "last_changed")
    private long lastChanged;
    @XmlAttribute(name = "tab_index")
    private int tabIndex;
    @XmlAttribute(name = "restorable_name")
    private String restorableName;
    @XmlAttribute(name = "last_zoom")
    private Float lastZoom;
    @XmlAttribute(name = "x")
    private int X;
    @XmlAttribute(name = "y")
    private int Y;
    @XmlAttribute(name = "last_selected")
    private String lastSelected;

    /**
     * Get the list of 'node_list_member' element items.
     *
     * @return list
     */
    public List<NodeListMember> getNodeListMemberList() {
        return nodeListMemberList;
    }

    /**
     * Set the list of 'node_list_member' element items.
     */
    public void setNodeListMemberList(List<NodeListMember> list) {
        nodeListMemberList = list;
    }

    /**
     * Get the number of 'node_list_member' element items.
     * @return count
     */
    public int sizeNodeListMemberList() {
        return nodeListMemberList.size();
    }

    /**
     * Add a 'node_list_member' element item.
     */
    public void addNodeListMember(NodeListMember item) {
        nodeListMemberList.add(item);
    }

    /**
     * Get 'node_list_member' element item by position.
     * @return item
     */
    public NodeListMember getNodeListMember(int index) {
        return nodeListMemberList.get(index);
    }

    /**
     * Remove all 'node_list_member' element items.
     */
    public void clearNodeListMemberList() {
        nodeListMemberList.clear();
    }

    /**
     * Get the 'last_changed' attribute value.
     *
     * @return value
     */
    public long getLastChanged() {
        return lastChanged;
    }

    /**
     * Set the 'last_changed' attribute value.
     */
    public void setLastChanged(long lastChanged) {
        this.lastChanged = lastChanged;
    }

    /**
     * Get the 'tab_index' attribute value.
     *
     * @return value
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Set the 'tab_index' attribute value.
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Get the 'restorable_name' attribute value.
     *
     * @return value
     */
    public String getRestorableName() {
        return restorableName;
    }

    /**
     * Set the 'restorable_name' attribute value.
     */
    public void setRestorableName(String restorableName) {
        this.restorableName = restorableName;
    }

    /**
     * Get the 'last_zoom' attribute value.
     *
     * @return value
     */
    public Float getLastZoom() {
        return lastZoom;
    }

    /**
     * Set the 'last_zoom' attribute value.
     */
    public void setLastZoom(Float lastZoom) {
        this.lastZoom = lastZoom;
    }

    /**
     * Get the 'x' attribute value.
     *
     * @return value
     */
    public int getX() {
        return X;
    }

    /**
     * Set the 'x' attribute value.
     */
    public void setX(int x) {
        X = x;
    }

    /**
     * Get the 'y' attribute value.
     *
     * @return value
     */
    public int getY() {
        return Y;
    }

    /**
     * Set the 'y' attribute value.
     */
    public void setY(int y) {
        Y = y;
    }

    /**
     * Get the 'last_selected' attribute value.
     *
     * @return value
     */
    public String getLastSelected() {
        return lastSelected;
    }

    /**
     * Set the 'last_selected' attribute value.
     */
    public void setLastSelected(String lastSelected) {
        this.lastSelected = lastSelected;
    }
}
