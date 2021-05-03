package com.example.shop.unit.services;

import com.example.shop.model.entity.Order;
import com.example.shop.model.service.orderServiceModels.OrderProfileServiceModel;
import com.example.shop.model.service.orderServiceModels.OrderShowServiceModel;
import com.example.shop.repository.OrderRepository;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import com.example.shop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    private Order order;

    private OrderServiceImpl serviceToTest;

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private ProductService mockProductService;

    @Mock
    private UserService mockUserService;

    @BeforeEach
    public void init() {

        order = new Order();

        order.setUsername("USERNAME");
        order.setPhoneNumber("0888 888 888");
        order.setFullName("FULL NAME");
        order.setEmail("EMAIL");
        order.setFinished(false);
        order.setCreatedOn(LocalDateTime.now());
        order.setProductName("PRODUCT NAME");
        order.setProductProducer("PRODUCER");
        order.setTotalPrice(BigDecimal.valueOf(150));
        order.setAddress("ADDRESS");
        order.setCount(1);
        order.setDeliveryOption("Econt");

        serviceToTest = new OrderServiceImpl(mockOrderRepository,mockProductService,mockUserService,new ModelMapper());
    }

    @Test
    public void testGetAllOrders() {
        Mockito.when(mockOrderRepository.getAllOrders()).thenReturn(List.of(order));
        List<OrderShowServiceModel> allOrders = serviceToTest.getAllOrders();
        Assertions.assertEquals(1, allOrders.size());
    }

    @Test
    public void testGetAllOrdersByUsername() {
        Mockito.when(mockOrderRepository.getAllByUsername("USERNAME")).thenReturn(List.of(order));
        List<OrderProfileServiceModel> allOrders = serviceToTest.getAllUserOrdersByUsername("USERNAME");
        Assertions.assertEquals(1, allOrders.size());
    }


}
