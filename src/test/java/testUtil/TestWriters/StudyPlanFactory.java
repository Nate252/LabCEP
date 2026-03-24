package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleConfig;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.ScheduleService;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyPlanFactory {

    private ScheduleService scheduleService;
    private MainEvaluation mainEvaluation;
    private final LocalDate startDate = LocalDate.of(2024, 2, 9);
    private final LocalDate endDate = LocalDate.of(2024, 3, 12);

    public StudyPlanFactory mainEvaluationFull() {
        this.mainEvaluation = new MainEvaluationFactory()
                .build();

        return this;
    }
    public StudyPlanFactory mainEvaluationWithoutLessonsPractice() {
        this.mainEvaluation = new MainEvaluationFactory()
                .withoutPracticeComponent()
                .withPracticeBonus(0)
                .withTheoryBonus(0)
                .withPerSemester(20)
                .build();

        return this;
    }
    public StudyPlanFactory mainEvaluationWithoutLecture() {
        this.mainEvaluation = new MainEvaluationFactory()
                .withoutLecture()
                .withTheoryBonus(0)
                .withPracticeBonus(0)
                .withPerSemester(75)
                .build();

        return this;
    }

    private ScheduleConfig createScheduleConfig() {
        Map<Integer, List<DayOfWeek>> weekSchedule = new HashMap<>();
        weekSchedule.put(1, List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY));
        weekSchedule.put(2, List.of(DayOfWeek.MONDAY));

        return new ScheduleConfig(weekSchedule);
    }

    public List<Lesson> createLessons() {
        List<Lesson> lessons = TestClassDatesExcel.createLessons();
        for (Lesson lesson : lessons) {
            lesson.setSchedule(createScheduleConfig());
        }
        return lessons;
    }
    public StudyPlanFactory scheduleServiceFull() {

        this.scheduleService = ScheduleService
                .builder()
                .lecture(createScheduleConfig())
                .rescheduledDates(null)
                .start(startDate)
                .end(endDate)
                .lessons(createLessons())
                .build();
        ;
        return this;
    }

    public StudyPlanFactory scheduleServiceWithoutLecture() {

        this.scheduleService = ScheduleService
                .builder()
                .lecture(null)
                .rescheduledDates(null)
                .start(startDate)
                .end(endDate)
                .lessons(createLessons())
                .build();
        ;
        return this;
    }

    public StudyPlanFactory scheduleServiceWithoutLessons() {
        this.scheduleService = ScheduleService
                .builder()
                .lecture(createScheduleConfig())
                .rescheduledDates(null)
                .start(startDate)
                .end(endDate)
                .lessons(null)
                .build();
        ;
        return this;
    }

    public StudyPlan build() {
        return StudyPlan.builder()
                .nameDiscipline("АППЗ")
                .namesGroupsStudent(TestScheduleFileExcel.getStudentsGroupsNames())
                .scheduleService(scheduleService)
                .mainEvaluation(mainEvaluation)
                .build();
    }
}
