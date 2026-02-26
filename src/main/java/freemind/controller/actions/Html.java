
package freemind.controller.actions;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="html">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:any minOccurs="0" maxOccurs="unbounded" processContents="skip" namespace="##any"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "html")
public class Html
{
    private List<Element> anyList = new ArrayList<Element>();

    /**
     * Get the list of 'html' element items.
     *
     * @return list
     */
    public List<Element> getAnyList() {
        return anyList;
    }

    /**
     * Set the list of 'html' element items.
     *
     * @param list
     */
    public void setAnyList(List<Element> list) {
        anyList = list;
    }

    /**
     * Get the number of 'html' element items.
     * @return count
     */
    public int sizeAnyList() {
        return anyList.size();
    }

    /**
     * Add a 'html' element item.
     * @param item
     */
    public void addAny(Element item) {
        anyList.add(item);
    }

    /**
     * Get 'html' element item by position.
     * @return item
     * @param index
     */
    public Element getAny(int index) {
        return anyList.get(index);
    }

    /**
     * Remove all 'html' element items.
     */
    public void clearAnyList() {
        anyList.clear();
    }
}
