package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.Dev.CreateEstimatingPlan.templateExcelReader.LocalDateParser.excelDateToLocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static testUtil.TestWriters.TestListStudentsFileExcel.*;
import static testUtil.TestWriters.TestScheduleFileExcel.*;

class ScheduleFileExcelTest {
    private static ScheduleFileExcel underTest;
    private static Sheet testSheet;


    @BeforeAll
    static void beforeAll() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));

        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();

        underTest = createScheduleLectureFileExcel(listStudents.getSharedData());
        underTest.createExcel();
        testSheet = underTest.getWorkbook().getSheetAt(1);
    }

    @Test
    void shouldReturnTrue_WhenSheetNameCorrectly() {
        String sheetName = "Лекції";
        assertThat(testSheet.getSheetName()).isEqualTo(sheetName);
    }

    @Test
    void shouldReturnTrue_WhenHeaderFirstDataCorrectly() {
        String weekNumber = ExcelLocaleProvider
                .getMessage("sheet.writer.lecture.header.number_of_week");
        String nameDiscipline = "АППЗ";
        Row headerName = testSheet.getRow(0);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        assertThat(evaluator.evaluate(headerName.getCell(2)).getStringValue()).isEqualTo(nameDiscipline);
        assertThat(headerName.getCell(3).getStringCellValue()).isEqualTo(weekNumber);
        assertThat(headerName.getCell(4).getNumericCellValue()).isEqualTo(1);
        assertThat(headerName.getCell(5).getNumericCellValue()).isEqualTo(1);
        assertThat(headerName.getCell(6).getNumericCellValue()).isEqualTo(2);
        assertThat(headerName.getCell(7).getNumericCellValue()).isEqualTo(3);
    }
    @Test
    void shouldReturnTrue_WhenStudentDataLocaleEnCorrectly() {
        ExcelLocaleProvider.setLocale(Locale.ENGLISH);

        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();

        ScheduleFileExcel scheduleFileExcel = createScheduleLectureFileExcel(listStudents.getSharedData());
        scheduleFileExcel.createExcel();
        Sheet testSheet = scheduleFileExcel.getWorkbook().getSheetAt(1);

        List<Group> groups = createGroups();
        int counterRow = 3;
        FormulaEvaluator evaluator = scheduleFileExcel.getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (Group group : groups) {
            List<Student> students = group.getStudents();
            int counterStudents = 1;

            for (Student student : students) {
                Row studentData = testSheet.getRow(counterRow);
                assertThat(evaluator.evaluate(studentData.getCell(0)).getNumberValue())
                        .isEqualTo(counterStudents);
                assertThat(evaluator.evaluate(studentData.getCell(1)).getStringValue())
                        .isEqualTo(group.getName());
                assertThat(evaluator.evaluate(studentData.getCell(2)).getStringValue())
                        .isEqualTo(student.getStudentShortFullName());
                assertThat(evaluator.evaluate(studentData.getCell(3)).getStringValue())
                        .isEqualTo("No");
                counterRow++;
                counterStudents++;
            }
        }

        ExcelLocaleProvider.setLocale(new Locale("uk"));
    }

    @Test
    void shouldReturnTrue_WhenHeaderSecondDataCorrectly() {
        String numberClasses = ExcelLocaleProvider
                .getMessage("sheet.writer.lecture.header.number_of_class");
        Row headerName = testSheet.getRow(1);


        assertThat(headerName.getCell(3).getStringCellValue()).isEqualTo(numberClasses);
        assertThat(headerName.getCell(4).getNumericCellValue()).isEqualTo(1);
        assertThat(headerName.getCell(5).getNumericCellValue()).isEqualTo(2);
        assertThat(headerName.getCell(6).getNumericCellValue()).isEqualTo(3);
        assertThat(headerName.getCell(7).getNumericCellValue()).isEqualTo(4);

    }

    @Test
    void shouldReturnTrue_WhenHeaderThirdDataCorrectly() {
        List<LocalDate> dates = getDates();
        Row headerName = testSheet.getRow(2);
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();

        assertThat(evaluator.evaluate(headerName.getCell(0)).getStringValue()).isEqualTo("#");
        assertThat(evaluator.evaluate(headerName.getCell(1)).getStringValue()).isEqualTo("Група");
        assertThat(headerName.getCell(2).getStringCellValue()).isEqualTo("Прізвище І.П.");
        assertThat(evaluator.evaluate(headerName.getCell(3)).getStringValue()).isEqualTo("Кампус");


        assertThat(excelDateToLocalDate(headerName.getCell(4).getNumericCellValue())).isEqualTo(dates.get(0));
        assertThat(excelDateToLocalDate(headerName.getCell(5).getNumericCellValue())).isEqualTo(dates.get(1));
        assertThat(excelDateToLocalDate(headerName.getCell(6).getNumericCellValue())).isEqualTo(dates.get(2));
        assertThat(excelDateToLocalDate(headerName.getCell(7).getNumericCellValue())).isEqualTo(dates.get(3));
        assertThat(headerName.getCell(8).getStringCellValue()).isEqualTo("Всього");
    }

    @Test
    void shouldReturnTrue_WhenHeaderStylesCorrectly() {
        Row headerName = testSheet.getRow(1);
        Row headerNameDate = testSheet.getRow(2);

        CellStyle cellTestStyleHeader = headerName.getCell(3).getCellStyle();
        CellStyle cellTestStyleDate = headerNameDate.getCell(4).getCellStyle();

        Font actualFontHeader = underTest.getWorkbook().getFontAt(cellTestStyleHeader.getFontIndex());
        Font actualFontDate = underTest.getWorkbook().getFontAt(cellTestStyleDate.getFontIndex());

        // header styles
        assertThat(actualFontHeader.getBold())
                .isTrue();

        assertThat(actualFontHeader.getFontHeightInPoints())
                .isEqualTo((short) 12);

        assertThat(cellTestStyleHeader.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(new BorderStyle[]{
                cellTestStyleHeader.getBorderTop(),
                cellTestStyleHeader.getBorderBottom(),
                cellTestStyleHeader.getBorderLeft(),
                cellTestStyleHeader.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);

        // date styles
        assertThat(actualFontDate.getFontHeightInPoints())
                .isEqualTo((short) 12);


        assertThat(new BorderStyle[]{
                cellTestStyleDate.getBorderTop(),
                cellTestStyleDate.getBorderBottom(),
                cellTestStyleDate.getBorderLeft(),
                cellTestStyleDate.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);
    }

    @Test
    void shouldReturnTrue_WhenStudentDataCorrectly() {
        List<Group> groups = createGroups();
        int counterRow = 3;
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (Group group : groups) {
            List<Student> students = group.getStudents();
            int counterStudents = 1;

            for (Student student : students) {
                Row studentData = testSheet.getRow(counterRow);
                assertThat(evaluator.evaluate(studentData.getCell(0)).getNumberValue())
                        .isEqualTo(counterStudents);
                assertThat(evaluator.evaluate(studentData.getCell(1)).getStringValue())
                        .isEqualTo(group.getName());
                assertThat(evaluator.evaluate(studentData.getCell(2)).getStringValue())
                        .isEqualTo(student.getStudentShortFullName());
                assertThat(evaluator.evaluate(studentData.getCell(3)).getStringValue())
                        .isEqualTo("Ні");
                counterRow++;
                counterStudents++;
            }
        }
    }

    @Test
    void shouldReturnTrue_WhenSharedDataMaxLectureAndLastAddressCorrect() {
        assertThat(underTest.getSharedData().getMaxLectureClasses())
                .isEqualTo(4);
        assertThat(underTest.getSharedData().getLastCellLectureScopeAddress())
                .isEqualTo("I");
    }

    @Test
    void shouldReturnTrue_WhenFreezePaneIsExists() {
        assertThat(testSheet.getTopRow()).isNotEqualTo(-1);
        assertThat(testSheet.getLeftCol()).isNotEqualTo(-1);
    }

    @Test
    void shouldReturnTrue_WhenDateFormatCorrect() {
        Row row = testSheet.getRow(2);
        CellStyle style = row.getCell(4).getCellStyle();
        String expectedFormatTest = "dd\".\"mm";

        assertThat(style.getDataFormatString()).isEqualTo(expectedFormatTest);
    }

    @Test
    void shouldReturnTrue_WhenTotalFormulaCorrect() {
        FormulaEvaluator evaluator = underTest.getWorkbook().getCreationHelper().createFormulaEvaluator();
        Row row = testSheet.getRow(3);
        row.getCell(4).setCellValue(1);

        assertThat(evaluator.evaluate(row.getCell(8)).getNumberValue())
                .isEqualTo(1);
        row.getCell(4).setBlank();
    }

    @Test
    void shouldReturnTrue_WhenCreateWithPracticeCorrect() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
        String sheetName = "ЛР КН-222б КН-222г ";

        ListStudentsFileExcel listStudents = createListStudentsFileExcel();
        listStudents.createExcel();
        ScheduleFileExcel practiceExcel = createSchedulePracticeFileExcel(listStudents.getSharedData());
        practiceExcel.createExcel();
        Sheet sheet = practiceExcel.getWorkbook().getSheetAt(1);

        assertThat(sheet.getSheetName()).isEqualTo(sheetName);
    }
}