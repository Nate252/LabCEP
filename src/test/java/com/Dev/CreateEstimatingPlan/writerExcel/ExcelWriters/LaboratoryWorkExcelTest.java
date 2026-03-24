package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static testUtil.TestWriters.TestClassDatesExcel.*;
import static testUtil.TestWriters.TestLaboratoryWorkExcel.createLaboratoryFileExcel;
import static testUtil.TestWriters.TestListStudentsFileExcel.*;

class LaboratoryWorkExcelTest {
    private static LaboratoryWorkExcel underTest;
    private static Sheet testSheet;

    @BeforeAll
    static void beforeAll() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));

        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();

        underTest = createLaboratoryFileExcel(listStudents.getSharedData());
        underTest.createExcel();
        testSheet = underTest.getWorkbook().getSheetAt(1);
    }

    @Test
    void shouldReturnTrue_WhenLaboratorySheetNameCorrect() {
        assertThat(testSheet.getSheetName()).isEqualTo("Лабораторні");
    }

    @Test
    void shouldReturnTrue_WhenLaboratoryHeaderDataCorrect() {
        List<String> listHeaders = List.of("Оцінка", "Виконана", "Захист");
        Row headerFirst = testSheet.getRow(0);
        Row headerSecond = testSheet.getRow(1);
        int counter = 4;
        for (Tasks task : practiceTasksForClassDates()) {
            assertThat(headerFirst.getCell(counter).getStringCellValue()).isEqualTo(task.getName());

            assertThat(headerSecond.getCell(counter).getStringCellValue()).isEqualTo(listHeaders.get(0));
            counter++;
            assertThat(headerSecond.getCell(counter).getStringCellValue()).isEqualTo(listHeaders.get(1));
            counter++;
            assertThat(headerSecond.getCell(counter).getStringCellValue()).isEqualTo(listHeaders.get(2));
            counter++;

            int counterSubTask = 1;
            for (String subTask : task.getSubTasks()) {
                assertThat(headerSecond.getCell(counter).getNumericCellValue()).isEqualTo(counterSubTask);
                assertThat(headerSecond.getCell(counter).getCellComment().getString().getString()).isEqualTo(subTask);
                counter++;
                counterSubTask++;
            }
        }
    }
    @Test
    void shouldReturnTrue_WhenLaboratoryMergedRegionCorrect() {
        List<CellRangeAddress> mergedRegions = testSheet.getMergedRegions();

        assertThat(mergedRegions).isNotNull();
        assertThat(
                mergedRegions.stream().anyMatch(region ->
                        region.isInRange(0, 4) && region.isInRange(0, 10)))
                .isTrue();
    }
    @Test
    void shouldReturnTrue_WhenLaboratoryHeaderStylesCorrect() {
        Row headerFirst = testSheet.getRow(0);
        Row headerSecond = testSheet.getRow(1);
        CellStyle cellTestStyleHeader = headerFirst.getCell(4).getCellStyle();
        CellStyle cellTestStyleHeaders = headerSecond.getCell(4).getCellStyle();
        Font actualFontHeader = underTest.getWorkbook().getFontAt(cellTestStyleHeader.getFontIndex());
        Font actualFontHeaders = underTest.getWorkbook().getFontAt(cellTestStyleHeaders.getFontIndex());

        for (Font font : List.of(actualFontHeaders, actualFontHeader)) {
            assertThat(font.getBold())
                    .isTrue();

        }

        for (CellStyle cellStyle : List.of(cellTestStyleHeader, cellTestStyleHeaders)) {
            assertThat(new BorderStyle[]{
                    cellStyle.getBorderTop(),
                    cellStyle.getBorderBottom(),
                    cellStyle.getBorderLeft(),
                    cellStyle.getBorderRight()
            }).containsOnly(BorderStyle.THIN);

            assertThat(cellStyle.getAlignment())
                    .isEqualTo(HorizontalAlignment.CENTER);

        }

        assertThat(actualFontHeader.getFontHeightInPoints())
                .isEqualTo((short) 12);
        assertThat(actualFontHeaders.getItalic())
                .isTrue();
        assertThat(actualFontHeaders.getFontHeightInPoints())
                .isEqualTo((short) 10);
    }

    @Test
    void shouldReturnTrue_WhenLaboratoryDataCellFormatCorrect() {
        List<String> listPoints = List.of("0.1", "0.5", "0.7", "1.0");
        String expectedFormatTest = "dd\".\"mmm";
        Row row = testSheet.getRow(2);
        CellStyle styleFirst = row.getCell(5).getCellStyle();
        CellStyle styleSecond = row.getCell(5).getCellStyle();
        List<? extends DataValidation> validations = testSheet.getDataValidations();

        assertThat(validations).isNotNull();
        assertThat(
                validations.stream()
                        .map(v -> v.getValidationConstraint().getExplicitListValues())
                        .filter(Objects::nonNull)
                        .anyMatch(values -> Arrays.equals(values, listPoints.toArray()))
        ).isTrue();

        assertThat(styleFirst.getDataFormatString()).isEqualTo(expectedFormatTest);
        assertThat(styleSecond.getDataFormatString()).isEqualTo(expectedFormatTest);
    }

    @Test
    void shouldReturnTrue_WhenSharedDataListNumCellsLaboratoryScopeCorrect() {
        List<Integer> nums = List.of(4, 11, 18, 24, 27);

        assertThat(underTest.getSharedData().getListNumCellsLaboratoryScope()).isEqualTo(nums);
    }
}