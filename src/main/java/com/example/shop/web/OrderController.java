package com.example.shop.web;

import com.example.shop.model.service.orderServiceModels.OrderShowServiceModel;
import com.example.shop.model.view.OrderShowViewModel;
import com.example.shop.service.OrderService;
import com.example.shop.web.interceptors.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PageTitle("Orders")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showOrdersPage(Model model) {

        List<OrderShowServiceModel> serviceModels = this.orderService.getAllOrders();

        List<OrderShowViewModel> viewModels = serviceModels.stream().map(order -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String date = order.getCreatedOn();
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            String formattedString = localDateTime.format(formatter);
            order.setCreatedOn(formattedString);

            return this.modelMapper.map(order, OrderShowViewModel.class);
        }).collect(Collectors.toList());

        model.addAttribute("orders", viewModels);

        return "orders";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteOrder(@PathVariable String id) {

        this.orderService.deleteOrderById(id);

        return "redirect:/orders/all";
    }

    @GetMapping("/finish/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String finishOrder(@PathVariable String id) {

        this.orderService.finishOrderById(id);

        return "redirect:/orders/all";
    }
}
