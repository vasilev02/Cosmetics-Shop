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
public class OrderControllerTest {

    private static final String ORDER_CONTROLLER_PREFIX = "/orders";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "pesho", roles = {"USER"})
    void testShouldReturnForbiddenStatusViewNameForShowOrdersPageUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ORDER_CONTROLLER_PREFIX + "/all")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void testShouldReturnOkStatusViewNameForShowOrdersPageAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ORDER_CONTROLLER_PREFIX + "/all")).
                andExpect(status().isOk()).
                andExpect(view().name("orders"));
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN", "ROOT"})
    void testShouldReturnOkStatusViewNameForShowOrdersPageRoot() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                ORDER_CONTROLLER_PREFIX + "/all")).
                andExpect(status().isOk()).
                andExpect(view().name("orders"));
    }

}
