package com.example.demo.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingResponseDto {
    private Integer bookingId;
    private String userName; // 從 User 實體取得
    private String storeName; // 從 StoresInfo 實體取得
    private Integer reservedSeatType;
    private Integer guestCount;
    private String guestName;
    private String guestPhone;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean status;
    private LocalDateTime createdAt; // 顯示建立時間
}
