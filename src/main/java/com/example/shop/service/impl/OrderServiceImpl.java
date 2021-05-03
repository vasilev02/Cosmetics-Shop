package com.example.shop.service.impl;

import com.example.shop.model.entity.Order;
import com.example.shop.model.entity.Product;
import com.example.shop.model.entity.User;
import com.example.shop.model.service.orderServiceModels.OrderAddServiceModel;
import com.example.shop.model.service.orderServiceModels.OrderProfileServiceModel;
import com.example.shop.model.service.orderServiceModels.OrderShowServiceModel;
import com.example.shop.repository.OrderRepository;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, UserService userService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderAddServiceModel addOrder(String id, OrderAddServiceModel serviceModel, String name) {

        Product product = this.productService.getProductEntityById(id);

        if (product.getCount() - serviceModel.getCount() < 0) {
            return serviceModel;
        }

        product.setCount(product.getCount() - serviceModel.getCount());
        this.productService.saveProductChanges(product);

        User user = this.userService.getUserEntityByUsername(name);

        Order order = this.modelMapper.map(serviceModel, Order.class);

        order.setUsername(user.getUsername());
        order.setEmail(user.getEmail());
        order.setFinished(false);
        order.setCreatedOn(LocalDateTime.now());
        order.setProductName(product.getName());
        order.setProductProducer(product.getProducer());

        BigDecimal sum = BigDecimal.valueOf(Long.parseLong(order.getCount().toString()));
        sum = sum.multiply(product.getPrice());

        order.setTotalPrice(sum);

        this.orderRepository.saveAndFlush(order);

        return serviceModel;
    }

    @Override
    public List<OrderShowServiceModel> getAllOrders() {
        return this.orderRepository.getAllOrders().stream().map(order -> {
            return this.modelMapper.map(order, OrderShowServiceModel.class);
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteOrderById(String id) {
        Order order = this.orderRepository.getOrderById(id).get();

        String productName = order.getProductName();
        String productProducer = order.getProductProducer();

        Product product = this.productService.getProductByNameAndProducer(productName, productProducer);
        product.setCount(product.getCount() + order.getCount());
        this.productService.saveProductChanges(product);

        this.orderRepository.deleteById(id);
    }

    @Override
    public void finishOrderById(String id) {

        Order order = this.orderRepository.getOrderById(id).get();
        order.setFinished(true);
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    public List<OrderProfileServiceModel> getAllUserOrdersByUsername(String name) {
        List<OrderProfileServiceModel> collect = this.orderRepository.getAllByUsername(name).stream().map(order -> {
            OrderProfileServiceModel serviceModel = this.modelMapper.map(order, OrderProfileServiceModel.class);
            serviceModel.setIsFinished(String.valueOf(order.isFinished()));
            return serviceModel;
        }).collect(Collectors.toList());

        return collect;
    }


}
