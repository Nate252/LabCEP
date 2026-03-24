package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;

import java.util.List;

public class Laboratory extends Lesson {
    public Laboratory(ScheduleConfig schedule, List<String> nameOfGroupsStudents, Teacher teacher) {
        super(schedule, nameOfGroupsStudents, teacher);
    }

    @Override
    public String toString() {
        return "\n\n" +
                "Лабораторні"
                + super.toString();
    }

    @Override
    public String getNameOfGroupsStudentsFileName() {
        String index = ExcelLocaleProvider.getMessage("sheet.writer.laboratory.lw");
        return index + " " + getNameOfGroupsStudentsString();
    }
}
