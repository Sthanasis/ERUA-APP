<script setup lang="ts">
import { VLayout, VMain } from 'vuetify/components';
import { IPiniaSnackbar } from '~/types/props';
import SnackbarContainer from '../Snackbar/SnackbarContainer.vue';

defineProps<{ snackbars: IPiniaSnackbar[] }>();
</script>

<template>
  <VLayout>
    <slot name="navbar" />
    <VMain class="main">
      <div class="home">
        <slot name="goHome" />
      </div>
      <div>
        <slot />
      </div>
      <SnackbarContainer />
    </VMain>
  </VLayout>
</template>

<style scoped>
.main {
  background-color: #f0eeeec7;
  height: 100vh;
  overflow: auto;
}

.main div:not(.home) {
  height: 100%;
  padding: 10px;
}

.main .home {
  position: absolute;
  padding: 10px;
}
</style>
