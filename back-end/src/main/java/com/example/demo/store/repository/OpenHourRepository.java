package com.example.demo.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.store.entity.OpenHour;

public interface OpenHourRepository extends JpaRepository<OpenHour, Integer> {

    // 1. 根據 storeId 找出該店所有的營業時間（用於顯示在編輯頁面）
    // 注意：這裡的方法名稱命名是根據 OpenHour 實體(entity) 裡找名為 store 的物件，再進去 store 裡面找 storeId 屬性
    List<OpenHour> findByStore_StoreId(Integer storeId);

    // 2. 根據 storeId 刪除所有營業時間（用於更新設定時，「先刪後增」的覆蓋邏輯）
    void deleteByStore_StoreId(Integer storeId);

    // 3. 根據 storeId 和 dayOfWeek 找出該店某天的營業時間
    Optional<OpenHour> findByStore_StoreIdAndDayOfWeek(Integer storeId, Integer dayOfWeek);
}