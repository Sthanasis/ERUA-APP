<script setup lang="ts">
import { useUserStore } from '~/pinia/modules/user';
import ToolbarWrapper from './ToolbarWrapper.vue';
import { useRouter } from 'vue-router';
import { useLayoutStore } from '~/pinia/modules/layout';
import { LOGIN, PROFILE } from '~/constants/routes';

defineEmits(['toggleMenu']);

const userStore = useUserStore();
const layoutStore = useLayoutStore();
const router = useRouter();

function goToUserProfile() {
  router.push({ name: PROFILE, params: { name: userStore.name } });
}

const handleLogout = () => {
  userStore.logout();
  router.push({ name: LOGIN });
};

const dropdownItems = [
  { title: 'Sign out', handler: handleLogout, icon: 'mdi-logout' },
];
</script>

<template>
  <ToolbarWrapper
    :items="dropdownItems"
    :is-authenticated="userStore.isAuthenticated"
    :is-admin="!!userStore.isAdmin"
    :user-email="userStore.email"
    @toggle-menu="$emit('toggleMenu')"
    @go-to-profile="goToUserProfile"
  >
    <template #pageTitle> {{ layoutStore.getCurrentViewName }} </template>
  </ToolbarWrapper>
</template>
