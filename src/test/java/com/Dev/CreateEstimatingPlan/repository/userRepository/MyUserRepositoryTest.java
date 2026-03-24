package com.Dev.CreateEstimatingPlan.repository.userRepository;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.GroupRepository;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MyUserRepositoryTest {
    @Autowired
    private MyUserRepository underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindById() {
        //given
        Long idUser = 1L;
        MyUser user = new MyUser(
                null,
                "Андрій",
                "Глушко",
                "Максимович",
                "case@gmail.com",
                "ROLE_ADMIN",
                "11111111Aa"

        );
        underTest.save(user);



        //when
        Optional<MyUser> findUser = underTest.findById(idUser);

        //then

        assertThat(findUser.isPresent()).isTrue();
        assertEquals(findUser.get().getId(),idUser);
        assertEquals(user, findUser.get());
    }

    @Test
    void shouldFindByEmail() {
        //given
        String email = "case@gmail.com";
        MyUser user = new MyUser(
                null,
                "Андрій",
                "Глушко",
                "Максимович",
                email,
                "ROLE_ADMIN",
                "11111111Aa"

        );

        underTest.save(user);

        //when
        Optional<MyUser> findUser = underTest.findByEmail(email);
        //then
        assertThat(findUser.isPresent()).isTrue();
        assertEquals(user, findUser.get());
    }
}