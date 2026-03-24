package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


@Data
public class ScheduleConfig {
    private final Map<Integer, List<DayOfWeek>> weekSchedule;

    public List<DayOfWeek> getDaysForWeek(int weekNumber) {
        return weekSchedule.getOrDefault(weekNumber, Collections.emptyList());
    }

    public List<LocalDate> generateSchedule(LocalDate start, LocalDate end,
                                            Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledDates) {

            List<LocalDate> schedule = new ArrayList<>();
            Set<LocalDate> excludedDates = new HashSet<>();

            if (rescheduledDates != null) {
                rescheduledDates.values().forEach(excludedDates::addAll);
            }

            LocalDate current = start;
            while (!current.isAfter(end)) {
                int weekNumber = ((current.getDayOfYear() - start.getDayOfYear()) / 7) % 2 == 0 ? 1 : 2;
                for (DayOfWeek day : getDaysForWeek(weekNumber)) {
                    if (current.getDayOfWeek() == day && !excludedDates.contains(current)) {
                        schedule.add(current);
                    }
                }
                current = current.plusDays(1);
            }

            if (rescheduledDates != null) {
                for (var entry : rescheduledDates.entrySet()) {
                    Map<Integer, DayOfWeek> key = entry.getKey();
                    List<LocalDate> dates = entry.getValue();

                    key.forEach((weekNumber, day) -> {
                        List<DayOfWeek> configuredDays = weekSchedule.get(weekNumber);
                        if (configuredDays != null && configuredDays.contains(day)) {
                            int occurrences = Collections.frequency(configuredDays, day);
                            schedule.addAll(Collections.nCopies(occurrences, dates).stream().flatMap(List::stream).toList());
                        }
                    });
                }
            }
            return schedule.stream().sorted().toList();
        }


    @Override
    public String toString() {
        return "\n" +
                "Розклад " + weekSchedule.toString() +
                '}' +
                "\n";
    }
}
