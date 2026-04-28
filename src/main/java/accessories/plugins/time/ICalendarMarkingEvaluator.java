package accessories.plugins.time;

import freemind.controller.actions.xml.calendar.CalendarMarking;
import freemind.controller.actions.xml.calendar.CalendarMarkings;

import java.util.Calendar;

public interface ICalendarMarkingEvaluator {

    CalendarMarking isMarked(Calendar pCalendar);

    void changeMarkings(CalendarMarkings pMarkings);

}
