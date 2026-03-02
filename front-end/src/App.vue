<script setup>
// import { computed } from 'vue';
// import { useRoute } from 'vue-router';
// import DefaultLayout from '@/layouts/default.vue';
// import BlankLayout from '@/layouts/blank.vue';
import Navbar from '@/layouts/navbar.vue';
import Footer from '@/layouts/footer.vue';
import { onMounted } from 'vue';
import { useProductsDepot } from '@/stores/productsDepot';

const depot = useProductsDepot();

onMounted(async () => {
  await depot.fetchProducts();
});
// const route = useRoute();

// const layout = computed(() => {
//   if (route.meta.layout === 'blank') return BlankLayout;
//   return DefaultLayout;
// });
</script>

<template>
  <Navbar />
  <!-- <component :is="layout">
    <RouterView />
  </component> -->
  <RouterView />
  <Footer />
</template>

<style scoped></style>
