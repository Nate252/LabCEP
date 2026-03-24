package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {
    static class TestLesson extends Lesson {
        public TestLesson(List<String> nameOfGroupsStudents) {
            super(null, nameOfGroupsStudents, null);
        }

        @Override
        public String getNameOfGroupsStudentsFileName() {
            return null;
        }
    }

    @Test
    void canGenerateLessonsSchedule() {
        LocalDate start = LocalDate.of(2025, 3, 3);
        LocalDate end = LocalDate.of(2025, 3, 14);
        Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledDates = new HashMap<>();

        ScheduleConfig mockSchedule1 = mock(ScheduleConfig.class);
        ScheduleConfig mockSchedule2 = mock(ScheduleConfig.class);

        List<LocalDate> mockedDates1 = List.of(LocalDate.of(2025, 3, 3), LocalDate.of(2025, 3, 10));
        List<LocalDate> mockedDates2 = List.of(LocalDate.of(2025, 3, 4), LocalDate.of(2025, 3, 11));

        when(mockSchedule1.generateSchedule(start, end, rescheduledDates)).thenReturn(mockedDates1);
        when(mockSchedule2.generateSchedule(start, end, rescheduledDates)).thenReturn(mockedDates2);

        Lesson lesson1 = new Laboratory(mockSchedule1, List.of("КН-221а"), null);
        Lesson lesson2 = new Laboratory(mockSchedule2, List.of("КН-221б"), null);

        ScheduleService service = ScheduleService.builder()
                .start(start)
                .end(end)
                .lessons(List.of(lesson1, lesson2))
                .rescheduledDates(rescheduledDates)
                .build();

        Map<Lesson, List<LocalDate>> result = service.generateLessonsSchedule();

        assertThat(result.get(lesson1)).isEqualTo(mockedDates1);
        assertThat(result.get(lesson2)).isEqualTo(mockedDates2);

        verify(mockSchedule1).generateSchedule(start, end, rescheduledDates);
        verify(mockSchedule2).generateSchedule(start, end, rescheduledDates);
    }

    @Test
    void generateLectureSchedule() {
        LocalDate start = LocalDate.of(2025, 3, 3);
        LocalDate end = LocalDate.of(2025, 3, 14);
        Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledDates = new HashMap<>();

        ScheduleConfig mockSchedule = mock(ScheduleConfig.class);
        List<LocalDate> expectedDates = List.of(LocalDate.of(2025, 3, 3), LocalDate.of(2025, 3, 10));

        when(mockSchedule.generateSchedule(start, end, rescheduledDates)).thenReturn(expectedDates);

        ScheduleService service = ScheduleService.builder()
                .start(start)
                .end(end)
                .lecture(mockSchedule)
                .rescheduledDates(rescheduledDates)
                .build();

        List<LocalDate> result = service.generateLectureSchedule();

        assertThat(result).isEqualTo(expectedDates);
        verify(mockSchedule).generateSchedule(start, end, rescheduledDates);
    }

    @Test
    void compareNumberOfGroups_True_GroupsAreEqual() {
        List<String> groups = List.of("КН-221а", "КН-221б", "КН-221в", "КН-221г");
        List<Lesson> lessons = List.of(
                new TestLesson(List.of("КН-221а", "КН-221б")),
                new TestLesson(List.of("КН-221в", "КН-221г"))
        );

        ScheduleService service = ScheduleService.builder()
                .lessons(lessons)
                .build();

        assertThat(service.compareNumberOfGroups(groups)).isTrue();
    }

    @Test
    void compareNumberOfGroups_False_LessonHasAnExtraGroup() {
        List<String> groups = List.of("КН-221а", "КН-221б", "КН-221в", "КН-221г", "КН-221д");
        List<Lesson> lessons = List.of(
                new TestLesson(List.of("КН-221а", "КН-221б")),
                new TestLesson(List.of("КН-221в", "КН-221г"))
        );

        ScheduleService service = ScheduleService.builder()
                .lessons(lessons)
                .build();

        assertThat(service.compareNumberOfGroups(groups)).isFalse();
    }


    @Test
    void compareNumberOfGroups_False_whenLessonContainsGroupNotInInputList() {
        List<String> groups = List.of("КН-221а", "КН-221б", "КН-221в", "КН-221г");
        List<Lesson> lessons = List.of(
                new TestLesson(List.of("КН-221а", "КН-221б")),
                new TestLesson(List.of("КН-221в", "КН-221д"))
        );

        ScheduleService service = ScheduleService.builder()
                .lessons(lessons)
                .build();

        assertThat(service.compareNumberOfGroups(groups)).isFalse();
    }
}
