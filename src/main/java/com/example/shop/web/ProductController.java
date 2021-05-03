package com.example.shop.web;

import com.example.shop.model.binding.ProductAddBindingModel;
import com.example.shop.model.service.productServiceModels.ProductAddServiceModel;
import com.example.shop.service.ProductService;
import com.example.shop.web.interceptors.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @PageTitle("Add product")
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddProductPage(Model model) {
        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
            model.addAttribute("nameProducerError", false);
            model.addAttribute("noImage", false);
        }
        return "product-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addProduct(@Valid ProductAddBindingModel productAddBindingModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);

            if (productAddBindingModel.getImage().getSize() == 0) {
                redirectAttributes.addFlashAttribute("noImage", true);
            }
            return "redirect:/products/add";
        }
        if (productAddBindingModel.getImage().getSize() == 0) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("noImage", true);
            return "redirect:/products/add";
        }

        if (this.productService.checkNameWithProducerIfExists(productAddBindingModel.getName(), productAddBindingModel.getProducer())) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("nameProducerError", bindingResult);
            return "redirect:/products/add";
        }

        ProductAddServiceModel serviceModel = this.modelMapper.map(productAddBindingModel, ProductAddServiceModel.class);
        this.productService.addProduct(serviceModel);
        redirectAttributes.addFlashAttribute("added", true);
        return "redirect:/shop/all";
    }
}

