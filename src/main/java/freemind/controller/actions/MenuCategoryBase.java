
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "menu_category")
@XmlSeeAlso({MenuSubmenu.class})
public class MenuCategoryBase
{
    @XmlTransient
    private List<Base> baseList = new ArrayList<Base>();

    @XmlElements({
        @XmlElement(name = "menu_category", type = MenuCategoryBase.class),
        @XmlElement(name = "menu_submenu", type = MenuSubmenu.class),
        @XmlElement(name = "menu_action", type = MenuAction.class),
        @XmlElement(name = "menu_checked_action", type = MenuCheckedAction.class),
        @XmlElement(name = "menu_radio_action", type = MenuRadioAction.class),
        @XmlElement(name = "menu_separator", type = MenuSeparator.class)
    })
    private List<Object> xmlElements;

    @XmlAttribute(name = "name")
    private String name;

    void afterUnmarshal(jakarta.xml.bind.Unmarshaller u, Object parent) {
        if (xmlElements != null && !xmlElements.isEmpty()) {
            baseList = new ArrayList<>();
            for (Object obj : xmlElements) {
                Base base = new Base();
                if (obj instanceof MenuCategoryBase) {
                    base.setMenuCategory((MenuCategoryBase) obj);
                } else if (obj instanceof MenuSubmenu) {
                    base.setMenuSubmenu((MenuSubmenu) obj);
                } else if (obj instanceof MenuAction) {
                    base.setMenuAction((MenuAction) obj);
                } else if (obj instanceof MenuCheckedAction) {
                    base.setMenuCheckedAction((MenuCheckedAction) obj);
                } else if (obj instanceof MenuRadioAction) {
                    base.setMenuRadioAction((MenuRadioAction) obj);
                } else if (obj instanceof MenuSeparator) {
                    base.setMenuSeparator((MenuSeparator) obj);
                }
                baseList.add(base);
            }
        }
    }

    boolean beforeMarshal(jakarta.xml.bind.Marshaller m) {
        if (baseList != null && !baseList.isEmpty()) {
            xmlElements = new ArrayList<>();
            for (Base base : baseList) {
                if (base.ifMenuCategory()) xmlElements.add(base.getMenuCategory());
                else if (base.ifMenuSubmenu()) xmlElements.add(base.getMenuSubmenu());
                else if (base.ifMenuAction()) xmlElements.add(base.getMenuAction());
                else if (base.ifMenuCheckedAction()) xmlElements.add(base.getMenuCheckedAction());
                else if (base.ifMenuRadioAction()) xmlElements.add(base.getMenuRadioAction());
                else if (base.ifMenuSeparator()) xmlElements.add(base.getMenuSeparator());
            }
        } else {
            xmlElements = null;
        }
        return true;
    }

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
