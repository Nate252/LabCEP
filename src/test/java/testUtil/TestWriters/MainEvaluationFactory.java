package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import org.w3c.dom.ls.LSException;
import testUtil.TestMainEvaluation;

import java.util.Arrays;
import java.util.List;

public class MainEvaluationFactory {
    private int theoryPoints = 20;
    private int practicePoints = 55;
    private int perSemester = 75;
    private int perExam = 20;
    private int theoryBonus = 10;
    private int practiceBonus = 10;

    private TheoryComponent theoryComponent = new TheoryComponent(
            new EducationComponent("ТЕОРІЯ",theoryPoints),
            TestMainEvaluation.theorySubComponents());
    private PracticeComponent practiceComponent = new PracticeComponent(
            new EducationComponent("ПРАКТИКА", practicePoints),
            TestClassDatesExcel.practiceTasksForClassDates());


    public MainEvaluationFactory withoutTheoryComponent() {
        this.theoryComponent = new TheoryComponent(new EducationComponent("ТЕОРІЯ",0),null);
        return this;
    }
    public MainEvaluationFactory withoutLecture() {
        List<EducationComponent> list = TestMainEvaluation.theorySubComponents();
        list.get(1).setName("Тест");
        this.theoryComponent = new TheoryComponent(new EducationComponent("ТЕОРІЯ",20),list);
        return this;
    }
    public MainEvaluationFactory withoutPracticeComponent() {
        this.practiceComponent = new PracticeComponent(new EducationComponent("ПРАКТИКА", 0),null);
        return this;
    }


    public MainEvaluationFactory withTheoryBonus(int bonus) {
        this.theoryBonus = bonus;
        return this;
    }

    public MainEvaluationFactory withPracticeBonus(int bonus) {
        this.practiceBonus = bonus;
        return this;
    }
    public MainEvaluationFactory withPerSemester(int points) {
        this.perSemester = points;
        return this;
    }

    public MainEvaluation build() {
        return MainEvaluation.builder()
                .theoryComponent(theoryComponent)
                .practiceComponent(practiceComponent)
                .perSemester(perSemester)
                .perDisciplineCreditOrExam(perExam)
                .additionScopeForTheory(theoryBonus)
                .additionScopeForPractice(practiceBonus)
                .build();
    }

}

