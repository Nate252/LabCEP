package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class Lesson {
    protected ScheduleConfig schedule;
    protected List<String> nameOfGroupsStudents;
    protected Teacher teacher;

    @Override
    public String toString() {
        return schedule +
                "Назви груп студентів =" +
                nameOfGroupsStudents +
                teacher +
                '}';
    }

    protected String getNameOfGroupsStudentsString() {
        StringBuilder builder = new StringBuilder();
        for (String name : nameOfGroupsStudents) {
            builder.append(name);
            builder.append(" ");
        }
        return builder.toString();
    }

    public abstract String getNameOfGroupsStudentsFileName();

}

