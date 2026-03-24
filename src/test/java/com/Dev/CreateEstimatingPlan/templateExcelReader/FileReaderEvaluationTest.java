package com.Dev.CreateEstimatingPlan.templateExcelReader;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtil.TestMainEvaluation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FileReaderEvaluationTest {
    private Sheet testSheet;
    private FileReaderEvaluation fileReaderEvaluation;

    private MainEvaluation mainEvaluation;

    @BeforeEach
    void setUp() {
        ExcelLocaleProvider.setLocale(Locale.forLanguageTag("uk"));
        mainEvaluation = TestMainEvaluation.defaultMainEvaluation();

        try (InputStream fileInputStream = getClass().getClassLoader().
                getResourceAsStream("test-files/ПланВивченняНД_uk.xlsm")) {
            assert fileInputStream != null;


            Workbook workbook = WorkbookFactory.create(fileInputStream);
            testSheet = workbook.getSheetAt(2);

            fileReaderEvaluation = new FileReaderEvaluation(testSheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnTrue_WhenCompletelyFilled() {
        MainEvaluation testMainEvaluation = fileReaderEvaluation.readEducationComponentMain();

        assertThat(testMainEvaluation).isEqualTo(mainEvaluation);
    }

    @Test
    void shouldReturnTrue_WithoutTheory() {
        Row rowTheory = testSheet.getRow(1);
        rowTheory.getCell(1).setCellValue(0);

        testSheet.getRow(22).getCell(1).setCellValue(60);

        mainEvaluation.getTheoryComponent().setList(null);
        mainEvaluation.getTheoryComponent().getComponent().setScope(0);


        MainEvaluation testMainEvaluation = fileReaderEvaluation.readEducationComponentMain();

        assertThat(testMainEvaluation.getTheoryComponent()).isEqualTo(mainEvaluation.getTheoryComponent());
    }

    @Test
    void shouldReturnTrue_WithoutPractice() {
        Row rowPractice = testSheet.getRow(4);
        rowPractice.getCell(1).setCellValue(0);

        testSheet.getRow(22).getCell(1).setCellValue(20);

        mainEvaluation.getPracticeComponent().setTasks(null);
        mainEvaluation.getPracticeComponent().getComponent().setScope(0);


        MainEvaluation testMainEvaluation = fileReaderEvaluation.readEducationComponentMain();

        assertThat(testMainEvaluation.getPracticeComponent()).isEqualTo(mainEvaluation.getPracticeComponent());
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheorySumPointsInvalid() {
        Row rowTheory = testSheet.getRow(1);
        rowTheory.getCell(1).setCellValue(1);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Сумма балів елементів які входять " +
                        "до розділу ТЕОРІЯ не дорівнює загальним балам за ТЕОРІЯ");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeSumPointsInvalid() {
        Row rowTheory = testSheet.getRow(4);
        rowTheory.getCell(1).setCellValue(1);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Сумма балів елементів які входять " +
                        "до розділу ПРАКТИКА не дорівнює загальним балам за ПРАКТИКА");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryElementsSumPointsInvalid() {
        Row rowTheory = testSheet.getRow(2);
        rowTheory.getCell(1).setCellValue(10);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Сумма балів елементів які входять " +
                        "до розділу ТЕОРІЯ не дорівнює загальним балам за ТЕОРІЯ");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeElementsSumPointsInvalid() {
        Row rowTheory = testSheet.getRow(5);
        rowTheory.getCell(1).setCellValue(10);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Сумма балів елементів які входять " +
                        "до розділу ПРАКТИКА не дорівнює загальним балам за ПРАКТИКА");
    }


    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryPointsIsNegative() {
        Row rowTheory = testSheet.getRow(1);
        rowTheory.getCell(1).setCellValue(-1);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Некоректно задані бали");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryPointsIsString() {
        Row rowTheory = testSheet.getRow(1);
        rowTheory.getCell(1).setCellValue("rock");

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Некоректно задані бали");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryElementWithZeroPoints() {
        Row rowTheory = testSheet.getRow(2);
        rowTheory.getCell(1).setCellValue(0);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Некоректно задані бали");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeElementWithoutPrefix() {
        Row rowPractice = testSheet.getRow(5);
        rowPractice.getCell(0).setCellValue("ЛР1");

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Неправильно задано назву практичного завдання");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeElementTaskWithoutPrefix() {
        Row rowPractice = testSheet.getRow(6);
        rowPractice.getCell(0).setCellValue("2");

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Пропущено назву лабораторної роботи або завдання");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryPointsCellIsBlank() {
        Row rowTheory = testSheet.getRow(2);
        Cell cell = rowTheory.getCell(1);
        cell.setBlank();

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessageContaining("Некоректно задані бали");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeSubTaskIsBlank() {
        Row rowSubTask = testSheet.getRow(6);
        Cell cell = rowSubTask.getCell(1);
        cell.setBlank();

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Пропущено назву опису завдання");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeElementCellNameIsBlank() {
        Row practiceElementTask = testSheet.getRow(5);
        Cell cell = practiceElementTask.getCell(0);
        cell.setBlank();

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано назву практичного завдання");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTheoryElementCellNameIsBlank() {
        Row rowTheoryElement = testSheet.getRow(2);
        Cell cell = rowTheoryElement.getCell(0);
        cell.setBlank();

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Пропущено назву");
    }

    //deadlines
    @Test
    void shouldThrowExcelReadHandlingException_WhenDeadLineCellIsBlank() {
        Row rowTheoryElement = testSheet.getRow(5);
        Cell cell = rowTheoryElement.getCell(2);
        cell.setBlank();

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Дедлайни вказано не для всіх завдань");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenDeadLineCellValueIsString() {
        Row rowTheoryElement = testSheet.getRow(5);
        Cell cell = rowTheoryElement.getCell(2);
        cell.setCellValue("test");

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Некоректно задане значення дедлайну");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenDeadLineCellValueIsZero() {
        Row rowTheoryElement = testSheet.getRow(5);
        Cell cell = rowTheoryElement.getCell(2);
        cell.setCellValue(0);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Некоректно задане значення дедлайну");
    }

    @Test
    void shouldReturnTrue_WhenDeadlinesIsLessThanNumberOfClasses() {
        int sumClasses = 16;
        Row rowTheoryElement = testSheet.getRow(5);
        Cell cell = rowTheoryElement.getCell(2);
        cell.setCellValue(2);
        MainEvaluation testMainEval = fileReaderEvaluation.readEducationComponentMain();
        assertThat(testMainEval.getPracticeComponent().getSumDeadlines())
                .isLessThan(sumClasses);
    }

    @Test
    void shouldReturnTrue_WhenDeadlinesIsGreaterThanNumberOfClasses() {
        int sumClasses = 16;
        Row rowTheoryElement = testSheet.getRow(5);
        Cell cell = rowTheoryElement.getCell(2);
        cell.setCellValue(10);
        MainEvaluation testMainEval = fileReaderEvaluation.readEducationComponentMain();
        assertThat(testMainEval.getPracticeComponent().getSumDeadlines())
                .isGreaterThan(sumClasses);
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPerSemesterIsNotEqualToTheoryAndPractice() {
        Row rowTheoryElement = testSheet.getRow(22);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(15);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Сумма балів за ТЕОРІЯ та ПРАКТИКА не дорівнє балам ЗА СЕМЕСТР");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPerSemesterIsGreaterThanHundred() {
        //theory
        testSheet.getRow(1).getCell(1).setCellValue(100);
        testSheet.getRow(2).getCell(1).setCellValue(50);
        testSheet.getRow(3).getCell(1).setCellValue(50);

        //practice
        testSheet.getRow(4).getCell(1).setCellValue(50);
        testSheet.getRow(5).getCell(1).setCellValue(10);
        testSheet.getRow(10).getCell(1).setCellValue(10);
        testSheet.getRow(15).getCell(1).setCellValue(10);
        testSheet.getRow(19).getCell(1).setCellValue(10);
        testSheet.getRow(20).getCell(1).setCellValue(10);

        //perSemester
        testSheet.getRow(22).getCell(1).setCellValue(150);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Бали ЗА СЕМЕСТР не можуть бути більше 100");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPerSemesterCellIsZero() {
        Row rowTheoryElement = testSheet.getRow(22);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(0);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Некоректно задані бали");
    }

    @Test
    void shouldReturnTrue_WhenFinalEvaluationCellIsZero() {
        Row rowTheoryElement = testSheet.getRow(23);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(0);
        MainEvaluation testMainEval = fileReaderEvaluation.readEducationComponentMain();
        assertThat(testMainEval.getPerDisciplineCreditOrExam()).isEqualTo(0);
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenFinalEvaluationCellIsNegative() {
        Row rowTheoryElement = testSheet.getRow(23);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(-1);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Некоректно задані бали");
    }

    @Test
    void shouldThrowExcelReadHandlingException_SemesterPointsAndAdditionalPointsExceedHundred() {
        Row rowTheoryElement = testSheet.getRow(24);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(50);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Сумма балів за семестр разом із додатковими балами перевищують 100");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenSemesterAndTEXAMPointsIsGreaterThanHundred() {
        Row rowTheoryElement = testSheet.getRow(23);
        Cell cell = rowTheoryElement.getCell(1);
        cell.setCellValue(50);

        assertThatThrownBy(() -> fileReaderEvaluation.readEducationComponentMain())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Сумма балів за семестр разом із балами за ЕКЗАМЕН/ЗАЛІК перевищують 100");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenNoElementsThatIncludedInPRACTICE() {
        for (int i = 5; i <= 21; i++) {
            testSheet.removeRow(testSheet.getRow(i));
        }

        FileReaderEvaluation fileReaderEvaluation = new FileReaderEvaluation(testSheet);

        assertThatThrownBy(fileReaderEvaluation::readEducationComponentMain)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Відсутні елементи які входять до ПРАКТИКА");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenHasNotNext() {
        fileReaderEvaluation.readEducationComponentMain();
        assertThatThrownBy(fileReaderEvaluation::next)
                .isInstanceOf(NoSuchElementException.class);
    }
}