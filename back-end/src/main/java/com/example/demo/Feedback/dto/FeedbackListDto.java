package com.example.demo.Feedback.dto;

import lombok.Data;

@Data
public class FeedbackListDto {
    private String status; // 狀態 (例如：已回覆)
    private String createdAt; // 建立日期
    private String typeName; // 類別 (例如：建議、抱怨)
    private String contents; // 完整內容 (前端拿這個來做 substring(0, 20))
    private String reply; // 客服回覆
    private String repliedAt; // 回覆日期
}
