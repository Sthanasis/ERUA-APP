<script setup lang="ts">
import { VNavigationDrawer, VDivider, VList } from 'vuetify/components';
import type { TNavTab } from '~/utils/navigation';

interface IProps {
  showDrawer: boolean;
  navTabs: TNavTab[];
}
defineProps<IProps>();
</script>

<template>
  <VNavigationDrawer
    :elevation="1"
    :rail="showDrawer"
    permanent
  >
    <VDivider />
    <VList
      density="default"
      nav
    >
      <slot
        v-for="tab in navTabs"
        :key="tab.value"
        name="nav-item"
        v-bind="{ tab }"
      />
    </VList>
  </VNavigationDrawer>
</template>
