<script setup lang="ts">
import { CSSProperties } from 'vue';
import { computed } from 'vue';

const props = defineProps<{ image: string }>();
const style = computed<CSSProperties>(() => ({
  width: '100vw',
  height: '100vh',
  position: 'fixed',
  zIndex: -1,
  left: 0,
  backgroundImage: `url(${props.image})`,
  backgroundSize: 'cover',
}));
</script>
<template>
  <div
    :src="image"
    :style="style"
  />
</template>
