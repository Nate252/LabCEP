package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.GroupRepository;
import com.Dev.CreateEstimatingPlan.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository repository;

    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Group> getAllGroups(List<String> names) {
        for (String name: names) {
            Optional<Group> group = repository.getGroupByName(name);
            group.orElseThrow(() -> new GroupNotFoundException("Група " + name + " не існує в базі даних"));
        }
        return repository.findAllByNames(names);
    }
}
