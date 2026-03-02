package com.example.demo.store.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReservationSettingsDto {
    private String name;
    private Integer timeSlot;
    private Integer timeLimit;

    private List<SeatSettingsDto> seatSettings;
    private List<OpenHourDto> openHours;

    @Data
    public static class SeatSettingsDto {
        private Integer seatType;
        private Integer totalCount;
    }

    @Data
    public static class OpenHourDto {
        private Integer dayOfWeek; // 0-6 代表周日到周六
        private String openTime; // 格式 "HH:mm"
        private String closeTime; // 格式 "HH:mm"
        private Boolean isClosed; // 是否全天休息
    }
}
