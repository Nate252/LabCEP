package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.FinalEvaluationExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;
import testUtil.TestMainEvaluation;

import java.util.List;

public class TestFinalEvaluationExcel {
    public static String perSemester = ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester");
    public static String bonuses = ExcelLocaleProvider.getMessage("sheet.writer.results.header.bonuses");
    public static String perExamOrCertification = ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perEXAMOrCERTIFICATION");
    public static String result = ExcelLocaleProvider.getMessage("sheet.evaluation_structure.result");
    public static String grade = ExcelLocaleProvider.getMessage("sheet.writer.results.header.grade");

    public static FinalEvaluationExcel createFinalEvaluation(MainEvaluation mainEvaluation, SharedData sharedData) {
        return FinalEvaluationExcel.builder()
                .sharedData(sharedData)
                .studentsGroupsNames(TestScheduleFileExcel.getStudentsGroupsNames())
                .mainEvaluation(mainEvaluation)
                .build();
    }
}
