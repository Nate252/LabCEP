package com.Dev.CreateEstimatingPlan.templateExcelReader;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtil.TestMainEvaluation;
import testUtil.TestStudyPlan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileReaderMainTest {
    private StudyPlan studyPlan;
    private Workbook workbook;
    private FileReaderMain underTest;

    private Workbook getWorkbook() {
        try (InputStream fileInputStream = getClass().getClassLoader().
                getResourceAsStream("test-files/ПланВивченняНД_uk.xlsm")) {
            assert fileInputStream != null;


            return WorkbookFactory.create(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        ExcelLocaleProvider.setLocale(Locale.forLanguageTag("uk"));
        studyPlan = TestStudyPlan.createPlanWithSaturdays();

        workbook = getWorkbook();
        underTest = new FileReaderMain(workbook);
    }

    @Test
    void shouldReturnTrue_WhenCompletelyFilledWithSaturdays() {
        assertThat(underTest.createPlan()).isEqualTo(studyPlan);
    }

    @Test
    void shouldReturnTrue_WhenCompletelyFilledDefault() {

        Sheet testSheet = workbook.getSheetAt(1);
        for (int i = 1; i <= 30; i++) {
            testSheet.getRow(i).getCell(0).setBlank();
        }

        assertThat(underTest.createPlan()).isEqualTo(TestStudyPlan.createPlanDefault());
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenNoLectureSchedule() {
        Sheet sheet = workbook.getSheetAt(0);
        sheet.getRow(8).getCell(1).setBlank();
        sheet.getRow(9).getCell(1).setBlank();

        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("В Теорія є елемент Лекції, проте в Структура вивчення не вказано розклад для лекцій");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenNoScheduleForClasses() {
        Sheet sheet = workbook.getSheetAt(0);
        sheet.getRow(12).getCell(0).setBlank();


        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не вказано розклад для Лабораторних/Практичних занять");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenScheduleNotForAllGroups() {
        Sheet sheet = workbook.getSheetAt(0);
        sheet.getRow(3).getCell(4).setCellValue("КН-222с");


        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не створено розклад для групи з списку груп або створено розклад для групи якої немає в списку груп");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenScheduleGroupIsNotInlistOfGroups() {
        Sheet sheet = workbook.getSheetAt(0);
        sheet.getRow(12).getCell(3).setCellValue("КН-222с");


        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не створено розклад для групи з списку груп або створено розклад для групи якої немає в списку груп");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenNameOfDisciplineIsBlank() {
        Sheet sheet = workbook.getSheetAt(0);
        sheet.getRow(2).getCell(1).setBlank();


        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано назву дисципліни");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenGroupNameIsNotString() {
        Sheet testSheet = workbook.getSheetAt(0);
        testSheet.getRow(3).getCell(1).setCellValue(10);

        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано назву групи");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenGroupsIsNull() {
        Sheet testSheet = workbook.getSheetAt(0);
        testSheet.getRow(3).getCell(1).setBlank();
        testSheet.getRow(3).getCell(2).setBlank();
        testSheet.getRow(3).getCell(3).setBlank();

        assertThatThrownBy(() -> underTest.createPlan())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не вказано жодної групи для вивчення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenRowIteratorIsEmpty() {
        workbook = getWorkbook();
        workbook.removeSheetAt(0);
        workbook.createSheet("Структура вивчення");
        underTest = new FileReaderMain(workbook);

        assertThatThrownBy(underTest::next)
                .isInstanceOf(NoSuchElementException.class);
    }
}