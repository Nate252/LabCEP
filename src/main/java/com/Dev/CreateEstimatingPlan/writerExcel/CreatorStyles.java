package com.Dev.CreateEstimatingPlan.writerExcel;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class CreatorStyles {
    private final Workbook workbook;
    private final static short FONT_SIZE = 14;
    private final static short FONT_SIZE_SMALL = 12;
    private final static short FONT_SIZE_SMALLER = 10;
    private final static String DATA_FORMAT_SCHEDULE = "dd\".\"mm";
    private final static String DATA_FORMAT_LAB = "dd\".\"mmm";

    public CreatorStyles(Workbook workbook) {
        this.workbook = workbook;
    }

    private CellStyle createListStudentsBase(short fSize) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBorder(BorderStyle.THIN)
                .build();
    }

    private CellStyle createBaseColor(short fSize, Colors color) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBorder(BorderStyle.THIN)
                .setFillColor(color)
                .build();
    }

    private CellStyle createListStudentsBaseWithWrap(short fSize) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBorder(BorderStyle.THIN)
                .setWrap(true)
                .build();
    }

    private CellStyle createBaseDateStyle(short fSize, String dateFormat) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBorder(BorderStyle.THIN)
                .setFormatData(dateFormat)
                .build();
    }

    private CellStyle createListStudentsHeader(short fSize) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBold(true)
                .setBorder(BorderStyle.THIN)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .build();
    }

    private CellStyle createHeaderColor(short fSize, Colors color) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBold(true)
                .setBorder(BorderStyle.THIN)
                .setFillColor(color)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .build();
    }

    private CellStyle createHeaderWithItalic(short fSize) {
        ExcelCellStyleBuilder styleBuilder = new ExcelCellStyleBuilder(workbook);

        return styleBuilder
                .setFontSize(fSize)
                .setBold(true)
                .setItalic(true)
                .setBorder(BorderStyle.THIN)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .build();
    }

    public Map<String, CellStyle> createMapStyles() {
        Map<String, CellStyle> styleMap = new HashMap<>();

        // List Students
        styleMap.put(ExcelBaseStyles.BASE_STYLE, createListStudentsBase(FONT_SIZE));
        styleMap.put(ExcelBaseStyles.BASE_STYLE_WRAP, createListStudentsBaseWithWrap(FONT_SIZE));
        styleMap.put(ExcelBaseStyles.HEADER, createListStudentsHeader(FONT_SIZE));

        // Shorted list Students
        styleMap.put(ExcelBaseStyles.BASE_STYLE_12, createListStudentsBase(FONT_SIZE_SMALL));
        styleMap.put(ExcelBaseStyles.HEADER_12, createListStudentsHeader(FONT_SIZE_SMALL));

        // Schedules
        styleMap.put(ExcelBaseStyles.FORMAT_DATE_SCHEDULE, createBaseDateStyle(FONT_SIZE_SMALL, DATA_FORMAT_SCHEDULE));

        //LABORATORY BASE_STYLE_10
        styleMap.put(ExcelBaseStyles.HEADER_ITALIC_10, createHeaderWithItalic(FONT_SIZE_SMALLER));
        styleMap.put(ExcelBaseStyles.BASE_STYLE_10, createListStudentsBase(FONT_SIZE_SMALLER));
        styleMap.put(ExcelBaseStyles.FORMAT_DATE_LAB, createBaseDateStyle(FONT_SIZE_SMALL, DATA_FORMAT_LAB));

        //Class Dates
        styleMap.put(ExcelBaseStyles.HEADER_STYLE_YELLOW_14, createHeaderColor(FONT_SIZE, Colors.YELLOW));
        styleMap.put(ExcelBaseStyles.FORMAT_DATE_SCHEDULE_14, createBaseDateStyle(FONT_SIZE, DATA_FORMAT_SCHEDULE));

        // Final evaluation
        styleMap.put(ExcelBaseStyles.HEADER_STYLE_LIGHT_GREEN_12, createHeaderColor(FONT_SIZE_SMALL, Colors.LIGHT_GREEN));
        styleMap.put(ExcelBaseStyles.HEADER_STYLE_GREEN_12, createHeaderColor(FONT_SIZE_SMALL, Colors.GREEN));
        styleMap.put(ExcelBaseStyles.HEADER_STYLE_BLUE_12, createHeaderColor(FONT_SIZE_SMALL, Colors.BLUE));
        styleMap.put(ExcelBaseStyles.HEADER_STYLE_BORROW_12, createHeaderColor(FONT_SIZE_SMALL, Colors.BORROW));
        styleMap.put(ExcelBaseStyles.BASE_STYLE_PIPI_12, createBaseColor(FONT_SIZE_SMALL, Colors.PIPI));
        return styleMap;
    }
}
