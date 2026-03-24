package com.Dev.CreateEstimatingPlan.templateExcelReader;

import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDate;
import java.util.Date;

public class LocalDateParser {
    public static LocalDate excelDateToLocalDate(double excelDate) {
        Date date = DateUtil.getJavaDate(excelDate);
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
