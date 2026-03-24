package com.Dev.CreateEstimatingPlan.service;

import com.Dev.CreateEstimatingPlan.entity.students.Group;

import java.util.List;

public interface GroupService {
    List<Group> getAllGroups(List<String> names);
}
