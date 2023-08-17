<script setup lang="ts">
import { VAvatar } from 'vuetify/components';
interface IProps {
  title?: string | null;
  size?: number | string;
}
defineProps<IProps>();
</script>
<template>
  <div>
    <VAvatar
      color="brown"
      :size="size"
    />
    <p
      v-if="title"
      class="text-caption mt-1"
    >
      {{ title }}
    </p>
  </div>
</template>

<style scoped>
.hero {
  display: flex;
  flex-flow: column;
  justify-content: center;
  align-items: center;
}
</style>
