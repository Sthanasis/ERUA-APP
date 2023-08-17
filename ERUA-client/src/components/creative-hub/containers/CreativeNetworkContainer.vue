<script setup lang="ts">
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { useSearch } from '~/composables/useSearch';

const { search } = useSearch();
</script>
<template>
  <PageActionsWrapper :search="search" />
</template>
