<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import BaseButton from '@/components/common/BaseButton.vue';
import Swal from 'sweetalert2';
import { useAuthStore } from '@/stores/auth'; // 引入你的身份驗證 Store

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();
const storeId = route.params.id;

// 1. 統一變數名稱
const storeConfig = ref(null);
const availableTimeSlots = ref([]); // 建議統一用這個名字
// 2. 修正計算屬性與輔助邏輯
const availableTableTypes = computed(() => {
    if (!storeConfig.value || !storeConfig.value.seatSettings) return [];
    return storeConfig.value.seatSettings.map(s => s.seatType);
});
// 3. 修正日期限制邏輯 (重新補上)
const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
const minDate = tomorrow.toISOString().split('T')[0];
const maxDate = computed(() => {
    const d = new Date();
    d.setMonth(d.getMonth() + 1);
    return d.toISOString().split('T')[0];
});
// 4. 修正 BookingForm 欄位
const bookingForm = ref({
    userId: authStore.user?.id || null,
    storeId: storeId,
    reservedSeatType: null,
    bookingDate: '',
    startTime: '', // 統一使用 startTime
    guestCount: 1,      // 新增：預約人數
    guestName: '',      // 新增：訪客姓名
    guestPhone: ''      // 新增：訪客電話
});
// 5. 修正 API 呼叫方法
const fetchStoreConfig = async () => {
    // 增加判斷：確保 ID 存在且不是字串 "undefined"
    if (!storeId || storeId === 'undefined') {
        console.warn('店鋪 ID 無效，不發送請求');
        return;
    }
    const res = await axios.get(`/api/bookings/config/${storeId}`);
    storeConfig.value = res.data;
};
const fetchSlots = async () => { // 統一命名為 fetchSlots
    if (!bookingForm.value.bookingDate || !bookingForm.value.reservedSeatType) return;
    const res = await axios.get('/api/bookings/available-slots', {
        params: { storeId, date: bookingForm.value.bookingDate, seatType: bookingForm.value.reservedSeatType }
    });
    availableTimeSlots.value = res.data;
};
// 6. 補上重置與送出方法
const resetTime = () => { bookingForm.value.startTime = ''; };
const handleBooking = async () => {
    if (!bookingForm.value.startTime) {
        Swal.fire('請選擇時段', '', 'warning');
        return;
    }
    // 調用寫好的 POST API
    await axios.post('/api/bookings', bookingForm.value, {
        headers: {
            Authorization: `Bearer ${authStore.token}` // 取得 token
        }
    });
    await Swal.fire('訂位成功！', '', 'success');
    router.push({ name: 'UserBookings' });
};

onMounted(fetchStoreConfig);
watch([() => bookingForm.value.bookingDate, () => bookingForm.value.reservedSeatType], () => {
    resetTime();
    fetchSlots();
});
</script>

<template>
    <div class="container py-4">
        <h1 class="text-gdg mb-4">{{ storeConfig?.name || '讀取中...' }}</h1>

        <div class="bg-gdg-light p-4 border mb-4">
            <p class="mb-0 text-muted">
                請選擇您的預約資訊。若該時段桌位已滿，按鈕將會隱藏。
            </p>
        </div>

        <div class="booking-options">
            <div class="mb-4">
                <label class="form-label d-block fw-bold mb-2">1. 選擇座位類型</label>
                <div class="d-flex flex-wrap gap-2">
                    <button v-for="type in availableTableTypes" :key="type" type="button" class="btn"
                        :class="bookingForm.reservedSeatType === type ? 'btn-gdg' : 'btn-outline-secondary'"
                        @click="bookingForm.reservedSeatType = type; resetTime()">
                        {{ type }} 人座
                    </button>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label fw-bold mb-2">2. 選擇日期</label>
                <input type="date" class="form-control" v-model="bookingForm.bookingDate" style="max-width: 300px;"
                    :min="minDate" :max="maxDate" @change="resetTime">
            </div>

            <div class="mb-4">
                <label class="form-label d-block fw-bold mb-2">3. 選擇時段</label>
                <div class="d-flex flex-wrap gap-2">
                    <template v-for="slot in availableTimeSlots" :key="slot.time">
                        <button v-if="slot.available" type="button" class="btn"
                            :class="bookingForm.startTime === slot.time ? 'btn-gdg' : 'btn-outline-secondary'"
                            @click="bookingForm.startTime = slot.time">
                            {{ slot.time }}
                        </button>
                    </template>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label fw-bold mb-2">4. 訪客資訊</label>
                <div class="row g-3">
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="姓名" v-model="bookingForm.guestName">
                    </div>
                    <div class="col-md-4">
                        <input type="tel" class="form-control" placeholder="電話" v-model="bookingForm.guestPhone">
                    </div>
                    <div class="col-md-4">
                        <input type="number" class="form-control" placeholder="人數" v-model="bookingForm.guestCount"
                            min="1">
                    </div>
                </div>
            </div>

            <hr class="my-4">

            <BaseButton color="gdg" class="w-100" @click="handleBooking">
                確認訂位
            </BaseButton>
        </div>
    </div>
</template>