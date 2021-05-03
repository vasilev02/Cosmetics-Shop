package com.example.shop.web;

import com.example.shop.model.binding.OrderAddBindingModel;
import com.example.shop.model.binding.ProductEditBindingModel;
import com.example.shop.model.service.orderServiceModels.OrderAddServiceModel;
import com.example.shop.model.service.productServiceModels.ProductDetailsServiceModel;
import com.example.shop.model.service.productServiceModels.ProductEditServiceModel;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.view.CommentViewModel;
import com.example.shop.model.view.ProductDetailsViewModel;
import com.example.shop.model.view.ProductShopViewModel;
import com.example.shop.service.CommentService;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import com.example.shop.web.interceptors.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final CommentService commentService;
    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShopController(ProductService productService, CommentService commentService, OrderService orderService, UserService userService, ModelMapper modelMapper) {
        this.productService = productService;
        this.commentService = commentService;
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PageTitle("All products")
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public String showShopPage(Model model) {

        List<ProductShopViewModel> viewModels = this.productService.getAllProducts().stream().map(product -> {
            return this.modelMapper.map(product, ProductShopViewModel.class);
        }).collect(Collectors.toList());

        model.addAttribute("products", viewModels);
        return "products-shop";
    }

    @PageTitle("Details")
    @GetMapping("/product/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetailsProductPage(@PathVariable String id, Model model) {

        ProductDetailsServiceModel serviceModel = this.productService.getProductById(id);
        ProductDetailsViewModel viewModel = this.modelMapper.map(serviceModel, ProductDetailsViewModel.class);

        List<CommentViewModel> commentViewModels = viewModel.getComments().stream().peek(comment -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String date = comment.getAddedOn();
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            String formattedString = localDateTime.format(formatter);
            comment.setAddedOn(formattedString);
            UserProfileServiceModel user = this.userService.getUserByUsername(comment.getUsername());
            comment.setImageUrl(user.getImageUrl());

        }).collect(Collectors.toList());

        viewModel.setComments(commentViewModels);

        model.addAttribute("productDetails", viewModel);
        return "product-details";
    }

    @GetMapping("/product/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteProduct(@PathVariable String id) {

        this.productService.deleteProductById(id);
        return "redirect:/shop/all";
    }

    @PageTitle("Edit")
    @GetMapping("/product/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditProductPage(@PathVariable String id, Model model) {

        ProductDetailsServiceModel serviceModel = this.productService.getProductById(id);
        ProductDetailsViewModel viewModel = this.modelMapper.map(serviceModel, ProductDetailsViewModel.class);
        model.addAttribute("productEditDetails", viewModel);

        if (!model.containsAttribute("productEditBindingModel")) {
            model.addAttribute("productEditBindingModel", new ProductEditBindingModel());
        }
        return "product-edit";
    }

    @PostMapping("/product/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editProduct(@Valid ProductEditBindingModel productEditBindingModel,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable String id) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productEditBindingModel", productEditBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productEditBindingModel", bindingResult);

            return "redirect:/shop/product/edit/" + id;
        }

        ProductEditServiceModel serviceModel = this.modelMapper.map(productEditBindingModel, ProductEditServiceModel.class);
        this.productService.editProduct(id, serviceModel);
        return "redirect:/shop/product/" + id;
    }

    @PageTitle("Buy")
    @GetMapping("/product/buy/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showBuyProductPage(@PathVariable String id, Model model) {

        ProductDetailsServiceModel serviceModel = this.productService.getProductById(id);

        if (serviceModel.getCount() == 0) {
            return "redirect:/shop/product/" + serviceModel.getId();
        }

        ProductDetailsViewModel viewModel = this.modelMapper.map(serviceModel, ProductDetailsViewModel.class);
        model.addAttribute("product", viewModel);

        if (!model.containsAttribute("orderAddBindingModel")) {
            model.addAttribute("orderAddBindingModel", new OrderAddBindingModel());
        }
        return "product-buy";
    }

    @PostMapping("/product/buy/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String buyProduct(@Valid OrderAddBindingModel orderAddBindingModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             @PathVariable String id, Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderAddBindingModel", orderAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderAddBindingModel", bindingResult);
            return "redirect:/shop/product/buy/" + id;
        }

        OrderAddServiceModel serviceModel = this.modelMapper.map(orderAddBindingModel, OrderAddServiceModel.class);
        this.orderService.addOrder(id, serviceModel, principal.getName());
        return "redirect:/";
    }

    @PostMapping("/product/comment/{id}")
    @PreAuthorize("isAuthenticated()")
    public String commentProduct(@PathVariable String id, @RequestParam String rating, @RequestParam String message,
                                 Principal principal) {

        if (message.length() < 5 || message.length() > 500) {
            return "redirect:/shop/product/" + id;
        }

        int intRate = Integer.parseInt(rating);
        if (intRate < 1 || intRate > 5) {
            return "redirect:/shop/product/" + id;
        }

        this.productService.addComment(id, principal.getName(), message, rating);
        return "redirect:/shop/product/" + id;
    }

    @GetMapping("/product/delete/comment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteComment(@PathVariable String id) {
        String productId = this.commentService.deleteCommentById(id);
        return "redirect:/shop/product/" + productId;
    }


}
