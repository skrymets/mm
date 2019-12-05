
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="transferable_file">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="file_name"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class TransferableFile
{
    private String fileName;

    /** 
     * Get the 'file_name' attribute value.
     * 
     * @return value
     */
    public String getFileName() {
        return fileName;
    }

    /** 
     * Set the 'file_name' attribute value.
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
