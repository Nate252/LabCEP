package com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Teacher {
    private String name;
    private String surname;
    private String middleName;
    private String position;
    private String academicDegree;
    private String email;

    public String getFIO() {
        return surname + " " + name + " " + middleName;
    }

    @Override
    public String toString() {
        return "\n"+
                "Викладач " +
                "name='" + name +
                ", surname='" + surname +
                ", middleName='" + middleName +
                ", position='" + position +
                ", academicDegree='" + academicDegree +
                ", email='" + email + "\n";
    }
}


