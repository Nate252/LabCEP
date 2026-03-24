package com.Dev.CreateEstimatingPlan.writerExcel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.poi.ss.usermodel.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
class ExcelCellStyleBuilderTest {
    private Workbook workbook;

    @BeforeEach
    void setUp() {
        workbook = new XSSFWorkbook();
    }

    @Test
    void setFontSize() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setFontSize((short) 16)
                .build();
        assertThat(16).isEqualTo(workbook.getFontAt(style.getFontIndex()).getFontHeightInPoints());
    }

    @Test
    void setBold() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setBold(true)
                .build();
        assertThat(workbook.getFontAt(style.getFontIndex()).getBold()).isTrue();
    }

    @Test
    void setItalic() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setItalic(true)
                .build();
        assertThat(workbook.getFontAt(style.getFontIndex()).getItalic()).isTrue();
    }

    @Test
    void setFormatData() {
        String formatDate = "dd.MM.yyyy";
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setFormatData(formatDate)
                .build();

        short formatIndex = style.getDataFormat();
        String actualFormat = workbook.getCreationHelper().createDataFormat().getFormat(formatIndex);

        assertThat(formatDate).isEqualTo(actualFormat);
    }

    @Test
    void setHorizontalAlignment() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .build();

        assertThat(HorizontalAlignment.CENTER).isEqualTo(style.getAlignment());
    }

    @Test
    void setWrap() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setWrap(true)
                .build();

        assertThat(style.getWrapText()).isTrue();
    }

    @Test
    void setBorder() {
        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setBorder(BorderStyle.THICK)
                .build();

        assertThat(BorderStyle.THICK).isEqualTo(style.getBorderTop());
        assertThat(BorderStyle.THICK).isEqualTo(style.getBorderBottom());
        assertThat(BorderStyle.THICK).isEqualTo(style.getBorderLeft());
        assertThat(BorderStyle.THICK).isEqualTo(style.getBorderRight());
    }

    @Test
    void setFillColor() {
        Workbook workbook = new XSSFWorkbook();
        Colors testColor = Colors.YELLOW;

        CellStyle style = new ExcelCellStyleBuilder(workbook)
                .setFillColor(testColor)
                .build();

        XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
        XSSFColor cellColor = xssfStyle.getFillForegroundXSSFColor();
        assertThat(cellColor).isNotNull();
        assertThat(testColor.getRgb()).isEqualTo(cellColor.getRGB());
    }
}