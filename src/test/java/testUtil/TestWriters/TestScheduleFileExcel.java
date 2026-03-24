package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Laboratory;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ScheduleFileExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;
import org.antlr.v4.runtime.misc.Pair;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestScheduleFileExcel {
    public static ScheduleFileExcel createScheduleLectureFileExcel(SharedData sharedData) {
        String sheetName = ExcelLocaleProvider.getMessage("name.component.lecture");


        return ScheduleFileExcel.builder()
                .sheetName(sheetName)
                .localDatesClasses(getDates())
                .sharedData(sharedData)
                .studentsGroupsNames(getStudentsGroupsNames())
                .build();


    }

    public static final String[] headersFirst = {"#",
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.group"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.surname"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.name"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.middle"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.email"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.phone"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.contract"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.campus"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.country")};
    public static final String[] headersSecond = {"#",
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.group"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.surname"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.name"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.middle"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.email"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.phone"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.contract"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.campus"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.country")};

    public static ScheduleFileExcel createSchedulePracticeFileExcel(SharedData sharedData) {
        Lesson lesson = getLesson();

        return ScheduleFileExcel.builder()
                .sheetName(lesson.getNameOfGroupsStudentsFileName())
                .localDatesClasses(getDates())
                .sharedData(sharedData)
                .studentsGroupsNames(lesson.getNameOfGroupsStudents())
                .build();

    }


    public static List<String> getStudentsGroupsNames() {
        return List.of("КН-222б", "КН-222в", "КН-222г");
    }

    public static List<LocalDate> getDates() {
        return List.of(
                LocalDate.of(2024, 9, 2),
                LocalDate.of(2024, 9, 7),
                LocalDate.of(2024, 9, 9),
                LocalDate.of(2024, 9, 16)
        );
    }

    public static Lesson getLesson() {
        return new Laboratory(null,List.of("КН-222б","КН-222г"),null);
    }

    public static Map<String, Pair<Integer, Integer>> createTestSharedData() {
        Map<String, Pair<Integer, Integer>> listNumGroups = new HashMap<>();
        listNumGroups.put("КН-222г", new Pair<>(7, 8));
        listNumGroups.put("КН-222в", new Pair<>(5, 6));
        listNumGroups.put("КН-222б", new Pair<>(3, 4));

        return listNumGroups;
    }
}
