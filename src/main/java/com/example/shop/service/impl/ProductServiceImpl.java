package com.example.shop.service.impl;

import com.example.shop.model.entity.Comment;
import com.example.shop.model.entity.Product;
import com.example.shop.model.service.productServiceModels.ProductAddServiceModel;
import com.example.shop.model.service.productServiceModels.ProductDetailsServiceModel;
import com.example.shop.model.service.productServiceModels.ProductEditServiceModel;
import com.example.shop.model.service.productServiceModels.ProductShopServiceModel;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.CloudinaryService;
import com.example.shop.service.CommentService;
import com.example.shop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CommentService commentService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductAddServiceModel addProduct(ProductAddServiceModel serviceModel) throws IOException {

        MultipartFile image = serviceModel.getImage();
        String imageUrl = cloudinaryService.uploadImage(image);

        Product product = this.modelMapper.map(serviceModel, Product.class);
        product.setAddedOn(LocalDateTime.now());
        product.setImageUrl(imageUrl);

        this.productRepository.saveAndFlush(product);
        return serviceModel;
    }

    @Override
    public boolean checkNameWithProducerIfExists(String name, String producer) {
        return this.productRepository.findProductByNameAndProducer(name, producer).isPresent();
    }

    @Override
    public List<ProductShopServiceModel> getAllProducts() {
        return this.productRepository.findProductByAddedOnOrderByAddedOnIdDesc().stream().map(product -> {
            return this.modelMapper.map(product, ProductShopServiceModel.class);
        }).collect(Collectors.toList());
    }

    @Override
    public ProductDetailsServiceModel getProductById(String id) {
        Optional<Product> product = this.productRepository.findById(id);

        product.get().getComments().sort(Comparator.comparing(Comment::getAddedOn).reversed());

        return this.modelMapper.map(product.get(), ProductDetailsServiceModel.class);
    }

    @Override
    public void deleteProductById(String id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public ProductEditServiceModel editProduct(String id, ProductEditServiceModel serviceModel) throws IOException {

        MultipartFile image = serviceModel.getImage();

        Optional<Product> product = this.productRepository.findById(id);

        product.get().setPrice(serviceModel.getPrice());
        product.get().setDescription(serviceModel.getDescription());
        product.get().setAddedOn(LocalDateTime.now());

        if (image.getSize() != 0) {
            String imageUrl = cloudinaryService.uploadImage(image);
            product.get().setImageUrl(imageUrl);
        }

        product.get().setCount(serviceModel.getCount());
        this.productRepository.saveAndFlush(product.get());
        return serviceModel;
    }

    @Override
    public Product getProductEntityById(String id) {
        return this.productRepository.findProductById(id).get();
    }

    @Override
    public void saveProductChanges(Product product) {
        this.productRepository.saveAndFlush(product);
    }

    @Override
    public Product getProductByNameAndProducer(String productName, String productProducer) {
        return this.productRepository.findProductByNameAndProducer(productName, productProducer).get();
    }

    @Override
    public void addComment(String id, String username, String message, String rating) {

        Product product = this.productRepository.findProductById(id).get();

        Comment comment = this.commentService.initializeComment(username, message, rating, product);

        List<Comment> comments = product.getComments();
        comments.add(comment);

        product.setComments(comments);

        this.productRepository.saveAndFlush(product);
    }
}
