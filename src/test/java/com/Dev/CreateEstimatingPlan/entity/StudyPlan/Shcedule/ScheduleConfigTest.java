package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class ScheduleConfigTest {

    private ScheduleConfig scheduleConfig;

    @BeforeEach
    public void setUp() {
        Map<Integer, List<DayOfWeek>> weekSchedule = new HashMap<>();
        weekSchedule.put(1, List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY));
        weekSchedule.put(2, List.of(DayOfWeek.MONDAY));

        scheduleConfig = new ScheduleConfig(weekSchedule);
    }

    @Test
    public void canGetDaysForWeek() {
        List<DayOfWeek> firstWeekDays = scheduleConfig.getDaysForWeek(1);
        assertEquals(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY), firstWeekDays);

        List<DayOfWeek> secondWeekDays = scheduleConfig.getDaysForWeek(2);
        assertEquals(List.of(DayOfWeek.MONDAY), secondWeekDays);

        List<DayOfWeek> notSetWeekDays = scheduleConfig.getDaysForWeek(3);
        assertTrue(notSetWeekDays.isEmpty());
    }

    @Test
    public void canGenerateScheduleBasic() {
        LocalDate start = LocalDate.of(2025, 3, 3);
        LocalDate end = LocalDate.of(2025, 3, 14);

        List<LocalDate> schedule = scheduleConfig.generateSchedule(start, end, null);

        List<LocalDate> expected = List.of(
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 3, 10)
        );

        assertEquals(expected, schedule);
    }

    @Test
    public void canGenerateScheduleWithRescheduledForSaturdays() {
        LocalDate start = LocalDate.of(2025, 3, 3);
        LocalDate end = LocalDate.of(2025, 3, 14);

        Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledSaturdays = new HashMap<>();
        Map<Integer, DayOfWeek> dateRescheduled = new HashMap<>();
        dateRescheduled.put(1, DayOfWeek.MONDAY);
        rescheduledSaturdays.put(dateRescheduled, List.of(LocalDate.of(2025, 3, 8)));

        List<LocalDate> schedule = scheduleConfig.generateSchedule(start, end, rescheduledSaturdays);

        List<LocalDate> expected = List.of(
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 3, 8),
                LocalDate.of(2025, 3, 8),
                LocalDate.of(2025, 3, 10)
        );

        assertEquals(expected, schedule);
    }

    @Test
    public void canGenerateScheduleWithExcludedDate() {
        LocalDate start = LocalDate.of(2025, 3, 3);
        LocalDate end = LocalDate.of(2025, 3, 14);

        Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledSaturdays = new HashMap<>();


        Map<Integer, DayOfWeek> dateRescheduledStr = new HashMap<>();
        dateRescheduledStr.put(2, DayOfWeek.FRIDAY);


        rescheduledSaturdays.put(dateRescheduledStr, List.of(LocalDate.of(2025, 3, 10)));

        List<LocalDate> schedule = scheduleConfig.generateSchedule(start, end, rescheduledSaturdays);

        List<LocalDate> expected = List.of(
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 3, 3)
        );

        assertEquals(expected, schedule);
    }
}
