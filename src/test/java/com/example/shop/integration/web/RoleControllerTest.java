package com.example.shop.integration.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class RoleControllerTest {

    private static final String ROLE_CONTROLLER_PREFIX = "/role";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "testUser", roles = {"USER","ADMIN","ROOT"})
    void testShouldReturnOkStatusAndViewModelForShowChangeRoleRoot() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ROLE_CONTROLLER_PREFIX + "/change")).
                andExpect(status().isOk()).
                andExpect(view().name("role-change")).
                andExpect(model().attributeExists("usernames"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER","ADMIN"})
    void testShouldReturnForbiddenStatusAndViewModelForShowChangeRoleAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ROLE_CONTROLLER_PREFIX + "/change")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER"})
    void testShouldReturnForbiddenStatusAndViewModelForShowChangeRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ROLE_CONTROLLER_PREFIX + "/change")).
                andExpect(status().isForbidden());
    }

}
