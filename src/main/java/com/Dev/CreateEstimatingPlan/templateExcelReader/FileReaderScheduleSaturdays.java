package com.Dev.CreateEstimatingPlan.templateExcelReader;

import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.validator.ExcelCellValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileReaderScheduleSaturdays {
    private final Iterator<Row> rowIterator;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final String sheetName;

    public FileReaderScheduleSaturdays(Sheet sheet) {
        rowIterator = sheet.iterator();
        sheetName = sheet.getSheetName();
    }

    //
    public Map<Map<Integer, DayOfWeek>, List<LocalDate>> readSaturdays() {
        Map<Map<Integer, DayOfWeek>, List<LocalDate>> rescheduledDates = new HashMap<>();

        if (!rowIterator.hasNext()) {
            generateExceptionExcel("Лист не містить жодного рядка");
        }

        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row dateRow = rowIterator.next();
            if (!rowIterator.hasNext()) {
                generateExceptionExcel("Знайдено порожню комірку замість дня з якого робиться перенесення");
            }
            Row dayRow = rowIterator.next();

            Cell dataCell = dateRow.getCell(0);
            Cell dayCell = dayRow.getCell(0);


            if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(dataCell)) {
                generateExceptionExcelWithAddress(
                        "Не задано дати перенесення", dataCell);
            }
            if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(dayCell)) {
                generateExceptionExcelWithAddress(
                        "Не задано дня з якого робиться перенесення", dayCell);
            }

            String dateCellValue = dateRow.getCell(0).getStringCellValue().trim();
            String dayCellValue = dayRow.getCell(0).getStringCellValue().trim();

            if (!isRescheduleDate(dateCellValue)) {
                if (isTransferDay(dateCellValue)) {
                    generateExceptionExcelWithAddress(
                            "Замість дати перенесення вказано день " +
                                    "з якого робиться перенесення", dataCell);
                }
                generateExceptionExcelWithAddress(
                        "Неправильно задано формат дати перенесення", dataCell);
            }

            if (!isTransferDay(dayCellValue)) {
                if (isRescheduleDate(dayCellValue)) {
                    generateExceptionExcelWithAddress(
                            "Замість дня з якого робиться перенесення вказано дату перенесення", dayCell);
                }
                generateExceptionExcelWithAddress(
                        "Неправильно задано формат дня з якого робиться перенесення", dayCell);
            }

            LocalDate localDate = parseDate(dateCellValue);
            Map<Integer, DayOfWeek> key = parseDayClass(dayCellValue);
            rescheduledDates.computeIfAbsent(key, k -> new ArrayList<>()).add(localDate);
        }

        return rescheduledDates;
    }

    private boolean isRescheduleDate(String dataCell) {
        String regex = "^\\d{2}\\.\\d{2}\\.\\d{4}\\s+\\p{L}+$";
        return dataCell.matches(regex);
    }

    private boolean isTransferDay(String day) {
        String regex = "^[\\p{L}’']+\\s+\\d+\\s+тиждень$";
        if (!day.matches(regex)) {
            return false;
        }

        String[] parts = day.split("\\s+");

        if (!DayOfWeekParser.isDayOfWeek(parts[0])) {
            return false;
        }

        int weekNumber = Integer.parseInt(parts[1]);

        return weekNumber == 1 || weekNumber == 2;
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input.split(" ")[0], DATE_FORMATTER);
        } catch (Exception e) {
            generateExceptionExcel("Помилка в даті " + input);
            return null;
        }
    }

    private Map<Integer, DayOfWeek> parseDayClass(String day) {
        Map<Integer, DayOfWeek> daysMap = new HashMap<>();

        String[] parts = day.split(" ");
        DayOfWeek dayOfWeek = DayOfWeekParser.getDayOfWeek(parts[0].toLowerCase());
        int weekType = Integer.parseInt(parts[1]);

        daysMap.put(weekType, dayOfWeek);

        return daysMap;
    }

    private void generateExceptionExcel(String message) {
        throw new ExcelReadHandlingException(
                sheetName,
                message
        );
    }

    private void generateExceptionExcelWithAddress(String message, Cell cellAddress) {
        throw new ExcelReadHandlingException(
                sheetName,
                cellAddress.getAddress().formatAsString(),
                message
        );
    }
}
