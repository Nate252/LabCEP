package com.Dev.CreateEstimatingPlan.writerExcel;

import lombok.Getter;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

@Getter
public enum Colors {
    PIPI(new byte[]{(byte) 255, (byte) 242, (byte) 204}),
    LIGHT_GREEN(new byte[]{(byte) 182, (byte) 215, (byte) 168}),
    GREEN(new byte[]{(byte) 0, (byte) 255, (byte)0}),
    BLUE(new byte[]{(byte) 0, (byte) 255, (byte)255}),
    BORROW(new byte[]{(byte) 246, (byte) 178, (byte)107}),
    YELLOW(new byte[]{(byte) 255, (byte) 255, (byte)0}),
    MAGENTA(new byte[]{(byte) 255, (byte) 0, (byte)255});

    private final byte[] rgb;
    Colors(byte[] rgb) {
        this.rgb = rgb;
    }
    public XSSFColor createColor() {
        return new XSSFColor(rgb, new DefaultIndexedColorMap());
    }
}
