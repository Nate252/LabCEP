package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.StudentRepository;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;

    private StudentServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentServiceImpl(studentRepository);
    }

    @Test
    void getAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void canFindByGroupNames() {
        // given
        List<String> groupsNames = Arrays.asList("КН-221а", "КН-221б");
        Group group1 = new Group("КН-221а");
        Group group2 = new Group("КН-221б");
        Student studentA = new Student(
                "СтудентА",
                "СтудентА",
                "СтудентА",
                "test@gmail.com",
                group1);


        Student studentB = new Student(
                "СтудентB",
                "СтудентB",
                "СтудентB",
                "test@gmail.com",
                group2);

        when(studentRepository.findByGroupNames(groupsNames)).thenReturn(List.of(studentA, studentB));

        // when
        List<Student> result = underTest.findByGroupNames(groupsNames);

        // then
        assertThat(result).isEqualTo(Arrays.asList(studentA,studentB));
        verify(studentRepository).findByGroupNames(groupsNames);
    }

    @Test
    void shouldWillThrowExceptionWhenGroupDoesNotExist() {
        // given
        List<String> groupsNames = List.of("Невідома");
        when(studentRepository.findByGroupNames(groupsNames)).thenReturn(Collections.emptyList());

        // when/then
        assertThatThrownBy(() -> underTest.findByGroupNames(groupsNames))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("Група яка вказана в списку : " + groupsNames + " не існує в базі даних");
    }
}