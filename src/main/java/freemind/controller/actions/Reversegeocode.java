
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="reversegeocode">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="result"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:string" use="required" name="attribution"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="querystring"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="timestamp"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reversegeocode")
public class Reversegeocode extends XmlAction
{
    private ResultBase result;
    @XmlAttribute(name = "attribution")
    private String attribution;
    @XmlAttribute(name = "querystring")
    private String querystring;
    @XmlAttribute(name = "timestamp")
    private String timestamp;

    /**
     * Get the 'result' element value.
     *
     * @return value
     */
    public ResultBase getResult() {
        return result;
    }

    /**
     * Set the 'result' element value.
     */
    public void setResult(ResultBase result) {
        this.result = result;
    }

    /**
     * Get the 'attribution' attribute value.
     *
     * @return value
     */
    public String getAttribution() {
        return attribution;
    }

    /**
     * Set the 'attribution' attribute value.
     */
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    /**
     * Get the 'querystring' attribute value.
     *
     * @return value
     */
    public String getQuerystring() {
        return querystring;
    }

    /**
     * Set the 'querystring' attribute value.
     */
    public void setQuerystring(String querystring) {
        this.querystring = querystring;
    }

    /**
     * Get the 'timestamp' attribute value.
     *
     * @return value
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the 'timestamp' attribute value.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
