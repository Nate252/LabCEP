package com.Dev.CreateEstimatingPlan.templateExcelReader;

import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtil.TestSaturdays;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileReaderScheduleSaturdaysTest {
    private Sheet testSheet;
    private FileReaderScheduleSaturdays underTest;

    Map<Map<Integer, DayOfWeek>, List<LocalDate>> saturdays;

    @BeforeEach
    void setUp() {
        ExcelLocaleProvider.setLocale(Locale.forLanguageTag("uk"));
        saturdays = TestSaturdays.saturdays();

        try (InputStream fileInputStream = getClass().getClassLoader().
                getResourceAsStream("test-files/ПланВивченняНД_uk.xlsm")) {
            assert fileInputStream != null;


            Workbook workbook = WorkbookFactory.create(fileInputStream);
            testSheet = workbook.getSheetAt(1);

            underTest = new FileReaderScheduleSaturdays(testSheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void readSaturdays() {
        assertThat(underTest.readSaturdays()).isEqualTo(saturdays);
    }

    @Test
    void shouldThrowExcelReadHandlingException_WhenRowIteratorIsEmpty() {
        Sheet sheet = mock(Sheet.class);
        Iterator<Row> rowIterator = mock(Iterator.class);

        when(sheet.iterator()).thenReturn(rowIterator);
        when(rowIterator.hasNext()).thenReturn(false);

        FileReaderScheduleSaturdays test = new FileReaderScheduleSaturdays(sheet);

        assertThatThrownBy(test::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Лист не містить жодного рядка");
    }

    @Test
    void shouldThrowExcelReadHandlingException_DateOfTransferIsBlank() {
        testSheet.getRow(1).getCell(0).setBlank();

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не задано дати перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayIsBlank() {
        testSheet.getRow(2).getCell(0).setBlank();

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Не задано дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_InsteadOfTransferDateTransferDay() {
        testSheet.getRow(1).getCell(0).setCellValue("понеділок 2 тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Замість дати перенесення вказано день з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_InsteadOfTransferDayTransferDate() {
        testSheet.getRow(2).getCell(0).setCellValue("07.09.2024 субота");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Замість дня з якого робиться перенесення вказано дату перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDateMissDate() {
        testSheet.getRow(1).getCell(0).setCellValue("субота");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дати перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDateIncorrectDate() {
        testSheet.getRow(1).getCell(0).setCellValue("07.09.0000 субота");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Помилка в даті 07.09.0000 субота");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayMissDay() {
        testSheet.getRow(2).getCell(0).setCellValue(" 2 тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayIncorrectDay() {
        testSheet.getRow(2).getCell(0).setCellValue("субота 2 тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayIncorrectNumberOfWeek() {
        testSheet.getRow(2).getCell(0).setCellValue("понеділок 5 тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayLengthLessThanThree() {
        testSheet.getRow(2).getCell(0).setCellValue("понеділок2тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayLengthMoreThanThree() {
        testSheet.getRow(2).getCell(0).setCellValue("понеділок 2 2 тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_TransferDayIncorrectNumberWeek() {
        testSheet.getRow(2).getCell(0).setCellValue("понеділок 1т тиждень");

        assertThatThrownBy(underTest::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Неправильно задано формат дня з якого робиться перенесення");
    }

    @Test
    void shouldThrowExcelReadHandlingException_RowIteratorHasNotNextAfterDateRow() {
        for (int i = 2; i <= 30; i++) {
            testSheet.removeRow(testSheet.getRow(i));
        }
        FileReaderScheduleSaturdays test = new FileReaderScheduleSaturdays(testSheet);

        assertThatThrownBy(test::readSaturdays)
                .isInstanceOf(ExcelReadHandlingException.class)
                .hasMessage("Знайдено порожню комірку замість дня з якого робиться перенесення");
    }

}