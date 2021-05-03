package com.example.shop.service;

import com.example.shop.model.service.orderServiceModels.OrderAddServiceModel;
import com.example.shop.model.service.orderServiceModels.OrderProfileServiceModel;
import com.example.shop.model.service.orderServiceModels.OrderShowServiceModel;

import java.util.List;

public interface OrderService {
    OrderAddServiceModel addOrder(String id, OrderAddServiceModel serviceModel, String name);

    List<OrderShowServiceModel> getAllOrders();

    void deleteOrderById(String id);

    void finishOrderById(String id);

    List<OrderProfileServiceModel> getAllUserOrdersByUsername(String name);
}
