package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.LaboratoryWorkExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;

import java.util.List;

public class TestLaboratoryWorkExcel {
    public static LaboratoryWorkExcel createLaboratoryFileExcel(SharedData sharedData) {
        List<String> groupsNames = TestScheduleFileExcel.getStudentsGroupsNames();
        PracticeComponent component = TestClassDatesExcel.createPracticeComponent(
                TestClassDatesExcel.practiceTasksForClassDates());

        return LaboratoryWorkExcel.builder()
                .sharedData(sharedData)
                .studentsGroupsNames(groupsNames)
                .practiceComponent(component)
                .build();
    }
}
