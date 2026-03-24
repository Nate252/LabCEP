package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class ScheduleService {
    private LocalDate start;
    private LocalDate end;
    private ScheduleConfig lecture;
    private List<Lesson> lessons;
    private Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledDates;

    public List<LocalDate> generateLectureSchedule() {
        return lecture.generateSchedule(start, end, rescheduledDates);
    }

    public Map<Lesson, List<LocalDate>> generateLessonsSchedule() {
        Map<Lesson, List<LocalDate>> lessonsSchedule = new HashMap<>();
        for (Lesson lesson : lessons) {
            lessonsSchedule.put(lesson, lesson.getSchedule().generateSchedule(start, end, rescheduledDates));
        }
        return lessonsSchedule;
    }

    public boolean compareNumberOfGroups(List<String> groupsCompare) {
        Set<String> inputGroups = new HashSet<>(groupsCompare);

        Set<String> lessonGroups = lessons.stream()
                .flatMap(lesson -> lesson.getNameOfGroupsStudents().stream())
                .collect(Collectors.toSet());
        return lessonGroups.equals(inputGroups);
    }

//    @Override
//    public String toString() {
//        return "\n" +
//                "Початок семестру " + start + "\n" +
//                "Кінець семестру " + end + "\n" +
//                lecture + "\n" +
//                lessons + "\n";
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleService that = (ScheduleService) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(lecture, that.lecture) && Objects.equals(lessons, that.lessons) && Objects.equals(rescheduledDates, that.rescheduledDates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, lecture, lessons, rescheduledDates);
    }
}
