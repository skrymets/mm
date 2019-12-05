
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="patterns">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="pattern" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class Patterns extends XmlAction
{
    private List<Pattern> patternList = new ArrayList<Pattern>();

    /** 
     * Get the list of 'pattern' element items.
     * 
     * @return list
     */
    public List<Pattern> getPatternList() {
        return patternList;
    }

    /** 
     * Set the list of 'pattern' element items.
     * 
     * @param list
     */
    public void setPatternList(List<Pattern> list) {
        patternList = list;
    }

    /** 
     * Get the number of 'pattern' element items.
     * @return count
     */
    public int sizePatternList() {
        return patternList.size();
    }

    /** 
     * Add a 'pattern' element item.
     * @param item
     */
    public void addPattern(Pattern item) {
        patternList.add(item);
    }

    /** 
     * Get 'pattern' element item by position.
     * @return item
     * @param index
     */
    public Pattern getPattern(int index) {
        return patternList.get(index);
    }

    /** 
     * Remove all 'pattern' element items.
     */
    public void clearPatternList() {
        patternList.clear();
    }
}
