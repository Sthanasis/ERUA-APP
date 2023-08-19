<script setup lang="ts">
import { defineAsyncComponent, onUpdated, computed } from 'vue';
import { VTabs, VTab, VWindow, VWindowItem } from 'vuetify/components';
import { ScienceShopItem, useScienceShop } from '~/pinia/modules/scienceShop';
import { storeToRefs } from 'pinia';

const ProblemsPage = defineAsyncComponent(
  () => import('~/components/science-shop/containers/ProblemPageContainer.vue')
);

const SolutionsPage = defineAsyncComponent(
  () => import('~/components/science-shop/containers/SolutionPageContainer.vue')
);

const store = useScienceShop();
const { sienceShopItem } = storeToRefs(store);

const isSolution = computed(
  () => sienceShopItem.value === ScienceShopItem.SOLUTION
);

onUpdated(() => {
  if (sienceShopItem.value === ScienceShopItem.SOLUTION) {
    store.setSienceShopItem(ScienceShopItem.SOLUTION);
  } else {
    store.setSienceShopItem(ScienceShopItem.PROBLEM);
  }
});
</script>
<template>
  <VTabs v-model="sienceShopItem">
    <VTab
      :value="ScienceShopItem.SOLUTION"
      color="primary"
    >
      Problem Solver
    </VTab>
    <VTab
      :value="ScienceShopItem.PROBLEM"
      color="primary"
    >
      Problem Owner
    </VTab>
  </VTabs>
  <VWindow v-model="sienceShopItem">
    <VWindowItem :value="ScienceShopItem.SOLUTION">
      <SolutionsPage v-if="isSolution" />
    </VWindowItem>
    <VWindowItem :value="ScienceShopItem.PROBLEM">
      <ProblemsPage v-if="!isSolution" />
    </VWindowItem>
  </VWindow>
</template>
