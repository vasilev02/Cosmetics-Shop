package com.example.shop.web.api;

import com.example.shop.model.view.ProductShopViewModel;
import com.example.shop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/products")
@RestController
public class ProductRestController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductRestController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api")
    public ResponseEntity<List<ProductShopViewModel>> findAll() {

        List<ProductShopViewModel> viewModels = this.productService.getAllProducts().stream().map(product -> {
            return this.modelMapper.map(product, ProductShopViewModel.class);
        }).collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(viewModels);
    }


}
