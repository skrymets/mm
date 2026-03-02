
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="hook">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="Parameters" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element ref="text" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="NAME"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "hook")
public class Hook
{
    private Parameters parameters;
    private Text text;
    @XmlAttribute(name = "NAME")
    private String NAME;

    /**
     * Get the 'Parameters' element value.
     *
     * @return value
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Set the 'Parameters' element value.
     */
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Get the 'text' element value.
     *
     * @return value
     */
    public Text getText() {
        return text;
    }

    /**
     * Set the 'text' element value.
     */
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * Get the 'NAME' attribute value.
     *
     * @return value
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * Set the 'NAME' attribute value.
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
