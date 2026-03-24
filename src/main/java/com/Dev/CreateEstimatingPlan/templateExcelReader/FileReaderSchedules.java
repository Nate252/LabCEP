package com.Dev.CreateEstimatingPlan.templateExcelReader;


import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.validator.ExcelCellValidator;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.*;
import org.apache.poi.ss.usermodel.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


public class FileReaderSchedules implements Iterator<Row> {
    private final Iterator<Row> rowIterator;
    private Row currentRow;


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

    private final Sheet saturdaysSheet;

    public FileReaderSchedules(Iterator<Row> iterator) {
        this.rowIterator = iterator;
        if (iterator.hasNext()) {
            currentRow = rowIterator.next();
        }
        this.saturdaysSheet = null;
    }

    public FileReaderSchedules(Iterator<Row> iterator, Sheet saturdaysSheet) {
        this.rowIterator = iterator;
        if (iterator.hasNext()) {
            currentRow = rowIterator.next();
        }
        this.saturdaysSheet = saturdaysSheet;
    }

    public ScheduleService readScheduleService() {

        if (saturdaysSheet != null) {
            FileReaderScheduleSaturdays fileReaderScheduleSaturdays = new FileReaderScheduleSaturdays(saturdaysSheet);

            return ScheduleService.builder()
                    .start(readStartDateSemester())
                    .end(readEndDateSemester())
                    .lecture(readLecture())
                    .lessons(readPractice())
                    .rescheduledDates(
                            fileReaderScheduleSaturdays
                                    .readSaturdays()
                    )
                    .build();
        }

        return ScheduleService.builder()
                .start(readStartDateSemester())
                .end(readEndDateSemester())
                .lecture(readLecture())
                .lessons(readPractice())
                .build();
    }


    private LocalDate readStartDateSemester() {
        LocalDate date;
        Cell startDateCell = currentRow.getCell(1);

        if (ExcelCellValidator.checkIsInvalidDateCell(startDateCell)) {
            generateExceptionExcel(1, "Неправильно задано дату початку семестра");
        }

        date = LocalDateParser.excelDateToLocalDate(startDateCell.getNumericCellValue());

        if (ExcelCellValidator.checkIsDateOutOfRange(date)) {
            generateExceptionExcel(1, "Дата початку семестра відрізняється від поточної на 5 років");
        }
        next();

        return date;
    }

    private LocalDate readEndDateSemester() {
        LocalDate date;
        Cell endDateCell = currentRow.getCell(1);

        if (ExcelCellValidator.checkIsInvalidDateCell(endDateCell)) {
            generateExceptionExcel(1, "Неправильно задано дату кінця семестра");
        }

        date = LocalDateParser.excelDateToLocalDate(endDateCell.getNumericCellValue());

        if (ExcelCellValidator.checkIsDateOutOfRange(date)) {
            generateExceptionExcel(1, "Дата кінця семестра відрізняється від поточної на 5 років");
        }

        next();

        return date;
    }

    private ScheduleConfig readLecture() {
        String nameLectures = ExcelLocaleProvider.getMessage("name.component.lecture");
        ScheduleConfig lecture;
        Cell cellLectureHeader;

        next();
        cellLectureHeader = currentRow.getCell(0);

        if (!Objects.equals(cellLectureHeader.getStringCellValue(), nameLectures)) {
            generateExceptionExcel(0, "Відстуня назва заголовку Лекції, не змінюйте шаблон");
        }
        next();

        lecture = readSchedule();
        next();
        next();
        return lecture;
    }

