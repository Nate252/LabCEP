package com.Dev.CreateEstimatingPlan.templateExcelReader;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.validator.ExcelCellValidator;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleService;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FileReaderMain implements Iterator<Row> {
    private final Workbook workbook;
    private final Iterator<Row> rowIterator;
    private Row currentRow;


    public FileReaderMain(Workbook workbook) {
        this.workbook = workbook;
        this.rowIterator = workbook.getSheet(
                ExcelLocaleProvider.getMessage("sheet.structure_of_study.name")
        ).iterator();
        if (rowIterator.hasNext()) {
            currentRow = rowIterator.next();
        }
    }

    @Override
    public boolean hasNext() {
        return rowIterator.hasNext();
    }

    @Override
    public Row next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentRow = rowIterator.next();
        return currentRow;
    }

    private boolean checkSheetSaturdaysIsEmpty() {
        String sheetSrDaysName = ExcelLocaleProvider.getMessage("sheet.saturday_postponement.name");
        Sheet saturdays = workbook.getSheet(sheetSrDaysName);

        for (int i = 1; i <= 4; i++) {
            Row row = saturdays.getRow(i);
            if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(row.getCell(0))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public StudyPlan createPlan() {
        String sheetEvalName = ExcelLocaleProvider.getMessage("sheet.evaluation_structure.name");
        String sheetSrDaysName = ExcelLocaleProvider.getMessage("sheet.saturday_postponement.name");


        String nameDiscipline = readNameDiscipline();
        List<String> namesOfGroups = readNamesOfGroups();
        ScheduleService scheduleService;
        MainEvaluation mainEvaluation;

        FileReaderSchedules fileReaderSchedules;
        FileReaderEvaluation fileReaderEvaluation = new FileReaderEvaluation(workbook.getSheet(sheetEvalName));

        if (checkSheetSaturdaysIsEmpty()) {
            fileReaderSchedules = new FileReaderSchedules(rowIterator);
        } else {
            fileReaderSchedules = new FileReaderSchedules(rowIterator, workbook.getSheet(sheetSrDaysName));
        }
        scheduleService = fileReaderSchedules.readScheduleService();
        mainEvaluation = fileReaderEvaluation.readEducationComponentMain();

        if (scheduleService.getLecture() == null && mainEvaluation.lectureIsExists()) {
            generateExceptionExcel("В Теорія є елемент Лекції, " +
                    "проте в Структура вивчення не вказано розклад для лекцій");
        }
        if (scheduleService.getLessons() == null && mainEvaluation.getPracticeComponent().getTasks() != null) {
            generateExceptionExcel("Не вказано розклад для Лабораторних/Практичних занять");
        }

        if (scheduleService.getLessons() != null && !scheduleService.compareNumberOfGroups(namesOfGroups)) {
            generateExceptionExcel("Не створено розклад для групи з списку груп або" +
                    " створено розклад для групи якої немає в списку груп");
        }


        return StudyPlan.builder()
                .nameDiscipline(nameDiscipline)
                .namesGroupsStudent(namesOfGroups)
                .scheduleService(scheduleService)
                .mainEvaluation(mainEvaluation)
                .build();
    }

    private String readNameDiscipline() {
        String codeName = ExcelLocaleProvider.getMessage("sheet.structure_of_study.nameDiscipline");
        String nameDiscipline;
        while (!currentRow.getCell(0).getStringCellValue().startsWith(codeName)) {
            next();
        }

        Cell nameDisciplineCell = currentRow.getCell(1);

        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(nameDisciplineCell)) {
            generateExceptionExcel(0, "Невірно вказано назву дисципліни");
        }
        nameDiscipline = nameDisciplineCell.getStringCellValue();

        next();
        return nameDiscipline;
    }

    private List<String> readNamesOfGroups() {
        List<String> namesOfGroups = new ArrayList<>();
        int lastCellNum = currentRow.getLastCellNum();
        for (int i = 1; i < lastCellNum; i++) {
            if (ExcelCellValidator.checkIsCellStringNull(currentRow.getCell(i))) {
                continue;
            }
            if (!ExcelCellValidator.checkIsCellTypeString(currentRow.getCell(i))) {
                generateExceptionExcel(i, "Невірно вказано назву групи");
            }
            Cell groupName = currentRow.getCell(i);
            namesOfGroups.add(groupName.getStringCellValue());
        }
        if (namesOfGroups.isEmpty()) {
            generateExceptionExcel(1, "Не вказано жодної групи для вивчення");
        }
        return namesOfGroups;
    }

    private void generateExceptionExcel(int cell, String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                currentRow.getCell(cell).getAddress().formatAsString(),
                message
        );
    }

    private void generateExceptionExcel(String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                message
        );
    }
}


