<script setup lang="ts">
import { useSnackbarStore } from '~/pinia/modules/snackbar';
import SnackbarWrapper from './SnackbarWrapper.vue';
const snackbarStore = useSnackbarStore();
function removeSnackbar(id: number) {
  snackbarStore.remove(id);
}
</script>
<template>
  <SnackbarWrapper
    :snackbars="snackbarStore.snackbars"
    @remove-snackbar="removeSnackbar"
  />
</template>
