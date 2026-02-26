
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="richcontent">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="html" minOccurs="1" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute use="required" name="TYPE">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class TYPE -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "richcontent")
public class Richcontent
{
    private Html html;
    private TYPE TYPE1;

    /**
     * Get the 'html' element value.
     *
     * @return value
     */
    public Html getHtml() {
        return html;
    }

    /**
     * Set the 'html' element value.
     *
     * @param html
     */
    public void setHtml(Html html) {
        this.html = html;
    }

    /**
     * Get the 'TYPE' attribute value.
     *
     * @return value
     */
    public TYPE getTYPE1() {
        return TYPE1;
    }

    /**
     * Set the 'TYPE' attribute value.
     *
     * @param TYPE1
     */
    public void setTYPE1(TYPE TYPE1) {
        this.TYPE1 = TYPE1;
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="NODE"/>
     *     &lt;xs:enumeration value="NOTE"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum TYPE {
        NODE, NOTE
    }
}
