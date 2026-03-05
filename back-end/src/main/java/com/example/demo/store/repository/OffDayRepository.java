package com.example.demo.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.store.entity.OffDay;

public interface OffDayRepository extends JpaRepository<OffDay, Integer> {
    List<OffDay> findByStore_StoreId(Integer storeId);
    void deleteByStore_StoreId(Integer storeId);
}