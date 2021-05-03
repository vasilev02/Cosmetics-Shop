package com.example.shop.model.binding;

import javax.validation.constraints.*;

public class OrderAddBindingModel {

    private String fullName;
    private String phoneNumber;
    private Integer count;
    private String deliveryOption;
    private String address;
    private String additionalInformation;

    public OrderAddBindingModel() {
    }

    @Size(min = 3, max = 30, message = "Full name must be between 3 and 30 characters")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Enter valid phone number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull(message = "Choose count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    @Size(min = 5, max = 50, message = "Address must be between 5 and 50 characters")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Size(max = 150, message = "Additional information must be under 150 characters")
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

}
