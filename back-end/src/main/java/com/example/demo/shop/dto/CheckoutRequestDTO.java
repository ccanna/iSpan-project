package com.example.demo.shop.dto;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    private String name;
    private String phone;
    private String city;
    private String district;
    private String street;
    private String deliveryMethod;
    private String paymentMethod;
    private String note;
}
