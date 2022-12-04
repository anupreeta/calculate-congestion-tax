package congestion.calculator;

import congestion.calculator.entity.CityEntity;
import congestion.calculator.entity.HolidayCalendarEntity;
import congestion.calculator.entity.HolidayMonthsEntity;
import congestion.calculator.entity.WorkingCalendarEntity;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Boolean isWeekend(WorkingCalendarEntity workingCalendarEntity, int day) {
        if(workingCalendarEntity == null) return false;

        if(workingCalendarEntity.isMonday() == false && day == Calendar.MONDAY) return true;
        if(workingCalendarEntity.isTuesday() == false && day == Calendar.TUESDAY) return true;
        if(workingCalendarEntity.isWednesday() == false && day == Calendar.WEDNESDAY) return true;
        if(workingCalendarEntity.isThursday() == false && day == Calendar.THURSDAY) return true;
        if(workingCalendarEntity.isFriday() == false && day == Calendar.FRIDAY) return true;
        if(workingCalendarEntity.isSaturday() == false && day == Calendar.SATURDAY) return true;
        if(workingCalendarEntity.isSunday() == false && day == Calendar.SUNDAY) return true;

        return false;
    }

    public static Boolean isHolidayMonth(HolidayMonthsEntity holidayMonthsEntity, int month) {
        if(holidayMonthsEntity == null) return false;

        if(holidayMonthsEntity.isJanuary() == true && month == (Calendar.JANUARY+1)) return true;
        if(holidayMonthsEntity.isFebruary() == true && month == (Calendar.FEBRUARY+1)) return true;
        if(holidayMonthsEntity.isMarch() == true && month == (Calendar.MARCH+1)) return true;
        if(holidayMonthsEntity.isApril() == true && month == (Calendar.APRIL+1)) return true;
        if(holidayMonthsEntity.isMay() == true && month == (Calendar.MAY+1)) return true;
        if(holidayMonthsEntity.isJune() == true && month == (Calendar.JUNE+1)) return true;
        if(holidayMonthsEntity.isJuly() == true && month == (Calendar.JULY+1)) return true;
        if(holidayMonthsEntity.isAugust() == true && month == (Calendar.AUGUST+1)) return true;
        if(holidayMonthsEntity.isSeptember() == true && month == (Calendar.SEPTEMBER+1)) return true;
        if(holidayMonthsEntity.isOctober() == true && month == (Calendar.OCTOBER+1)) return true;
        if(holidayMonthsEntity.isNovember() == true && month == (Calendar.NOVEMBER+1)) return true;
        if(holidayMonthsEntity.isDecember() == true && month == (Calendar.DECEMBER+1)) return true;

        return false;
    }

    public static Boolean isPerOrPostOrInPublicHoliday(Date date, CityEntity cityEntity) {
        Set<HolidayCalendarEntity> publicHolidays = cityEntity.getHolidayCalendarEntities();
        if(publicHolidays == null || publicHolidays.isEmpty()) return false;

        if (publicHolidays.stream().filter(holiday -> DateUtils.isSameDay(holiday.getDate(), date)).count() > 0 ) return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInBetweenIncludingEndPoints(
                        holiday.getDate(),
                        DateUtils.addDays(holiday.getDate(), cityEntity.getCityPreferenceEntity().getNumberOfTaxFreeDaysAfterHoliday()),
                        date
                ))
                .count() > 0 ) return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInBetweenIncludingEndPoints(
                        DateUtils.addDays(holiday.getDate(), -(cityEntity.getCityPreferenceEntity().getNumberOfTaxFreeDaysBeforeHoliday())),
                        holiday.getDate(),
                        date
                ))
                .count() > 0 ) return true;
        return false;
    }

    public static boolean isDateInBetweenIncludingEndPoints(final Date min, final Date max, final Date date){
        return !(date.before(min) || date.after(max));
    }

    public static String removeTime(Date date) {
        return dateFormat.format(date);
    }

    public static Date objectToDate(Object date) throws ParseException {
        if(date instanceof String)
            return dateAndTimeFormat.parse(date.toString());
        else
            return dateAndTimeFormat.parse(dateAndTimeFormat.format(((Date)date)));
    }

    public static void sortDateByAsc(List<Date> dates) {
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date object1, Date object2) {
                return object1.compareTo(object2);
            }
        });
    }
}
