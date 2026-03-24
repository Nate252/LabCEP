package testUtil;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestMainEvaluation {
    public static EducationComponent theoryComponent() {
        return new EducationComponent("ТЕОРІЯ", 20);
    }

    public static List<EducationComponent> theorySubComponents() {
        return Arrays.asList(
                new EducationComponent("ІЗ СРС", 15),
                new EducationComponent("Лекції", 5)
        );
    }

    public static TheoryComponent Theory() {
        return new TheoryComponent(theoryComponent(), theorySubComponents());
    }

    public static EducationComponent practiceComponent() {
        return new EducationComponent("ПРАКТИКА", 60);
    }

    public static List<Tasks> practiceTasks() {
        return Arrays.asList(
                new Tasks(new EducationComponent("ЛР1", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті", "Редірект, форвард"), 4),

                new Tasks(new EducationComponent("ЛР2", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті", "Редірект, форвард"), 4),

                new Tasks(new EducationComponent("ЛР3", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті"), 4),

                new Tasks(new EducationComponent("ЛР4", 10),
                        Collections.emptyList(), 2),

                new Tasks(new EducationComponent("ЛР5", 5),
                        Collections.singletonList("Основи ВЕБ"), 2)
        );
    }

    public static PracticeComponent practice() {
        return new PracticeComponent(practiceComponent(), practiceTasks());
    }

    public static MainEvaluation defaultMainEvaluation() {
        return MainEvaluation.builder()
                .theoryComponent(Theory())
                .practiceComponent(practice())
                .perSemester(80)
                .additionScopeForPractice(10)
                .additionScopeForTheory(10)
                .perDisciplineCreditOrExam(20)
                .build();
    }
}
