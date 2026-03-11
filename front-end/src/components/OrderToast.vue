<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from 'axios'

const show = ref(false)
const displayName = ref('')
const productName = ref('')
let timer = null

const fetchLatest = async () => {
    try {
        const res = await axios.get('http://localhost:8080/api/orders/latest')
        if (res.data && res.data.displayName) {
            displayName.value = res.data.displayName
            productName.value = res.data.productName
            show.value = true
            setTimeout(() => { show.value = false }, 4000)
        }
    } catch (err) {
        console.error(err)
    }
}

onMounted(() => {
    setTimeout(fetchLatest, 2000)
    timer = setInterval(fetchLatest, 30000)
})

onUnmounted(() => {
    clearInterval(timer)
})
</script>

<template>
    <transition name="toast-slide">
        <div v-if="show" class="order-toast">
            <div class="toast-icon">🛍️</div>
            
            <div class="toast-text">
                <span class="name">{{ displayName }}</span> 剛剛購買了
                <span class="product">{{ productName }}</span>
            </div>
        </div>
    </transition>
</template>

<style scoped>
.order-toast {
    position: fixed;
    bottom: 30px;
    left: 30px;
    background: white;
    border-left: 4px solid #9f9572;
    border-radius: 8px;
    padding: 12px 18px;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.12);
    z-index: 9999;
    max-width: 300px;
    font-size: 0.9rem;
}

.toast-icon {
    font-size: 1.3rem;
}

.toast-text {
    color: #444;
    line-height: 1.5;
}

.name {
    font-weight: bold;
    color: #9f9572;
}

.product {
    font-weight: bold;
    color: #333;
}

.toast-slide-enter-active,
.toast-slide-leave-active {
    transition: all 0.4s ease;
}

.toast-slide-enter-from,
.toast-slide-leave-to {
    opacity: 0;
    transform: translateX(-30px);
}
</style>