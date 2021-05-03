package com.example.shop.service;

import com.example.shop.model.entity.Product;
import com.example.shop.model.service.productServiceModels.ProductAddServiceModel;
import com.example.shop.model.service.productServiceModels.ProductDetailsServiceModel;
import com.example.shop.model.service.productServiceModels.ProductEditServiceModel;
import com.example.shop.model.service.productServiceModels.ProductShopServiceModel;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductAddServiceModel addProduct(ProductAddServiceModel serviceModel) throws IOException;

    boolean checkNameWithProducerIfExists(String name, String producer);

    List<ProductShopServiceModel> getAllProducts();

    ProductDetailsServiceModel getProductById(String id);

    void deleteProductById(String id);

    ProductEditServiceModel editProduct(String id, ProductEditServiceModel serviceModel) throws IOException;

    Product getProductEntityById(String id);

    void saveProductChanges(Product product);

    Product getProductByNameAndProducer(String productName, String productProducer);

    void addComment(String id, String username, String message, String rating);
}
