
package freemind.controller.actions;

import java.math.BigInteger;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="font">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute use="optional" name="BOLD">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class BOLD -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute use="optional" name="STRIKETHROUGH">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class STRIKETHROUGH -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute use="optional" name="ITALIC">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class ITALIC -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute type="xs:string" use="required" name="NAME"/>
 *     &lt;xs:attribute type="xs:integer" use="required" name="SIZE"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "font")
public class Font
{
    private BOLD BOLD1;
    private STRIKETHROUGH STRIKETHROUGH1;
    private ITALIC ITALIC1;
    @XmlAttribute(name = "NAME")
    private String NAME;
    @XmlAttribute(name = "SIZE")
    private BigInteger SIZE;

    /**
     * Get the 'BOLD' attribute value.
     *
     * @return value
     */
    public BOLD getBOLD1() {
        return BOLD1;
    }

    /**
     * Set the 'BOLD' attribute value.
     *
     * @param BOLD1
     */
    public void setBOLD1(BOLD BOLD1) {
        this.BOLD1 = BOLD1;
    }

    /**
     * Get the 'STRIKETHROUGH' attribute value.
     *
     * @return value
     */
    public STRIKETHROUGH getSTRIKETHROUGH1() {
        return STRIKETHROUGH1;
    }

    /**
     * Set the 'STRIKETHROUGH' attribute value.
     *
     * @param STRIKETHROUGH1
     */
    public void setSTRIKETHROUGH1(STRIKETHROUGH STRIKETHROUGH1) {
        this.STRIKETHROUGH1 = STRIKETHROUGH1;
    }

    /**
     * Get the 'ITALIC' attribute value.
     *
     * @return value
     */
    public ITALIC getITALIC1() {
        return ITALIC1;
    }

    /**
     * Set the 'ITALIC' attribute value.
     *
     * @param ITALIC1
     */
    public void setITALIC1(ITALIC ITALIC1) {
        this.ITALIC1 = ITALIC1;
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
     *
     * @param NAME
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    /**
     * Get the 'SIZE' attribute value.
     *
     * @return value
     */
    public BigInteger getSIZE() {
        return SIZE;
    }

    /**
     * Set the 'SIZE' attribute value.
     *
     * @param SIZE
     */
    public void setSIZE(BigInteger SIZE) {
        this.SIZE = SIZE;
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="true"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum BOLD {
        TRUE("true");
        private final String value;

        private BOLD(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static BOLD convert(String value) {
            for (BOLD inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="true"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum STRIKETHROUGH {
        TRUE("true");
        private final String value;

        private STRIKETHROUGH(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static STRIKETHROUGH convert(String value) {
            for (STRIKETHROUGH inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="true"/>
     *     &lt;xs:enumeration value="false"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum ITALIC {
        TRUE("true"), FALSE("false");
        private final String value;

        private ITALIC(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static ITALIC convert(String value) {
            for (ITALIC inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
}
