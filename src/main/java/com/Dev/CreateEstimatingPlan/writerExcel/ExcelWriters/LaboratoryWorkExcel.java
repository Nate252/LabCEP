package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import lombok.Builder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;

import java.io.IOException;
import java.util.List;

public class LaboratoryWorkExcel extends AbstractTemplate {
    private final Sheet sheet;
    private final List<String> studentsGroupsNames;
    private final PracticeComponent practiceComponent;

    @Builder
    public LaboratoryWorkExcel(SharedData sharedData, List<String> studentsGroupsNames, PracticeComponent practiceComponent) {
        super(sharedData);
        sheet = workbook.createSheet(ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.name"));
        this.studentsGroupsNames = studentsGroupsNames;
        this.practiceComponent = practiceComponent;
    }

    @Override
    void createHeader() {
        CellStyle headerItalicStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_ITALIC_10);
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_12);
        writeStudentsHeader(sheet, 1);

        Row labNamesRow = sheet.getRow(0);
        Row labElements = sheet.getRow(1);

        int counter = 4;
        for (Tasks tasks : practiceComponent.getTasks()) {
            int first = counter;

            Cell labName = labNamesRow.createCell(counter);
            labName.setCellValue(tasks.getComponent().getName());
            labName.setCellStyle(headerStyle);

            Cell grade = labElements.createCell(counter);
            grade.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.grade"));
            grade.setCellStyle(headerItalicStyle);

            Cell dateCompleted = labElements.createCell(++counter);
            dateCompleted.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.completed"));
            dateCompleted.setCellStyle(headerItalicStyle);

            Cell dateDefence = labElements.createCell(++counter);
            dateDefence.setCellValue(ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.defence"));
            dateDefence.setCellStyle(headerItalicStyle);

            List<String> listSubTasks = tasks.getSubTasks();

            for (int i = 0, j = 1; i < listSubTasks.size(); i++, j++) {
                Cell subtaskCell = labElements.createCell(++counter);
                subtaskCell.setCellStyle(headerItalicStyle);
                subtaskCell.setCellValue(j);
                setComment(subtaskCell, listSubTasks.get(i));
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, first, counter));

            sharedData.getListNumCellsLaboratoryScope().add(first);

            counter++;
        }
        sharedData.getListNumCellsLaboratoryScope().add(counter);
    }

    @Override
    void createContent() {
        CellStyle baseStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_10);
        CellStyle dateStyle = sharedData.getStyle(ExcelBaseStyles.FORMAT_DATE_LAB);

        String[] typeOfGrade = {"0.1", "0.5", "0.7", "1.0"};
        List<Integer> listIndexes = sharedData.getListNumCellsLaboratoryScope();
        int firstIndex;


        writeStudentsLinks(studentsGroupsNames, sheet, 2);


        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            for (int j = 0; j < listIndexes.size() - 1; j++) {
                firstIndex = listIndexes.get(j);

                createDropDown(typeOfGrade, i, firstIndex++, baseStyle);
                createCellDate(row, firstIndex++,dateStyle);
                createCellDate(row, firstIndex++,dateStyle);

                if (j + 1 < listIndexes.size()) {
                    while (firstIndex != listIndexes.get(j + 1)) {
                        Cell cell = row.createCell(firstIndex++);
                        cell.setCellStyle(baseStyle);
                    }
                }
            }
        }
    }


    private void createDropDown(String[] values, int IndexRow, int columnIndex, CellStyle cellStyle) {
        CellRangeAddressList addressList = new CellRangeAddressList(IndexRow, IndexRow, columnIndex, columnIndex);

        createDropDownList(values, addressList);
        sheet.getRow(IndexRow).createCell(columnIndex).setCellStyle(cellStyle);
    }

    public void createDropDownList(String[] values, CellRangeAddressList addressList) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }

    private void createCellDate(Row row, int indexCell, CellStyle cellStyle) {
        Cell cell = row.createCell(indexCell);
        cell.setCellStyle(cellStyle);
    }

    private void setComment(Cell cell, String commentMessage) {
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        CreationHelper creationHelper = workbook.getCreationHelper();
        XSSFClientAnchor anchor = (XSSFClientAnchor) creationHelper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setCol2(0);
        anchor.setRow1(0);
        anchor.setRow2(0);

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(creationHelper.createRichTextString(commentMessage));
        cell.setCellComment(comment);
    }
}
