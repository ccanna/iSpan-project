package com.example.demo.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.reservation.dto.BookingRequestDto;
import com.example.demo.reservation.dto.BookingResponseDto;
import com.example.demo.reservation.dto.SlotAvailDto;
import com.example.demo.reservation.entity.Booking;
import com.example.demo.reservation.repository.BookingRepository;
import com.example.demo.store.dto.ReservationSettingsDto;
import com.example.demo.store.entity.OpenHour;
import com.example.demo.store.entity.Seat;
import com.example.demo.store.entity.SeatId;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.OpenHourRepository;
import com.example.demo.store.repository.SeatRepository;
import com.example.demo.store.repository.StoreInfoRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final StoreInfoRepository storeInfoRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final OpenHourRepository openHourRepository;

    // 取得店家訂位設定
    // 與StoreOperationService.java中的getStoreBookingConfig()方法幾乎相同，但不限定商家本人登入
    // 重用 ReservationSettingsDto
    public ReservationSettingsDto getStoreReservationConfig(Integer storeId) {
        // 1. 抓取商店基礎資訊
        StoresInfo store = storeInfoRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("找不到該店家"));
        // 2. 準備 DTO
        ReservationSettingsDto dto = new ReservationSettingsDto();
        dto.setName(store.getStoreName());
        dto.setTimeSlot(store.getTimeSlot());
        dto.setTimeLimit(store.getTimeLimit());
        // 3. 獲取營業時間
        List<ReservationSettingsDto.OpenHourDto> ohDtos = openHourRepository.findByStore_StoreId(storeId).stream()
                .map(oh -> {
                    ReservationSettingsDto.OpenHourDto ohDto = new ReservationSettingsDto.OpenHourDto();
                    ohDto.setDayOfWeek(oh.getDayOfWeek());
                    ohDto.setOpenTime(oh.getOpenTime().toString());
                    ohDto.setCloseTime(oh.getCloseTime().toString());
                    ohDto.setIsClosed(false);
                    return ohDto;
                }).toList();
        dto.setOpenHours(ohDtos);
        // 4. 獲取座位設定
        List<ReservationSettingsDto.SeatSettingsDto> sDtos = seatRepository.findByIdStoreId(storeId).stream()
                .map(s -> {
                    ReservationSettingsDto.SeatSettingsDto sDto = new ReservationSettingsDto.SeatSettingsDto();
                    sDto.setSeatType(s.getId().getSeatType());
                    sDto.setTotalCount(s.getTotalCount());
                    return sDto;
                }).toList();
        dto.setSeatSettings(sDtos);
        return dto;
    }

    // 取得某一天某桌型的可訂位時段
    public List<SlotAvailDto> getAvailableSlots(Integer storeId, LocalDate date, Integer seatType) {
        // 獲取店家資訊
        StoresInfo store = storeInfoRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("找不到該店家"));

        // 獲取店家timeSlot (時段間隔) 和 timeLimit (用餐限時)
        Integer timeSlot = store.getTimeSlot();
        Integer timeLimit = store.getTimeLimit();

        // 取得當天是星期幾 (Java 的列舉 1-7)
        int dayOfWeek = date.getDayOfWeek().getValue();
        // 資料庫存 0(日)-6(六)，要做轉換
        int dbDayValue = (dayOfWeek == 7) ? 0 : dayOfWeek;

        // 獲取OpenHour
        OpenHour openHour = openHourRepository.findByStore_StoreIdAndDayOfWeek(storeId, dbDayValue)
                .orElseThrow(() -> new RuntimeException("該店家今日未營業"));

        // 使用複合主鍵 SeatId 查詢
        SeatId seatId = new SeatId(storeId, seatType);
        Seat seatConfig = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("該店未提供此桌型"));
        Integer totalTables = seatConfig.getTotalCount();

        // -----------------開始寫可訂位時段迴圈------------------
        List<SlotAvailDto> slots = new ArrayList<>();
        LocalTime currentTime = openHour.getOpenTime();
        LocalTime closeTime = openHour.getCloseTime(); // 最後可訂位時間

        while (!currentTime.isAfter(closeTime)) {
            // A. 計算當前時段的末端
            LocalTime slotEnd = currentTime.plusMinutes(timeLimit);

            // B. 調用下方createBooking()方法裡面的 countOverlappingBookings() repository 方法
            long occupied = bookingRepository.countOverlappingBookings(
                    storeId, date, seatType, currentTime, slotEnd);

            // C. 判斷是否為過去時間
            boolean isPast = date.equals(LocalDate.now()) && currentTime.isBefore(LocalTime.now());

            // D. 判定並加入清單 (occupied < totalTables)
            boolean isAvailable = (occupied < totalTables) && !isPast;
            slots.add(new SlotAvailDto(currentTime.toString(), isAvailable));

            // E. 往後跳過一個時段間隔
            currentTime = currentTime.plusMinutes(timeSlot);

        }

        return slots;
    }

    // 前端輸入建立訂位
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        // 1. 驗證使用者與店家是否存在
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("找不到該使用者"));
        StoresInfo store = storeInfoRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("找不到該店家"));

        // 2. 處理結束時間 (如果 Dto 沒傳，則根據店家 timeLimit 計算)
        LocalTime startTime = dto.getStartTime();
        LocalTime endTime = dto.getEndTime();
        if (endTime == null) {
            endTime = startTime.plusMinutes(store.getTimeLimit());
        }

        // 3. 檢查座位的可用性
        // a. 獲取該店該桌型的總桌數
        SeatId seatId = new SeatId(store.getStoreId(), dto.getReservedSeatType());
        Seat seatConfig = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("該店家未設定此類別的座位"));
        Integer totalTables = seatConfig.getTotalCount();

        // b. 檢查時間重疊的已訂位數量
        long currentBookings = bookingRepository.countOverlappingBookings(
                store.getStoreId(), // id為該店家
                dto.getBookingDate(), // 前台傳入的日期
                dto.getReservedSeatType(), // 前台傳入的座位類別
                startTime,
                endTime);

        if (currentBookings >= totalTables) {
            throw new RuntimeException("該時段此類別座位已滿");
        }

        // 4. 建立訂位實體並儲存
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setStore(store);
        booking.setReservedSeatType(dto.getReservedSeatType());
        booking.setGuestCount(dto.getGuestCount());
        booking.setGuestName(dto.getGuestName());
        booking.setGuestPhone(dto.getGuestPhone());
        booking.setBookingDate(dto.getBookingDate());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(true);

        Booking savedBooking = bookingRepository.save(booking);

        // 5. 轉換回 Response Dto
        return convertToResponse(savedBooking);
    }

    private BookingResponseDto convertToResponse(Booking booking) {
        BookingResponseDto resp = new BookingResponseDto();
        resp.setBookingId(booking.getBookingId());
        resp.setUserName(booking.getUser().getName()); // 使用 User.getName()
        resp.setStoreName(booking.getStore().getStoreName());
        resp.setReservedSeatType(booking.getReservedSeatType());
        resp.setGuestCount(booking.getGuestCount());
        resp.setGuestName(booking.getGuestName());
        resp.setGuestPhone(booking.getGuestPhone());
        resp.setBookingDate(booking.getBookingDate());
        resp.setStartTime(booking.getStartTime());
        resp.setEndTime(booking.getEndTime());
        resp.setStatus(booking.getStatus());
        resp.setCreatedAt(booking.getCreatedAt());
        return resp;
    }
}
