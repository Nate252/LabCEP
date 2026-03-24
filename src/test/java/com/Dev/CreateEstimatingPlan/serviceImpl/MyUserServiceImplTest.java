package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.exception.MyUserException;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserServiceImplTest {
    @Mock
    private MyUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MyUserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new MyUserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void canFindAllUsers() {
        underTest.findAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void CanFindUserById() {
        //given
        Long id = 1L;
        MyUser myUser = new MyUser();
        myUser.setId(id);


        //when
        when(userRepository.findById(id)).thenReturn(Optional.of(myUser));

        Optional<MyUser> found = underTest.findUserById(id);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        verify(userRepository).findById(id);
    }

    @Test
    void canSaveUser() {
        String password = "11111111Aa";
        String email = "case@gmail.com";
        //given
        MyUser user = new MyUser(
                null,
                "Андрій",
                "Глушко",
                "Максимович",
                email,
                "ROLE_ADMIN",
                password

        );

        //when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        underTest.saveUser(user);

        //then
        ArgumentCaptor<MyUser> userArgumentCaptor = ArgumentCaptor.forClass(MyUser.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        MyUser captureUser = userArgumentCaptor.getValue();
        System.out.println(captureUser);

        assertThat(captureUser).isEqualTo(user);
    }

    @Test
    void saveUserWillThrowWhenEmailsIsTaken() {
        //given
        String password = "11111111Aa";
        String email = "case@gmail.com";
        MyUser user = new MyUser(
                null,
                "Андрій",
                "Глушко",
                "Максимович",
                email,
                "ROLE_ADMIN",
                password

        );

        //when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> underTest.saveUser(user))
                .isInstanceOf(MyUserException.class)
                .hasMessageContaining("Користувач з вказаною поштою : " + user.getEmail() + " вже зареєстрований");
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }


    @Test
    void canEditUser() {
        // given
        Long id = 1L;
        String password = "oldPassword";
        String oldEmail = "test@gmail.com";
        String newEmail = "testNew@gmail.com";
        MyUser currentUser = new MyUser();
        currentUser.setId(id);
        currentUser.setEmail(oldEmail);
        currentUser.setPassword(password);

        MyUser editedUser = new MyUser();
        editedUser.setEmail(newEmail);
        editedUser.setPassword(password);

        when(userRepository.findById(id)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(userRepository.save(any(MyUser.class))).thenAnswer(i -> i.getArgument(0));

        // when
        MyUser resultUser = underTest.editUser(id, editedUser);

        // then
        assertThat(resultUser).isEqualTo(editedUser);
    }

    @Test
    void EditUserWillThrowWhenEmailsIsTaken() {
        // given
        Long id = 1L;
        String password = "oldPassword";
        String oldEmail = "test@gmail.com";
        String newEmail = "testNew@gmail.com";
        MyUser currentUser = new MyUser();
        currentUser.setId(id);
        currentUser.setEmail(oldEmail);
        currentUser.setPassword(password);

        MyUser editedUser = new MyUser();
        editedUser.setEmail(newEmail);
        editedUser.setPassword(password);

        MyUser userWithTheSameEmail = new MyUser();
        userWithTheSameEmail.setId(2L); // інший користувач, але з таким самим email
        userWithTheSameEmail.setEmail(newEmail);


        when(userRepository.findById(id)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(userWithTheSameEmail));


        // when / then
        assertThatThrownBy(() -> underTest.editUser(id, editedUser))
                .isInstanceOf(MyUserException.class)
                .hasMessageContaining("Не можна змінити пошту через те що ця пошта вже використовується : " + newEmail);

        verify(userRepository, never()).save(any());
    }

    @Test
    void EditUserWillThrowWhenUserIsNotExist() {
        // given
        Long id = 100L;
        String password = "oldPassword";
        String oldEmail = "test@gmail.com";
        String newEmail = "testNew@gmail.com";
        MyUser currentUser = new MyUser();
        currentUser.setId(id);
        currentUser.setEmail(oldEmail);
        currentUser.setPassword(password);

        MyUser editedUser = new MyUser();
        editedUser.setEmail(newEmail);
        editedUser.setPassword(password);


        when(userRepository.findById(id)).thenReturn(Optional.empty());


        // when / then
        assertThatThrownBy(() -> underTest.editUser(id, editedUser))
                .isInstanceOf(MyUserException.class)
                .hasMessageContaining("Такого користувача не існує");

        verify(userRepository, never()).save(any());

    }

    @Test
    void canDeleteUserById() {
        // given
        Long id = 1L;
        // when
        underTest.deleteUserById(id);
        // then
        verify(userRepository).deleteById(id);
    }

    @Test
    void canChangePassword() {
        //given
        Long id = 1L;
        String password = "newPassword";

        MyUser user = new MyUser();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // when
        underTest.changePassword(id, password);

        //then
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);

    }

    @Test
    void changePasswordWillThrowWhenUserIsNotExist() {
        // given
        Long id = 100L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> underTest.changePassword(id, "newPassword"))
                .isInstanceOf(MyUserException.class)
                .hasMessageContaining("Користувач не знайдений");

        verify(userRepository, never()).save(any());
    }
}