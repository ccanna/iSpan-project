import { defineStore } from 'pinia'
import Swal from 'sweetalert2'
import cartAPI from '../api/cart'

export const useCartStore = defineStore('cart', {
    state: () => ({
        // 從 localStorage 讀取，若無則為空陣列
        items: [] //存放購物車內容(DTO)
    }),

    getters: {
        totalQuantity(state) {
            return state.items.reduce((sum, item) => sum + item.quantity, 0)
        },
        totalPrice(state) {
            return state.items.reduce((sum, item) => sum + item.price * item.quantity, 0)
        }
    },

    actions: {
        // 所有的 function 必須都在 actions 的這個大括號裡面！

        async fetchCart() {
            try {
                const response = await cartAPI.getAll();
                this.items = response || [];
                console.log('購物車同步成功', this.items)
            } catch (error) {
                console.error('獲取購物車失敗', error)
                this.items = [];
            }
        },

        //加入購物車 對應/api/cart/add
        async addToCart(product) {

            try {
                await cartAPI.add({
                    productId: product.id,
                    quantity: product.quantity || 1
                })
                await this.fetchCart(); //加入商品後自動更新購物車
            } catch (error) {
                console.error('加入購物車失敗', error)
                throw error
            }
        },


        async increase(cartDetailsId) {
            const item = this.items.find(i => i.id === cartDetailsId)

            // 注意：確保傳入的 item 物件裡有 stock 屬性
            if (item) {
                await this.addToCart({ id: item.productId, quantity: 1 })
            }
        },


        async decrease(cartDetailsId) {
            const item = this.items.find(i => i.id === cartDetailsId)
            if (item) {
                if (item.quantity > 1) {
                    await this.addToCart({ id: item.productId, quantity: -1 })
                } else {
                    await this.remove(cartDetailsId)
                }
            }
        },



        async remove(cartDetailsId) {

            try {
                await cartAPI.delete(cartDetailsId)

                const index = this.items.findIndex(i => i.id === cartDetailsId)
                if (index !== -1) {
                    this.items.splice(index, 1);
                    console.log(`商品ID ${cartDetailsId}已成功從資料庫與前端刪除`)
                }
            } catch (error) {
                console.error("刪除失敗", error)
                Swal.fire('錯誤', '無法移除商品', 'error')
            }
        },

        async clearCart() {
            try {
                await cartAPI.clear()
                this.items = []
            } catch (error) {
                console.error("清空購物車失敗", error)

            }

        }
    }

})

