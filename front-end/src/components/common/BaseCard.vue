<script setup>
import { computed } from 'vue';

const props = defineProps({
  maxWidth: {
    type: String,
    default: '100%'
  },
  shadow: {
    type: Boolean,
    default: false
  },
  border: {
    type: Boolean,
    default: true
  },
  hoverEffect: {
    type: Boolean,
    default: false
  }
});

const cardStyles = computed(() => ({
  maxWidth: props.maxWidth,
}));

const cardClasses = computed(() => ({
  'card': true,
  'card-gdg': props.border,
  'shadow': props.shadow,
  'card-hover': props.hoverEffect
}));
</script>

<template>
  <div :class="cardClasses" :style="cardStyles">
    <div v-if="$slots.header" class="card-header bg-transparent border-0 pt-4 px-4">
      <slot name="header"></slot>
    </div>
    <div class="card-body p-4">
      <slot></slot>
    </div>
    <div v-if="$slots.footer" class="card-footer bg-transparent border-0 pb-4 px-4">
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<style scoped>
.card {
  margin: 0 auto;
}
</style>
