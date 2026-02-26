
package freemind.controller.actions;

import java.math.BigInteger;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Parameters">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="REMINDUSERAT"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ORIGINAL_ID"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_MAP_LAT"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_MAP_LON"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_MAP_TOOLTIP_LOCATION"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_POS_LAT"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_POS_LON"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_TILE_SOURCE"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="XML_STORAGE_ZOOM"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="CLONE_ID"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="CLONE_IDS"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="CLONE_ITSELF"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Parameters")
public class Parameters
{
    @XmlAttribute(name = "REMINDUSERAT")
    private BigInteger REMINDUSERAT;
    @XmlAttribute(name = "ORIGINAL_ID")
    private String ORIGINALID;
    @XmlAttribute(name = "XML_STORAGE_MAP_LAT")
    private String XMLSTORAGEMAPLAT;
    @XmlAttribute(name = "XML_STORAGE_MAP_LON")
    private String XMLSTORAGEMAPLON;
    @XmlAttribute(name = "XML_STORAGE_MAP_TOOLTIP_LOCATION")
    private String XMLSTORAGEMAPTOOLTIPLOCATION;
    @XmlAttribute(name = "XML_STORAGE_POS_LAT")
    private String XMLSTORAGEPOSLAT;
    @XmlAttribute(name = "XML_STORAGE_POS_LON")
    private String XMLSTORAGEPOSLON;
    @XmlAttribute(name = "XML_STORAGE_TILE_SOURCE")
    private String XMLSTORAGETILESOURCE;
    @XmlAttribute(name = "XML_STORAGE_ZOOM")
    private String XMLSTORAGEZOOM;
    @XmlAttribute(name = "CLONE_ID")
    private String CLONEID;
    @XmlAttribute(name = "CLONE_IDS")
    private String CLONEIDS;
    @XmlAttribute(name = "CLONE_ITSELF")
    private String CLONEITSELF;

    /**
     * Get the 'REMINDUSERAT' attribute value.
     *
     * @return value
     */
    public BigInteger getREMINDUSERAT() {
        return REMINDUSERAT;
    }

    /**
     * Set the 'REMINDUSERAT' attribute value.
     *
     * @param REMINDUSERAT
     */
    public void setREMINDUSERAT(BigInteger REMINDUSERAT) {
        this.REMINDUSERAT = REMINDUSERAT;
    }

    /**
     * Get the 'ORIGINAL_ID' attribute value.
     *
     * @return value
     */
    public String getORIGINALID() {
        return ORIGINALID;
    }

    /**
     * Set the 'ORIGINAL_ID' attribute value.
     *
     * @param ORIGINALID
     */
    public void setORIGINALID(String ORIGINALID) {
        this.ORIGINALID = ORIGINALID;
    }

    /**
     * Get the 'XML_STORAGE_MAP_LAT' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEMAPLAT() {
        return XMLSTORAGEMAPLAT;
    }

    /**
     * Set the 'XML_STORAGE_MAP_LAT' attribute value.
     *
     * @param XMLSTORAGEMAPLAT
     */
    public void setXMLSTORAGEMAPLAT(String XMLSTORAGEMAPLAT) {
        this.XMLSTORAGEMAPLAT = XMLSTORAGEMAPLAT;
    }

    /**
     * Get the 'XML_STORAGE_MAP_LON' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEMAPLON() {
        return XMLSTORAGEMAPLON;
    }

    /**
     * Set the 'XML_STORAGE_MAP_LON' attribute value.
     *
     * @param XMLSTORAGEMAPLON
     */
    public void setXMLSTORAGEMAPLON(String XMLSTORAGEMAPLON) {
        this.XMLSTORAGEMAPLON = XMLSTORAGEMAPLON;
    }

    /**
     * Get the 'XML_STORAGE_MAP_TOOLTIP_LOCATION' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEMAPTOOLTIPLOCATION() {
        return XMLSTORAGEMAPTOOLTIPLOCATION;
    }

    /**
     * Set the 'XML_STORAGE_MAP_TOOLTIP_LOCATION' attribute value.
     *
     * @param XMLSTORAGEMAPTOOLTIPLOCATION
     */
    public void setXMLSTORAGEMAPTOOLTIPLOCATION(
            String XMLSTORAGEMAPTOOLTIPLOCATION) {
        this.XMLSTORAGEMAPTOOLTIPLOCATION = XMLSTORAGEMAPTOOLTIPLOCATION;
    }

    /**
     * Get the 'XML_STORAGE_POS_LAT' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEPOSLAT() {
        return XMLSTORAGEPOSLAT;
    }

    /**
     * Set the 'XML_STORAGE_POS_LAT' attribute value.
     *
     * @param XMLSTORAGEPOSLAT
     */
    public void setXMLSTORAGEPOSLAT(String XMLSTORAGEPOSLAT) {
        this.XMLSTORAGEPOSLAT = XMLSTORAGEPOSLAT;
    }

    /**
     * Get the 'XML_STORAGE_POS_LON' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEPOSLON() {
        return XMLSTORAGEPOSLON;
    }

    /**
     * Set the 'XML_STORAGE_POS_LON' attribute value.
     *
     * @param XMLSTORAGEPOSLON
     */
    public void setXMLSTORAGEPOSLON(String XMLSTORAGEPOSLON) {
        this.XMLSTORAGEPOSLON = XMLSTORAGEPOSLON;
    }

    /**
     * Get the 'XML_STORAGE_TILE_SOURCE' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGETILESOURCE() {
        return XMLSTORAGETILESOURCE;
    }

    /**
     * Set the 'XML_STORAGE_TILE_SOURCE' attribute value.
     *
     * @param XMLSTORAGETILESOURCE
     */
    public void setXMLSTORAGETILESOURCE(String XMLSTORAGETILESOURCE) {
        this.XMLSTORAGETILESOURCE = XMLSTORAGETILESOURCE;
    }

    /**
     * Get the 'XML_STORAGE_ZOOM' attribute value.
     *
     * @return value
     */
    public String getXMLSTORAGEZOOM() {
        return XMLSTORAGEZOOM;
    }

    /**
     * Set the 'XML_STORAGE_ZOOM' attribute value.
     *
     * @param XMLSTORAGEZOOM
     */
    public void setXMLSTORAGEZOOM(String XMLSTORAGEZOOM) {
        this.XMLSTORAGEZOOM = XMLSTORAGEZOOM;
    }

    /**
     * Get the 'CLONE_ID' attribute value.
     *
     * @return value
     */
    public String getCLONEID() {
        return CLONEID;
    }

    /**
     * Set the 'CLONE_ID' attribute value.
     *
     * @param CLONEID
     */
    public void setCLONEID(String CLONEID) {
        this.CLONEID = CLONEID;
    }

    /**
     * Get the 'CLONE_IDS' attribute value.
     *
     * @return value
     */
    public String getCLONEIDS() {
        return CLONEIDS;
    }

    /**
     * Set the 'CLONE_IDS' attribute value.
     *
     * @param CLONEIDS
     */
    public void setCLONEIDS(String CLONEIDS) {
        this.CLONEIDS = CLONEIDS;
    }

    /**
     * Get the 'CLONE_ITSELF' attribute value.
     *
     * @return value
     */
    public String getCLONEITSELF() {
        return CLONEITSELF;
    }

    /**
     * Set the 'CLONE_ITSELF' attribute value.
     *
     * @param CLONEITSELF
     */
    public void setCLONEITSELF(String CLONEITSELF) {
        this.CLONEITSELF = CLONEITSELF;
    }
}
