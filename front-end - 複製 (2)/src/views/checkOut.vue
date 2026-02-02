<script setup>
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import Swal from 'sweetalert2'

const cartStore = useCartStore()
const router = useRouter()



// 收件人資料表單
const orderForm = ref({
    name: '',
    phone: '',
    address: '',
    email: '',
    paymentMethod: 'credit_card', // 預設信用卡
    note: ''
})

const handleCheckout = () => {
    // 簡單表單驗證
    if (!orderForm.value.name || !orderForm.value.phone || !orderForm.value.address) {
        Swal.fire('錯誤', '請填寫完整的收件人資訊', 'error')
        return
    }

    if (cartStore.items.length === 0) {
        Swal.fire('錯誤', '購物車是空的', 'error')
        return
    }

    // 這裡模擬送出訂單
    Swal.fire({
        title: '確認送出訂單？',
        text: `總金額為 NT$ ${cartStore.totalPrice}`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '確定下單',
        cancelButtonText: '再檢查一下'
    }).then((result) => {
        if (result.isConfirmed ) {
            // 💡 提示：之後串綠界時，這裡會呼叫後端 API 取得綠界的導向表單
            Swal.fire('成功', '訂單已建立！', 'success'),
            router.push('/shopStore')
            // 測試用：清空購物車並導回首頁
            // cartStore.items = []
            // cartStore.saveToStorage()
            // router.push('/shopStore')
        }
        
    })
}
</script>

<template>
    <div class="checkout-container">
        <h2 class="section-title">結帳確認</h2>

        <div class="checkout-layout">
            <div class="form-section">
                <div class="card">
                    <div class="card-header">收件人資訊</div>
                    <div class="card-body">
                        <div class="form-group">
                            <label>姓名 *</label>
                            <input v-model="orderForm.name" type="text" class="form-control"  >
                            <placeholder style="color: #cdbabab4;">請輸入收件人姓名</placeholder>
                        </div>
                        <br>
                        <div class="form-group">
                            <label>手機號碼 *</label>
                            <input v-model="orderForm.phone" type="tel" class="form-control" >
                            <placeholder style="color: #cdbabab4;">ex:0912345678</placeholder>

                        </div>
                        <br>
                        <div class="form-group">
                            <label>電子信箱</label>
                            <input v-model="orderForm.email" type="email" class="form-control" >
                            <placeholder style="color: #cdbabab4;">example@mail.com</placeholder>

                        </div>
                        <br>
                        <div class="form-group">
                            <label>收件地址 *</label>
                            <input v-model="orderForm.address" type="text" class="form-control" >
                            <placeholder style="color: #cdbabab4;">請輸入詳細地址</placeholder>
                            
                        </div>
                        <br>
                        <div class="form-group">
                            <label>備註</label>
                            <textarea v-model="orderForm.note" class="form-control" rows="2"></textarea>
                        </div>
                    </div>
                </div>

                <div class="card mt-4">
                    <div class="card-header">付款方式</div>
                    <div class="card-body">
                        <div class="payment-options">
                            <label class="payment-radio">
                                <input type="radio" v-model="orderForm.paymentMethod" value="credit_card">
                                <span class="radio-label">
                                    <i class="bi bi-credit-card"></i> 信用卡 (支援綠界支付)
                                </span>
                            </label>
                            <label class="payment-radio">
                                <input type="radio" v-model="orderForm.paymentMethod" value="cod">
                                <span class="radio-label">
                                    <i class="bi bi-truck"></i> 貨到付款
                                </span>
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="summary-section">
                <div class="card">
                    <div class="card-header">訂單明細</div>
                    <div class="card-body p-0">
                        <ul class="order-list">
                            <li v-for="item in cartStore.items" :key="item.id" class="order-item">
                                <img :src="item.image" class="item-thumb">
                                <div class="item-info">
                                    <p class="item-name">{{ item.name }}</p>
                                    <p class="item-price">NT$ {{ item.price }} x {{ item.quantity }}</p>
                                </div>
                                <div class="item-subtotal">
                                    NT$ {{ item.price * item.quantity }}
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="card-footer">
                        <div class="total-row">
                            <span>商品小計</span>
                            <span>NT$ {{ cartStore.totalPrice }}</span>
                        </div>
                        <div class="total-row">
                            <span>運費</span>
                            <span class="text-success">免運</span>
                        </div>
                        <hr>
                        <div class="total-row final-price">
                            <span>總計</span>
                            <span>NT$ {{ cartStore.totalPrice }}</span>
                        </div>
                        <button @click="handleCheckout" class="btn-submit-order">確認送出訂單</button>
                        <button @click="router.push('/cart')" class="btn-back">返回購物車</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.checkout-container {
    max-width: 1000px; /* 因為是直排，寬度縮小一點比較好看 */
    margin: 40px auto;
    padding: 0 20px;
}

/* 關鍵修改：改為垂直排列 */
.checkout-layout {
    display: flex;
    flex-direction: column; 
    gap: 30px;
    align-items: center;
}

.form-section, .summary-section {
    width: 100%; /* 寬度佔滿 */
}

/* 卡片樣式 */
.card {
    border: 3px solid #eee;
    width: 1000px;
    height: auto;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.card-header {
    background: #f8f9fa;
    padding: 15px 20px;
    font-weight: bold;
    border-bottom: 1px solid #eee;
}

.card-body { padding: 20px; }

/* 表單樣式 */
.form-group {  margin-bottom: 15px; text-align: left;}
.form-group label { display: block; margin-bottom: 5px; font-size: 0.9rem; color: #666; }
.form-control {
    width: 100%;
    padding: 10px;
    border: 1px solid #cdbabab4;
    border-radius: 6px;
}

/* 付款方式 */
.payment-radio {
    display: flex;
    align-items: center;
    padding: 12px;
    border: 1px solid #eee;
    border-radius: 8px;
    margin-bottom: 10px;
    cursor: pointer;
    transition: 0.3s;
}

.payment-radio:hover { background: #f0fdf4; }
.payment-radio input { margin-right: 15px; }
.radio-label i { margin-right: 8px; color: #198754; }

/* 訂單明細清單 */
.order-list { list-style: none; padding: 0; margin: 0; }
.order-item {
    display: flex;
    align-items: center;
    padding: 15px 20px;
    border-bottom: 1px solid #f9f9f9;
}

.item-thumb { width: 50px; height: 50px; border-radius: 6px; object-fit: cover; margin-right: 15px; }
.item-info { flex-grow: 1; }
.item-name { font-size: 0.85rem; margin: 0; font-weight: 500; }
.item-price { font-size: 0.8rem; color: #888; margin: 0; }
.item-subtotal { font-weight: bold; font-size: 0.9rem; }

/* 總計區塊 */
.total-row { display: flex; justify-content: space-between; margin-bottom: 10px; }
.final-price { font-size: 1.2rem; font-weight: bold; color: #198754; }

.btn-submit-order {
    width: 100%;
    background: #198754;
    color: white;
    border: none;
    padding: 15px;
    border-radius: 8px;
    font-size: 1.1rem;
    font-weight: bold;
    cursor: pointer;
    margin-top: 20px;
    transition: 0.3s;
}

.btn-submit-order:hover { background: #146c43; }
.btn-back {
    width: 100%;
    background: transparent;
    border: 1px solid #ccc;
    color: #666;
    padding: 10px;
    border-radius: 8px;
    margin-top: 10px;
    cursor: pointer;
}

@media (max-width: 768px) {
    .checkout-layout { display: flex;
    flex-direction: column; 
    gap: 30px; }
    .summary-section { position: static; width: 100%; }
}
</style>