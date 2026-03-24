package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TheoryComponentTest {
    @Test
    void canGetNameAndScope() {
        //given
        EducationComponent mockComponent = mock(EducationComponent.class);
        when(mockComponent.getName()).thenReturn("Theory");
        when(mockComponent.getScope()).thenReturn(10);

        //when
        TheoryComponent theory = new TheoryComponent(mockComponent, List.of());

        //then
        assertEquals("Theory", theory.getName());
        assertEquals(10, theory.getScope());
    }

    @Test
    void canGetSumOfTheoryPoints() {
        //given
        EducationComponent firstComp = new EducationComponent("Лекції", 15);
        EducationComponent secondComp = new EducationComponent("Тест", 5);
        EducationComponent theoryComponent = new EducationComponent("Теорія", 20);

        TheoryComponent theory = new TheoryComponent(theoryComponent, List.of(firstComp, secondComp));
        assertEquals(20, theory.getSumOfTheoryPoints());
    }
    @Test
    void GetSumOfTheoryPointsIsZero() {
        //given
        EducationComponent theoryComponent = new EducationComponent("Теорія", 20);

        TheoryComponent theory = new TheoryComponent(theoryComponent, Collections.emptyList());
        assertEquals(0, theory.getSumOfTheoryPoints());
    }

    @Test
    void compareSumTheoryIsTrue() {
        EducationComponent firstComp = new EducationComponent("Лекції", 15);
        EducationComponent secondComp = new EducationComponent("Тест", 5);
        EducationComponent theoryComponent = new EducationComponent("Теорія", 20);

        TheoryComponent theory = new TheoryComponent(theoryComponent, List.of(firstComp, secondComp));
        assertTrue(theory.compareSumTheory());
    }

    @Test
    void compareSumTheoryIsFalse() {
        EducationComponent firstComp = new EducationComponent("Лекції", 15);
        EducationComponent secondComp = new EducationComponent("Тест", 5);
        EducationComponent theoryComponent = new EducationComponent("Теорія", 1);

        TheoryComponent theory = new TheoryComponent(theoryComponent, List.of(firstComp, secondComp));
        assertFalse(theory.compareSumTheory());
    }

    @Test
    void testLectureIsExistsIsTrue() {
        EducationComponent firstComp = new EducationComponent("Лекції", 15);
        EducationComponent theoryComponent = new EducationComponent("Теорія", 15);
        TheoryComponent theory = new TheoryComponent(theoryComponent, List.of(firstComp));

        try (var mocked = mockStatic(ExcelLocaleProvider.class)) {
            mocked.when(() -> ExcelLocaleProvider.getMessage("name.component.lecture"))
                    .thenReturn("Лекції");

            assertTrue(theory.lectureIsExists());
        }
    }

    @Test
    void testLectureIsExistsWithNoLectureElIsFalse() {
        EducationComponent nonLec = new EducationComponent("Тест", 5);
        EducationComponent theoryComponent = new EducationComponent("Теорія", 5);
        TheoryComponent theory = new TheoryComponent(theoryComponent, List.of(nonLec));

        try (var mocked = mockStatic(ExcelLocaleProvider.class)) {
            mocked.when(() -> ExcelLocaleProvider.getMessage("name.component.lecture"))
                    .thenReturn("Лекції");

            assertFalse(theory.lectureIsExists());
        }
    }
    @Test
    void testLectureIsExistsWithEmptyListIsFalse() {

        EducationComponent theoryComponent = new EducationComponent("Теорія", 5);
        TheoryComponent theory = new TheoryComponent(theoryComponent,Collections.emptyList());

        try (var mocked = mockStatic(ExcelLocaleProvider.class)) {
            mocked.when(() -> ExcelLocaleProvider.getMessage("name.component.lecture"))
                    .thenReturn("Лекції");

            assertFalse(theory.lectureIsExists());
        }
    }
}