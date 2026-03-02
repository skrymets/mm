
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="linktarget">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="optional" name="COLOR"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="SOURCE"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="DESTINATION"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ENDARROW"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ENDINCLINATION"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ID"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="STARTARROW"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="STARTINCLINATION"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "linktarget")
public class Linktarget
{
    @XmlAttribute(name = "COLOR")
    private String COLOR;
    @XmlAttribute(name = "SOURCE")
    private String SOURCE;
    @XmlAttribute(name = "DESTINATION")
    private String DESTINATION;
    @XmlAttribute(name = "ENDARROW")
    private String ENDARROW;
    @XmlAttribute(name = "ENDINCLINATION")
    private String ENDINCLINATION;
    @XmlAttribute(name = "ID")
    private String ID;
    @XmlAttribute(name = "STARTARROW")
    private String STARTARROW;
    @XmlAttribute(name = "STARTINCLINATION")
    private String STARTINCLINATION;

    /**
     * Get the 'COLOR' attribute value.
     *
     * @return value
     */
    public String getCOLOR() {
        return COLOR;
    }

    /**
     * Set the 'COLOR' attribute value.
     */
    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    /**
     * Get the 'SOURCE' attribute value.
     *
     * @return value
     */
    public String getSOURCE() {
        return SOURCE;
    }

    /**
     * Set the 'SOURCE' attribute value.
     */
    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    /**
     * Get the 'DESTINATION' attribute value.
     *
     * @return value
     */
    public String getDESTINATION() {
        return DESTINATION;
    }

    /**
     * Set the 'DESTINATION' attribute value.
     */
    public void setDESTINATION(String DESTINATION) {
        this.DESTINATION = DESTINATION;
    }

    /**
     * Get the 'ENDARROW' attribute value.
     *
     * @return value
     */
    public String getENDARROW() {
        return ENDARROW;
    }

    /**
     * Set the 'ENDARROW' attribute value.
     */
    public void setENDARROW(String ENDARROW) {
        this.ENDARROW = ENDARROW;
    }

    /**
     * Get the 'ENDINCLINATION' attribute value.
     *
     * @return value
     */
    public String getENDINCLINATION() {
        return ENDINCLINATION;
    }

    /**
     * Set the 'ENDINCLINATION' attribute value.
     */
    public void setENDINCLINATION(String ENDINCLINATION) {
        this.ENDINCLINATION = ENDINCLINATION;
    }

    /**
     * Get the 'ID' attribute value.
     *
     * @return value
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the 'ID' attribute value.
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Get the 'STARTARROW' attribute value.
     *
     * @return value
     */
    public String getSTARTARROW() {
        return STARTARROW;
    }

    /**
     * Set the 'STARTARROW' attribute value.
     */
    public void setSTARTARROW(String STARTARROW) {
        this.STARTARROW = STARTARROW;
    }

    /**
     * Get the 'STARTINCLINATION' attribute value.
     *
     * @return value
     */
    public String getSTARTINCLINATION() {
        return STARTINCLINATION;
    }

    /**
     * Set the 'STARTINCLINATION' attribute value.
     */
    public void setSTARTINCLINATION(String STARTINCLINATION) {
        this.STARTINCLINATION = STARTINCLINATION;
    }
}
