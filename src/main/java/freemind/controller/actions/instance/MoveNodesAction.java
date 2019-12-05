
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="move_nodes_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="node_list_member" minOccurs="1" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:int" use="required" name="direction"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class MoveNodesAction extends NodeAction
{
    private List<NodeListMember> nodeListMemberList = new ArrayList<NodeListMember>();
    private int direction;

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
     * 
     * @param list
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
     * @param item
     */
    public void addNodeListMember(NodeListMember item) {
        nodeListMemberList.add(item);
    }

    /** 
     * Get 'node_list_member' element item by position.
     * @return item
     * @param index
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
     * Get the 'direction' attribute value.
     * 
     * @return value
     */
    public int getDirection() {
        return direction;
    }

    /** 
     * Set the 'direction' attribute value.
     * 
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
