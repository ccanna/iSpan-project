<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';

const router = useRouter();
const email = ref('');
const password = ref('');

const goToRegister = () => {
  router.push('/register');
};

const handleLogin = () => {
  console.log('Login attempt with:', email.value, password.value);
};
</script>

<template>
  <div class="login-page bg-gdg-light min-vh-100 d-flex align-items-center justify-content-center p-3">
    <BaseCard maxWidth="450px" :shadow="true" :hoverEffect="false">
      <template #header>
        <div class="text-center mb-4">
          <h2 class="text-gdg fw-bold">會員登入</h2>
          <!-- <p class="text-muted small">歡迎來到綠色餐飲指南</p> -->
        </div>
      </template>

      <form @submit.prevent="handleLogin">
        <div class="mb-4">
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
        
        <div class="mb-4">
          <label for="password" class="form-label text-gdg fw-medium">密碼</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            class="form-control form-control-lg rounded-0 border-gdg-light" 
            placeholder="輸入密碼"
            required
          >
        </div>

        <div class="d-grid gap-3 pt-2">
          <BaseButton 
            color="gdg" 
            size="lg" 
            label="立即登入" 
            @click="handleLogin"
          />
          
          <div class="text-center">
            <span class="text-muted small">尚未擁有帳號？</span>
            <BaseButton 
              color="outline-gdg" 
              size="sm" 
              label="註冊會員" 
              class="ms-2 border-0"
              @click="goToRegister"
            />
          </div>
        </div>
      </form>

      <template #footer>
        <div class="text-center border-top pt-3 mt-2">
          <a href="#" class="text-muted small text-decoration-none">忘記密碼？</a>
        </div>
      </template>
    </BaseCard>
  </div>
</template>

<style scoped>
/* Specific input refinement for GDG style */
.form-control:focus {
  border-color: #9f9572;
  box-shadow: 0 0 0 0.25rem rgba(160, 150, 115, 0.25);
}

.border-gdg-light {
  border-color: #e2dfd5;
}
</style>