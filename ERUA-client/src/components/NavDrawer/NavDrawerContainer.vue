<script setup lang="ts">
import { useUserStore } from '~/pinia/modules/user';
import { VListItem } from 'vuetify/components';
import navTabs from '~/utils/navigation';
import NavDrawerWrapper from './NavDrawerWrapper.vue';
import { storeToRefs } from 'pinia';

interface IProps {
  open: boolean;
}
defineProps<IProps>();
const store = useUserStore();
const { getUserRole } = storeToRefs(store);
</script>

<template>
  <NavDrawerWrapper
    :show-drawer="open"
    :nav-tabs="navTabs"
  >
    <template #nav-item="{ tab }">
      <VListItem
        v-if="!tab.canAccess || tab.canAccess?.includes(getUserRole)"
        :prepend-icon="tab.icon"
        :title="tab.title"
        :value="tab.value"
        :to="tab.to"
      />
    </template>
  </NavDrawerWrapper>
</template>
