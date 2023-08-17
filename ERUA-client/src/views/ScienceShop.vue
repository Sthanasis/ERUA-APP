<script setup lang="ts">
import { defineAsyncComponent } from 'vue';
import { onUpdated } from 'vue';
import { ref } from 'vue';
import { VTabs, VTab, VWindow, VWindowItem } from 'vuetify/components';
import { ScienceShopItem, useScienceShop } from '~/pinia/modules/scienceShop';

const ProblemsPage = defineAsyncComponent(
  () => import('~/components/science-shop/containers/ProblemPageContainer.vue')
);

const SolutionsPage = defineAsyncComponent(
  () => import('~/components/science-shop/containers/SolutionPageContainer.vue')
);

const { setSienceShopItem } = useScienceShop();

const tab = ref('solutions');

onUpdated(() => {
  if (tab.value === 'solutions') {
    setSienceShopItem(ScienceShopItem.SOLUTION);
  }
  if (tab.value === 'problems') {
    setSienceShopItem(ScienceShopItem.PROBLEM);
  }
});
</script>
<template>
  <VTabs v-model="tab">
    <VTab
      value="solutions"
      color="primary"
    >
      Problem Solver
    </VTab>
    <VTab
      value="problems"
      color="primary"
    >
      Problem Owner
    </VTab>
  </VTabs>
  <VWindow v-model="tab">
    <VWindowItem value="solutions"> <SolutionsPage /> </VWindowItem>
    <VWindowItem value="problems"> <ProblemsPage /> </VWindowItem>
  </VWindow>
</template>
