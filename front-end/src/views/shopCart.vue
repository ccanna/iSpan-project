<script setup>
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router';

const router = useRouter();
const cartStore = useCartStore()

const goToCheckOut = () =>{
    //點擊進入結帳
    router.push({name:'checkOut'});
};

const backToShop = () =>{
    //點擊返回選購商品
    router.push({name:'ShopStore'});
};
</script>

<template>

    <table class="table" v-if="cartStore.items.length" style="vertical-align: middle" >
        <thead>
        <tr>
            <th style="font-size: 20px; padding-bottom: 30px; padding-top: 30px">商品</th>
            <th style="font-size: 20px; padding-bottom: 30px; padding-top: 30px">價格</th>
            <th style="font-size: 20px; padding-bottom: 30px; padding-top: 30px">數量</th>
            <th></th>
            <th style="font-size: 20px; padding-bottom: 30px; padding-top: 30px">小計</th>
        </tr>
        </thead>

        <tbody >
        <tr  v-for="item in cartStore.items"  :key="item.id">
            <img :src="item.image" width=200px height="200px" class="card-img-left" :alt="item.productName" />
            <td >{{ item.name }}</td>
            <td>NT$ {{ item.price }}</td>
            
                <td>
                    <div class="stepper-container" >
                        
                        <button type="button" class="square-btn left-round" @click="cartStore.decrease(item.id)">
                            <i class="bi bi-dash"></i>
                        </button>
                        <div class="square-num" >
                            {{ item.quantity }}
                        </div>
                        <button type="button" class="square-btn right-round" @click="cartStore.increase(item.id)">
                            <i class="bi bi-plus-lg"></i>
                        </button>
                    </div>
                </td>

            


            <td>NT$ {{ item.price * item.quantity }}</td>
            <td>
            <button class="btn-outline-gdg"
                @click="cartStore.remove(item.id)">
                刪除
            </button>
            </td>
        </tr>
        </tbody>

    </table>

    <div style="text-align: center;" v-if="!cartStore.items.length">
        <h1 style="padding-top: 30px; padding-bottom: 30px;">🛒購物車是空的 </h1> 
    </div>


    <div class="text-right">
        <h5>共:  {{ cartStore.totalQuantity }} 項商品</h5>

        <h3 >總金額：NT$ {{ cartStore.totalPrice }}</h3>
    </div>

    

    <hr/>
    <div class="action-buttons-row">
        <div class="button-group-right" v-if="cartStore.items.length">
            <button type="button"   class="btnBackToShop "  data-bs-toggle="modal" data-bs-target="#exampleModal"  @click="backToShop">
                繼續選購
            </button>
        </div>
        <div class="button-group-right" v-if="cartStore.items.length">
            <button type="button"   class="btnBuy "  data-bs-toggle="modal" data-bs-target="#exampleModal"  @click="goToCheckOut">
                結帳
            </button>
        </div>
    </div>



    
</template>

<style>



</style>