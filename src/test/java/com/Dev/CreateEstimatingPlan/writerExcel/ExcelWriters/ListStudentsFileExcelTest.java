package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static testUtil.TestWriters.TestListStudentsFileExcel.*;

class ListStudentsFileExcelTest {

    private static ListStudentsFileExcel underTest;
    private static Sheet testSheet;


    @BeforeAll
    static void beforeAll() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));

        underTest = createListStudentsFileExcel();
        underTest.createExcel();
        testSheet = underTest.getWorkbook().getSheetAt(0);
    }

    @Test
    void shouldReturnTrue_WhenSheetNameCorrectly() {
        String sheetName = "Список";
        assertThat(testSheet.getSheetName()).isEqualTo(sheetName);
    }

    @Test
    void shouldReturnTrue_WhenNameDisciplineCorrectly() {
        Row topicName = testSheet.getRow(0);
        String nameDiscipline = "АППЗ";

        assertThat(topicName.getCell(2).getStringCellValue())
                .isEqualTo(nameDiscipline);
    }

    @Test
    void shouldReturnTrue_WhenHeaderDataCorrectly() {
        Row headerName = testSheet.getRow(1);

        for (int i = 0; i < headerName.getLastCellNum(); i++) {
            assertThat(headerName.getCell(i).getStringCellValue())
                    .isEqualTo(headers[i]);
        }
    }

    @Test
    void shouldReturnTrue_WhenHeaderStylesCorrectly() {
        Row headerName = testSheet.getRow(1);
        CellStyle cellTestStyle = headerName.getCell(0).getCellStyle();
        Font actualFont = underTest.getWorkbook().getFontAt(cellTestStyle.getFontIndex());

        assertThat(actualFont.getBold())
                .isTrue();

        assertThat(actualFont.getFontHeightInPoints())
                .isEqualTo((short) 14);

        assertThat(cellTestStyle.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(new BorderStyle[]{
                cellTestStyle.getBorderTop(),
                cellTestStyle.getBorderBottom(),
                cellTestStyle.getBorderLeft(),
                cellTestStyle.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);
    }

    @Test
    void shouldReturnTrue_WhenDropdownListCorrectly() {
        List<? extends DataValidation> validations = testSheet.getDataValidations();
        assertThat(validations).isNotNull();

        assertThat(
                validations.stream()
                        .map(v -> v.getValidationConstraint().getExplicitListValues())
                        .filter(Objects::nonNull)
                        .anyMatch(values -> Arrays.equals(values, typesMarker))

        ).isTrue();
        assertThat(
                validations.stream()
                        .map(v -> v.getValidationConstraint().getExplicitListValues())
                        .filter(Objects::nonNull)
                        .anyMatch(values -> Arrays.equals(values, countries))
        ).isTrue();
    }

    @Test
    void shouldReturnTrue_WhenSharedDataListNumGroupsCorrectly() {
        assertThat(underTest.getSharedData().getListNumGroups())
                .isEqualTo(createTestSharedData());
    }

    @Test
    void shouldReturnTrue_WhenStudentDataCorrectly() {
        Row dataStudentFirst = testSheet.getRow(2);
        Row dataStudentSecond = testSheet.getRow(3);
        Student student = underTest.getGroups().get(0).getStudents().get(0);
        Student studentSecond = underTest.getGroups().get(0).getStudents().get(1);

        assertThat(dataStudentFirst.getCell(0).getNumericCellValue()).isEqualTo(1);
        assertThat(dataStudentFirst.getCell(1).getStringCellValue()).isEqualTo(student.getGroup().getName());
        assertThat(dataStudentFirst.getCell(2).getStringCellValue()).isEqualTo(student.getSurname().toUpperCase());
        assertThat(dataStudentFirst.getCell(3).getStringCellValue()).isEqualTo(student.getName());
        assertThat(dataStudentFirst.getCell(4).getStringCellValue()).isEqualTo(student.getMiddleName());
        assertThat(dataStudentFirst.getCell(5).getStringCellValue()).isEqualTo(student.getEmail());
        assertThat(dataStudentFirst.getCell(6).getStringCellValue()).isEqualTo(student.getPhoneNumbers());
        assertThat(dataStudentFirst.getCell(7).getStringCellValue()).isEqualTo("Ні");
        assertThat(dataStudentFirst.getCell(8).getStringCellValue()).isEqualTo("Ні");
        assertThat(dataStudentFirst.getCell(9).getStringCellValue()).isEqualTo("Україна");

        assertThat(dataStudentSecond.getCell(0).getNumericCellValue()).isEqualTo(2);
        assertThat(dataStudentSecond.getCell(1).getStringCellValue()).isEqualTo(studentSecond.getGroup().getName());
        assertThat(dataStudentSecond.getCell(2).getStringCellValue()).isEqualTo(studentSecond.getSurname().toUpperCase());
        assertThat(dataStudentSecond.getCell(3).getStringCellValue()).isEqualTo(studentSecond.getName());
        assertThat(dataStudentSecond.getCell(4).getStringCellValue()).isEqualTo(studentSecond.getMiddleName());
        assertThat(dataStudentSecond.getCell(5).getStringCellValue()).isEqualTo(studentSecond.getEmail());
        assertThat(dataStudentSecond.getCell(6).getStringCellValue()).isEqualTo(studentSecond.getPhoneNumbers());
        assertThat(dataStudentSecond.getCell(7).getStringCellValue()).isEqualTo("Ні");
        assertThat(dataStudentSecond.getCell(8).getStringCellValue()).isEqualTo("Ні");
        assertThat(dataStudentSecond.getCell(9).getStringCellValue()).isEqualTo("Україна");
    }

    @Test
    void shouldReturnTrue_WhenFreezePaneIsExists() {
        assertThat(testSheet.getTopRow()).isNotEqualTo(-1);
        assertThat(testSheet.getLeftCol()).isNotEqualTo(-1);
    }
}