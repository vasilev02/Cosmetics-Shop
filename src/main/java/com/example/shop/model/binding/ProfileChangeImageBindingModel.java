package com.example.shop.model.binding;

import org.springframework.web.multipart.MultipartFile;

public class ProfileChangeImageBindingModel {

    private MultipartFile imageUrl;

    public ProfileChangeImageBindingModel() {
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }
}
