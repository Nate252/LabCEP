package com.Dev.CreateEstimatingPlan.writerExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreatorStylesTest {
    private final static short FONT_SIZE = 14;
    private final static String DATA_FORMAT_LAB = "dd\".\"mmm";
    private Workbook workbook;
    private CreatorStyles creatorStyles;
    private Map<String, CellStyle> map;

    @BeforeEach
    void setUp() {
        workbook = new XSSFWorkbook();
        creatorStyles = new CreatorStyles(workbook);
        map = creatorStyles.createMapStyles();
    }

    @Test
    void createListStudentsBase_True_WithBaseStyle() {
        CellStyle testStyle = map.get(BASE_STYLE);
        Font font = workbook.getFontAt(testStyle.getFontIndex());

        assertThat(font.getFontHeightInPoints()).isEqualTo(FONT_SIZE);
        assertThat(testStyle.getBorderTop()).isEqualTo(BorderStyle.THIN);
    }

    @Test
    void createBaseColor_True_WithBaseColorYellow() {
        CellStyle testStyle = map.get(ExcelBaseStyles.HEADER_STYLE_YELLOW_14);
        XSSFCellStyle xssfStyle = (XSSFCellStyle) testStyle;
        XSSFColor color = xssfStyle.getFillForegroundXSSFColor();

        assertThat(color.getRGB()).containsExactly(Colors.YELLOW.getRgb());
    }

    @Test
    void createListStudentsBaseWithWrap_True_WithBaseWrap() {
        CellStyle testStyle = map.get(ExcelBaseStyles.BASE_STYLE_WRAP);
        assertThat(testStyle.getWrapText()).isTrue();
    }

    @Test
        ////////////////////////////////////
    void createBaseDateStyle_True_WithBaseDateFormat() {
        CellStyle testStyle = map.get(ExcelBaseStyles.FORMAT_DATE_LAB);

        DataFormat dataFormat = workbook.createDataFormat();
        short formatIndex = dataFormat.getFormat(DATA_FORMAT_LAB);

        assertThat(testStyle.getDataFormat()).isEqualTo(formatIndex);
    }

    @Test
    void createListStudentsHeader_True_WithStudentsHeader() {
        CellStyle testStyle = map.get(ExcelBaseStyles.HEADER);

        Font font = workbook.getFontAt(testStyle.getFontIndex());

        assertThat(font.getBold()).isTrue();
        assertThat(testStyle.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
    }

    @Test
    void createHeaderColor_True_WithHeaderWithColor() {
        CellStyle testStyle = map.get(ExcelBaseStyles.HEADER_STYLE_YELLOW_14);

        XSSFCellStyle xssfStyle = (XSSFCellStyle) testStyle;
        Font font = workbook.getFontAt(testStyle.getFontIndex());

        assertThat(font.getBold()).isTrue();
        assertThat(testStyle.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
        assertThat(xssfStyle.getFillForegroundXSSFColor().getRGB()).containsExactly(Colors.YELLOW.getRgb());
    }

    @Test
    void createHeaderWithItalic_True_WithItalicCenterBold() {
        CellStyle testStyle = map.get(ExcelBaseStyles.HEADER_ITALIC_10);

        Font font = workbook.getFontAt(testStyle.getFontIndex());

        assertThat(font.getBold()).isTrue();
        assertThat(font.getItalic()).isTrue();
        assertThat(testStyle.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
    }

    @Test
    void createMapStyles_True_WithSixStylesToCompare() {
        Map<String, CellStyle> map = creatorStyles.createMapStyles();

        assertThat(map).containsKeys(
                BASE_STYLE,
                HEADER,
                FORMAT_DATE_LAB,
                HEADER_STYLE_YELLOW_14,
                HEADER_STYLE_GREEN_12,
                BASE_STYLE_PIPI_12
        );
        assertThat(map).hasSize(16);
    }
}
