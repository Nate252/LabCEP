package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import lombok.Builder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScheduleFileExcel extends AbstractTemplate{
    private final Sheet sheet;
    private final List<LocalDate> localDatesClasses;
    private final List<String> studentsGroupsNames;
    private final static short FONT_SIZE = 12;

    @Builder
    public ScheduleFileExcel(String sheetName, List<LocalDate> localDatesClasses, SharedData sharedData, List<String> studentsGroupsNames) {
        super(sharedData);
        this.sheet = workbook.createSheet(sheetName);
        this.localDatesClasses = localDatesClasses;
        this.studentsGroupsNames = studentsGroupsNames;
    }

    @Override
    void createHeader() {
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_12);
        CellStyle dateStyle = sharedData.getStyle(ExcelBaseStyles.FORMAT_DATE_SCHEDULE);


        writeStudentsHeader(sheet,2);

        Row firstRow = sheet.getRow(0);
        Cell weekCounter =  firstRow.createCell(firstRow.getLastCellNum());
        weekCounter.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.lecture.header.number_of_week"));
        weekCounter.setCellStyle(headerStyle);

        Row secondRow = sheet.createRow(1);
        Cell classesCounter =  secondRow.createCell(3);
        classesCounter.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.lecture.header.number_of_class"));
        classesCounter.setCellStyle(headerStyle);


        Map<LocalDate, Integer> weekMap = new HashMap<>();
        int weekNumber = 1;
        LocalDate previousDate = null;

        for (LocalDate date : localDatesClasses) {

            if (previousDate != null && date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) != previousDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                weekNumber++;
            }
            weekMap.put(date, weekNumber);
            previousDate = date;
        }

        Row thirdRow = sheet.getRow(2);


        int counter =  thirdRow.getLastCellNum();
        int classNumber = 0;

        for (LocalDate date : localDatesClasses) {
            classNumber++;

            Cell dateCell  = thirdRow.createCell(counter);
            dateCell.setCellValue(date);
            dateCell.setCellStyle(dateStyle);

            Cell classCell = secondRow.createCell(counter);
            classCell.setCellValue(classNumber);
            classCell.setCellStyle(headerStyle);


            // Записуємо номер тижня в перший рядок
            Cell weekCell = firstRow.createCell(counter);
            weekCell.setCellValue(weekMap.get(date));
            weekCell.setCellStyle(headerStyle);

            counter++;
        }
        sharedData.setMaxLectureClasses(classNumber);

        Cell sumScopeHeader = thirdRow.createCell(thirdRow.getLastCellNum());
        sumScopeHeader.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.lecture.header.total"));
        sumScopeHeader.setCellStyle(headerStyle);
    }

    @Override
    void createContent() {
        CellStyle baseStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_12);
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_12);
        String sheetLecture = ExcelLocaleProvider.getMessage("name.component.lecture");


        writeStudentsLinks(studentsGroupsNames, sheet,3);


        //start row 2   3
        // start cell 4


//   formula.setCellFormula("SUM(B" + (i + 1) + ":C" + (i + 1) +")");

        // start row for students
        for (int j = 3,firstCell = 4; j <= sheet.getLastRowNum(); j++,firstCell++) {
            Row startRow = sheet.getRow(j);
            for (int i = 0, counter = 4; i < localDatesClasses.size(); i++,counter++) {
                startRow.createCell(counter).setCellStyle(baseStyle);
            }
            Cell sumScope = startRow.createCell(startRow.getLastCellNum());
            String lastCellScopeAddress = startRow.getCell(startRow.getLastCellNum() - 2).getAddress().formatAsString();
            sumScope.setCellFormula("SUM(E" + firstCell + ":" + lastCellScopeAddress + ")");

            XSSFFormulaEvaluator formulaEvaluator =
                    (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
            formulaEvaluator.evaluateFormulaCell(sumScope);
            sumScope.setCellStyle(headerStyle);
        }
        if (Objects.equals(sheet.getSheetName(), sheetLecture)) {
            Cell lastCellScopes = sheet.getRow(3).getCell(sheet.getRow(3).getLastCellNum() - 1);
            String lastCellAddress = lastCellScopes.getAddress().formatAsString();
            System.out.println("PR " + lastCellAddress);
            String lastCellAddressLetter = lastCellAddress.replaceAll("[0-9]", "");
            System.out.println("MD " + lastCellAddressLetter);
            sharedData.setLastCellLectureScopeAddress(lastCellAddressLetter);
        }
    }

    // можно винести в абстракт з додавнням параметру стилю(Laboratory + Schedule)
//    private CellStyle createHeadCellStyleDate(short fontSize) {
//       CellStyle dataStyle = createBaseCellStyle(fontSize);
//       CreationHelper helperDate = workbook.getCreationHelper();
//       dataStyle.setDataFormat(helperDate.createDataFormat().getFormat("dd\".\"mm"));
//        return dataStyle;
//    }

}
