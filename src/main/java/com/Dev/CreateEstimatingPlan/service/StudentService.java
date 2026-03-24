package com.Dev.CreateEstimatingPlan.service;

import com.Dev.CreateEstimatingPlan.entity.students.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    List<Student> findByGroupNames(List<String> groupNames);
}
