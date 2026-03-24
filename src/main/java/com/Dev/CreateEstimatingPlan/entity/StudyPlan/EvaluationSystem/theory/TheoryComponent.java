package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class TheoryComponent {
    private EducationComponent component;
    private List<EducationComponent> list;

    @Override
    public String toString() {
        return "\n" +
                component +
                "\n" +
                list;
    }

    public String getName() {
        return component.getName();
    }
    public int getScope() {
        return component.getScope();
    }
    public int getSumOfTheoryPoints() {
        return list != null ? list.stream()
                .mapToInt(EducationComponent::getScope)
                .sum() : 0;
    }
    public boolean compareSumTheory() {
        return getSumOfTheoryPoints() == getScope();
    }
    public boolean lectureIsExists() {
        String sheetName = ExcelLocaleProvider.getMessage("name.component.lecture");
        return list != null && list.stream()
                .anyMatch(lecture -> lecture.getName().equalsIgnoreCase(sheetName));
    }
}
