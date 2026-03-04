package com.example.demo.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long userId; // 預約人的 ID
    private Integer storeId; // 店家的 ID
    private Integer reservedSeatType;
    private Integer guestCount;
    private String guestName;
    private String guestPhone;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
