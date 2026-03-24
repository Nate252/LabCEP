package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtil.TestWriters.MainEvaluationFactory;
import testUtil.TestWriters.TestFinalEvaluationExcel;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static testUtil.TestWriters.TestLaboratoryWorkExcel.createLaboratoryFileExcel;
import static testUtil.TestWriters.TestListStudentsFileExcel.createListStudentsFileExcel;
import static testUtil.TestWriters.TestScheduleFileExcel.createScheduleLectureFileExcel;

class FinalEvaluationExcelFormulasTest {
    private FinalEvaluationExcel underTest;
    private Sheet testSheet;

    @BeforeAll
    static void beforeAll() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
    }


    void setUp(MainEvaluation mainEvaluation) {
        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();

        ScheduleFileExcel scheduleFileExcel = createScheduleLectureFileExcel(listStudents.getSharedData());
        scheduleFileExcel.createExcel();

        LaboratoryWorkExcel laboratoryWorkExcel = createLaboratoryFileExcel(scheduleFileExcel.getSharedData());
        laboratoryWorkExcel.createExcel();


        underTest = TestFinalEvaluationExcel.createFinalEvaluation(mainEvaluation, laboratoryWorkExcel.getSharedData());
        underTest.createExcel();
        testSheet = underTest.getWorkbook().getSheetAt(3);
    }

    @Test
    void shouldReturnTrue_WhenLectureFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .build();
        setUp(mainEvaluation);


        Sheet lecture = underTest.getWorkbook().getSheet("Лекції");
        lecture.getRow(3).getCell(4).setCellValue(1);

        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        assertThat(evaluator.evaluate(testSheet.getRow(2).getCell(6)).getNumberValue()).isEqualTo(1.3);
    }
    @Test
    void shouldReturnTrue_WhenLectureFormulaLocaleEnCorrect() {
        ExcelLocaleProvider.setLocale(Locale.ENGLISH);

        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .build();
        setUp(mainEvaluation);


        Sheet lecture = underTest.getWorkbook().getSheet("Lectures");
        lecture.getRow(3).getCell(4).setCellValue(1);

        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        assertThat(evaluator.evaluate(testSheet.getRow(2).getCell(6)).getNumberValue()).isEqualTo(1.3);
        beforeAll();
    }
    @Test
    void shouldReturnTrue_WhenSumTheoryWithAdditionPointsFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .build();
        setUp(mainEvaluation);


        testSheet.getRow(2).getCell(5).setCellValue(15);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        Sheet lecture = underTest.getWorkbook().getSheet("Лекції");
        lecture.getRow(3).getCell(4).setCellValue(1);
        lecture.getRow(3).getCell(5).setCellValue(1);
        lecture.getRow(3).getCell(6).setCellValue(1);
        lecture.getRow(3).getCell(7).setCellValue(1);

        evaluator.evaluate(testSheet.getRow(2).getCell(6));


        assertThat(evaluator.evaluate(testSheet.getRow(2).getCell(4)).getNumberValue()).isEqualTo(30);
    }

    @Test
    void shouldReturnTrue_WhenSumTheoryWithoutAdditionPointsFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withTheoryBonus(0)
                .build();
        setUp(mainEvaluation);

        testSheet.getRow(2).getCell(5).setCellValue(15);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        Sheet lecture = underTest.getWorkbook().getSheet("Лекції");
        lecture.getRow(3).getCell(4).setCellValue(1);
        lecture.getRow(3).getCell(5).setCellValue(1);
        lecture.getRow(3).getCell(6).setCellValue(1);
        lecture.getRow(3).getCell(7).setCellValue(1);

        evaluator.evaluate(testSheet.getRow(2).getCell(6));


        assertThat(evaluator.evaluate(testSheet.getRow(2).getCell(4)).getNumberValue()).isEqualTo(20);

    }

    @Test
    void shouldReturnTrue_WhenPracticeFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .build();
        setUp(mainEvaluation);

        Sheet laboratory = underTest.getWorkbook().getSheet("Лабораторні");
        laboratory.getRow(2).getCell(4).setCellValue(1.0);

        Cell cell = testSheet.getRow(2).getCell(8);
        underTest.getWorkbook().getCreationHelper().createFormulaEvaluator().evaluateAll();

        assertThat(cell.getErrorCellValue()).isEqualTo((byte) 15);
    }

    @Test
    void shouldReturnTrue_WhenSumPracticeWithAdditionPointsFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .build();
        setUp(mainEvaluation);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        testSheet.getRow(2).getCell(8).removeFormula();
        testSheet.getRow(2).getCell(9).removeFormula();
        testSheet.getRow(2).getCell(10).removeFormula();
        testSheet.getRow(2).getCell(11).removeFormula();

        testSheet.getRow(2).getCell(8).setCellValue(15);
        testSheet.getRow(2).getCell(9).setCellValue(15);
        testSheet.getRow(2).getCell(10).setCellValue(15);
        testSheet.getRow(2).getCell(11).setCellValue(10);

        Cell cellPracticeSum = testSheet.getRow(2).getCell(7);

        assertThat(evaluator.evaluate(cellPracticeSum).getNumberValue()).isEqualTo(65);
    }

    @Test
    void shouldReturnTrue_WhenPracticeWithoutAdditionPointsFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withPracticeBonus(0)
                .build();
        setUp(mainEvaluation);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        testSheet.getRow(2).getCell(8).removeFormula();
        testSheet.getRow(2).getCell(9).removeFormula();
        testSheet.getRow(2).getCell(10).removeFormula();
        testSheet.getRow(2).getCell(11).removeFormula();

        testSheet.getRow(2).getCell(8).setCellValue(15);
        testSheet.getRow(2).getCell(9).setCellValue(15);
        testSheet.getRow(2).getCell(10).setCellValue(15);
        testSheet.getRow(2).getCell(11).setCellValue(10);

        Cell cellPracticeSum = testSheet.getRow(2).getCell(7);

        assertThat(evaluator.evaluate(cellPracticeSum).getNumberValue()).isEqualTo(55);
    }

    @Test
    void shouldReturnTrue_WhenPerSemesterFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withPracticeBonus(0)
                .withTheoryBonus(0)
                .build();
        setUp(mainEvaluation);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        Cell cellPracticeSum = testSheet.getRow(2).getCell(7);
        cellPracticeSum.removeFormula();
        cellPracticeSum.setCellValue(55);
        Cell cellTheorySum = testSheet.getRow(2).getCell(4);
        cellTheorySum.removeFormula();
        cellTheorySum.setCellValue(20);

        Cell perSemester = testSheet.getRow(2).getCell(12);
        assertThat(evaluator.evaluate(perSemester).getNumberValue()).isEqualTo(75);

    }
    @Test
    void shouldReturnTrue_WhenResultFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withPracticeBonus(0)
                .withTheoryBonus(0)
                .build();
        setUp(mainEvaluation);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        Cell perSemester = testSheet.getRow(2).getCell(12);
        perSemester.removeFormula();
        perSemester.setCellValue(75);

        Cell bonuses = testSheet.getRow(2).getCell(13);
        bonuses.setCellValue(5);

        Cell exam = testSheet.getRow(2).getCell(14);
        exam.setCellValue(15);

        Cell result = testSheet.getRow(2).getCell(15);

        assertThat(evaluator.evaluate(result).getNumberValue()).isEqualTo(95);
    }
    @Test
    void shouldReturnTrue_WhenGradeFormulaCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withPracticeBonus(0)
                .withTheoryBonus(0)
                .build();
        setUp(mainEvaluation);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        Cell result = testSheet.getRow(2).getCell(15);
        result.removeFormula();
        result.setCellValue(95);

        Cell grade = testSheet.getRow(2).getCell(16);


        assertThat(evaluator.evaluate(grade).getStringValue()).isEqualTo("5A");
    }
    @Test
    void shouldReturnTrue_WhenFinalEvaluationWithoutTheoryCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withoutTheoryComponent()
                .withTheoryBonus(0)
                .withPerSemester(55)
                .build();
        setUp(mainEvaluation);

        assertThat(testSheet.getRow(0).getCell(4).getStringCellValue())
                .isEqualTo(mainEvaluation.getPracticeComponent().getName());
    }

    @Test
    void shouldReturnTrue_WhenFinalEvaluationWithoutPracticeCorrect() {
        MainEvaluation mainEvaluation = new MainEvaluationFactory()
                .withoutPracticeComponent()
                .withPracticeBonus(0)
                .withPerSemester(20)
                .build();
        setUp(mainEvaluation);

        assertThat(testSheet.getRow(0).getCell(4).getStringCellValue())
                .isEqualTo(mainEvaluation.getTheoryComponent().getName());
    }

}