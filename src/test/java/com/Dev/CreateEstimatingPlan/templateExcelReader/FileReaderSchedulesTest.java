package com.Dev.CreateEstimatingPlan.templateExcelReader;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Practice;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleService;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import testUtil.TestScheduleService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

class FileReaderSchedulesTest {
    private Sheet testSheet;
    private FileReaderSchedules underTest;

    private ScheduleService scheduleService;

    private Iterator<Row> getIterator() {
        Iterator<Row> rowIterator = testSheet.iterator();
        for (int i = 0; i < 4; i++) {
            rowIterator.next();
        }
        return rowIterator;
    }

    private Sheet getSheet(int index) {
        Workbook workbook;
        try (InputStream fileInputStream = getClass().getClassLoader().
                getResourceAsStream("test-files/ПланВивченняНД_uk.xlsm")) {
            assert fileInputStream != null;


            workbook = WorkbookFactory.create(fileInputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return workbook.getSheetAt(index);
    }

    @BeforeEach
    void setUp() {
        ExcelLocaleProvider.setLocale(Locale.forLanguageTag("uk"));
        scheduleService = TestScheduleService.defaultScheduleService();

        try (InputStream fileInputStream = getClass().getClassLoader().
                getResourceAsStream("test-files/ПланВивченняНД_uk.xlsm")) {
            assert fileInputStream != null;


            Workbook workbook = WorkbookFactory.create(fileInputStream);
            testSheet = workbook.getSheetAt(0);

            underTest = new FileReaderSchedules(getIterator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnTrue_WhenCompletelyFilledDefault() {
        assertThat(underTest.readScheduleService()).isEqualTo(scheduleService);
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenStartDateIsBlank() {

        testSheet.getRow(4).getCell(1).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано дату початку семестра");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenLectureNameIsBlank() {

        testSheet.getRow(7).getCell(0).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Відстуня назва заголовку Лекції, не змінюйте шаблон");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenPracticeNameIsBlank() {

        testSheet.getRow(11).getCell(0).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Відстуня назва заголовку Практика, не змінюйте шаблон");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenEndDateIsBlank() {

        testSheet.getRow(5).getCell(1).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано дату кінця семестра");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenEndDateIsFarInTheFuture() {

        testSheet.getRow(5).getCell(1).setCellValue(
                TestScheduleService.startDateSemester().plusYears(10));

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Дата кінця семестра відрізняється від поточної на 5 років");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenStartDateIsFarInTheFuture() {

        testSheet.getRow(4).getCell(1).setCellValue(TestScheduleService.startDateSemester().plusYears(10));

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Дата початку семестра відрізняється від поточної на 5 років");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenStartDateIsFarInThePast() {

        testSheet.getRow(4).getCell(1).setCellValue(TestScheduleService.startDateSemester().minusYears(10));

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Дата початку семестра відрізняється від поточної на 5 років");
    }

    @Test
    void shouldReturnTrue_WithoutLecture() {
        testSheet.getRow(8).getCell(1).setBlank();
        testSheet.getRow(9).getCell(1).setBlank();

        assertThat(underTest.readScheduleService().getLecture()).isEqualTo(null);
    }

    @Test
    void shouldReturnTrue_WithoutPractice() {
        testSheet.getRow(12).getCell(0).setBlank();
        assertThat(underTest.readScheduleService().getLessons()).isEqualTo(null);
    }


    // Test Teacher
    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherSurnameIsBlank() {
        testSheet.getRow(13).getCell(1).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано Прізвище викладача");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherNameIsBlank() {
        testSheet.getRow(13).getCell(2).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано Ім'я викладача");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherMiddleNameIsBlank() {
        testSheet.getRow(13).getCell(3).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано По батькові викладача");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherAcademicDegreeIsBlank() {
        testSheet.getRow(13).getCell(4).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано назву наукового ступеню");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherPositionIsBlank() {
        testSheet.getRow(13).getCell(5).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано назву посади");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherEmailIsBlank() {
        testSheet.getRow(13).getCell(6).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано електронну адресу");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTeacherEmailIsNotString() {
        testSheet.getRow(13).getCell(6).setCellValue(10);

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано електронну адресу");
    }

    //    groups
    @Test
    void shouldThrowExcelReadHandlingException_WhenGroupNameIsNotString() {
        testSheet.getRow(12).getCell(1).setCellValue(10);

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Невірно вказано назву групи");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenGroupsIsNull() {
        testSheet.getRow(12).getCell(1).setBlank();
        testSheet.getRow(12).getCell(2).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не вказано жодної групи для вивчення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenMissedSchedule() {

        testSheet.getRow(16).getCell(1).setBlank();
        testSheet.getRow(17).getCell(1).setBlank();

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("В секції ПРАКТИКА пропущено розклад для груп [КН-222г, КН-222б]");

    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenTwoTypesOfClassesIndicated() {

        testSheet.getRow(15).getCell(1).setCellValue("ПН");
        testSheet.getRow(16).getCell(1).setCellValue("ПН");

        assertThatThrownBy(() -> underTest.readScheduleService())
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("В секції ПРАКТИКА вказано розклад відразу для 2 типів занять(ПЗ,ЛАБ)");

    }

    @Test
    void shouldReturnTrue_WhenCreatePracticeTypeClasses() {

        testSheet.getRow(16).getCell(1).setBlank();
        testSheet.getRow(17).getCell(1).setBlank();
        testSheet.getRow(22).getCell(1).setBlank();
        testSheet.getRow(23).getCell(1).setBlank();

        testSheet.getRow(14).getCell(1).setCellValue("ПН");
        testSheet.getRow(15).getCell(1).setCellValue("ПН");
        testSheet.getRow(20).getCell(1).setCellValue("ПН");
        testSheet.getRow(21).getCell(1).setCellValue("ПН");

        assertThat(underTest.readScheduleService().getLessons().get(0))
                .isInstanceOf(Practice.class);

    }

    @Test
    void shouldReturnTrue_WhenPracticeNoNext() {
        for (int i = 12; i <= 35; i++) {
            testSheet.removeRow(testSheet.getRow(i));
        }

        FileReaderSchedules fileReaderSchedules = new FileReaderSchedules(getIterator());

        assertThat(fileReaderSchedules.readScheduleService().getLessons()).isNull();
    }

    @Test
    void shouldReturnTrue_WhenCompletelyFilledWithSaturdays() {
        FileReaderSchedules fileReaderSchedulesTest = new FileReaderSchedules(getIterator(), getSheet(1));
        assertThat(fileReaderSchedulesTest.readScheduleService()).isEqualTo(TestScheduleService.ScheduleServiceWithSaturdays());
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenIteratorIsNull() {
        for (int i = 12; i <= 35; i++) {
            testSheet.removeRow(testSheet.getRow(i));
        }
        FileReaderSchedules fileReaderSchedules = new FileReaderSchedules(getIterator());
        fileReaderSchedules.readScheduleService();

        assertThatThrownBy(fileReaderSchedules::next)
                .isInstanceOf(NoSuchElementException.class);
    }
}