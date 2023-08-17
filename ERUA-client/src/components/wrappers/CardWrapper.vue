<script setup lang="ts">
import { VCard, VCardTitle, VCardSubtitle } from 'vuetify/components';
interface IProps {
  title?: string;
  subtitle?: string;
  noPadding?: boolean;
  overflow?: boolean;
  variant?: 'outlined' | 'tonal' | 'flat' | 'text' | 'elevated' | 'plain';
}

const props = defineProps<IProps>();
const ELEVATION = props.variant !== 'elevated' ? 0 : 3;
</script>
<template>
  <VCard
    :class="{ 'py-1 px-4': !noPadding }"
    :elevation="ELEVATION"
    :variant="variant ?? 'elevated'"
  >
    <VCardTitle
      v-if="title"
      class="text-left px-0"
    >
      {{ title }}
    </VCardTitle>
    <VCardSubtitle
      v-if="subtitle"
      class="text-left mb-5 px-0"
    >
      {{ subtitle }}
    </VCardSubtitle>
    <div
      :class="{
        content: overflow,
      }"
    >
      <slot />
    </div>
    <slot name="actions" />
  </VCard>
</template>
<style scoped>
.content {
  overflow: auto;
  max-height: 500px;
}
</style>
