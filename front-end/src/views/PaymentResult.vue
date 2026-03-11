<script setup>
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'

const route = useRoute()
const router = useRouter()
const isSuccess = ref(false)
const orderInfo = ref({})

onMounted(() => {
    const status = route.query.status
    isSuccess.value = status === 'success'
    orderInfo.value = {
        merchantTradeNo: route.query.tradeNo,
        amount: route.query.amount,
        paymentDate: route.query.paymentDate,
    }
})
</script>

<template>
    <div style="text-align:center; padding: 60px 20px;">
        <div v-if="isSuccess">
            <h2 style="color: #198754;">✅ 付款成功！</h2>
            <p>訂單編號：{{ orderInfo.merchantTradeNo }}</p>
            <p>交易金額：NT$ {{ orderInfo.amount }}</p>
            <p>付款時間：{{ orderInfo.paymentDate }}</p>
        </div>
        <div v-else>
            <h2 style="color: #dc3545;">❌ 付款失敗</h2>
            <p>請重新嘗試或選擇其他付款方式</p>
        </div>
        <button @click="router.push('/shopStore')" 
            style="margin-top:30px; padding:10px 30px; background:#198754; color:white; border:none; border-radius:8px; cursor:pointer;">
            返回商店
        </button>
    </div>
</template>