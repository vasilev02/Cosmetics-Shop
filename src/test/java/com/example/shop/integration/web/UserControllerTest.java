package com.example.shop.integration.web;

import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {

    private static final String USER_CONTROLLER_PREFIX = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                USER_CONTROLLER_PREFIX + "/login")).
                andExpect(status().isOk()).
                andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER"})
    void testLoginRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                USER_CONTROLLER_PREFIX + "/login")).
                andExpect(status().is3xxRedirection());
    }

    @Test
    void testLRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                USER_CONTROLLER_PREFIX + "/register")).
                andExpect(status().isOk()).
                andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER"})
    void testRegisterRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                USER_CONTROLLER_PREFIX + "/register")).
                andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void testRegisterUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX + "/register")
                .param("username", "valo").
                        param("email", "valentin7969@gmail.com").
                        param("password", "password1").
                        param("confirmPassword", "password1").
                        with(csrf())).
                andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, this.userRepository.count());
    }


}
