package com.Dev.CreateEstimatingPlan.repository.studentsRepository;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GroupRepositoryTest {
    @Autowired
    private GroupRepository underTest;

    @Autowired
    private TestEntityManager entityManager;
    @Test
    void findAllByNames() {
        //given
        List<String> groupsNames = Arrays.asList("КН-221а", "КН-221б");

        Group groupA = new Group(groupsNames.get(0));
        Group groupB = new Group(groupsNames.get(1));

        entityManager.persist(groupA);
        entityManager.persist(groupB);

        //when
        List<Group> groups = underTest.findAllByNames(groupsNames);

        //then
        assertThat(groups).isEqualTo(Arrays.asList(groupA,groupB));
    }

    @Test
    void getGroupByName() {
        String name = "КН-221в";

        Group group = new Group(name);
        entityManager.persist(group);

        Optional<Group> group2 = underTest.getGroupByName(name);
        assertThat(group2.isEmpty()).isFalse();
    }
}