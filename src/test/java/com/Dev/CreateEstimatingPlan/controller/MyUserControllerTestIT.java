package com.Dev.CreateEstimatingPlan.controller;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.exception.MyUserException;
import com.Dev.CreateEstimatingPlan.serviceImpl.MyUserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@ActiveProfiles("testConf")
@AutoConfigureMockMvc
class MyUserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MyUserServiceImpl userService;

    @Test
    public void shouldRedirectUnauthorisedUserToLogin() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldAllowAccessHomeWithAuth() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Вітаємо")));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldForbidAllowGetAllUsers() throws Exception {
        mockMvc.perform(get("/showUsers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldAllowGetAllUsersAndReturnListUsers() throws Exception {
        MyUser myUser = new MyUser();
        myUser.setName("TestUser");

        when(userService.findAllUsers()).thenReturn(List.of(myUser));

        mockMvc.perform(get("/showUsers"))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/showUsers"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", Matchers.contains(myUser)));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnAddUserWhenAdmin() throws Exception {
        mockMvc.perform(get("/addUserShow"))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/addUser"))
                .andExpect(model().attributeExists("newUser"))
        ;
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldRedirectToShowUsersWhenAddUserSuccessful() throws Exception {
        MyUser user = new MyUser();
        user.setName("TestUser");

        mockMvc.perform(post("/addUser")
                        .param("username", "TestUser")
                        .flashAttr("newUser", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showUsers"));

        verify(userService).saveUser(any(MyUser.class));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldThrowMyUserExceptionWhenUserIsExists() throws Exception {
        doThrow(new MyUserException("Користувач вже існує")).when(userService).saveUser(any(MyUser.class));

        mockMvc.perform(post("/addUser")
                        .param("username", "testUser")
                        .param("email", "test@example.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/addUser"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", containsString("Користувач вже існує")));
    }

    //    edit show
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnForbiddenWhenNotAllowEditUserShow() throws Exception {
        mockMvc.perform(get("/editUserShow/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldRedirectToShowUsersWhenUserIsAbsent() throws Exception {
        mockMvc.perform(get("/editUserShow/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/showUsers"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnOkWhenEditUserShowAdmin() throws Exception {
        MyUser user = new MyUser();
        user.setName("TestUser");
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/editUserShow/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/editUser"));
    }

    //    edit
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnRedirectShowUsersWhenAdminEditUserSuccessful() throws Exception {
        mockMvc.perform(post("/editUser/1")
                        .flashAttr("editUser", new MyUser())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showUsers"));

        verify(userService).editUser(eq(1L), any(MyUser.class));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnForbiddenWhenNotAllowEditUser() throws Exception {
        mockMvc.perform(post("/editUser/1")
                        .flashAttr("editUser", new MyUser())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldThrowMyUserException_WhenExistingException() throws Exception {
        String errorMessage = "Такого користувача не існує";
        doThrow(new MyUserException(errorMessage))
                .when(userService).editUser(eq(1L), any(MyUser.class));

        mockMvc.perform(post("/editUser/1")
                        .flashAttr("editUser", new MyUser())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/editUser"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", containsString(errorMessage)));

        verify(userService).editUser(eq(1L), any(MyUser.class));
    }


    //    delete
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnForbiddenWhenRoleUser() throws Exception {
        mockMvc.perform(post("/deleteUser/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldReturnRedirectShowUsersWhenDeleteUserSuccessful() throws Exception {
        mockMvc.perform(post("/deleteUser/1")
                        .param("id", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showUsers"));
    }

    // change password
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnForbiddenChangeUserPswShowWhenRoleUser() throws Exception {
        mockMvc.perform(get("/changeUserPswShow/1")
                        .param("psw", "newPassword")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldReturnRedirectShowUsersWhenChangeUserPswSuccessful() throws Exception {
        mockMvc.perform(post("/changeUserPsw/1")
                        .param("psw", "psw123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showUsers"));

        verify(userService).changePassword(1L, "psw123");
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldReturnForbiddenShowUsersWhenCSRFInvalid() throws Exception {
        mockMvc.perform(post("/changeUserPsw/1")
                        .param("psw", "psw123"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldThrowMyUserExceptionWhenNonExistentUser() throws Exception {
        String errorMessage = "Користувача не існує";

        doThrow(new MyUserException(errorMessage))
                .when(userService).changePassword(eq(1L), anyString());

        mockMvc.perform(post("/changeUserPsw/1")
                        .param("psw", "psw123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("MyUser/changeUserPassword"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", containsString(errorMessage)));
    }
}