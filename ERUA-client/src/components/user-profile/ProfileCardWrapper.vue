<script setup lang="ts">
import CardWrapper from '../wrappers/CardWrapper.vue';
defineProps<{ title: string }>();
</script>
<template>
  <CardWrapper
    :title="title"
    class="mt-10"
  >
    <slot />
  </CardWrapper>
</template>
