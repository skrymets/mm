
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="menu_action_base">
 *   &lt;xs:attribute type="xs:string" use="required" name="field"/>
 *   &lt;xs:attribute type="xs:string" use="optional" name="key_ref"/>
 *   &lt;xs:attribute type="xs:string" use="optional" name="name"/>
 * &lt;/xs:complexType>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MenuActionBase
{
    @XmlAttribute(name = "field")
    private String field;
    @XmlAttribute(name = "key_ref")
    private String keyRef;
    @XmlAttribute(name = "name")
    private String name;

    /**
     * Get the 'field' attribute value.
     *
     * @return value
     */
    public String getField() {
        return field;
    }

    /**
     * Set the 'field' attribute value.
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Get the 'key_ref' attribute value.
     *
     * @return value
     */
    public String getKeyRef() {
        return keyRef;
    }

    /**
     * Set the 'key_ref' attribute value.
     */
    public void setKeyRef(String keyRef) {
        this.keyRef = keyRef;
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
     */
    public void setName(String name) {
        this.name = name;
    }
}
