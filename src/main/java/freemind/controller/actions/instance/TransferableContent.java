
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="transferable_content">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="transferable_file" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;xs:element type="xs:string" name="Transferable" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element type="xs:string" name="TransferableAsPlainText" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element type="xs:string" name="TransferableAsRTF" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element type="xs:string" name="TransferableAsDrop" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element type="xs:string" name="TransferableAsHtml" minOccurs="0" maxOccurs="1"/>
 *       &lt;xs:element type="xs:string" name="TransferableAsImage" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class TransferableContent
{
    private List<TransferableFile> transferableFileList = new ArrayList<TransferableFile>();
    private String transferable;
    private String transferableAsPlainText;
    private String transferableAsRTF;
    private String transferableAsDrop;
    private String transferableAsHtml;
    private String transferableAsImage;

    /** 
     * Get the list of 'transferable_file' element items.
     * 
     * @return list
     */
    public List<TransferableFile> getTransferableFileList() {
        return transferableFileList;
    }

    /** 
     * Set the list of 'transferable_file' element items.
     * 
     * @param list
     */
    public void setTransferableFileList(List<TransferableFile> list) {
        transferableFileList = list;
    }

    /** 
     * Get the number of 'transferable_file' element items.
     * @return count
     */
    public int sizeTransferableFileList() {
        return transferableFileList.size();
    }

    /** 
     * Add a 'transferable_file' element item.
     * @param item
     */
    public void addTransferableFile(TransferableFile item) {
        transferableFileList.add(item);
    }

    /** 
     * Get 'transferable_file' element item by position.
     * @return item
     * @param index
     */
    public TransferableFile getTransferableFile(int index) {
        return transferableFileList.get(index);
    }

    /** 
     * Remove all 'transferable_file' element items.
     */
    public void clearTransferableFileList() {
        transferableFileList.clear();
    }

    /** 
     * Get the 'Transferable' element value.
     * 
     * @return value
     */
    public String getTransferable() {
        return transferable;
    }

    /** 
     * Set the 'Transferable' element value.
     * 
     * @param transferable
     */
    public void setTransferable(String transferable) {
        this.transferable = transferable;
    }

    /** 
     * Get the 'TransferableAsPlainText' element value.
     * 
     * @return value
     */
    public String getTransferableAsPlainText() {
        return transferableAsPlainText;
    }

    /** 
     * Set the 'TransferableAsPlainText' element value.
     * 
     * @param transferableAsPlainText
     */
    public void setTransferableAsPlainText(String transferableAsPlainText) {
        this.transferableAsPlainText = transferableAsPlainText;
    }

    /** 
     * Get the 'TransferableAsRTF' element value.
     * 
     * @return value
     */
    public String getTransferableAsRTF() {
        return transferableAsRTF;
    }

    /** 
     * Set the 'TransferableAsRTF' element value.
     * 
     * @param transferableAsRTF
     */
    public void setTransferableAsRTF(String transferableAsRTF) {
        this.transferableAsRTF = transferableAsRTF;
    }

    /** 
     * Get the 'TransferableAsDrop' element value.
     * 
     * @return value
     */
    public String getTransferableAsDrop() {
        return transferableAsDrop;
    }

    /** 
     * Set the 'TransferableAsDrop' element value.
     * 
     * @param transferableAsDrop
     */
    public void setTransferableAsDrop(String transferableAsDrop) {
        this.transferableAsDrop = transferableAsDrop;
    }

    /** 
     * Get the 'TransferableAsHtml' element value.
     * 
     * @return value
     */
    public String getTransferableAsHtml() {
        return transferableAsHtml;
    }

    /** 
     * Set the 'TransferableAsHtml' element value.
     * 
     * @param transferableAsHtml
     */
    public void setTransferableAsHtml(String transferableAsHtml) {
        this.transferableAsHtml = transferableAsHtml;
    }

    /** 
     * Get the 'TransferableAsImage' element value.
     * 
     * @return value
     */
    public String getTransferableAsImage() {
        return transferableAsImage;
    }

    /** 
     * Set the 'TransferableAsImage' element value.
     * 
     * @param transferableAsImage
     */
    public void setTransferableAsImage(String transferableAsImage) {
        this.transferableAsImage = transferableAsImage;
    }
}
