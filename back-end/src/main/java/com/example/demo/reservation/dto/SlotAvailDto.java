package com.example.demo.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotAvailDto {
    // 預約時段(格式: 12:00，所以用String)
    private String time;

    // 該時段是否可訂位
    private Boolean available;
}