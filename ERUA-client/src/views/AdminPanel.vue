<script setup lang="ts">
import { VTabs, VTab, VWindow, VWindowItem } from 'vuetify/components';
import StakeholderTableContainer from '~/components/AdminUserTable/StakeholderTableContainer.vue';
</script>
<template>
  <VTabs>
    <VTab
      value="stakeholders"
      color="primary"
    >
      Stakeholders
    </VTab>
  </VTabs>
  <VWindow>
    <VWindowItem value="stakeholders">
      <StakeholderTableContainer />
    </VWindowItem>
  </VWindow>
  <AdminUserTableContainer />
</template>
