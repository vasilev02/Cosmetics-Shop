package com.example.shop.model.service.userServiceModels;

import org.springframework.web.multipart.MultipartFile;

public class ProfileChangeImageServiceModel {

    private MultipartFile imageUrl;

    public ProfileChangeImageServiceModel() {
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }

}
