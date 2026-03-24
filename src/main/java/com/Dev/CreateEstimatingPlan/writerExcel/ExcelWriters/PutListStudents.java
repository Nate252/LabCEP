package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class PutListStudents {

    public static void writeHeaderStudentsList(Sheet sheet, SharedData sharedData, int secondRow) {
        CellStyle cellStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_12);
        String nameSheet = ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.name");

        Row headerRowName = sheet.createRow(0);
        Cell disciplineName = headerRowName.createCell(2);
        disciplineName.setCellFormula(nameSheet + "!C" + 1);
        disciplineName.setCellStyle(cellStyle);


        Row headerRowNames = sheet.createRow(secondRow); // start row default 1


        Cell counter = headerRowNames.createCell(0);
        counter.setCellFormula(nameSheet + "!A" + 2);
        counter.setCellStyle(cellStyle);

        Cell groupName = headerRowNames.createCell(1);
        groupName.setCellFormula(nameSheet + "!B" + 2);
        groupName.setCellStyle(cellStyle);

        Cell fio = headerRowNames.createCell(2);
        fio.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.fio"));
        fio.setCellStyle(cellStyle);

        Cell campus = headerRowNames.createCell(3);
        campus.setCellFormula(nameSheet + "!I" + 2);
        campus.setCellStyle(cellStyle);


        sheet.setAutoFilter(new CellRangeAddress(secondRow,secondRow,0,3));
        sheet.createFreezePane(0,++secondRow); // need +1

    }
    public static void writeStudents(Sheet listSheet, List<String> groupNames, SharedData sharedData,int rowStart) {
        CellStyle cellStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_12);
        int rowIndex = rowStart; // startRowDefault 2

        for (String groupName : groupNames) {

            Pair<Integer, Integer> rows = sharedData.getListNumGroups().get(groupName);
            rowIndex = createListStudentsData(listSheet,rows,cellStyle, rowIndex);
        }
    }


    private static int createListStudentsData(Sheet listStudents,
                                              Pair<Integer,Integer> interval, CellStyle cellStyle, int rowIndex) {
        String nameSheet = ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.name");

        for (int i = interval.a; i <= interval.b; i++) {
            Row row = listStudents.createRow(rowIndex);


            Cell numberStudent = row.createCell(0);
            numberStudent.setCellFormula(nameSheet + "!A" + i);
            numberStudent.setCellStyle(cellStyle);

            Cell groupStudent = row.createCell(1);
            groupStudent.setCellFormula(nameSheet + "!B" + i);
            groupStudent.setCellStyle(cellStyle);

            Cell fio = row.createCell(2);
            fio.setCellFormula(nameSheet + "!C" + i +
                    "&\" \"&LEFT("+ nameSheet +"!D"
                    + i + ",1)&\".\"&LEFT("+ nameSheet
                    +"!E" + i + ",1)&\".\"");
            fio.setCellStyle(cellStyle);

            Cell campus = row.createCell(3);
            campus.setCellFormula(nameSheet + "!I" + i);
            campus.setCellStyle(cellStyle);

            rowIndex++;
        }
        return rowIndex;
    }
}