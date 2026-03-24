package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(
                "Максим",
                "Петренко",
                "Андрійович",
                null,
                null,
                null
        );
    }

    @Test
    void getFIO() {
        //given
        String expected = "Петренко Максим Андрійович";
        //when
        String result = teacher.getFIO();
        //then
        assertThat(result).isEqualTo(expected);
    }
}