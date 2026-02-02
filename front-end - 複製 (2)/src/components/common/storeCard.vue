<script setup>
import {useCartStore} from '@/stores/cart'
import Swal from 'sweetalert2';
import productsData from '@/data/productsData.json'
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const cartStore = useCartStore()
const products = ref(productsData);

const goToDetail = (id) =>{
    //點擊進入商品詳情
    router.push({name:'productsDetail', params:{id}});
};

const addToCart = (item) => {
    cartStore.addToCart({
        id: item.id,
        name: item.productName,
        price: item.price,
        image: item.image,
        description: item.description
    })
    
    Swal.fire({
        icon: 'success',
        title: '成功加入購物車',
        text: `已選購  ${item.productName}`,
        timer: 1500,
        showConfirmButton: false
    });


}


const productsList = computed(()=>{
    const newList =products.value.map(x => {
        let newData = {
            id: x.id,
            productName: x.productName,
            price: x.price,
            description: x.description,
            image: x.image
        }
        return newData
    })
    return newList
})



</script>


    <template >
        

<div class="shop-container">
    <div class="shop-grid">

    <!-- 卡片  -->

    <div class="card" v-for="item in productsList" :key="item.id"  @click="goToDetail(item.id)" >
        <img :src="item.image" width=450px height="450px" class="card-img-top" :alt="item.productName" />
        <div class="card-body">
            <h5 class="card-title">{{item.productName}}</h5>
            <!-- <p class="card-text text-muted" style="font-size:0.9rem">{{item.description}}</p> -->
            <div class="price text-success">NT$ {{item.price}}</div>
        </div>
        <div class="card-footer">
            <button class="btnBuy "   @click="addToCart(item)" @click.stop="加入購物車">加入購物車</button>

        </div>
    </div>
    
    
    </div>
</div>

        
    

    </template>
<style scoped>

</style>