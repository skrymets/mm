
package freemind.controller.actions;

import java.math.BigInteger;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="attribute_layout">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:integer" use="required" name="NAME_WIDTH"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="VALUE_WIDTH"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "attribute_layout")
public class AttributeLayout
{
    @XmlAttribute(name = "NAME_WIDTH")
    private BigInteger NAMEWIDTH;
    @XmlAttribute(name = "VALUE_WIDTH")
    private BigInteger VALUEWIDTH;

    /**
     * Get the 'NAME_WIDTH' attribute value.
     *
     * @return value
     */
    public BigInteger getNAMEWIDTH() {
        return NAMEWIDTH;
    }

    /**
     * Set the 'NAME_WIDTH' attribute value.
     */
    public void setNAMEWIDTH(BigInteger NAMEWIDTH) {
        this.NAMEWIDTH = NAMEWIDTH;
    }

    /**
     * Get the 'VALUE_WIDTH' attribute value.
     *
     * @return value
     */
    public BigInteger getVALUEWIDTH() {
        return VALUEWIDTH;
    }

    /**
     * Set the 'VALUE_WIDTH' attribute value.
     */
    public void setVALUEWIDTH(BigInteger VALUEWIDTH) {
        this.VALUEWIDTH = VALUEWIDTH;
    }
}
