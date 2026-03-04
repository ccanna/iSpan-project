package com.example.demo.store.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.user.UserRepository;
import com.example.demo.store.dto.ReservationSettingsDto;
import com.example.demo.store.entity.OpenHour;
import com.example.demo.store.entity.Seat;
import com.example.demo.store.entity.SeatId;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.OpenHourRepository;
import com.example.demo.store.repository.SeatRepository;
import com.example.demo.store.repository.StoreInfoRepository;

@Service
public class StoreOperationService extends StoreBaseService {

    private final OpenHourRepository openHourRepository;
    private final SeatRepository seatRepository;

    // 手動寫建構子來調用 super(...)
    public StoreOperationService(UserRepository userRepository,
            StoreInfoRepository storeInfoRepository,
            OpenHourRepository openHourRepository,
            SeatRepository seatRepository) {
        super(userRepository, storeInfoRepository);
        this.openHourRepository = openHourRepository;
        this.seatRepository = seatRepository;
    }

    // 取得目前店家的預約設定
    public ReservationSettingsDto getReservationSettings() {
        // 1. 取得目前登入的店家資訊
        StoresInfo store = getMyStore();
        Integer storeId = store.getStoreId();

        ReservationSettingsDto dto = new ReservationSettingsDto();
        dto.setTimeSlot(store.getTimeSlot());
        dto.setTimeLimit(store.getTimeLimit());

        // 2. 獲取營業時間
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

        // 3. 獲取座位設定
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

    // 更新店家座位、營業時間與時段設定
    @Transactional
    public void updateReservationSettings(ReservationSettingsDto dto) {
        // 1. 取得目前登入的店家資訊
        StoresInfo store = getMyStore();

        Integer storeId = store.getStoreId();

        // 2. 更新店家的基礎設定 (時段、限時)
        store.setTimeSlot(dto.getTimeSlot());
        store.setTimeLimit(dto.getTimeLimit());
        storeInfoRepository.save(store);

        // 3. 更新營業時間 (先刪除，後新增)
        openHourRepository.deleteByStore_StoreId(storeId);
        openHourRepository.flush(); // 強制執行刪除

        if (dto.getOpenHours() != null) {
            List<OpenHour> newOpenHours = dto.getOpenHours().stream().map(hDto -> {
                LocalTime open = LocalTime.parse(hDto.getOpenTime());
                LocalTime close = LocalTime.parse(hDto.getCloseTime());

                // 阻擋跨日營業時間
                if (!close.isAfter(open)) {
                    throw new IllegalArgumentException("營業時間設定錯誤：結束時間必須晚於開始時間（星期 " + hDto.getDayOfWeek() + "）");
                }

                OpenHour oh = new OpenHour();
                oh.setStore(store);
                oh.setDayOfWeek(hDto.getDayOfWeek());
                oh.setOpenTime(open);
                oh.setCloseTime(close);
                return oh;
            }).toList();
            openHourRepository.saveAll(newOpenHours);
        }

        // 4. 更新座位設定 (先刪除，後新增)
        seatRepository.deleteById_StoreId(storeId);
        seatRepository.flush(); // 強制執行刪除

        if (dto.getSeatSettings() != null) {
            List<Seat> newSeats = dto.getSeatSettings().stream().map(sDto -> {
                Seat seat = new Seat();
                // 處理複合主鍵 SeatId
                SeatId sid = new SeatId(storeId, sDto.getSeatType());
                seat.setId(sid);
                seat.setStore(store);
                seat.setTotalCount(sDto.getTotalCount());
                return seat;
            }).toList();
            seatRepository.saveAll(newSeats);
        }

    }
}