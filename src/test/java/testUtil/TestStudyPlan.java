package testUtil;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;

import java.util.List;

public class TestStudyPlan {
    public static StudyPlan createPlanDefault() {
        return StudyPlan.builder()
                .nameDiscipline("АППЗ")
                .namesGroupsStudent(List.of("КН-222г","КН-222б","КН-222в"))
                .scheduleService(TestScheduleService.defaultScheduleService())
                .mainEvaluation(TestMainEvaluation.defaultMainEvaluation())
                .build();
    }
    public static StudyPlan createPlanWithSaturdays() {
        return StudyPlan.builder()
                .nameDiscipline("АППЗ")
                .namesGroupsStudent(List.of("КН-222г","КН-222б","КН-222в"))
                .scheduleService(TestScheduleService.ScheduleServiceWithSaturdays())
                .mainEvaluation(TestMainEvaluation.defaultMainEvaluation())
                .build();
    }
}
