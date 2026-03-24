package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.Colors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testUtil.CreateExcelUtil;
import testUtil.TestWriters.MainEvaluationFactory;
import testUtil.TestWriters.TestFinalEvaluationExcel;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static testUtil.TestWriters.TestFinalEvaluationExcel.*;
import static testUtil.TestWriters.TestLaboratoryWorkExcel.createLaboratoryFileExcel;
import static testUtil.TestWriters.TestListStudentsFileExcel.createListStudentsFileExcel;
import static testUtil.TestWriters.TestScheduleFileExcel.createScheduleLectureFileExcel;

public class FinalEvaluationExcelStylesDataTest {
    private static FinalEvaluationExcel underTest;
    private static Sheet testSheet;


    @BeforeAll
    static void beforeAll() {
        MainEvaluationFactory mainEvaluationFactory = new MainEvaluationFactory();

        ExcelLocaleProvider.setLocale(new Locale("uk"));

        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();

        ScheduleFileExcel scheduleFileExcel = createScheduleLectureFileExcel(listStudents.getSharedData());
        scheduleFileExcel.createExcel();

        LaboratoryWorkExcel laboratoryWorkExcel = createLaboratoryFileExcel(scheduleFileExcel.getSharedData());
        laboratoryWorkExcel.createExcel();

        underTest = TestFinalEvaluationExcel.createFinalEvaluation(mainEvaluationFactory.build(), laboratoryWorkExcel.getSharedData());
        underTest.createExcel();

        testSheet = underTest.getWorkbook().getSheetAt(3);
    }
    @Test
    void shouldReturnTrue_WhenFinalEvaluationSheetNameCorrect() {
        assertThat(testSheet.getSheetName()).isEqualTo("Підсумки");
    }

    @Test
    void shouldReturnTrue_WhenHeaderFirstDataCorrectly() {
        Row headerName = testSheet.getRow(0);
        Row headerPoints = testSheet.getRow(1);
        MainEvaluation mainEvaluation = underTest.getMainEvaluation();
        TheoryComponent theoryComponent = mainEvaluation.getTheoryComponent();
        PracticeComponent practiceComponent = mainEvaluation.getPracticeComponent();
        int counter = 4;

        //theory
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(theoryComponent.getName());
        assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(theoryComponent.getScope());
        counter++;
        for (EducationComponent component : theoryComponent.getList()) {
            assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(component.getName());
            assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(component.getScope());
            counter++;
        }

        //practice
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(practiceComponent.getName());
        assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(practiceComponent.getScope());
        counter++;
        for (Tasks tasks : practiceComponent.getTasks()) {
            assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(tasks.getName());
            assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(tasks.getScope());
            counter++;
        }
        // perSemester
        String perSemesterWithTwoAdditionPoints = perSemester + " (+ " + mainEvaluation.getSumAddition() + ")";
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(perSemesterWithTwoAdditionPoints);
        assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(mainEvaluation.getPerSemester());
        counter++;

        // bonuses
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(bonuses);
        counter++;

        // perExamOrCertification
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(perExamOrCertification);
        assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(mainEvaluation.getPerDisciplineCreditOrExam());
        counter++;

        // result
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(result);
        assertThat(headerPoints.getCell(counter).getNumericCellValue()).isEqualTo(100);
        counter++;

        // grade
        assertThat(headerName.getCell(counter).getStringCellValue()).isEqualTo(grade);
        assertThat(headerPoints.getCell(counter).getStringCellValue()).isEqualTo("5A");
        counter++;

    }

