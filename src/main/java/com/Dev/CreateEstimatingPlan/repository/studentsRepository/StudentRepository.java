package com.Dev.CreateEstimatingPlan.repository.studentsRepository;

import com.Dev.CreateEstimatingPlan.entity.students.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query("SELECT s FROM Student s WHERE s.group.name IN :groupNames")
    List<Student> findByGroupNames(@Param("groupNames") List<String> groupNames);
}
