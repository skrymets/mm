
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="calendar_marking">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="color"/>
 *     &lt;xs:attribute type="xs:long" use="required" name="start_date"/>
 *     &lt;xs:attribute type="xs:long" use="optional" name="end_date"/>
 *     &lt;xs:attribute use="required" name="repeat_type">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class RepeatType -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute type="xs:int" use="optional" name="repeat_each_n_occurence"/>
 *     &lt;xs:attribute type="xs:int" use="optional" name="first_occurence"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CalendarMarking
{
    private String name;
    private String color;
    private long startDate;
    private Long endDate;
    private RepeatType repeatType;
    private Integer repeatEachNOccurence;
    private Integer firstOccurence;

    /** 
     * Get the 'name' attribute value.
     * 
     * @return value
     */
    public String getName() {
        return name;
    }

    /** 
     * Set the 'name' attribute value.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * Get the 'color' attribute value.
     * 
     * @return value
     */
    public String getColor() {
        return color;
    }

    /** 
     * Set the 'color' attribute value.
     * 
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /** 
     * Get the 'start_date' attribute value.
     * 
     * @return value
     */
    public long getStartDate() {
        return startDate;
    }

    /** 
     * Set the 'start_date' attribute value.
     * 
     * @param startDate
     */
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /** 
     * Get the 'end_date' attribute value.
     * 
     * @return value
     */
    public Long getEndDate() {
        return endDate;
    }

    /** 
     * Set the 'end_date' attribute value.
     * 
     * @param endDate
     */
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    /** 
     * Get the 'repeat_type' attribute value.
     * 
     * @return value
     */
    public RepeatType getRepeatType() {
        return repeatType;
    }

    /** 
     * Set the 'repeat_type' attribute value.
     * 
     * @param repeatType
     */
    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    /** 
     * Get the 'repeat_each_n_occurence' attribute value.
     * 
     * @return value
     */
    public Integer getRepeatEachNOccurence() {
        return repeatEachNOccurence;
    }

    /** 
     * Set the 'repeat_each_n_occurence' attribute value.
     * 
     * @param repeatEachNOccurence
     */
    public void setRepeatEachNOccurence(Integer repeatEachNOccurence) {
        this.repeatEachNOccurence = repeatEachNOccurence;
    }

    /** 
     * Get the 'first_occurence' attribute value.
     * 
     * @return value
     */
    public Integer getFirstOccurence() {
        return firstOccurence;
    }

    /** 
     * Set the 'first_occurence' attribute value.
     * 
     * @param firstOccurence
     */
    public void setFirstOccurence(Integer firstOccurence) {
        this.firstOccurence = firstOccurence;
    }
    /** 
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="never"/>
     *     &lt;xs:enumeration value="yearly"/>
     *     &lt;xs:enumeration value="yearly_every_nth_day"/>
     *     &lt;xs:enumeration value="yearly_every_nth_week"/>
     *     &lt;xs:enumeration value="yearly_every_nth_month"/>
     *     &lt;xs:enumeration value="monthly"/>
     *     &lt;xs:enumeration value="monthly_every_nth_day"/>
     *     &lt;xs:enumeration value="monthly_every_nth_week"/>
     *     &lt;xs:enumeration value="weekly"/>
     *     &lt;xs:enumeration value="weekly_every_nth_day"/>
     *     &lt;xs:enumeration value="daily"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum RepeatType {
        NEVER("never"), YEARLY("yearly"), YEARLY_EVERY_NTH_DAY(
                "yearly_every_nth_day"), YEARLY_EVERY_NTH_WEEK(
                "yearly_every_nth_week"), YEARLY_EVERY_NTH_MONTH(
                "yearly_every_nth_month"), MONTHLY("monthly"), MONTHLY_EVERY_NTH_DAY(
                "monthly_every_nth_day"), MONTHLY_EVERY_NTH_WEEK(
                "monthly_every_nth_week"), WEEKLY("weekly"), WEEKLY_EVERY_NTH_DAY(
                "weekly_every_nth_day"), DAILY("daily");
        private final String value;

        private RepeatType(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static RepeatType convert(String value) {
            for (RepeatType inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
}
