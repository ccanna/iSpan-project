package com.example.demo.store.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "open_hours")
public class OpenHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoresInfo store;

    @Column(nullable = false)
    private Integer dayOfWeek; // 0-6

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @AssertTrue(message = "結束時間必須晚於開始時間（目前不支援跨日營業）")
    public boolean isValidTimeRange() {
        if (openTime == null || closeTime == null) {
            return true; // 交給 @Column(nullable = false) 或 @NotNull 處理
        }
        return closeTime.isAfter(openTime);
    }
}