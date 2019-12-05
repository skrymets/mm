
package freemind.controller.actions.generated.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="calendar_markings">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="calendar_marking" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CalendarMarkings extends XmlAction
{
    private List<CalendarMarking> calendarMarkingList = new ArrayList<CalendarMarking>();

    /** 
     * Get the list of 'calendar_marking' element items.
     * 
     * @return list
     */
    public List<CalendarMarking> getCalendarMarkingList() {
        return calendarMarkingList;
    }

    /** 
     * Set the list of 'calendar_marking' element items.
     * 
     * @param list
     */
    public void setCalendarMarkingList(List<CalendarMarking> list) {
        calendarMarkingList = list;
    }

    /** 
     * Get the number of 'calendar_marking' element items.
     * @return count
     */
    public int sizeCalendarMarkingList() {
        return calendarMarkingList.size();
    }

    /** 
     * Add a 'calendar_marking' element item.
     * @param item
     */
    public void addCalendarMarking(CalendarMarking item) {
        calendarMarkingList.add(item);
    }

    /** 
     * Get 'calendar_marking' element item by position.
     * @return item
     * @param index
     */
    public CalendarMarking getCalendarMarking(int index) {
        return calendarMarkingList.get(index);
    }

    /** 
     * Remove all 'calendar_marking' element items.
     */
    public void clearCalendarMarkingList() {
        calendarMarkingList.clear();
    }
}