    private List<Lesson> readPractice() {
        String namePractice = ExcelLocaleProvider.getMessage("sheet.structure_of_study.practice");
        String nameSubGroup = ExcelLocaleProvider.getMessage("sheet.structure_of_study.subGroup");
        List<Lesson> lessons = new ArrayList<>();
        Cell cellLectureHeader;


        cellLectureHeader = currentRow.getCell(0);

        if (!Objects.equals(cellLectureHeader.getStringCellValue(), namePractice)) {
            generateExceptionExcel(0, "Відстуня назва заголовку Практика, не змінюйте шаблон");
        }
        if (rowIterator.hasNext()) {
            next();

            if (ExcelCellValidator.checkIsCellStringNull(currentRow.getCell(0))) {
                return null;
            }
        } else {
            return null;
        }
        while (!ExcelCellValidator.checkIsCellStringNull(currentRow.getCell(0))&&
                !ExcelCellValidator.checkIsNotMatchingStringCell(currentRow.getCell(0), nameSubGroup)) {
            lessons.add(readLesson());
        }

        return lessons;
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
            generateExceptionExcel(1,"Не вказано жодної групи для вивчення");
        }
        next();
        return namesOfGroups;
    }

    private Teacher readTeacher() {
        Cell teacherSurnameCell = currentRow.getCell(1);
        Cell teacherNameCell = currentRow.getCell(2);
        Cell teacherMiddleNameCell = currentRow.getCell(3);

        Cell academicDegreeCell = currentRow.getCell(4);
        Cell positionCell = currentRow.getCell(5);
        Cell emailCell = currentRow.getCell(6);


        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(teacherSurnameCell)) {
            generateExceptionExcel(1, "Невірно вказано Прізвище викладача");
        }
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(teacherNameCell)) {
            generateExceptionExcel(2, "Невірно вказано Ім'я викладача");
        }
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(teacherMiddleNameCell)) {
            generateExceptionExcel(3, "Невірно вказано По батькові викладача");
        }
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(academicDegreeCell)) {
            generateExceptionExcel(4, "Невірно вказано назву наукового ступеню");
        }
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(positionCell)) {
            generateExceptionExcel(5, "Невірно вказано назву посади");
        }
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(emailCell)) {
            generateExceptionExcel(6, "Невірно вказано електронну адресу");
        }

        next();
        return Teacher.builder()
                .surname(teacherSurnameCell.getStringCellValue().trim())
                .name(teacherNameCell.getStringCellValue().trim())
                .middleName(teacherMiddleNameCell.getStringCellValue().trim())
                .academicDegree(academicDegreeCell.getStringCellValue().trim())
                .position(positionCell.getStringCellValue().trim())
                .email(emailCell.getStringCellValue().trim())
                .build();
    }

    private Lesson readLesson() {
        Lesson lesson = null;
        List<String> namesOfGroups = new ArrayList<>();
        Teacher teacher = null;
        ScheduleConfig practiceSchedule = null;
        ScheduleConfig laboratorySchedule = null;

        namesOfGroups = readNamesOfGroups();
        teacher = readTeacher();

        practiceSchedule = readSchedule();
        System.out.println("PR " + practiceSchedule);
        next();
        laboratorySchedule = readSchedule();
        if (rowIterator.hasNext()) {
            next();
        }
        System.out.println("PR " + laboratorySchedule);
        if (practiceSchedule == null && laboratorySchedule == null) {
            generateExceptionExcel("В секції ПРАКТИКА пропущено розклад для груп " + namesOfGroups);
        }
        if (practiceSchedule != null && laboratorySchedule != null) {
            generateExceptionExcel("В секції ПРАКТИКА вказано розклад відразу для 2 типів занять(ПЗ,ЛАБ)");
        }

        if (practiceSchedule != null) {
            lesson = new Practice(laboratorySchedule, namesOfGroups, teacher);

        } else {
            lesson = new Laboratory(laboratorySchedule, namesOfGroups, teacher);
        }
        return lesson;
    }

    private ScheduleConfig readSchedule() {
        Map<Integer, List<DayOfWeek>> listMap = new HashMap<>();
        for (int i = 1; i <= 2; i++) {
            List<DayOfWeek> days = new ArrayList<>();
            Cell firstDayCell = currentRow.getCell(1);
            Cell secondDayCell = currentRow.getCell(2);

            addDayToList(firstDayCell, days);
            addDayToList(secondDayCell, days);

            if (!days.isEmpty()) {
                listMap.put(i, days);
            }
            if (i == 1) {
                next();
            }
        }
        if (!listMap.isEmpty()) {
            return new ScheduleConfig(listMap);
        }
        return null;
    }

    private static void addDayToList(Cell firstDayCell, List<DayOfWeek> days) {
        if (!ExcelCellValidator.checkIsCellEmptyWithCellTypeString(firstDayCell)) {
            String firstDay = firstDayCell.getStringCellValue();
            days.add(DayOfWeekParser.parseDayOfWeek(firstDay));
        }
    }
    private void generateExceptionExcel(String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                message
        );
    }

    private void generateExceptionExcel(int cell, String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                currentRow.getCell(cell).getAddress().formatAsString(),
                message
        );
    }
}
