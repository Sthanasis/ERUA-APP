<script setup lang="ts">
import {
  VAppBar,
  VSpacer,
  VAppBarNavIcon,
  VAppBarTitle,
  VList,
  VMenu,
  VAvatar,
  VIcon,
  VListItem,
} from 'vuetify/components';
import { LOGIN, PANEL } from '~/constants/routes';
import HeroWrapper from '../Hero/HeroWrapper.vue';

type TListItem = {
  title: string;
  handler: () => void;
  icon?: string;
};
interface IProps {
  items: TListItem[];
  isAuthenticated: boolean;
  userEmail: string | null;
  isAdmin: boolean;
}
defineProps<IProps>();
defineEmits(['toggleMenu', 'goToProfile']);
</script>

<template>
  <VAppBar
    :elevation="2"
    color="secondary"
  >
    <VAppBarNavIcon @click="$emit('toggleMenu')" />

    <VAppBarTitle>
      <slot name="pageTitle" />
    </VAppBarTitle>
    <VSpacer />
    <VBtn
      variant="text"
      icon="mdi-magnify"
    />
    <VBtn
      v-if="isAdmin"
      icon
      :to="{ name: PANEL }"
    >
      <VAvatar>
        <VIcon icon="mdi-controller" />
      </VAvatar>
    </VBtn>
    <VMenu>
      <template #activator="{ props }">
        <VBtn
          v-if="isAuthenticated"
          icon
          v-bind="props"
        >
          <VAvatar>
            <VIcon icon="mdi-account-circle" />
          </VAvatar>
        </VBtn>
        <VBtn
          v-else
          icon
          :to="{ name: LOGIN }"
        >
          <VAvatar>
            <VIcon icon="mdi-login" />
          </VAvatar>
        </VBtn>
      </template>

      <VList :items="items">
        <VListItem
          class="mx-auto text-center"
          @click="$emit('goToProfile')"
        >
          <HeroWrapper :title="userEmail" />
        </VListItem>
        <VListItem
          v-for="(item, i) in items"
          :key="i"
          :value="item.title"
          :append-icon="item.icon"
          @click="item.handler"
        >
          {{ item.title }}
        </VListItem>
      </VList>
    </VMenu>
  </VAppBar>
</template>
