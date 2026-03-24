package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/*
* better to create abstract class with get
* */
@Data
@AllArgsConstructor
public class PracticeComponent {
    private EducationComponent component;
    private List<Tasks> tasks;


    @Override
    public String toString() {
        return  "\n" +
                component +
                "\n" +
                 tasks;
    }
    public String getName() {
        return component.getName();
    }
    public int getScope() {
        return component.getScope();
    }
    public boolean isDeadlineEmpty() {
        return tasks == null || tasks.isEmpty() || tasks.get(0).getDeadLine() == 0;
    }

    public int getSumDeadlines() {
        return tasks.stream()
                .mapToInt(Tasks::getDeadLine)
                .sum();
    }
    public int getSumOfPracticePoints() {
        return tasks != null ? tasks.stream()
                .mapToInt(Tasks::getScope)
                .sum() : 0;
    }

    public boolean checkDeadlinesZero() {
        return getSumDeadlines() > 0 && tasks.stream().anyMatch(task -> task.getDeadLine() == 0);
    }
    public boolean compareSumPractice() {
        return getSumOfPracticePoints() == getScope();
    }
    public int getSizeTasks() {
        return tasks.size();
    }
}
