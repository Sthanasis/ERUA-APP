<script setup lang="ts">
import Toolbar from '~/components/Toolbar';
import NavDrawer from '~/components/NavDrawer';
import LayoutWrapper from './LayoutWrapper.vue';
import { ref } from 'vue';
import { useTheme } from 'vuetify';
import { VThemeProvider } from 'vuetify/components';

import { useLayoutStore } from '~/pinia/modules/layout';
import { useSnackbarStore } from '~/pinia/modules/snackbar';
import { useRouter } from 'vue-router';
import { HOME } from '~/constants/routes';

const { name } = useTheme();
const openDrawer = ref(false);
const router = useRouter();
const store = useLayoutStore();
const snackbarStore = useSnackbarStore();

function goToHome() {
  router.push({ name: HOME });
}
const handleToggleDrawer = () => (openDrawer.value = !openDrawer.value);
</script>

<template>
  <VThemeProvider :theme="name">
    <LayoutWrapper :snackbars="snackbarStore.snackbars">
      <template
        v-if="store.isRegisterOrLogin"
        #goHome
      >
        <VBtn
          icon="mdi-home"
          position="fixed"
          color="primary"
          @click="goToHome"
        />
      </template>
      <template
        v-else
        #navbar
      >
        <Toolbar @toggle-menu="handleToggleDrawer" />
        <NavDrawer :open="openDrawer" />
      </template>
      <slot />
    </LayoutWrapper>
  </VThemeProvider>
</template>
