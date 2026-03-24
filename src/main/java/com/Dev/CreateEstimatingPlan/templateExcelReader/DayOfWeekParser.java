package com.Dev.CreateEstimatingPlan.templateExcelReader;

import java.time.DayOfWeek;
import java.util.Map;

public class DayOfWeekParser {
    private static final Map<String, DayOfWeek> DAYS_MAP = Map.of(
            "понеділок", DayOfWeek.MONDAY,
            "вівторок", DayOfWeek.TUESDAY,
            "середа", DayOfWeek.WEDNESDAY,
            "четвер", DayOfWeek.THURSDAY,
            "п’ятниця", DayOfWeek.FRIDAY
    );
    private static final Map<String, DayOfWeek> daysMap = Map.of(
            "ПН", DayOfWeek.MONDAY,
            "ВТ", DayOfWeek.TUESDAY,
            "СР", DayOfWeek.WEDNESDAY,
            "ЧТ", DayOfWeek.THURSDAY,
            "ПТ", DayOfWeek.FRIDAY
    );

    public static DayOfWeek parseDayOfWeek(String firstWeekFirstClass) {
        return daysMap.get(firstWeekFirstClass);
    }
    public static boolean isDayOfWeek(String value) {
        String[] parts = value.split(" ");
        return DAYS_MAP.containsKey(parts[0].toLowerCase());
    }
    public static DayOfWeek getDayOfWeek(String day) {
        return DAYS_MAP.get(day.toLowerCase());
    }
}
