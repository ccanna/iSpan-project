

<script setup>

import storeCard from '../components/common/storeCard.vue'
import { useRouter} from 'vue-router'
// import { useCartStore } from '@/stores/cart';
import banner from '../components/common/banner.vue'


const router =useRouter();
const goToCart = () => {
  // 括號內填寫在 router/index.js 設定的 path
    router.push('/cart') 
}

// const cartStore = useCartStore()


</script>

<template>
<banner />


<storeCard />




<!-- 浮動購物車
<div class="position-fixed top-50 end-0 p-1 pe-3">
    <button type="button" class="btn btn-primary position-relative" @click="router.push('/cart')" >
    <i class="bi bi-cart4" ></i>

        <span class="badge bg-danger position-absolute top-0 start-100 translate-middle">
        {{ cartStore.totalQuantity }}
        </span>
    </button>
</div> -->



    
</template>

<style scoped>

</style>