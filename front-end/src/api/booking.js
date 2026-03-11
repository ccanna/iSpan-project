import api from './config';

export const bookingAPI = {
    /**
     * 獲取特定使用者的所有訂位
     * @param {number|string} userId - 使用者 ID
     */
    getUserBookings(userId) {
        return api.get(`/bookings/user/${userId}`);
    },

    /**
     * 獲取特定店家的所有訂位資料 (商家模式)
     * @param {number|string} storeId - 店家 ID
     */
    getStoreBookings(storeId) {
        return api.get(`/bookings/store/${storeId}`);
    },

    /**
     * 獲取店家的訂位基本配置（桌型、規則等）
     * @param {number|string} storeId - 店家 ID
     */
    getBookingConfig(storeId) {
        return api.get(`/bookings/config/${storeId}`);
    },

    /**
     * 根據日期與桌型查詢可用時段
     * @param {Object} params - 包含 storeId, date, seatType
     */
    getAvailableSlots(params) {
        return api.get('/bookings/available-slots', { params });
    },

    /**
     * 建立新訂位
     * @param {Object} data - 訂位資料
     */
    createBooking(data) {
        return api.post('/bookings', data);
    },

    /**
     * 使用者模式更新訂位資訊 (通常僅限姓名、電話)
     * @param {number|string} bookingId - 訂位 ID
     * @param {Object} data - 更新資料
     */
    updateUserBooking(bookingId, data) {
        return api.put(`/bookings/${bookingId}`, data);
    },

    /**
     * 商家模式更新訂位 (例如修改時間、日期)
     * @param {number|string} bookingId - 訂位 ID
     * @param {Object} data - 更新資料
     */
    updateStoreBooking(bookingId, data) {
        return api.put(`/bookings/store/${bookingId}`, data);
    },

    /**
     * 刪除或取消訂位
     * @param {number|string} bookingId - 訂位 ID
     */
    deleteBooking(bookingId) {
        return api.delete(`/bookings/${bookingId}`);
    }
};

export default bookingAPI;
