package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class LessonTest {

    private ScheduleConfig schedule;
    private Teacher teacher;
    private List<String> groups;

    @BeforeEach
    void setUp() {
        schedule = null;
        teacher = null;
        groups = List.of("КН-221а", "КН-221б");
    }

    @Test
    void testGetNameOfGroupsStudentsString_directly() {
        Lesson lesson = new Lesson(schedule, groups, teacher) {
            @Override
            public String getNameOfGroupsStudentsFileName() {
                return "unused";
            }
        };

        assertThat((lesson).getNameOfGroupsStudentsString())
                .isEqualTo("КН-221а КН-221б ");
    }

    @Test
    void getNameOfGroupsStudentsFileNameLaboratory() {
        Laboratory laboratory = new Laboratory(schedule, groups, teacher);

        try (MockedStatic<ExcelLocaleProvider> mocked = mockStatic(ExcelLocaleProvider.class)) {
            mocked.when(() -> ExcelLocaleProvider.getMessage("sheet.writer.laboratory.lw"))
                    .thenReturn("ЛР");

            String result = laboratory.getNameOfGroupsStudentsFileName();
            assertThat(result).isEqualTo("ЛР КН-221а КН-221б ");
        }
    }

    @Test
    void getNameOfGroupsStudentsFileNamePractice() {
        Practice practice = new Practice(schedule, groups, teacher);

        try (MockedStatic<ExcelLocaleProvider> mocked = mockStatic(ExcelLocaleProvider.class)) {
            mocked.when(() -> ExcelLocaleProvider.getMessage("sheet.writer.laboratory.pw"))
                    .thenReturn("ПР");

            String result = practice.getNameOfGroupsStudentsFileName();
            assertThat(result).isEqualTo("ПР КН-221а КН-221б ");
        }
    }
}