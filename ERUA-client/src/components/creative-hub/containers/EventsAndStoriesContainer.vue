<script setup lang="ts">
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { PageAction } from '~/types/utilities';
import { ref, computed } from 'vue';

const actions: PageAction[] = [
  {
    name: '+ Event',
    callback: () => console.log('yo'),
    color: 'primary',
  },
];

const todos = ref([
  {
    description: '1st day of 2nd Digital Escape Meeting',
    isComplete: false,
    dates: new Date(2023, 4, 24),
    color: 'red',
  },
  {
    description: '2nd day of 2nd Digital Escape Meeting',
    isComplete: false,
    dates: new Date(2023, 4, 25),
    color: 'red',
  },
  {
    description: '3rd day of 2nd Digital Escape Meeting',
    isComplete: false,
    dates: new Date(2023, 4, 26),
    color: 'red',
  },
]);

const attributes = computed(() => [
  // Attributes for todos
  ...todos.value.map((todo) => ({
    dates: todo.dates,
    dot: {
      color: todo.color,
      class: todo.isComplete ? 'opacity-75' : '',
    },
    popover: {
      label: todo.description,
    },
  })),
]);
</script>
<template>
  <PageActionsWrapper :actions="actions" />
  <div :style="{ display: 'flex', width: '100%', justifyContent: 'center' }">
    <div :style="{ width: '800px' }">
      <VCalendar
        expanded
        :attributes="attributes"
      />
    </div>
  </div>
</template>
