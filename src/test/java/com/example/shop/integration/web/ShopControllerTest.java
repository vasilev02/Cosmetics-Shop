package com.example.shop.integration.web;

import com.example.shop.model.entity.Product;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ShopControllerTest {

    private static final String PRODUCT_CONTROLLER_PREFIX = "/shop";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        Product product = new Product();
        product.setName("Good girl");
        product.setProducer("AVON");
        product.setCount(5);
        product.setAddedOn(LocalDateTime.now());
        product.setPrice(BigDecimal.valueOf(50));
        product.setImageUrl("image url");
        product.setDescription("some description");
        product.setComments(null);
        this.productRepository.saveAndFlush(product);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER"})
    void testShouldReturnOkStatusForShowAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/all")).
                andExpect(status().isOk()).
                andExpect(view().name("products-shop"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER", "ADMIN"})
    void testShouldReturnValidStatusViewNameAndModelForDetails() throws Exception {

        Optional<Product> product = this.productRepository.findProductByNameAndProducer("Good girl", "AVON");

        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/product/{id}", product.get().getId()
        )).
                andExpect(status().isOk()).
                andExpect(view().name("product-details")).
                andExpect(model().attributeExists("productDetails"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER", "ADMIN"})
    void testShouldReturnValidStatusViewNameAndModelForProductDelete() throws Exception {

        Optional<Product> product = this.productRepository.findProductByNameAndProducer("Good girl", "AVON");

        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/product/delete/{id}", product.get().getId()
        )).
                andExpect(status().is3xxRedirection());

        Assertions.assertEquals(0, this.productRepository.count());
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER", "ADMIN"})
    void testShouldReturnValidStatusViewNameAndModelForProductEdit() throws Exception {

        Optional<Product> product = this.productRepository.findProductByNameAndProducer("Good girl", "AVON");

        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/product/edit/{id}", product.get().getId()
        )).
                andExpect(status().isOk()).
                andExpect(view().name("product-edit")).
                andExpect(model().attributeExists("productEditBindingModel"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER", "ADMIN"})
    void testShouldReturnValidStatusViewNameAndModelForProductBuy() throws Exception {

        Optional<Product> product = this.productRepository.findProductByNameAndProducer("Good girl", "AVON");

        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/product/buy/{id}", product.get().getId()
        )).
                andExpect(status().isOk()).
                andExpect(view().name("product-buy")).
                andExpect(model().attributeExists("orderAddBindingModel"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"USER", "ADMIN"})
    void testShouldReturnValidStatusViewNameAndModelForCommentDelete() throws Exception {

        Optional<Product> product = this.productRepository.findProductByNameAndProducer("Good girl", "AVON");

        mockMvc.perform(MockMvcRequestBuilders.get(
                PRODUCT_CONTROLLER_PREFIX + "/product/delete/{id}", product.get().getId()
        )).
                andExpect(status().is3xxRedirection());

        Assertions.assertEquals(0, this.productRepository.count());
    }

}
