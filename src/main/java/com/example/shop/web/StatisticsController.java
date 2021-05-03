package com.example.shop.web;

import com.example.shop.service.CommentService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import com.example.shop.web.interceptors.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

    private final UserService userService;
    private final ProductService productService;
    private final CommentService commentService;

    @Autowired
    public StatisticsController(UserService userService, ProductService productService, CommentService commentService) {
        this.userService = userService;
        this.productService = productService;
        this.commentService = commentService;
    }

    @PageTitle("Statistics")
    @GetMapping("/statistics")
    public String showStatisticsPage(Model model) {

        model.addAttribute("totalUsers", this.userService.getUsernames().size());
        model.addAttribute("totalProducts", this.productService.getAllProducts().size());
        model.addAttribute("totalComments", this.commentService.getCommentsCount());

        return "statistics";
    }

}
