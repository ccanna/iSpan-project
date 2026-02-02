<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';

const router = useRouter();

const email = ref('');
const username = ref('');
const password = ref('');
const confirmPassword = ref('');

const handleRegister = () => {
  // 基本驗證可以在此加入，但使用者要求直接 alert 並跳轉
  alert("註冊成功");
  router.push('/login');
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<template>
  <div class="register-page bg-gdg-light min-vh-100 d-flex align-items-center justify-content-center p-3">
    <BaseCard maxWidth="450px" :shadow="true" :hoverEffect="false">
      <template #header>
        <div class="text-center mb-4">
          <h2 class="text-gdg fw-bold">會員註冊</h2>
          <!-- <p class="text-muted small">歡迎來到綠色餐飲指南</p> -->
        </div>
      </template>

      <form @submit.prevent="handleRegister">
        <div class="mb-3">
          <label for="username" class="form-label text-gdg fw-medium">使用者名稱</label>
          <input 
            type="text" 
            id="username" 
            v-model="username" 
            class="form-control form-control-lg rounded-0 border-gdg-light" 
            placeholder="請輸入使用者名稱"
            required
          >
        </div>

        <div class="mb-3">
          <label for="email" class="form-label text-gdg fw-medium">電子郵件</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            class="form-control form-control-lg rounded-0 border-gdg-light" 
            placeholder="example@email.com"
            required
          >
        </div>
        
        <div class="mb-3">
          <label for="password" class="form-label text-gdg fw-medium">密碼</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            class="form-control form-control-lg rounded-0 border-gdg-light" 
            placeholder="請輸入密碼"
            required
          >
        </div>

        <div class="mb-4">
          <label for="confirmPassword" class="form-label text-gdg fw-medium">確認密碼</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="confirmPassword" 
            class="form-control form-control-lg rounded-0 border-gdg-light" 
            placeholder="請再次輸入密碼"
            required
          >
        </div>

        <div class="d-grid gap-3 pt-2">
          <BaseButton 
            color="gdg" 
            size="lg" 
            label="立即註冊" 
            type="submit"
          />
          
          <div class="text-center">
            <span class="text-muted small">已經有帳號了？</span>
            <BaseButton 
              color="outline-gdg" 
              size="sm" 
              label="登入會員" 
              class="ms-2 border-0"
              @click="goToLogin"
            />
          </div>
        </div>
      </form>
    </BaseCard>
  </div>
</template>

<style scoped>
.form-control:focus {
  border-color: #9f9572;
  box-shadow: 0 0 0 0.25rem rgba(160, 150, 115, 0.25);
}

.border-gdg-light {
  border-color: #e2dfd5;
}
</style>
