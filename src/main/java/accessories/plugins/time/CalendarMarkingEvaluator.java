package accessories.plugins.time;

import freemind.controller.actions.CalendarMarking;
import freemind.controller.actions.CalendarMarkings;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class CalendarMarkingEvaluator implements ICalendarMarkingEvaluator {

    private CalendarMarkings mCalendarMarkings;
    private final HashMap<Long, CalendarMarking> mCache = new HashMap<>();

    private interface RepetitionHandler {

        Calendar getFirst(Calendar pStartDate, CalendarMarking pMarking);

        Calendar getNext(Calendar pDay, CalendarMarking pMarking);
    }

    private abstract static class BasicRepetitionHandler implements RepetitionHandler {
        public Calendar compareIfStillBefore(Calendar pDay, CalendarMarking pMarking) {
            if (pMarking.getEndDate() > 0) {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTimeInMillis(pMarking.getEndDate());
                if (pDay.compareTo(cal2) <= 0) {
                    return pDay;
                } else {
                    return null;
                }
            }
            return pDay;
        }

        public Calendar shiftToBeAfterStartDate(Calendar pFirstDate,
                                                CalendarMarking pMarking, Calendar pUserStartDate) {
            while (pFirstDate != null && pUserStartDate.compareTo(pFirstDate) > 0) {
                // first occurrence is after the start date. we shift unless done.
                long millies = pFirstDate.getTimeInMillis();
                pFirstDate = getNext(pFirstDate, pMarking);
                if (millies >= pFirstDate.getTimeInMillis()) {
                    throw new IllegalArgumentException("Next doesn't work: " + pFirstDate.getTime());
                }
            }
            return pFirstDate;
        }

    }

    private abstract static class DirektBeginnerHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate, CalendarMarking pMarking) {
            return pStartDate;
        }

    }

    private static class NeverHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            return null;
        }

    }

    private static class WeeklyHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            pDay.add(Calendar.WEEK_OF_YEAR, pMarking.getRepeatEachNOccurence());
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class WeeklyEveryNthDayHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar clone = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.DAY_OF_WEEK, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, clone);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.DAY_OF_WEEK, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.WEEK_OF_YEAR) != pDay.get(Calendar.WEEK_OF_YEAR)) {
                // we switched the week. Let's start from new:
                pDay.set(Calendar.DAY_OF_WEEK, pMarking.getFirstOccurence());
            }
            Calendar nextCal = compareIfStillBefore(pDay, pMarking);
            return nextCal;
        }

    }

    private static class MonthlyHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            pDay.add(Calendar.MONTH, pMarking.getRepeatEachNOccurence());
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class MonthlyEveryNthDayHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar clone = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.DAY_OF_MONTH, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, clone);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.DAY_OF_MONTH, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.MONTH) != pDay.get(Calendar.MONTH)) {
                // we switched the month. Let's start from new:
                pDay.set(Calendar.DAY_OF_MONTH, pMarking.getFirstOccurence());
            }
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class MonthlyEveryNthWeekHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar clone = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.WEEK_OF_MONTH, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, clone);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.WEEK_OF_MONTH, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.MONTH) != pDay.get(Calendar.MONTH)) {
                // we switched the month. Let's start from new:
                pDay.set(Calendar.WEEK_OF_MONTH, pMarking.getFirstOccurence());
            }
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class YearlyHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            pDay.add(Calendar.YEAR, pMarking.getRepeatEachNOccurence());
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class YearlyEveryNthDayHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar clone = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.DAY_OF_YEAR, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, clone);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.DAY_OF_YEAR, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.YEAR) != pDay.get(Calendar.YEAR)) {
                // we switched the year. Let's start from new:
                pDay.set(Calendar.DAY_OF_YEAR, pMarking.getFirstOccurence());
            }
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class YearlyEveryNthWeekHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar userStartDate = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.WEEK_OF_YEAR, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, userStartDate);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.WEEK_OF_YEAR, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.YEAR) != pDay.get(Calendar.YEAR)) {
                // we switched the year. Let's start from new:
                pDay.set(Calendar.WEEK_OF_YEAR, pMarking.getFirstOccurence());
                if (clone.get(Calendar.YEAR) == pDay.get(Calendar.YEAR)) {
                    // case that the first week is still in the old year.
                    pDay.add(Calendar.WEEK_OF_YEAR, pMarking.getRepeatEachNOccurence());
                }
            }
            Calendar nextCal = compareIfStillBefore(pDay, pMarking);
            return nextCal;
        }

    }

    private static class YearlyEveryNthMonthHandler extends BasicRepetitionHandler {

        @Override
        public Calendar getFirst(Calendar pStartDate,
                                 CalendarMarking pMarking) {
            Calendar clone = (Calendar) pStartDate.clone();
            pStartDate.set(Calendar.MONTH, pMarking.getFirstOccurence());
            return shiftToBeAfterStartDate(pStartDate, pMarking, clone);
        }

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            Calendar clone = (Calendar) pDay.clone();
            pDay.add(Calendar.MONTH, pMarking.getRepeatEachNOccurence());
            if (clone.get(Calendar.YEAR) != pDay.get(Calendar.YEAR)) {
                // we switched the year. Let's start from new:
                pDay.set(Calendar.MONTH, pMarking.getFirstOccurence());
            }
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static class DailyHandler extends DirektBeginnerHandler {

        @Override
        public Calendar getNext(Calendar pDay, CalendarMarking pMarking) {
            pDay.add(Calendar.DAY_OF_YEAR, pMarking.getRepeatEachNOccurence());
            return compareIfStillBefore(pDay, pMarking);
        }

    }

    private static HashMap<String, RepetitionHandler> sHandlerMap;

    public CalendarMarkingEvaluator(CalendarMarkings pCalendarMarkings) {
        mCalendarMarkings = pCalendarMarkings;
        if (sHandlerMap == null) {
            sHandlerMap = new HashMap<>();
            sHandlerMap.put(CalendarMarking.RepeatType.NEVER.xmlValue(), new NeverHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.DAILY.xmlValue(), new DailyHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.WEEKLY.xmlValue(), new WeeklyHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.WEEKLY_EVERY_NTH_DAY.xmlValue(), new WeeklyEveryNthDayHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.MONTHLY.xmlValue(), new MonthlyHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.MONTHLY_EVERY_NTH_DAY.xmlValue(), new MonthlyEveryNthDayHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.MONTHLY_EVERY_NTH_WEEK.xmlValue(), new MonthlyEveryNthWeekHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.YEARLY.xmlValue(), new YearlyHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.YEARLY_EVERY_NTH_DAY.xmlValue(), new YearlyEveryNthDayHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.YEARLY_EVERY_NTH_WEEK.xmlValue(), new YearlyEveryNthWeekHandler());
            sHandlerMap.put(CalendarMarking.RepeatType.YEARLY_EVERY_NTH_MONTH.xmlValue(), new YearlyEveryNthMonthHandler());
        }
    }

    public Set<Calendar> getAtLeastTheFirstNEntries(int n) {
        Set<Calendar> retValue = new TreeSet<>();
        for (int i = 0; i < mCalendarMarkings.sizeCalendarMarkingList(); i++) {
            int count = 0;
            CalendarMarking marking = mCalendarMarkings.getCalendarMarking(i);
            // common error for self written entries:
            if (marking.getRepeatEachNOccurence() == 0) {
                marking.setRepeatEachNOccurence(1);
            }
            // get first occurrence:
            Calendar firstDay = Calendar.getInstance();
            firstDay.setTimeInMillis(marking.getStartDate());
            String repeatType = marking.getRepeatType().xmlValue();
            if (!sHandlerMap.containsKey(repeatType)) {
                log.error("Repeat type {} unknown.", repeatType);
                continue;
            }
            RepetitionHandler handler = sHandlerMap
                    .get(repeatType);
            firstDay = handler.getFirst(firstDay, marking);
            if (firstDay == null) {
                continue;
            }
            while (count < n) {
                retValue.add((Calendar) firstDay.clone());
                firstDay = handler.getNext(firstDay, marking);
                if (firstDay == null) {
                    break;
                }
                count++;
            }
        }
        return retValue;
    }

    @Override
    public CalendarMarking isMarked(Calendar pCalendar) {
        long millies = pCalendar.getTimeInMillis();
        if (mCache.containsKey(millies)) {
            return mCache.get(millies);
        }
        pCalendar = (Calendar) pCalendar.clone();
        for (int i = 0; i < mCalendarMarkings.sizeCalendarMarkingList(); i++) {
            CalendarMarking marking = mCalendarMarkings.getCalendarMarking(i);
            // common error for self written entries:
            if (marking.getRepeatEachNOccurence() == 0) {
                marking.setRepeatEachNOccurence(1);
            }
            // get first occurrence:
            Calendar firstDay = Calendar.getInstance();
            firstDay.setTimeInMillis(marking.getStartDate());
            RepetitionHandler handler = sHandlerMap
                    .get(marking.getRepeatType());
            firstDay = handler.getFirst(firstDay, marking);
            if (firstDay == null) {
                continue;
            }
            if (equal(pCalendar, firstDay)) {
                mCache.put(millies, marking);
                return marking;
            }
            while (pCalendar.compareTo(firstDay) >= 0) {
                firstDay = handler.getNext(firstDay, marking);
                if (firstDay == null) {
                    break;
                }
                if (equal(pCalendar, firstDay)) {
                    mCache.put(millies, marking);
                    return marking;
                }
            }
        }
        mCache.put(millies, null);
        return null;
    }

    private boolean equal(Calendar pCalendar, Calendar pOtherDay) {
        return pCalendar.get(Calendar.YEAR) == pOtherDay.get(Calendar.YEAR)
                && pCalendar.get(Calendar.MONTH) == pOtherDay
                .get(Calendar.MONTH)
                && pCalendar.get(Calendar.DAY_OF_MONTH) == pOtherDay
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Don't use for endless repetitions!
     */
    public void print() {
        for (int i = 0; i < mCalendarMarkings.sizeCalendarMarkingList(); i++) {
            CalendarMarking marking = mCalendarMarkings.getCalendarMarking(i);
            // get first occurrence:
            Calendar firstDay = Calendar.getInstance();
            firstDay.setTimeInMillis(marking.getStartDate());
            RepetitionHandler handler = sHandlerMap
                    .get(marking.getRepeatType());
            firstDay = handler.getFirst(firstDay, marking);
            printDate(firstDay);
            while (firstDay != null) {
                firstDay = handler.getNext(firstDay, marking);
                printDate(firstDay);
            }
        }
    }

    public void printDate(Calendar firstDay) {
        if (firstDay != null) {
            log.debug("Date: {}", DateFormat.getDateInstance().format(firstDay.getTime()));
        }
    }

    @Override
    public void changeMarkings(CalendarMarkings pMarkings) {
        mCalendarMarkings = pMarkings;
        mCache.clear();
    }
}
