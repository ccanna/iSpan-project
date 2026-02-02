import { createRouter, createWebHistory } from 'vue-router';



// 定義router
const routes = [
  {
    path: '/login',
    component: () => import('@/views/LoginView.vue'),
    name: 'Login'
  },

  {
    path: '/shopStore',
    component: () => import('@/views/shopStore.vue'),
    name: 'ShopStore'
  },
  {
    path: '/cart',
    component: () => import('@/views/shopCart.vue'),
    name: 'ShopCart',
    name: 'Login',
    meta: { layout: 'blank' }
  },
  {
    path: '/register',
    component: () => import('@/views/RegisterView.vue'),
    name: 'Register',
    meta: { layout: 'blank' }
  },
  {
    path: '/productsDetail/:id',
    component: () => import('@/views/productsDetail.vue'),
    name: 'productsDetail',
  }
  ,
  {
    path: '/checkOut',
    component: () => import('@/views/checkOut.vue'),
    name: 'checkOut',
  }

];

// 建立router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

export default router;
