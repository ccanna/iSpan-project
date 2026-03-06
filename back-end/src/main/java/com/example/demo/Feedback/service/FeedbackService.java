package com.example.demo.Feedback.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Feedback.dto.FeedbackListDto;
import com.example.demo.Feedback.dto.FeedbackRequestDto;
import com.example.demo.Feedback.entity.Feedback;
import com.example.demo.Feedback.entity.FeedbackTypes;
import com.example.demo.Feedback.repository.FeedbackRepository;
import com.example.demo.Feedback.repository.FeedbackStatusRepository;
import com.example.demo.Feedback.repository.FeedbackTypesRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final FeedbackStatusRepository feedbackStatusRepo;
    private final UserRepository userRepo;
    private final FeedbackTypesRepository feedbackTypesRepo;

    public void createFeedback(FeedbackRequestDto dto) {

        // 檢查是否空值
        if (Strings.isBlank(dto.getEmail())) {
            throw new RuntimeException("Email is required");
        }

        // dto轉換entity
        Feedback feedback = new Feedback();
        feedback.setEmail(dto.getEmail());
        feedback.setName(dto.getName());
        feedback.setPhone(dto.getPhone());
        feedback.setContents(dto.getContents());
        feedback.setCaseNumber(dto.getCaseNumber());

        // 處理關聯 (關鍵修正)
        // 處理 Type (從 DTO 拿到的 typeId)
        if (dto.getTypeId() != null) {
            FeedbackTypes type = new FeedbackTypes();
            type.setTypeId(dto.getTypeId());
            feedback.setFeedbackTypes(type);
        }

        // 處理已登入會員 (userId 存在時才關聯)
        if (dto.getUserId() != null) {
            userRepo.findById(dto.getUserId()).ifPresent(feedback::setUser);
        }

        // 寫入資料庫
        feedbackRepo.save(feedback);

    }

    public List<FeedbackTypes> getAllTypes() {
        return feedbackTypesRepo.findAll();
    }

    public List<FeedbackListDto> getFeedbacksByEmail(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該使用者"));

        List<Feedback> feedbacks = feedbackRepo.findByUserOrderByCreatedAtDesc(user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return feedbacks.stream().map(f -> {
            FeedbackListDto dto = new FeedbackListDto();
            dto.setStatus(f.getFeedbackStatus() != null ? f.getFeedbackStatus().getStatusName() : "處理中");
            dto.setCreatedAt(f.getCreatedAt().format(formatter));
            // 帶上類別名稱
            dto.setTypeName(f.getFeedbackTypes() != null ? f.getFeedbackTypes().getTypeName() : "一般");
            // 帶上「完整」內容
            dto.setContents(f.getContents());
            dto.setReply(f.getReply()); // null when no reply yet
            dto.setRepliedAt(f.getRepliedAt() != null ? f.getRepliedAt().format(formatter) : null);
            return dto;
        }).collect(Collectors.toList());
    }

}