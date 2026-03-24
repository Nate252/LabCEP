package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * better to create abstract class with get
 * */
@Data
@AllArgsConstructor
public class EducationComponent {
    private String name;
    private int scope;

    @Override
    public String toString() {
        return name + " " + scope;
    }
}
