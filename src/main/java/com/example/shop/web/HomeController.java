package com.example.shop.web;

import com.example.shop.service.UserService;
import com.example.shop.web.interceptors.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PageTitle("Home")
    @GetMapping("/")
    public String showIndexPage(Principal principal) {

        if (principal != null) {
            return "home";
        }
        return "index";
    }

    @PageTitle("Information")
    @GetMapping("/info")
    public String showInformationPage() {
        return "info";
    }

}
