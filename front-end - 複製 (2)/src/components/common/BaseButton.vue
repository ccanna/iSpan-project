<script setup>
import { computed } from 'vue';

const props = defineProps({
  label: {
    type: String,
    default: 'Button'
  },
  color: {
    type: String,
    default: 'gdg', // gdg, outline-gdg, primary, secondary, etc.
  },
  size: {
    type: String,
    default: 'md', // sm, md, lg
  },
  type: {
    type: String,
    default: 'button'
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

defineEmits(['click']);

const buttonClasses = computed(() => {
  const classes = ['btn'];
  
  // Color mapping
  if (props.color === 'gdg') {
    classes.push('btn-gdg');
  } else if (props.color === 'outline-gdg') {
    classes.push('btn-outline-gdg');
  } else {
    classes.push(`btn-${props.color}`);
  }

  // Size mapping
  if (props.size === 'sm') {
    classes.push('btn-sm');
  } else if (props.size === 'lg') {
    classes.push('btn-lg');
  }
  
  return classes.join(' ');
});
</script>

<template>
  <button 
    :type="type" 
    :class="buttonClasses" 
    :disabled="disabled"
    @click="$emit('click')"
  >
    <slot>{{ label }}</slot>
  </button>
</template>

<style scoped>
/* Scoped styles if needed, but mostly relying on custom.scss */
.btn {
  font-weight: 500;
  letter-spacing: 1px;
}
</style>
