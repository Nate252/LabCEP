package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleService;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ScheduleFileExcel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtil.TestWriters.StudyPlanFactory;
import testUtil.TestWriters.TestFileExcelCreator;
import testUtil.TestWriters.TestListStudentsFileExcel;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileExcelCreatorTest {

    private FileExcelCreator fileExcelCreator;

    private static List<Group> groups;


    @BeforeEach
    void setUp() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
        groups = TestListStudentsFileExcel.createGroups();
    }

    @Test
    void shouldReturnTrue_WhenFullCreationCorrect() {
        StudyPlan studyPlan = new StudyPlanFactory()
                .mainEvaluationFull()
                .scheduleServiceFull()
                .build();

        fileExcelCreator = new FileExcelCreator(studyPlan);
        fileExcelCreator.createExcel(groups);

        Workbook workbook = fileExcelCreator.getSharedData().getWorkbook();
        ScheduleService service = fileExcelCreator.getStudyPlan().getScheduleService();
        String sheetLabFirstName = service.getLessons().get(0).getNameOfGroupsStudentsFileName();
        String sheetLabSecondName =service.getLessons().get(1).getNameOfGroupsStudentsFileName();


        assertThat(workbook.getSheetAt(0)).isNotEqualTo(TestFileExcelCreator.sheetListStudentsName);
        assertThat(workbook.getSheetAt(1)).isNotEqualTo(TestFileExcelCreator.sheetLectureName);
        assertThat(workbook.getSheetAt(2)).isNotEqualTo(sheetLabFirstName);
        assertThat(workbook.getSheetAt(3)).isNotEqualTo(sheetLabSecondName);
        assertThat(workbook.getSheetAt(4)).isNotEqualTo(TestFileExcelCreator.sheetClassDatesName);
        assertThat(workbook.getSheetAt(5)).isNotEqualTo(TestFileExcelCreator.sheetLaboratoryName);
        assertThat(workbook.getSheetAt(6)).isNotEqualTo(TestFileExcelCreator.sheetResultsName);
    }

    @Test
    void shouldReturnTrue_WhenLectureIsNotExists() {
        StudyPlan studyPlan = new StudyPlanFactory()
                .scheduleServiceWithoutLecture()
                .mainEvaluationWithoutLecture()
                .build();
        System.out.println(studyPlan);
        fileExcelCreator = new FileExcelCreator(studyPlan);
        fileExcelCreator.createExcel(groups);

        for (Sheet sheet : fileExcelCreator.getSharedData().getWorkbook()) {
            assertThat(sheet).isNotEqualTo(TestFileExcelCreator.sheetLectureName);
        }
    }
    @Test
    void shouldReturnTrue_WhenPracticeIsNotExists() {
        StudyPlan studyPlan = new StudyPlanFactory()
                .scheduleServiceWithoutLessons()
                .mainEvaluationWithoutLessonsPractice()
                .build();

        String firstGroup = "ЛР КН-222в ";
        String secondGroup = "ЛР КН-222г КН-222б ";

        fileExcelCreator = new FileExcelCreator(studyPlan);
        fileExcelCreator.createExcel(groups);

        for (Sheet sheet : fileExcelCreator.getSharedData().getWorkbook()) {
            assertThat(sheet).isNotEqualTo(TestFileExcelCreator.sheetLaboratoryName);
            assertThat(sheet).isNotEqualTo(TestFileExcelCreator.sheetClassDatesName);
            assertThat(sheet).isNotEqualTo(firstGroup);
            assertThat(sheet).isNotEqualTo(secondGroup);
        }
    }


}