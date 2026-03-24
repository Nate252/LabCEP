package com.Dev.CreateEstimatingPlan.validator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ExcelCellValidatorTest {
// Should_ThrowException_When_AgeLessThan18
    @Test
    void checkNumCellWithoutZero_True_CellIsNull() {
        assertThat(ExcelCellValidator.checkNumCellWithoutZero(null)).isTrue();
    }
    @Test
    void checkNumCellWithoutZero_True_CellIsNotNumeric() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        assertThat(ExcelCellValidator.checkNumCellWithoutZero(cell)).isTrue();
    }
    @Test
    void checkNumCellWithoutZero_True_CellValueIsZeroOrNegative() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(0.0);
        assertThat(ExcelCellValidator.checkNumCellWithoutZero(cell)).isTrue();
    }
    @Test
    void checkNumCellWithoutZero_False_CellValueIsPositive() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(10.0);
        assertThat(ExcelCellValidator.checkNumCellWithoutZero(cell)).isFalse();
    }

    @Test
    void checkNumCellWithZero_False_CellValueIsZero() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(0.0);
        assertThat(ExcelCellValidator.checkNumCellWithZero(cell)).isFalse();
    }

    @Test
    void checkNumCellWithZero_True_CellValueIsNegative() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(-5.0);
        assertThat(ExcelCellValidator.checkNumCellWithZero(cell)).isTrue();
    }


    @Test
    void checkIsInvalidStringCell_True_CellValueIsBlank() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("");
        assertThat(ExcelCellValidator.checkIsCellEmptyWithCellTypeString(cell)).isTrue();
    }

    @Test
    void checkIsInvalidStringCellWithPrefix_True_PrefixIsNotMatch() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("#lab1");

        assertThat(ExcelCellValidator.checkIsInvalidStringCellWithPrefix(cell, "*")).isTrue();
    }
    @Test
    void checkIsInvalidStringCellWithPrefix_True_CellTypeIsBlank() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.BLANK);
        when(cell.getStringCellValue()).thenReturn("");

        assertThat(ExcelCellValidator.checkIsInvalidStringCellWithPrefix(cell, "*")).isTrue();
    }
    @Test
    void checkIsInvalidStringCellWithPrefix_False_PrefixIsNotMatch() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("#lab1");

        assertThat(ExcelCellValidator.checkIsInvalidStringCellWithPrefix(cell, "#"))
                .isFalse();
    }
    @Test
    void checkIsCellStringNull_True_CellIsNull() {
        assertThat(ExcelCellValidator.checkIsCellStringNull(null)).isTrue();
    }
    @Test
    void checkIsCellTypeString_True_CellIsNull() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        assertThat(ExcelCellValidator.checkIsCellTypeString(cell)).isTrue();
    }


    @Test
    void checkIsCellEmptyWithCellTypeString_True_CellTypeIsString() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("");
        assertThat(ExcelCellValidator.checkIsCellEmptyWithCellTypeString(cell)).isTrue();
    }

    @Test
    void checkIsNotMatchingStringCell_True_CellTypeIsNotString() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.BOOLEAN);

        assertThat(ExcelCellValidator.checkIsNotMatchingStringCell(cell, "name"))
                .isTrue();
    }

    @Test
    void checkIsNotMatchingStringCell_True_CellValueIsDiffer() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("lab1");

        assertThat(ExcelCellValidator.checkIsNotMatchingStringCell(cell, "name"))
                .isTrue();
    }

    @Test
    void checkIsInvalidDateCell_True_CellValueIsNotDate() {
        Cell cell = mock(Cell.class);
        CellStyle style = mock(CellStyle.class);

        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getCellStyle()).thenReturn(style);
        when(style.getDataFormat()).thenReturn((short) 2);
        when(cell.getNumericCellValue()).thenReturn(54816.0);


        assertThat(ExcelCellValidator.checkIsInvalidDateCell(cell)).isTrue();
    }
    @Test
    void checkIsInvalidDateCell_False_CellValueIsNotDate() {
        Cell cell = mock(Cell.class);
        CellStyle style = mock(CellStyle.class);

        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getCellStyle()).thenReturn(style);
        when(style.getDataFormat()).thenReturn((short) 14);
        when(cell.getNumericCellValue()).thenReturn(54816.0);

        assertThat(ExcelCellValidator.checkIsInvalidDateCell(cell)).isFalse();
    }

    @Test
    void checkIsDateOutOfRange_True_DateIsOld() {
        LocalDate oldDate = LocalDate.now().minusYears(10);
        assertThat(ExcelCellValidator.checkIsDateOutOfRange(oldDate))
                .isTrue();
    }
    @Test
    void checkIsDateOutOfRange_True_DateIsInFuture() {
        LocalDate futureDate = LocalDate.now().plusYears(10);
        assertThat(ExcelCellValidator.checkIsDateOutOfRange(futureDate))
                .isTrue();
    }
    @Test
    void checkIsDateOutOfRange_False_DateIsInRange() {
        LocalDate validDate = LocalDate.now();
        assertThat(ExcelCellValidator.checkIsDateOutOfRange(validDate))
                .isFalse();
    }
}