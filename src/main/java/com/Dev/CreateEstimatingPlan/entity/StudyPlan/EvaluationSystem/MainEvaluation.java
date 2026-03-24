package com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class MainEvaluation {
    private TheoryComponent theoryComponent;
    private PracticeComponent practiceComponent;
    private int perSemester;
    private int perDisciplineCreditOrExam;
    private int additionScopeForTheory;
    private int additionScopeForPractice;
//    private int totalExtraPoints;

    @Override
    public String toString() {
        return theoryComponent +
                " " +
                practiceComponent +
                "\n" +
                "За семестр " + perSemester +
                "\n" +
                "За залік/екзамен " + perDisciplineCreditOrExam +
                "\n" +
                "Додаткові за теорію " + additionScopeForTheory +
                "\n" +
                "Додаткові за практику " + additionScopeForPractice +
                "\n";
//                + "Додаткові " + totalExtraPoints +
//                "\n";
    }

    public int getSumAddition() {
        int sum = 0;

        if (theoryComponent.getList() != null) {
            sum += additionScopeForTheory;
        }

        if (practiceComponent.getTasks() != null) {
            sum += additionScopeForPractice;
        }

        return sum;
    }

    public boolean lectureIsExists() {
        return theoryComponent.lectureIsExists();
    }
    public boolean isLecture(int index) {
        return Objects.equals(theoryComponent.getList().get(index).getName(), "Лекції");
    }
    public EducationComponent getTheoryComponent(int index) {
        return theoryComponent.getList().get(index);
    }

//    public boolean checkPointsPerSemester() {
//       return theoryComponent.getScope() + practiceComponent.getScope() == perSemester;
//    }
}
