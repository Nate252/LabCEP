package com.Dev.CreateEstimatingPlan.repository.studentsRepository;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByGroupNames() {
        //given
        List<String> groupsNames = Arrays.asList("КН-221а", "КН-221б");

        // Create groups
        Group groupA = new Group(groupsNames.get(0));
        Group groupB = new Group(groupsNames.get(1));

        // Save groups to the database
        entityManager.persist(groupA);
        entityManager.persist(groupB);

        // Create students
        Student studentA = new Student(
                "СтудентА",
                "СтудентА",
                "СтудентА",
                "test@gmail.com",
                groupA);


        Student studentB = new Student(
                "СтудентB",
                "СтудентB",
                "СтудентB",
                "test@gmail.com",
                groupB);

        // Save students to the database
        entityManager.persist(studentA);
        entityManager.persist(studentB);

        //when
        List<Student> students = underTest.findByGroupNames(groupsNames);

        //then
        assertThat(students).isEqualTo(Arrays.asList(studentA,studentB));
    }
}