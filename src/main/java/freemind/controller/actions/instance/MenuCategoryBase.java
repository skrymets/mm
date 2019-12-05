
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="menu_category_base">
 *   &lt;xs:sequence>
 *     &lt;xs:group ref="base" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 *   &lt;xs:attribute type="xs:string" use="required" name="name"/>
 * &lt;/xs:complexType>
 * </pre>
 */
public class MenuCategoryBase
{
    private List<Base> baseList = new ArrayList<Base>();
    private String name;

    /** 
     * Get the list of 'base' group items.
     * 
     * @return list
     */
    public List<Base> getBaseList() {
        return baseList;
    }

    /** 
     * Set the list of 'base' group items.
     * 
     * @param list
     */
    public void setBaseList(List<Base> list) {
        baseList = list;
    }

    /** 
     * Get the number of 'base' group items.
     * @return count
     */
    public int sizeBaseList() {
        return baseList.size();
    }

    /** 
     * Add a 'base' group item.
     * @param item
     */
    public void addBase(Base item) {
        baseList.add(item);
    }

    /** 
     * Get 'base' group item by position.
     * @return item
     * @param index
     */
    public Base getBase(int index) {
        return baseList.get(index);
    }

    /** 
     * Remove all 'base' group items.
     */
    public void clearBaseList() {
        baseList.clear();
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
}
