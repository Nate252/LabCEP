package testUtil;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.*;
import com.Dev.CreateEstimatingPlan.templateExcelReader.FileReaderScheduleSaturdays;
import org.apache.commons.math3.stat.inference.TTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class TestScheduleService {

    public static LocalDate startDateSemester() {
        return LocalDate.of(2024, 9, 2);
    }

    public static LocalDate endDateSemester() {
        return LocalDate.of(2024, 12, 3);
    }

    public static Map<Integer, List<DayOfWeek>> getDays(DayOfWeek day) {
        Map<Integer, List<DayOfWeek>> listMap = new HashMap<>();

        if (day.equals(DayOfWeek.MONDAY)) {
            listMap.put(1, List.of(DayOfWeek.MONDAY));
            listMap.put(2, List.of(DayOfWeek.MONDAY));
        }
        if (day.equals(DayOfWeek.WEDNESDAY)) {
            listMap.put(1, List.of(DayOfWeek.WEDNESDAY));
            listMap.put(2, List.of(DayOfWeek.WEDNESDAY));
        }
        if (day.equals(DayOfWeek.THURSDAY)) {
            listMap.put(1, List.of(DayOfWeek.THURSDAY));
            listMap.put(2, List.of(DayOfWeek.THURSDAY));
        }

        return listMap;
    }

    public static ScheduleConfig lecture() {
        return new ScheduleConfig(getDays(DayOfWeek.MONDAY));
    }

    public static Teacher teacherFirst() {
        return Teacher.builder()
                .surname("Мельник")
                .name("Олександр")
                .middleName("Ігорович")
                .academicDegree("ктн")
                .position("доцент")
                .email("email@khpi.edu.ua")
                .build();
    }

    public static Teacher teacherSecond() {
        return Teacher.builder()
                .surname("Глушко")
                .name("Олександр")
                .middleName("Ігорович")
                .academicDegree("ктн")
                .position("доцент")
                .email("email@khpi.edu.ua")
                .build();
    }

    public static Lesson laboratoryLessonFirst() {
        return new Laboratory(
                new ScheduleConfig(getDays(DayOfWeek.WEDNESDAY)),
                List.of("КН-222г", "КН-222б"),
                teacherFirst()

        );
    }

    public static Lesson laboratoryLessonSecond() {
        return new Laboratory(
                new ScheduleConfig(getDays(DayOfWeek.THURSDAY)),
                List.of("КН-222в"),
                teacherSecond()
        );
    }

    public static List<Lesson> lessons() {
        return List.of(laboratoryLessonFirst(), laboratoryLessonSecond());
    }


    public static ScheduleService defaultScheduleService() {
        return ScheduleService.builder()
                .start(startDateSemester())
                .end(endDateSemester())
                .lecture(lecture())
                .lessons(lessons())
                .build();
    }

    public static ScheduleService ScheduleServiceWithSaturdays() {
        return ScheduleService.builder()
                .start(startDateSemester())
                .end(endDateSemester())
                .lecture(lecture())
                .lessons(lessons())
                .rescheduledDates(TestSaturdays.saturdays())
                .build();
    }
}
