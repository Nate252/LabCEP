package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

@Getter
public abstract class AbstractTemplate {
    protected Workbook workbook;
    protected final SharedData sharedData;

    public AbstractTemplate(SharedData sharedData) {
        this.workbook = sharedData.getWorkbook();
        this.sharedData = sharedData;
    }

    public final void createExcel() {
        createHeader();
        createContent();
    }

    abstract void createHeader();

    abstract void createContent();

    protected void writeStudentsHeader(Sheet sheet, int secondRowStart) {
        PutListStudents.writeHeaderStudentsList(sheet, sharedData, secondRowStart);
    }

    protected void writeStudentsLinks(List<String> groupName, Sheet sheet, int startRow) {
        PutListStudents.writeStudents(sheet, groupName, sharedData, startRow);
    }
}
