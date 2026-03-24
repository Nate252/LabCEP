package com.Dev.CreateEstimatingPlan.validator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDate;
import java.util.Objects;

public class ExcelCellValidator {

    public static boolean checkNumCellWithoutZero(Cell cell) {
        return checkIsCellEmptyWithCellTypeNumeric(cell)
                || cell.getNumericCellValue() <= 0;
    }

    public static boolean checkNumCellWithZero(Cell cell) {
        return checkIsCellEmptyWithCellTypeNumeric(cell)
                || cell.getNumericCellValue() < 0;
    }

    public static boolean checkIsCellEmptyWithCellTypeString(Cell cell) {
        return cell == null || cell.getCellType() != CellType.STRING || cell.getStringCellValue().isBlank();
    }

    public static boolean checkIsInvalidStringCellWithPrefix(Cell cell, String prefix) {
        if (checkIsCellEmptyWithCellTypeString(cell)) {
            return true;
        }
        return !cell.getStringCellValue().startsWith(prefix);
    }

    public static boolean checkIsCellStringNull(Cell cell) {
        return cell == null || cell.getCellType()== CellType.BLANK;
    }

    public static boolean checkIsCellTypeString(Cell cell) {
        return cell.getCellType() == CellType.STRING;
    }

    public static boolean checkIsNotMatchingStringCell(Cell cell, String name) {
        return cell.getCellType() != CellType.STRING
                || !Objects.equals(cell.getStringCellValue(), name);
    }

    public static boolean checkIsInvalidDateCell(Cell cell) {
        return checkIsCellEmptyWithCellTypeNumeric(cell) ||
                !DateUtil.isCellDateFormatted(cell);
    }

    public static boolean checkIsDateOutOfRange(LocalDate date) {
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(5);
        LocalDate maxDate = now.plusYears(5);

        return date.isBefore(minDate) || date.isAfter(maxDate);
    }

    private static boolean checkIsCellEmptyWithCellTypeNumeric(Cell cell) {
        return cell == null || cell.getCellType() != CellType.NUMERIC;
    }

}