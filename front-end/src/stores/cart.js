import { defineStore } from 'pinia'
import Swal from 'sweetalert2'

export const useCartStore = defineStore('cart', {
    state: () => ({
        // 從 localStorage 讀取，若無則為空陣列
        items: JSON.parse(localStorage.getItem('cart')) || []
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

        addToCart(product) {
            const existing = this.items.find(i => i.id === product.id)
            if (existing) {
                if (!product.stock || existing.quantity < product.stock) {
                    existing.quantity += (product.quantity || 1)
                } else {
                    Swal.fire('提示', '已達庫存上限', 'warning')
                    return
                }
            } else {
                // 修正：補上缺失的 t (this.items)
                this.items.push({ ...product, quantity: product.quantity || 1 })
            }
            this.saveToStorage()
        },

        increase(id) {
            const item = this.items.find(i => i.id === id)

            // 注意：確保傳入的 item 物件裡有 stock 屬性
            if (!item.stock || item.quantity < item.stock) {
                item.quantity++
                this.saveToStorage()
            } else {
                Swal.fire('提示', '已達庫存上限囉！', 'warning')
            }
        },


        decrease(id) {
            const item = this.items.find(i => i.id === id)
            if (item) {
                if (item.quantity > 1) {
                    item.quantity--
                } else {
                    this.remove(id)
                }
                this.saveToStorage()
            }
        },

        remove(id) {
            this.items = this.items.filter(i => i.id !== id)
            this.saveToStorage()
        },

        saveToStorage() {
            // 修正：補上缺失的 t (setItem)
            localStorage.setItem('cart', JSON.stringify(this.items))
        }
    } // actions 結束
})