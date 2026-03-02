package accessories.plugins.time;

import freemind.controller.actions.CalendarMarking;
import freemind.controller.actions.CalendarMarkings;

import java.util.Calendar;

public interface ICalendarMarkingEvaluator {

    CalendarMarking isMarked(Calendar pCalendar);

    void changeMarkings(CalendarMarkings pMarkings);

}
