<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { storeAPI } from '@/api/store';
import BaseButton from '@/components/common/BaseButton.vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
const router = useRouter();
const authStore = useAuthStore();
const route = useRoute();

// 店家資訊相關
const storeName = ref('');
const storeDescription = ref('');
const storePhone = ref('');
const storeAddress = ref('');
const myLabels = ref([]);
const coverImage = ref(''); // 儲存資料庫回傳的檔名

// 計算圖片路徑
const getImageUrl = (imgName) => {
    if (!imgName) return 'https://placehold.co/600x400?text=No+Image';
    return `/pictures/StoreProfile/${imgName}`;
};

// 獲取店家資訊(將後端資料庫值存入對應的 ref 變數)
const fetchStoreInfo = async () => {
    const id = route.params.id;
    if (!id) return;

    try {
        const response = await storeAPI.getStoreInfoById(id);
        console.log('API Response:', response);

        // 防禦性檢查：確保 response 存在且具有 success 屬性
        if (response && response.success) {
            const data = response.data;
            if (data) {
                storeName.value = data.storeName;
                storeDescription.value = data.description;
                storePhone.value = data.storePhone;
                storeAddress.value = data.address;
                coverImage.value = data.coverImage;
                myLabels.value = data.categories || [];
            } else {
                console.warn('店家資料為空');
            }
        } else {
            console.error('後端回傳失敗:', response ? response.message : '無回應');
        }
    } catch (error) {
        console.error('獲取店家資訊失敗:', error);
    }
};

// 頁面載入時獲取店家資訊
onMounted(() => {
    fetchStoreInfo();
});

// 前往訂位頁面
const goToReservation = () => {
    if (!authStore.isLoggedIn) {
        // 如果沒登入，跳轉到登入頁，並帶上當前路徑以便登入後跳回來
        router.push({ name: 'Login', query: { redirect: route.fullPath } });
        return;
    }
    // 已登入，跳轉到訂位頁
    router.push({ name: 'Reservation', params: { id: route.params.id } });
};

</script>

<template>
    <div class="container py-4">
        <div v-if="storeName" class="mb-4">
            <div class="card shadow-sm mb-4">
                <div class="row g-0">
                    <div class="col-md-5">
                        <img :src="getImageUrl(coverImage)" class="img-fluid rounded-start h-100 object-fit-contain"
                            :alt="storeName">
                    </div>
                    <div class="col-md-7">
                        <div class="card-body p-4">
                            <h1 class="card-title text-gdg h2 mb-3">{{ storeName }}</h1>

                            <div class="mb-3">
                                <span v-for="cat in myLabels" :key="cat.categoryId" class="badge bg-secondary me-2">
                                    {{ cat.categoryName }}
                                </span>
                            </div>

                            <div class="mb-4 border-bottom pb-3">
                                <p class="card-text text-muted">{{ storeDescription || '暫無描述' }}</p>
                            </div>

                            <div class="row g-3">
                                <div class="col-12">
                                    <i class="bi bi-geo-alt-fill text-gdg me-2"></i>
                                    <strong>地址：</strong> {{ storeAddress }}
                                </div>
                                <div class="col-12">
                                    <i class="bi bi-telephone-fill text-gdg me-2"></i>
                                    <strong>電話：</strong> {{ storePhone }}
                                </div>
                            </div>

                            <div class="mt-4 pt-3">
                                <div class="mt-4 pt-3">
                                    <i class="bi bi-calendar-check-fill text-gdg me-2"> *預約功能需先登入</i>
                                    <br>
                                    <BaseButton color="gdg" @click="goToReservation" class="px-5">
                                        我要訂位
                                    </BaseButton>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div v-else class="text-center py-5">
            <div class="spinner-border text-gdg mb-3" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="text-muted">正在載入專屬店家資訊...</p>
        </div>
    </div>
</template>

<style scoped></style>