package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

@Getter
public class ListStudentsFileExcel extends AbstractTemplate {
    private final String nameDiscipline;
    private final List<Group> groups;
    private final Sheet sheet;
    private final static short FONT_SIZE = 14;

    @Builder
    public ListStudentsFileExcel(String nameDiscipline, List<Group> groups,
                                 SharedData sharedData) {
        super(sharedData);
        this.nameDiscipline = nameDiscipline;
        this.groups = groups;
        sheet = workbook.createSheet(ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.name"));
    }


    @Override
    void createHeader() {
        final String[] headers = {"#",
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.group"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.surname"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.name"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.middle"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.email"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.phone"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.contract"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.campus"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.country")};

        createRowWithValues(0, 2, nameDiscipline);
        createRowWithValues(1, 0, headers);

        sheet.createFreezePane(0, 2);
        sheet.setAutoFilter(new CellRangeAddress(1, 1, 0, 9));
    }

    private void createRowWithValues(int rowIndex, int startColumn, String... values) {
        Row row = sheet.createRow(rowIndex);
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(startColumn + i);
            cell.setCellValue(values[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    @Override
    void createContent() {
        int counterRow = 2;
        String[] types = {
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.yes"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.no")};
        String[] countries = {
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.abroad"),
                ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.uk")};

        CellStyle textStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE);
        CellStyle wrapStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_WRAP);

        int start;
        int end;

        for (Group group : groups) {
            int i = 1;
            start = counterRow + 1;
            for (Student student : group.getStudents()) {

                Row row = sheet.createRow(counterRow);

                createCellWithStyle(row, i, textStyle);
                createCellWithStyle(row, 1, group.getName(), textStyle);
                createCellWithStyle(row, 2, student.getSurname().toUpperCase(), textStyle);
                createCellWithStyle(row, 3, student.getName(), textStyle);
                createCellWithStyle(row, 4, student.getMiddleName(), textStyle);
                createCellWithStyle(row, 5, student.getEmail(), textStyle);

                createCellWithStyle(row, 6, student.getPhoneNumbers(), wrapStyle);
                row.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * 2);

                setDropDowns(counterRow, types, countries, textStyle);


                counterRow++;
                i++;
            }
            end = counterRow;

            sharedData.getListNumGroups().put(group.getName(), new Pair<>(start, end));


            System.out.println(sharedData);
        }
    }

    private void createCellWithStyle(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCellWithStyle(Row row, int value, CellStyle style) {
        Cell cell = row.createCell(0);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void setDropDowns(int rowIndex, String[] types, String[] countries, CellStyle cellStyle) {
        createDropDown(types, rowIndex, 7, cellStyle);
        createDropDown(types, rowIndex, 8, cellStyle);
        createDropDown(countries, rowIndex, 9, cellStyle);
    }

    private void createDropDown(String[] values, int rowIndex, int columnIndex, CellStyle cellStyle) {
        createDropDownList(values, new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex));
        Cell cell = sheet.getRow(rowIndex).createCell(columnIndex);
        cell.setCellValue(values[1]);
        cell.setCellStyle(cellStyle);
    }

    private void createDropDownList(String[] values, CellRangeAddressList addressList) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }

}
