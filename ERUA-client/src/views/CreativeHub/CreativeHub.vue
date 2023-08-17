<script setup lang="ts">
import { ref } from 'vue';
import { VTabs, VTab, VWindow, VWindowItem } from 'vuetify/components';
import Showroom from './Showroom.vue';
import EventsAndStories from './EventsAndStories.vue';
import CreativeNetwork from './CreativeNetwork.vue';
import IdeasForum from './IdeasForum.vue';
import Laboratory from './Laboratory.vue';

const tabs = ref([
  'showroom',
  'events and stories',
  'creative network',
  'ideas forum',
  'experimentation laboratory',
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
    <VWindowItem value="showroom">
      <Showroom />
    </VWindowItem>
    <VWindowItem value="events and stories">
      <EventsAndStories />
    </VWindowItem>
    <VWindowItem value="creative network">
      <CreativeNetwork />
    </VWindowItem>
    <VWindowItem value="ideas forum">
      <IdeasForum />
    </VWindowItem>
    <VWindowItem value="experimentation laboratory">
      <Laboratory />
    </VWindowItem>
  </VWindow>
</template>
