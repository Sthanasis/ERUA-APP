<script setup lang="ts">
import type { IEruaMemberSolution } from '~/types/responseData';
import ProfileCardWrapper from './ProfileCardWrapper.vue';
import { VList, VListItem, VListSubheader } from 'vuetify/components';
import { computed } from 'vue';

const props = defineProps<{ solutions: IEruaMemberSolution[] }>();
const length = computed(() => props.solutions.length);
</script>
<template>
  <ProfileCardWrapper title="Science Shop Solutions">
    <template v-if="length > 0">
      <VList>
        <VListItem
          v-for="(solution, index) in solutions"
          :key="solution.name"
          :value="solution.name"
          :active="index % 2 === 0"
          append-icon="mdi-lightbulb-on"
          color="grey"
        >
          {{ solution.name }}
        </VListItem>
      </VList>
      <VListSubheader>Total: {{ length }}</VListSubheader>
    </template>
    <template v-else>
      <div>You have no solutions</div>
    </template>
  </ProfileCardWrapper>
</template>
