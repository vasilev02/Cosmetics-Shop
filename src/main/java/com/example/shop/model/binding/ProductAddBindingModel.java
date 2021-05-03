package com.example.shop.model.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductAddBindingModel {

    private String name;
    private MultipartFile image;
    private String producer;
    private BigDecimal price;
    private String description;
    private Integer count;

    public ProductAddBindingModel() {
    }

    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Size(min = 3, max = 15, message = "Producer must be between 3 and 15 characters")
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @NotNull(message = "Enter price")
    @Positive(message = "Enter positive price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Enter count")
    @Min(value = 1, message = "Enter positive count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
