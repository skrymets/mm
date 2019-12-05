
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="hook_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="node_list_member" minOccurs="1" maxOccurs="unbounded"/>
 *           &lt;xs:element ref="node_child_parameter" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:string" use="required" name="hook_name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class HookNodeAction extends NodeAction
{
    private List<NodeListMember> nodeListMemberList = new ArrayList<NodeListMember>();
    private List<NodeChildParameter> nodeChildParameterList = new ArrayList<NodeChildParameter>();
    private String hookName;

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
     * Get the list of 'node_child_parameter' element items.
     * 
     * @return list
     */
    public List<NodeChildParameter> getNodeChildParameterList() {
        return nodeChildParameterList;
    }

    /** 
     * Set the list of 'node_child_parameter' element items.
     * 
     * @param list
     */
    public void setNodeChildParameterList(List<NodeChildParameter> list) {
        nodeChildParameterList = list;
    }

    /** 
     * Get the number of 'node_child_parameter' element items.
     * @return count
     */
    public int sizeNodeChildParameterList() {
        return nodeChildParameterList.size();
    }

    /** 
     * Add a 'node_child_parameter' element item.
     * @param item
     */
    public void addNodeChildParameter(NodeChildParameter item) {
        nodeChildParameterList.add(item);
    }

    /** 
     * Get 'node_child_parameter' element item by position.
     * @return item
     * @param index
     */
    public NodeChildParameter getNodeChildParameter(int index) {
        return nodeChildParameterList.get(index);
    }

    /** 
     * Remove all 'node_child_parameter' element items.
     */
    public void clearNodeChildParameterList() {
        nodeChildParameterList.clear();
    }

    /** 
     * Get the 'hook_name' attribute value.
     * 
     * @return value
     */
    public String getHookName() {
        return hookName;
    }

    /** 
     * Set the 'hook_name' attribute value.
     * 
     * @param hookName
     */
    public void setHookName(String hookName) {
        this.hookName = hookName;
    }
}
