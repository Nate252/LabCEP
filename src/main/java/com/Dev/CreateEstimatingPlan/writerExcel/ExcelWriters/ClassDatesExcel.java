package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Teacher;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import lombok.Builder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassDatesExcel extends AbstractTemplate {
    private final Sheet sheet;
    private final PracticeComponent practiceComponent;
    private final Map<Lesson, List<LocalDate>> lessons;

    @Builder
    public ClassDatesExcel(SharedData sharedData, PracticeComponent practiceComponent,
                           Map<Lesson, List<LocalDate>> lessons) {
        super(sharedData);
        sheet = workbook.createSheet(ExcelLocaleProvider.getMessage("sheet.writer.class_dates.name"));
        this.lessons = lessons;
        this.practiceComponent = practiceComponent;
    }

    @Override
    void createHeader() {
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER);
        CellStyle headerYellowStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_YELLOW_14);

        int counterCell = 0;
        int counterRow = 0;
        for (Map.Entry<Lesson, List<LocalDate>> lessonData :
                lessons.entrySet()) {
            Row row = sheet.createRow(counterRow);

            Cell cellGroups = row.createCell(0);
            cellGroups.setCellValue(lessonData.getKey().getNameOfGroupsStudentsFileName());
            cellGroups.setCellStyle(headerYellowStyle);
            counterCell++;
            counterRow++;

            Row secondRow = sheet.createRow(counterRow);

            Cell weekHeader = secondRow.createCell(0);
            weekHeader.setCellValue(ExcelLocaleProvider.getMessage(
                    "sheet.writer.class_dates.header.week"));
            weekHeader.setCellStyle(headerStyle);
            counterCell++;

            Cell classesHeader = secondRow.createCell(1);
            classesHeader.setCellValue(ExcelLocaleProvider.getMessage(
                    "sheet.writer.class_dates.header.class"));
            classesHeader.setCellStyle(headerStyle);
            counterCell++;

            Cell dateHeader = secondRow.createCell(2);
            dateHeader.setCellValue(ExcelLocaleProvider.getMessage(
                    "sheet.writer.class_dates.header.date"));
            dateHeader.setCellStyle(headerStyle);
            counterCell++;

            if (!practiceComponent.isDeadlineEmpty()) {
                Cell deadLineHeader = secondRow.createCell(3);
                deadLineHeader.setCellValue(ExcelLocaleProvider.getMessage(
                        "sheet.writer.class_dates.header.deadline"));
                deadLineHeader.setCellStyle(headerStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(
                    cellGroups.getRowIndex(),
                    cellGroups.getRowIndex(),
                    0,
                    secondRow.getLastCellNum()-1));
            counterRow += lessonData.getValue().size() + 1;
        }

        Row teacherHeaderFirst = sheet.getRow(0);
        Row teacherHeaderSecond = sheet.getRow(1);
        int startCellIndex = teacherHeaderSecond.getLastCellNum() + 1;


        Cell cellTeacherHeader = teacherHeaderFirst.createCell(startCellIndex);
        cellTeacherHeader.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher"));
        cellTeacherHeader.setCellStyle(headerStyle);


        Cell subHeaderGroups = teacherHeaderSecond.createCell(startCellIndex);
        subHeaderGroups.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher.groups"));
        subHeaderGroups.setCellStyle(headerStyle);
        startCellIndex++;

        Cell subHeaderFIO = teacherHeaderSecond.createCell(startCellIndex);
        subHeaderFIO.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher.fio"));
        subHeaderFIO.setCellStyle(headerStyle);
        startCellIndex++;

        Cell subHeaderAcademicDegree = teacherHeaderSecond.createCell(startCellIndex);
        subHeaderAcademicDegree.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher.academic_degree"));
        subHeaderAcademicDegree.setCellStyle(headerStyle);
        startCellIndex++;

        Cell subHeaderPosition = teacherHeaderSecond.createCell(startCellIndex);
        subHeaderPosition.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher.position"));
        subHeaderPosition.setCellStyle(headerStyle);
        startCellIndex++;

        Cell subHeaderEmail = teacherHeaderSecond.createCell(startCellIndex);
        subHeaderEmail.setCellValue(ExcelLocaleProvider.getMessage(
                "sheet.writer.class_dates.header.teacher.email"));
        subHeaderEmail.setCellStyle(headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(
                0,
                0,
                subHeaderGroups.getColumnIndex(),
                subHeaderEmail.getColumnIndex()));
        startCellIndex++;
    }

    @Override
    void createContent() {
        CellStyle textStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE);
        CellStyle dateStyle = sharedData.getStyle(ExcelBaseStyles.FORMAT_DATE_SCHEDULE_14);

        int startCellTeacher = sheet.getRow(0).getLastCellNum() - 1;

        int counterSchedule = 2;
        int counterTeachers = 2;

        System.out.println(practiceComponent.getTasks());
        for (Map.Entry<Lesson, List<LocalDate>> lessonData :
                lessons.entrySet()) {

            int classesCounter = 1;
            int weekNumber = 1;
            LocalDate previousDate = null;
            Map<LocalDate, Integer> weekMap = new HashMap<>();
            Teacher teacher = lessonData.getKey().getTeacher();
            String groups = lessonData.getKey().getNameOfGroupsStudentsFileName().substring(3);

            List<Tasks> tasks = practiceComponent.getTasks();
            int taskCounter = 0;
            int startRowTask = counterSchedule;

            // write Schedule
            for (LocalDate date : lessonData.getValue()) {

                if (previousDate != null && date.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
                        != previousDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                    weekNumber++;
                }
                weekMap.put(date, weekNumber);
                previousDate = date;


                Row row = sheet.createRow(counterSchedule);

                Cell weekNumberCell = row.createCell(0);
                weekNumberCell.setCellValue(weekMap.get(date));
                weekNumberCell.setCellStyle(textStyle);

                Cell classesCounterCell = row.createCell(1);
                classesCounterCell.setCellValue(classesCounter);
                classesCounterCell.setCellStyle(textStyle);

                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(date);
                dateCell.setCellStyle(dateStyle);

                if (sheet.getRow(1).getCell(3) != null) {
                    if (lessonData.getValue().size() < practiceComponent.getSumDeadlines()) {
                        throw new ExcelReadHandlingException(
                                "Структура оцінювання",
                                "Сумма встановленої кількості занять для дедлайну перевищує кількість занять"
                               );
                    }

                    Cell deadLineCell = row.createCell(3);
                    deadLineCell.setCellStyle(textStyle);

                    if (taskCounter < tasks.size()) {
                        Tasks currentTask = tasks.get(taskCounter);
                        int deadline = currentTask.getDeadLine();
                        String taskName = currentTask.getName();

                        if (counterSchedule == startRowTask) {
                            Cell labCell = row.getCell(3);
                            labCell.setCellValue(taskName);
                            labCell.setCellStyle(textStyle);

                            if (deadline > 1) {
                                CellRangeAddress region = new CellRangeAddress(
                                        counterSchedule,
                                        counterSchedule + deadline - 1,
                                        3,
                                        3);
                                System.out.println(region.formatAsString());
                                sheet.addMergedRegion(region);
                            }
                        }

                        if (counterSchedule == startRowTask + deadline - 1) {
                            taskCounter++;
                            if (taskCounter < tasks.size()) {
                                startRowTask = counterSchedule + 1;
                            }
                        }
                    }
                }
                classesCounter++;
                counterSchedule++;
            }

            Row rowTeacher = sheet.getRow(counterTeachers);
            int counterT = startCellTeacher;

            // write Teacher
            Cell subHeaderGroups = rowTeacher.createCell(counterT);
            subHeaderGroups.setCellValue(groups);
            subHeaderGroups.setCellStyle(textStyle);
            counterT++;

            Cell subHeaderFIO = rowTeacher.createCell(counterT);
            subHeaderFIO.setCellValue(teacher.getFIO());
            subHeaderFIO.setCellStyle(textStyle);
            counterT++;

            Cell subHeaderAcademicDegree = rowTeacher.createCell(counterT);
            subHeaderAcademicDegree.setCellValue(teacher.getAcademicDegree());
            subHeaderAcademicDegree.setCellStyle(textStyle);
            counterT++;

            Cell subHeaderPosition = rowTeacher.createCell(counterT);
            subHeaderPosition.setCellValue(teacher.getPosition());
            subHeaderPosition.setCellStyle(textStyle);
            counterT++;

            Cell subHeaderEmail = rowTeacher.createCell(counterT);
            subHeaderEmail.setCellValue(teacher.getEmail());
            subHeaderEmail.setCellStyle(textStyle);
            counterT++;

            counterTeachers++;
            counterSchedule += 2;
        }

    }
}
