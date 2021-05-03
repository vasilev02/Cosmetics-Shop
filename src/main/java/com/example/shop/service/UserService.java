package com.example.shop.service;

import com.example.shop.model.entity.User;
import com.example.shop.model.service.orderServiceModels.OrderProfileServiceModel;
import com.example.shop.model.service.userServiceModels.ProfileChangeImageServiceModel;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.service.userServiceModels.UserRegisterServiceModel;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserRegisterServiceModel registerAndLoginUser(UserRegisterServiceModel serviceModel);

    boolean checkUsernameIfExists(String username);

    boolean checkEmailIfExists(String email);

    List<String> getUsernames();

    void changeRole(String username, String role);

    UserProfileServiceModel getUserByUsername(String name);

    void deleteProfileById(String id);

    User getUserEntityByUsername(String name);


    void changeProfilePicture(ProfileChangeImageServiceModel serviceModel, String name) throws IOException;
}
