<script setup lang="ts">
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { useSearch } from '~/composables/useSearch';
import { PageAction } from '~/types/utilities';

const { search } = useSearch();

const actions: PageAction[] = [
  {
    name: 'New Idea',
    callback: () => console.log('yo'),
    color: 'primary',
  },
];
</script>
<template>
  <PageActionsWrapper
    :search="search"
    :actions="actions"
  />
</template>
