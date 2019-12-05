
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="menu_submenu">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="menu_category_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="name_ref"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class MenuSubmenu extends MenuCategoryBase
{
    private String nameRef;

    /** 
     * Get the 'name_ref' attribute value.
     * 
     * @return value
     */
    public String getNameRef() {
        return nameRef;
    }

    /** 
     * Set the 'name_ref' attribute value.
     * 
     * @param nameRef
     */
    public void setNameRef(String nameRef) {
        this.nameRef = nameRef;
    }
}
