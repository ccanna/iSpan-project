package com.example.demo.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.dto.ApiResponse;
import com.example.demo.store.dto.ReservationSettingsDto;
import com.example.demo.store.service.StoreOperationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/owner/store/reservation-settings")
@RequiredArgsConstructor
public class StoreOperationController {

    private final StoreOperationService storeOperationService;

    // 獲取店家的預約設定 (座位、營業時間、時段配置)
    @GetMapping
    public ResponseEntity<ApiResponse<ReservationSettingsDto>> getReservationSettings() {
        try {
            ReservationSettingsDto settings = storeOperationService.getReservationSettings();
            return ResponseEntity.ok(ApiResponse.success(settings));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(ApiResponse.error(e.getMessage()));
        }
    }

    // 更新店家的預約設定
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateReservationSettings(@RequestBody ReservationSettingsDto dto) {
        try {
            storeOperationService.updateReservationSettings(dto);
            return ResponseEntity.ok(ApiResponse.success("預約設定更新成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