    @Test
    void shouldReturnTrue_WhenTheoryHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle styleTheory = headerFirst.getCell(4).getCellStyle();
        Font fontTheory = underTest.getWorkbook().getFontAt(styleTheory.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(styleTheory);
        Colors testColor = Colors.LIGHT_GREEN;

        assertThat(fontTheory.getBold())
                .isTrue();

        assertThat(CreateExcelUtil.getBorderStyle(styleTheory)).containsOnly(BorderStyle.THIN);

        assertThat(styleTheory.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(fontTheory.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(cellColor).isNotNull();
        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenTheoryContentStylesCorrect() {
        Row headerSecond = testSheet.getRow(2);

        CellStyle styleTheoryElement = headerSecond.getCell(5).getCellStyle();
        Font fontTheoryElement = underTest.getWorkbook().getFontAt(styleTheoryElement.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(styleTheoryElement);
        Colors testColor = Colors.PIPI;

        assertThat(fontTheoryElement.getBold())
                .isFalse();
        assertThat(fontTheoryElement.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(styleTheoryElement)).containsOnly(BorderStyle.THIN);


        assertThat(cellColor).isNotNull();
        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPracticeHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle stylePractice = headerFirst.getCell(7).getCellStyle();
        Font fontPractice = underTest.getWorkbook().getFontAt(stylePractice.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(stylePractice);
        Colors testColor = Colors.LIGHT_GREEN;

        assertThat(fontPractice.getBold())
                .isTrue();

        assertThat(fontPractice.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(stylePractice))
                .containsOnly(BorderStyle.THIN);

        assertThat(stylePractice.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPracticeContentStylesCorrect() {
        Row headerSecond = testSheet.getRow(2);

        CellStyle stylePracticeElement = headerSecond.getCell(8).getCellStyle();
        Font fontPracticeElement = underTest.getWorkbook().getFontAt(stylePracticeElement.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(stylePracticeElement);
        Colors testColor = Colors.PIPI;

        assertThat(fontPracticeElement.getBold())
                .isFalse();
        assertThat(fontPracticeElement.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(stylePracticeElement)).containsOnly(BorderStyle.THIN);


        assertThat(cellColor).isNotNull();
        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPerSemesterHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle stylePerSemester = headerFirst.getCell(12).getCellStyle();
        Font fontPerSemester = underTest.getWorkbook().getFontAt(stylePerSemester.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(stylePerSemester);
        Colors testColor = Colors.GREEN;

        assertThat(fontPerSemester.getBold())
                .isTrue();

        assertThat(fontPerSemester.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(stylePerSemester))
                .containsOnly(BorderStyle.THIN);

        assertThat(stylePerSemester.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPerBonusesHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle styleBonuses = headerFirst.getCell(13).getCellStyle();
        Font fontBonuses = underTest.getWorkbook().getFontAt(styleBonuses.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(styleBonuses);
        Colors testColor = Colors.BORROW;

        assertThat(fontBonuses.getBold())
                .isTrue();

        assertThat(fontBonuses.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(styleBonuses))
                .containsOnly(BorderStyle.THIN);

        assertThat(styleBonuses.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPerExamHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle stylePerExam = headerFirst.getCell(14).getCellStyle();
        Font fontPerExam = underTest.getWorkbook().getFontAt(stylePerExam.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(stylePerExam);
        Colors testColor = Colors.BORROW;

        assertThat(fontPerExam.getBold())
                .isTrue();

        assertThat(fontPerExam.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(stylePerExam))
                .containsOnly(BorderStyle.THIN);

        assertThat(stylePerExam.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenPerResultHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle styleResult = headerFirst.getCell(15).getCellStyle();
        Font fontResult = underTest.getWorkbook().getFontAt(styleResult.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(styleResult);
        Colors testColor = Colors.BLUE;

        assertThat(fontResult.getBold())
                .isTrue();

        assertThat(fontResult.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(styleResult))
                .containsOnly(BorderStyle.THIN);

        assertThat(styleResult.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }

    @Test
    void shouldReturnTrue_WhenGradeHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);

        CellStyle styleGrade = headerFirst.getCell(16).getCellStyle();
        Font fontGrade = underTest.getWorkbook().getFontAt(styleGrade.getFontIndex());
        XSSFColor cellColor = CreateExcelUtil.getXSSFColor(styleGrade);
        Colors testColor = Colors.BLUE;

        assertThat(fontGrade.getBold())
                .isTrue();

        assertThat(fontGrade.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(CreateExcelUtil.getBorderStyle(styleGrade))
                .containsOnly(BorderStyle.THIN);

        assertThat(styleGrade.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(cellColor).isNotNull();

        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }
}
