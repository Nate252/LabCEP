package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;

import java.util.List;

public class Practice extends Lesson {
    public Practice(ScheduleConfig schedule, List<String> nameOfGroupsStudents, Teacher teacher) {
        super(schedule, nameOfGroupsStudents, teacher);
    }
    @Override
    public String toString() {
        return "\n\n" +
                "Практичні"
                +super.toString();
    }

    @Override
    public String getNameOfGroupsStudentsFileName() {
        String index = ExcelLocaleProvider.getMessage("sheet.writer.laboratory.pw");
        return index + " " + getNameOfGroupsStudentsString();
    }
}
