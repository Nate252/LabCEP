package com.Dev.CreateEstimatingPlan.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, length = 35)
    private String name;

    @Column(name = "user_surname", nullable = false, length = 35)
    private String surname;

    @Column(name = "user_middle_name", nullable = false, length = 35)
    private String middleName;

    @Column(name = "user_email", nullable = false, unique = true, length = 60)
    private String email;

    @Column(name = "user_roles", nullable = false)
    private String roles;

    @Column(nullable = false)
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUser myUser = (MyUser) o;
        return Objects.equals(id, myUser.id) && Objects.equals(name, myUser.name) && Objects.equals(surname, myUser.surname) && Objects.equals(middleName, myUser.middleName) && Objects.equals(email, myUser.email) && Objects.equals(roles, myUser.roles) && Objects.equals(password, myUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, middleName, email, roles, password);
    }
}
