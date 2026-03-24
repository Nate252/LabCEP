package com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleService;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudyPlan {
    private MainEvaluation mainEvaluation;
    private ScheduleService scheduleService;
    private String nameDiscipline;
    private List<String> namesGroupsStudent;

    @Override
    public String toString() {
        return  nameDiscipline +  "\n" +
                namesGroupsStudent + "\n" +
                mainEvaluation + "\n" +
                scheduleService + "\n";
    }
}
