package com.example.demo.store.controller;

import com.example.demo.store.dto.StoreCreateUpdateDto;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.service.StoreInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/owner/store")
@RequiredArgsConstructor
public class StoreInfoController {

    private final StoreInfoService storeInfoService;

    // 獲取當前登入店家的資訊
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StoresInfo>> getMyStoreInfo() {
        try {
            // 直接呼叫，如果沒店，BaseStoreService 會直接噴 RuntimeException
            StoresInfo store = storeInfoService.getMyStore();
            return ResponseEntity.ok(ApiResponse.success(store));
        } catch (RuntimeException e) {
            // 統一捕捉異常訊息並回傳
            return ResponseEntity.status(403).body(ApiResponse.error(e.getMessage()));
        }
    }

    // 根據 ID 獲取店家資訊 (公開端點)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoresInfo>> getStoreInfoById(@PathVariable Integer id) {
        try {
            StoresInfo store = storeInfoService.getStoreById(id);
            return ResponseEntity.ok(ApiResponse.success(store));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    // 更新當前店家的資訊 (支援圖片上傳，使用 multipart/form-data)
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StoresInfo>> updateMyStoreInfo(@Valid @ModelAttribute StoreCreateUpdateDto dto) {
        try {
            StoresInfo updatedStore = storeInfoService.updateMyStoreInfo(dto);
            return ResponseEntity.ok(ApiResponse.success("店家資訊更新成功", updatedStore));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
