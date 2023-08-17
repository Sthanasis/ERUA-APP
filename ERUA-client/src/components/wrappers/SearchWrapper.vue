<script setup lang="ts">
import { VTextField } from 'vuetify/components';
interface ISearchWrapperProps {
  text?: string;
  label?: string;
  loading?: boolean;
}
defineProps<ISearchWrapperProps>();
defineEmits(['search']);
</script>
<template>
  <div class="search">
    <VTextField
      :model-value="text"
      :label="label ?? 'Search'"
      density="compact"
      prepend-inner-icon="mdi-magnify"
      variant="solo"
      single-line
      hide-details
      @update:model-value="(val:string) => $emit('search',val)"
    />
  </div>
</template>
<style>
.search {
  width: 250px;
  margin-left: 5px;
  margin-right: 5px;
}
</style>
