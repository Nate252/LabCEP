package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.*;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
public class FileExcelCreator {

    private final StudyPlan studyPlan;
    private final SharedData sharedData;

    public FileExcelCreator(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
        this.sharedData = new SharedData(new XSSFWorkbook());
    }

    public static byte[] writeToByteArray(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Помилка запису Excel-файлу", e);
        }
    }

    public static void sortGroupByStudentsSurname(List<Group> groups) {
        Collator ukCollator = Collator.getInstance(new Locale("uk", "UA"));

        for (Group group : groups) {
            if (group.getStudents() != null) {
                group.getStudents().sort(Comparator.comparing(Student::getSurname, ukCollator));
            }
        }
    }

    private void createListStudentsFileExcel(List<Group> groups) {
        ListStudentsFileExcel listStudentsFileExcel =
                ListStudentsFileExcel.builder()
                        .nameDiscipline(studyPlan.getNameDiscipline())
                        .groups(groups)
                        .sharedData(sharedData)
                        .build();
        listStudentsFileExcel.createExcel();
    }

    private void createScheduleLectureFileExcel() {
        String sheetName = ExcelLocaleProvider.getMessage("name.component.lecture");
        List<LocalDate> dates = studyPlan.getScheduleService().generateLectureSchedule();
        List<String> groupsNames = studyPlan.getNamesGroupsStudent();

            ScheduleFileExcel scheduleFileExcel =
                    ScheduleFileExcel.builder()
                            .sheetName(sheetName)
                            .localDatesClasses(dates)
                            .sharedData(sharedData)
                            .studentsGroupsNames(groupsNames)
                            .build();
            scheduleFileExcel.createExcel();

    }

    private void createSchedulePracticeFileExcel(Map<Lesson, List<LocalDate>> lessons) {
        for (Map.Entry<Lesson, List<LocalDate>> lessonData :
                lessons.entrySet()) {
            Lesson lesson = lessonData.getKey();

            ScheduleFileExcel scheduleFileExcel = ScheduleFileExcel.builder()
                    .sheetName(lesson.getNameOfGroupsStudentsFileName())
                    .localDatesClasses(lessonData.getValue())
                    .sharedData(sharedData)
                    .studentsGroupsNames(lesson.getNameOfGroupsStudents())
                    .build();
            scheduleFileExcel.createExcel();
        }

    }

    private void createScheduleLaboratoryFileExcel() {
        List<String> groupsNames = studyPlan.getNamesGroupsStudent();

        LaboratoryWorkExcel laboratoryWorkExcel =
                LaboratoryWorkExcel.builder()
                        .sharedData(sharedData)
                        .studentsGroupsNames(groupsNames)
                        .practiceComponent(studyPlan.getMainEvaluation().getPracticeComponent())
                        .build();
        laboratoryWorkExcel.createExcel();

    }

    private void createClassDatesExcelFileExcel(Map<Lesson, List<LocalDate>> lessons) {
        ClassDatesExcel classDatesExcel =
                ClassDatesExcel.builder()
                        .sharedData(sharedData)
                        .practiceComponent(studyPlan.getMainEvaluation()
                                .getPracticeComponent())
                        .lessons(lessons)
                        .build();
        classDatesExcel.createExcel();
    }

    private void createFinalEvaluationExcelFileExcel() {
        List<String> groupsNames = studyPlan.getNamesGroupsStudent();

        FinalEvaluationExcel finalEvaluationExcel =
                FinalEvaluationExcel.builder()
                        .sharedData(sharedData)
                        .studentsGroupsNames(groupsNames)
                        .mainEvaluation(studyPlan.getMainEvaluation())
                        .build();
        finalEvaluationExcel.createExcel();
    }

    private boolean checkSchedule(){
        return studyPlan.getMainEvaluation().getPracticeComponent().getTasks() == null &&
                studyPlan.getScheduleService().getLessons() == null;
    }
    private void createSheetsExcel(List<Group> groups) {
        createListStudentsFileExcel(groups);

        if (studyPlan.getScheduleService().getLecture() != null) {
            createScheduleLectureFileExcel();
        }

        if (!checkSchedule()) {

            Map<Lesson, List<LocalDate>> lessons = studyPlan.getScheduleService().generateLessonsSchedule();

            createSchedulePracticeFileExcel(lessons);
            createClassDatesExcelFileExcel(lessons);
        }

        if (studyPlan.getMainEvaluation().getPracticeComponent().getTasks() != null) {
            createScheduleLaboratoryFileExcel();
        }

        createFinalEvaluationExcelFileExcel();
    }


    public byte[] createExcel(List<Group> groups) {

        sortGroupByStudentsSurname(groups);

        createSheetsExcel(groups);

        return writeToByteArray(sharedData.getWorkbook());
    }
}
