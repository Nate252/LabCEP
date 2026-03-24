package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.GroupRepository;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    @Mock
    private GroupRepository groupRepository;
    private GroupServiceImpl underTest;
    @BeforeEach
    void setUp() {
        underTest = new GroupServiceImpl(groupRepository);
    }
    @Test
    void canGetAllGroups() {
        // given
        List<String> groupsNames = Arrays.asList("КН-221а", "КН-221б");
        Group group1 = new Group("КН-221а");
        Group group2 = new Group("КН-221б");

        when(groupRepository.getGroupByName(groupsNames.get(0))).thenReturn(Optional.of(group1));
        when(groupRepository.getGroupByName(groupsNames.get(1))).thenReturn(Optional.of(group2));
        when(groupRepository.findAllByNames(groupsNames)).thenReturn(List.of(group1, group2));

        // when
        List<Group> result = underTest.getAllGroups(groupsNames);

        // then
        assertThat(result).isEqualTo(Arrays.asList(group1, group2));
        verify(groupRepository).findAllByNames(groupsNames);
    }

    @Test
    void shouldWillThrowExceptionWhenGroupDoesNotExist() {
        // given
        List<String> groupsNames = Arrays.asList("КН-221а", "КН-221б");
        Group group = new Group(groupsNames.get(0));

        when(groupRepository.getGroupByName(groupsNames.get(0))).thenReturn(Optional.of(group));
        when(groupRepository.getGroupByName(groupsNames.get(1))).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> underTest.getAllGroups(groupsNames))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("Група " + groupsNames.get(1) + " не існує в базі даних");

        verify(groupRepository, never()).findAllByNames(any());
    }
}