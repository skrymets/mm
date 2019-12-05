
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="revert_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="map"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="localFileName"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="filePrefix"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class RevertXmlAction extends XmlAction
{
    private String map;
    private String localFileName;
    private String filePrefix;

    /** 
     * Get the 'map' attribute value.
     * 
     * @return value
     */
    public String getMap() {
        return map;
    }

    /** 
     * Set the 'map' attribute value.
     * 
     * @param map
     */
    public void setMap(String map) {
        this.map = map;
    }

    /** 
     * Get the 'localFileName' attribute value.
     * 
     * @return value
     */
    public String getLocalFileName() {
        return localFileName;
    }

    /** 
     * Set the 'localFileName' attribute value.
     * 
     * @param localFileName
     */
    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    /** 
     * Get the 'filePrefix' attribute value.
     * 
     * @return value
     */
    public String getFilePrefix() {
        return filePrefix;
    }

    /** 
     * Set the 'filePrefix' attribute value.
     * 
     * @param filePrefix
     */
    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }
}
