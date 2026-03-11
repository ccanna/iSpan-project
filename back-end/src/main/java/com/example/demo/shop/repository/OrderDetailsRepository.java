package com.example.demo.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.shop.entity.OrderDetails;

import jakarta.transaction.Transactional;

public interface  OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    
    @Transactional
    void deleteByOrderId(Integer orderId);

    List<OrderDetails> findByProduct_ProductId(Integer productId);

}
