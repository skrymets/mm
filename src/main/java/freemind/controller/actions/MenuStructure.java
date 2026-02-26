
package freemind.controller.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="menu_structure">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="menu_category" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "menu_structure")
public class MenuStructure
{
    @XmlElement(name = "menu_category")
    private List<MenuCategoryBase> menuCategoryList = new ArrayList<MenuCategoryBase>();

    /**
     * Get the list of 'menu_category' element items.
     *
     * @return list
     */
    public List<MenuCategoryBase> getMenuCategoryList() {
        return menuCategoryList;
    }

    /**
     * Set the list of 'menu_category' element items.
     *
     * @param list
     */
    public void setMenuCategoryList(List<MenuCategoryBase> list) {
        menuCategoryList = list;
    }

    /**
     * Get the number of 'menu_category' element items.
     * @return count
     */
    public int sizeMenuCategoryList() {
        return menuCategoryList.size();
    }

    /**
     * Add a 'menu_category' element item.
     * @param item
     */
    public void addMenuCategory(MenuCategoryBase item) {
        menuCategoryList.add(item);
    }

    /**
     * Get 'menu_category' element item by position.
     * @return item
     * @param index
     */
    public MenuCategoryBase getMenuCategory(int index) {
        return menuCategoryList.get(index);
    }

    /**
     * Remove all 'menu_category' element items.
     */
    public void clearMenuCategoryList() {
        menuCategoryList.clear();
    }
}
