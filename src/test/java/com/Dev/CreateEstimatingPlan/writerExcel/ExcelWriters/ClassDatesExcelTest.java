package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Teacher;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.Colors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testUtil.TestWriters.TestClassDatesExcel;
import testUtil.TestWriters.TestScheduleFileExcel;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.Dev.CreateEstimatingPlan.templateExcelReader.LocalDateParser.excelDateToLocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static testUtil.TestWriters.TestClassDatesExcel.*;

class ClassDatesExcelTest {
    private static ClassDatesExcel underTest;
    private static Sheet testSheet;

    @BeforeAll
    static void beforeAll() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
        underTest = createClassDatesExcelFileExcel(createPracticeComponent(practiceTasksForClassDates()));
        underTest.createExcel();
        testSheet = underTest.getWorkbook().getSheetAt(0);
    }

    @Test
    void shouldReturnTrue_WhenClassDatesSheetNameCorrect() {
        assertThat(testSheet.getSheetName()).isEqualTo("Дати занять");
    }

    @Test
    void shouldReturnTrue_WhenClassDatesHeaderCorrect() {
        List<String> listHeaders = List.of(
                ExcelLocaleProvider.getMessage(
                        "sheet.writer.class_dates.header.week"),
                ExcelLocaleProvider.getMessage(
                        "sheet.writer.class_dates.header.class"),

                ExcelLocaleProvider.getMessage(
                        "sheet.writer.class_dates.header.date"),

                ExcelLocaleProvider.getMessage(
                        "sheet.writer.class_dates.header.deadline")
        );

        for (int i = 0, j = 0; i < 2; i++) {
            Row row = testSheet.getRow(j);
            Row headers = testSheet.getRow(++j);
            assertThat(row.getCell(0).getStringCellValue())
                    .isEqualTo(TestClassDatesExcel.createLessons().get(i).getNameOfGroupsStudentsFileName());

            for (int b = 0; b < listHeaders.size(); b++) {
                assertThat(headers.getCell(b).getStringCellValue())
                        .isEqualTo(listHeaders.get(b));
            }
            j += 5;
        }
    }

    @Test
    void shouldReturnTrue_WhenClassDatesHeadersStylesCorrect() {
        Row row = testSheet.getRow(0);
        Row headers = testSheet.getRow(1);
        CellStyle cellTestStyleHeader = row.getCell(0).getCellStyle();
        CellStyle cellTestStyleHeaders = headers.getCell(0).getCellStyle();
        Font actualFontHeader = underTest.getWorkbook().getFontAt(cellTestStyleHeader.getFontIndex());
        Font actualFontHeaders = underTest.getWorkbook().getFontAt(cellTestStyleHeaders.getFontIndex());

        assertThat(actualFontHeader.getBold())
                .isTrue();
        assertThat(actualFontHeader.getFontHeightInPoints())
                .isEqualTo((short) 14);
        assertThat(cellTestStyleHeader.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);
        assertThat(new BorderStyle[]{
                cellTestStyleHeader.getBorderTop(),
                cellTestStyleHeader.getBorderBottom(),
                cellTestStyleHeader.getBorderLeft(),
                cellTestStyleHeader.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);

        Colors testColor = Colors.YELLOW;
        XSSFCellStyle xssfStyle = (XSSFCellStyle) cellTestStyleHeader;
        XSSFColor cellColor = xssfStyle.getFillForegroundXSSFColor();
        assertThat(cellColor).isNotNull();
        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());

        assertThat(actualFontHeaders.getBold())
                .isTrue();

        assertThat(actualFontHeaders.getFontHeightInPoints())
                .isEqualTo((short) 14);

        assertThat(cellTestStyleHeaders.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(new BorderStyle[]{
                cellTestStyleHeaders.getBorderTop(),
                cellTestStyleHeaders.getBorderBottom(),
                cellTestStyleHeaders.getBorderLeft(),
                cellTestStyleHeaders.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);

    }

    @Test
    void shouldReturnTrue_WhenClassDatesDataCorrect() {
        List<LocalDate> dates = TestScheduleFileExcel.getDates();
        List<Tasks> tasks = practiceTasksForClassDates();
        List<Double> weekNumbers = List.of(1.0, 1.0, 2.0, 3.0);

        for (int i = 2, index = 0; i < 5; i++, index++) {
            Row row = testSheet.getRow(i);

            assertThat(row.getCell(0).getNumericCellValue()).isEqualTo(weekNumbers.get(index));
            assertThat(row.getCell(1).getNumericCellValue()).isEqualTo(index + 1);
            assertThat(excelDateToLocalDate(row.getCell(2).getNumericCellValue())).isEqualTo(dates.get(index));
            assertThat(row.getCell(3).getStringCellValue()).isEqualTo(tasks.get(index).getName());
        }
    }

    @Test
    void shouldReturnTrue_WhenTeacherHeadersStylesCorrect() {
        Row row = testSheet.getRow(0);
        Row headers = testSheet.getRow(1);

        CellStyle cellTestStyleHeader = row.getCell(5).getCellStyle();
        CellStyle cellTestStyleHeaders = headers.getCell(5).getCellStyle();

        Font actualFontHeader = underTest.getWorkbook().getFontAt(cellTestStyleHeader.getFontIndex());
        Font actualFontHeaders = underTest.getWorkbook().getFontAt(cellTestStyleHeaders.getFontIndex());


        assertThat(actualFontHeader.getBold())
                .isTrue();

        assertThat(actualFontHeader.getFontHeightInPoints())
                .isEqualTo((short) 14);

        assertThat(cellTestStyleHeader.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(new BorderStyle[]{
                cellTestStyleHeader.getBorderTop(),
                cellTestStyleHeader.getBorderBottom(),
                cellTestStyleHeader.getBorderLeft(),
                cellTestStyleHeader.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);

        assertThat(actualFontHeaders.getBold())
                .isTrue();

        assertThat(actualFontHeaders.getFontHeightInPoints())
                .isEqualTo((short) 14);

        assertThat(cellTestStyleHeaders.getAlignment())
                .isEqualTo(HorizontalAlignment.CENTER);

        assertThat(new BorderStyle[]{
                cellTestStyleHeaders.getBorderTop(),
                cellTestStyleHeaders.getBorderBottom(),
                cellTestStyleHeaders.getBorderLeft(),
                cellTestStyleHeaders.getBorderRight()
        })
                .containsOnly(BorderStyle.THIN);
    }

    @Test
    void shouldReturnTrue_WhenTeacherDataStylesCorrect() {
        List<Lesson> lessons = TestClassDatesExcel.createLessons();
        List<String> groups = List.of("КН-222в ", "КН-222б КН-222г ");
        int counter = 2;
        for (Lesson lesson : lessons) {
            Row row = testSheet.getRow(counter);
            Teacher teacherFirst = lesson.getTeacher();

            assertThat(row.getCell(5).getStringCellValue()).isEqualTo(groups.get(counter - 2));
            assertThat(row.getCell(6).getStringCellValue()).isEqualTo(teacherFirst.getFIO());
            assertThat(row.getCell(7).getStringCellValue()).isEqualTo(teacherFirst.getAcademicDegree());
            assertThat(row.getCell(8).getStringCellValue()).isEqualTo(teacherFirst.getPosition());
            assertThat(row.getCell(9).getStringCellValue()).isEqualTo(teacherFirst.getEmail());

            counter++;
        }
    }

    @Test
    void shouldReturnTrue_WhenClassDatesWithoutDeadlinesDataCorrect() {
        ClassDatesExcel classDatesExcel = createClassDatesExcelFileExcel(
                createPracticeComponent(practiceTasksForClassDatesWithoutDeadlines()));
        classDatesExcel.createExcel();
        Sheet sheet = classDatesExcel.getWorkbook().getSheetAt(0);

        Row row = sheet.getRow(1);
        assertThat(row.getCell(3)).isNull();
    }

    @Test
    void shouldReturnTrue_WhenClassDatesDeadlinesMergedRegion() {
        List<Tasks> tasks = practiceTasksForClassDates();
        tasks.get(0).setDeadLine(2);
        tasks.get(tasks.size() - 1).setDeadLine(0);

        ClassDatesExcel classDatesExcel = createClassDatesExcelFileExcel(
                createPracticeComponent(tasks));
        classDatesExcel.createExcel();
        Sheet sheet = classDatesExcel.getWorkbook().getSheetAt(0);

        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();

        assertThat(mergedRegions).isNotNull();
        assertThat(
                mergedRegions.stream().anyMatch(region ->
                        region.isInRange(2,3) && region.isInRange(3,3)))
                .isTrue();

    }

    @Test
    void shouldReturnTrue_WhenClassDatesHeaderMergedRegion() {
        List<CellRangeAddress> mergedRegions = testSheet.getMergedRegions();

        assertThat(mergedRegions).isNotNull();
        assertThat(
                mergedRegions.stream().anyMatch(region ->
                        region.isInRange(0,3)))
                .isTrue();
    }
    @Test
    void shouldReturnTrue_WhenTeacherHeaderMergedRegion() {
        List<CellRangeAddress> mergedRegions = testSheet.getMergedRegions();

        assertThat(mergedRegions).isNotNull();
        assertThat(
                mergedRegions.stream().anyMatch(region ->
                        region.isInRange(0,5) && region.isInRange(0,9)))
                .isTrue();
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenDeadLinesExceedsNumberOfClasses() {
        List<Tasks> tasks = practiceTasksForClassDates();
        tasks.get(0).setDeadLine(20);

        ClassDatesExcel classDatesExcel = createClassDatesExcelFileExcel(
                createPracticeComponent(tasks));

        assertThatThrownBy(classDatesExcel::createExcel)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Сумма встановленої кількості занять для дедлайну перевищує кількість занять");
    }
}