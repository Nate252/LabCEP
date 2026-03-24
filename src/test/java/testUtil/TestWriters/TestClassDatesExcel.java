package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Laboratory;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Teacher;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ClassDatesExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.util.*;

public class TestClassDatesExcel {
    public static ClassDatesExcel createClassDatesExcelFileExcel(PracticeComponent practiceComponent) {
        return ClassDatesExcel.builder()
                .sharedData(new SharedData(new XSSFWorkbook()))
                .practiceComponent(practiceComponent)
                .lessons(createLessonsWithDate())
                .build();
    }


    public static Map<Lesson, List<LocalDate>> createLessonsWithDate() {
        Map<Lesson, List<LocalDate>> lessonListMap = new HashMap<>();
        List<Lesson> lessons = createLessons();
        lessonListMap.put(lessons.get(0), TestScheduleFileExcel.getDates());
        lessonListMap.put(lessons.get(1), TestScheduleFileExcel.getDates());
        return lessonListMap;

    }

    public static Teacher createTeacherFirst() {
        return Teacher.builder()
                .name("Олександр")
                .surname("Мельник")
                .middleName("Ігорович")
                .academicDegree("к.т.н.")
                .position("доцент")
                .email("email@khpi.edu.ua")
                .build();
    }

    public static Teacher createTeacherSecond() {
        return Teacher.builder()
                .name("Олександр")
                .surname("Глушко")
                .middleName("Ігорович")
                .academicDegree("к.т.н.")
                .position("доцент")
                .email("email@khpi.edu.ua")
                .build();
    }

    public static List<Lesson> createLessons() {
        return List.of(
                new Laboratory(null, List.of("КН-222в"), createTeacherFirst()),
                new Laboratory(null, List.of("КН-222б", "КН-222г"), createTeacherSecond())
        );
    }

    public static PracticeComponent createPracticeComponent(List<Tasks> tasks) {
        return new PracticeComponent(null, tasks);
    }
    public static List<Tasks> practiceTasksForClassDatesWithoutDeadlines() {
        List<Tasks> tasks = practiceTasksForClassDates();
        tasks.forEach(task ->  task.setDeadLine(0));

        return tasks;
    }
    public static List<Tasks> practiceTasksForClassDates() {
        return Arrays.asList(
                new Tasks(new EducationComponent("ЛР1", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті", "Редірект, форвард"), 1),

                new Tasks(new EducationComponent("ЛР2", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті", "Редірект, форвард"), 1),

                new Tasks(new EducationComponent("ЛР3", 15),
                        Arrays.asList("Основи ВЕБ", "Перший проект", "Таблиця в сервлеті"), 1),

                new Tasks(new EducationComponent("ЛР4", 10),
                        Collections.emptyList(), 1)
        );
    }
}



