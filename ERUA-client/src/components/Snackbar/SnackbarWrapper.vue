<script setup lang="ts">
import { IPiniaSnackbar } from '~/types/props';
import Snackbar from './Snackbar.vue';

defineProps<{ snackbars: IPiniaSnackbar[] }>();
defineEmits(['remove-snackbar']);
</script>
<template>
  <Teleport to="body">
    <div
      v-if="snackbars.length > 0"
      class="container"
    >
      <Snackbar
        v-for="snackbar in snackbars"
        :key="snackbar.id"
        :snackbar="snackbar"
        @remove="$emit('remove-snackbar', snackbar.id)"
      />
    </div>
  </Teleport>
</template>

<style scoped>
.container {
  position: absolute;
  display: flex;
  flex-flow: column;
  bottom: 2%;
  right: 2%;
}
</style>
