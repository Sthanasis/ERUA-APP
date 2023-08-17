<script setup lang="ts">
import { IPiniaSnackbar } from '~/types/props';
import { VSheet } from 'vuetify/components';

defineProps<{ snackbar: IPiniaSnackbar }>();
defineEmits(['remove']);
</script>
<template>
  <VSheet
    class="snackbar"
    :class="snackbar.variant"
    @click="$emit('remove')"
  >
    <h5>{{ snackbar.variant }}</h5>
    <div>{{ snackbar.text }}</div>
  </VSheet>
</template>

<style scoped>
.snackbar {
  max-width: 300px;
  color: whitesmoke;
  padding: 10px;
  border-radius: 5px;
  margin-top: 5px;
  cursor: pointer;
  animation-name: fade;
  animation-duration: 0.2s;
  animation-timing-function: ease-in-out;
}
.error {
  background-color: #b00020;
}

.success {
  background-color: #4caf50;
}

.info {
  background-color: #2196f3;
}
@keyframes fade {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}
</style>
