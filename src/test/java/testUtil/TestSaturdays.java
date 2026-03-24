package testUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSaturdays {
    public static Map<Map<Integer, DayOfWeek>, List<LocalDate>> saturdays() {
        Map<Map<Integer, DayOfWeek>, List<LocalDate>> schedule = new HashMap<>();

        schedule.put(Map.of(1, DayOfWeek.THURSDAY), List.of(
                LocalDate.of(2024, 11, 2)
        ));
        schedule.put(Map.of(2, DayOfWeek.THURSDAY), List.of(
                LocalDate.of(2024, 9, 28),
                LocalDate.of(2024, 12, 2)
        ));
        schedule.put(Map.of(1, DayOfWeek.FRIDAY), List.of(
                LocalDate.of(2024, 11, 9)
        ));
        schedule.put(Map.of(1, DayOfWeek.MONDAY), List.of(
                LocalDate.of(2024, 10, 12)
        ));
        schedule.put(Map.of(2, DayOfWeek.WEDNESDAY), List.of(
                LocalDate.of(2024, 9, 21),
                LocalDate.of(2024, 11, 30)
        ));
        schedule.put(Map.of(1, DayOfWeek.WEDNESDAY), List.of(
                LocalDate.of(2024, 10, 26)
        ));
        schedule.put(Map.of(2, DayOfWeek.FRIDAY), List.of(
                LocalDate.of(2024, 10, 5),
                LocalDate.of(2024, 12, 3)
        ));
        schedule.put(Map.of(2, DayOfWeek.MONDAY), List.of(
                LocalDate.of(2024, 9, 7),
                LocalDate.of(2024, 11, 16)
        ));
        schedule.put(Map.of(2, DayOfWeek.TUESDAY), List.of(
                LocalDate.of(2024, 9, 14),
                LocalDate.of(2024, 10, 19),
                LocalDate.of(2024, 11, 23)
        ));
        return schedule;
    }
}
