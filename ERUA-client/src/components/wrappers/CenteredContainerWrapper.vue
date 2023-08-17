<script setup lang="ts">
import { VContainer, VRow } from 'vuetify/components';
</script>
<template>
  <VContainer class="full-height">
    <VRow
      class="full-height"
      justify="space-around"
      :align="'center'"
    >
      <slot />
    </VRow>
  </VContainer>
</template>
<style scoped>
.full-height {
  height: 100%;
}
</style>
