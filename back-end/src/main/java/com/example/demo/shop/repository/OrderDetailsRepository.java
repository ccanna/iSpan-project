package com.example.demo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.shop.entity.OrderDetails;

public interface  OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    
}
