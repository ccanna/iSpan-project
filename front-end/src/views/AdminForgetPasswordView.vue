<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import BaseCard from '@/components/common/BaseCard.vue';
import BaseButton from '@/components/common/BaseButton.vue';
import api from '@/api/config';
import Swal from 'sweetalert2';

const router = useRouter();
const email = ref('');
const isSubmitting = ref(false);

const handleSendResetLink = async () => {
    if (isSubmitting.value) return;

    isSubmitting.value = true;
    const data = { email: email.value };

    try {
        console.log('Admin forgot password request with:', data);
        const response = await api.post('/admins/forgot-password', data);
        console.log('Admin forgot password success:', response);
        
        await Swal.fire({
            icon: 'success',
            title: '重設信件已發送',
            text: `管理員密碼重設信件已發送至 ${email.value}`,
            confirmButtonColor: '#1e3c72'
        });
        
        router.push('/admin/login');
    } catch (error) {
        console.error('Admin forgot password failed:', error);
        
        let errorMessage = '請稍後再試。';
        if (error.response && error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message;
        } else if (error.message) {
            errorMessage = error.message;
        }

        Swal.fire({
            icon: 'error',
            title: '發送請求失敗',
            text: errorMessage,
            confirmButtonColor: '#1e3c72'
        });
    } finally {
        isSubmitting.value = false;
    }
};

</script>

<template>
    <div class="admin-login-page bg-admin-content min-vh-100 d-flex align-items-center justify-content-center p-3">
        <BaseCard class="admin-card" maxWidth="450px" :shadow="false" :border="false">
            <template #header>
                <div class="text-center mb-4 mt-2">
                    <h2 class="text-admin-primary fw-bold h4">管理員密碼重設</h2>
                </div>
            </template>

            <div class="text-center px-4 mb-4">
                <p class="text-muted small fw-medium">
                    輸入您管理員帳戶的電子郵件地址，<br>我們將發送密碼重設連結給您。
                </p>
            </div>

            <form @submit.prevent="handleSendResetLink">
                <div class="mb-4">
                    <label for="email" class="form-label text-dark fw-medium small">電子郵件</label>
                    <input 
                        type="email" 
                        id="email" 
                        v-model="email" 
                        class="form-control admin-form-control" 
                        placeholder="請輸入您的電子郵件地址"
                        required
                    >
                </div>

                <div class="d-grid gap-2">
                    <BaseButton 
                        color="primary" 
                        class="btn-admin-primary w-100 py-2 fw-bold" 
                        :label="isSubmitting ? '發送中...' : '發送密碼重設信件'" 
                        :disabled="isSubmitting"
                        @click="handleSendResetLink"
                    />
                </div>
                
                <div class="text-center mt-3 pt-2">
                    <button type="button" @click="router.push('/admin/login')" class="btn btn-link text-muted small text-decoration-none fw-medium">
                        <i class="bi bi-arrow-left me-1"></i> 取消並返回登入
                    </button>
                </div>
            </form>
        </BaseCard>
    </div>
</template>

<style lang="scss" scoped>
@import '@/assets/styles/custom.scss';

.admin-login-page {
  background-color: #f5f6fa;
}

.admin-form-control {
  &:focus {
    border-color: #1e3c72;
    box-shadow: 0 0 0 0.25rem rgba(30, 60, 114, 0.15);
  }
}
</style>
