package com.Dev.CreateEstimatingPlan.writerExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelCellStyleBuilder {
    private final Workbook workbook;
    private final CellStyle style;
    private final Font font;

    public ExcelCellStyleBuilder(Workbook workbook) {
        this.workbook = workbook;
        this.style = workbook.createCellStyle();
        this.font = workbook.createFont();
    }

    public ExcelCellStyleBuilder setFontSize(short size) {
        font.setFontHeightInPoints(size);
        return this;
    }

    public ExcelCellStyleBuilder setBold(boolean bold) {
        font.setBold(bold);
        return this;
    }

    public ExcelCellStyleBuilder setItalic(boolean italic) {
        font.setItalic(italic);
        return this;
    }

    public ExcelCellStyleBuilder setFormatData(String format) {
        CreationHelper helperDate = workbook.getCreationHelper();
        style.setDataFormat(helperDate.createDataFormat().getFormat(format));
        return this;
    }

    public ExcelCellStyleBuilder setHorizontalAlignment(HorizontalAlignment alignment) {
        style.setAlignment(alignment);
        return this;
    }

    public ExcelCellStyleBuilder setWrap(boolean wrap) {
        style.setWrapText(wrap);
        return this;
    }

    public ExcelCellStyleBuilder setBorder(BorderStyle borderStyle) {
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        return this;
    }

    public ExcelCellStyleBuilder setFillColor(Colors color) {
        style.setFillForegroundColor(color.createColor());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return this;
    }

    public CellStyle build() {
        style.setFont(font);
        return style;
    }
}
