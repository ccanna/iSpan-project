package com.example.demo.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.shop.entity.Stock;

public interface  StockRepository extends JpaRepository<Stock, Integer> {
    

    Optional<Stock> findByProductId(Integer productId);

}
