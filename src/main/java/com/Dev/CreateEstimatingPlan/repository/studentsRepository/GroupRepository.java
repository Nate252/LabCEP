package com.Dev.CreateEstimatingPlan.repository.studentsRepository;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.students WHERE g.name IN :groupNames")
    List<Group> findAllByNames(@Param("groupNames") List<String> groupNames);
    Optional<Group> getGroupByName(String name);
}
