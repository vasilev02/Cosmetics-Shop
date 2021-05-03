package com.example.shop.model.service.userServiceModels;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterServiceModel {

    private String username;
    private String email;
    private String password;

    public UserRegisterServiceModel() {
    }

    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Enter valid email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{6,}$", message = "Minimum six characters with at least one uppercase,lowercase,digit and symbol")
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

}
