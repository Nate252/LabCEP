package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class Tasks {
    private EducationComponent component;
    private List<String> subTasks;
    private int deadLine;
    @Override
    public String toString() {
        return "\n" +
                component +
                 subTasks +
                " " + deadLine;
    }
    public String getName() {
        return component.getName();
    }
    public int getScope() {
        return component.getScope();
    }
}
