<script setup lang="ts">
import { VDataTable } from 'vuetify/labs/VDataTable';
import { VProgressCircular, VIcon } from 'vuetify/components';
import { TColumn, TRow } from '~/types/table';
import CenteredContainerWrapper from '../wrappers/CenteredContainerWrapper.vue';

interface IProps {
  rows: TRow[];
  columns: TColumn[];
  loading?: boolean;
}

defineProps<IProps>();
defineEmits(['activate', 'deactivate']);
</script>
<template>
  <CenteredContainerWrapper v-if="loading">
    <VProgressCircular
      indeterminate
      color="primary"
    />
  </CenteredContainerWrapper>

  <VDataTable
    v-else
    class="elevation-1"
    :headers="columns"
    :items="rows"
  >
    <template #item.actions="{ item }">
      <VBtn
        v-if="item.raw.authorized"
        icon="mdi-account-off"
        variant="text"
        @click="$emit('deactivate', item.value)"
      />
      <VBtn
        v-else
        icon="mdi-account-check"
        variant="text"
        @click="$emit('activate', item.value)"
      />
    </template>

    <template #item.authorized="{ item }">
      <VIcon v-if="item.raw.authorized"> mdi-check </VIcon>
      <VIcon v-else> mdi-close </VIcon>
    </template>
  </VDataTable>
</template>
