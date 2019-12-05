
package freemind.controller.actions.generated.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_offers">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="collaboration_map_offer" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="is_single_offer"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationOffers extends CollaborationActionBase
{
    private List<CollaborationMapOffer> collaborationMapOfferList = new ArrayList<CollaborationMapOffer>();
    private Boolean isSingleOffer;

    /** 
     * Get the list of 'collaboration_map_offer' element items.
     * 
     * @return list
     */
    public List<CollaborationMapOffer> getCollaborationMapOfferList() {
        return collaborationMapOfferList;
    }

    /** 
     * Set the list of 'collaboration_map_offer' element items.
     * 
     * @param list
     */
    public void setCollaborationMapOfferList(List<CollaborationMapOffer> list) {
        collaborationMapOfferList = list;
    }

    /** 
     * Get the number of 'collaboration_map_offer' element items.
     * @return count
     */
    public int sizeCollaborationMapOfferList() {
        return collaborationMapOfferList.size();
    }

    /** 
     * Add a 'collaboration_map_offer' element item.
     * @param item
     */
    public void addCollaborationMapOffer(CollaborationMapOffer item) {
        collaborationMapOfferList.add(item);
    }

    /** 
     * Get 'collaboration_map_offer' element item by position.
     * @return item
     * @param index
     */
    public CollaborationMapOffer getCollaborationMapOffer(int index) {
        return collaborationMapOfferList.get(index);
    }

    /** 
     * Remove all 'collaboration_map_offer' element items.
     */
    public void clearCollaborationMapOfferList() {
        collaborationMapOfferList.clear();
    }

    /** 
     * Get the 'is_single_offer' attribute value.
     * 
     * @return value
     */
    public Boolean getIsSingleOffer() {
        return isSingleOffer;
    }

    /** 
     * Set the 'is_single_offer' attribute value.
     * 
     * @param isSingleOffer
     */
    public void setIsSingleOffer(Boolean isSingleOffer) {
        this.isSingleOffer = isSingleOffer;
    }
}
