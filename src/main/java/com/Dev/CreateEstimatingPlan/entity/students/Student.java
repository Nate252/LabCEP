package com.Dev.CreateEstimatingPlan.entity.students;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name", nullable = false, length = 35)
    private String name;

    @Column(name = "student_surname", nullable = false, length = 35)
    private String surname;

    @Column(name = "student_middle_name", nullable = false, length = 35)
    private String middleName;

    @Column(name = "student_email", nullable = false, length = 80)
    private String email;

    @Column(name = "student_phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "student_second_phone_number", length = 15)
    private String secondPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;


    public Student(String name, String surname, String middleName, String email, Group group) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.email = email;
        this.group = group;
    }

    public Student(String name, String surname, String middleName, String email, String phoneNumber, String secondPhoneNumber, Group group) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.secondPhoneNumber = secondPhoneNumber;
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", secondPhoneNumber='" + secondPhoneNumber + '\'' +
                ", group=" + group +
                '}' + "\n";
    }
    public String getPhoneNumbers() {
        if (phoneNumber == null && secondPhoneNumber == null) {
            return " ";
        }

        StringBuilder result = new StringBuilder();
        result.append(" ");
        if (phoneNumber != null) {
            result.append(formatPhoneNumber(phoneNumber));
        }
        if (secondPhoneNumber != null) {
            if (!result.isEmpty()) {
                result.append("; \n");
            }
            result.append(formatPhoneNumber(secondPhoneNumber)).append(";");
        } else {
            result.append(";");
        }
        return result.toString();

    }

    private String formatPhoneNumber(String number) {
        return number.replaceAll("(\\+380)(\\d{2})(\\d{3})(\\d{2})(\\d{2})", "$1($2)-$3-$4-$5");
    }

    public String getStudentShortFullName() {
        return String.format("%s %s.%s.", surname.toUpperCase(), name.toUpperCase().charAt(0), middleName.toUpperCase().charAt(0));

    }
}
