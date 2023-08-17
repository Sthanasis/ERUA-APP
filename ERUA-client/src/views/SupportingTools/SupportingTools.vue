<script setup lang="ts">
import { ref } from 'vue';
import { VTabs, VTab, VWindow, VWindowItem } from 'vuetify/components';
import Placeholder from '../Placeholder.vue';
import HackathonsContainer from '~/components/supporting-tools/containers/HackathonsContainer.vue';

const tabs = ref([
  'hackathons',
  'ideathons',
  'funding opportunities',
  're-erua',
  'open science',
  'knowledge base aeiforia',
  'forum',
]);

const tab = ref(tabs.value[0]);
</script>
<template>
  <VTabs v-model="tab">
    <VTab
      v-for="item in tabs"
      :key="item"
      :value="item"
      color="primary"
    >
      {{ item }}
    </VTab>
  </VTabs>
  <VWindow v-model="tab">
    <VWindowItem value="hackathons">
      <HackathonsContainer />
    </VWindowItem>
    <VWindowItem value="ideathons">
      <Placeholder />
    </VWindowItem>
    <VWindowItem value="funding opportunities">
      <Placeholder />
    </VWindowItem>
    <VWindowItem value="re-erua">
      <Placeholder />
    </VWindowItem>
    <VWindowItem value="open science">
      <Placeholder />
    </VWindowItem>
    <VWindowItem value="knowledge base aeiforia">
      <Placeholder />
    </VWindowItem>
    <VWindowItem value="forum">
      <Placeholder />
    </VWindowItem>
  </VWindow>
</template>
