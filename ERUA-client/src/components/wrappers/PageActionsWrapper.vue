<script setup lang="ts">
import { toRefs } from 'vue';
import SearchWrapper from '~/components/wrappers/SearchWrapper.vue';
import type { PageAction } from '~/types/utilities';

interface IPageActionsWrapperProps {
  actions?: PageAction[];
  search?: string;
}

const props = defineProps<IPageActionsWrapperProps>();
const emits = defineEmits(['search']);

const { search } = toRefs(props);
function searchFn(val: string) {
  emits('search', val);
}
</script>
<template>
  <slot />
  <div class="header">
    <SearchWrapper
      v-if="typeof search === 'string'"
      v-model="search"
      @search="searchFn"
    />
    <template
      v-for="action in actions"
      :key="action.name"
    >
      <VBtn
        v-if="action.condition === undefined || action.condition"
        class="mx-1"
        :color="action.color"
        @click="action.callback"
      >
        {{ action.name }}
      </VBtn>
    </template>
  </div>
</template>
<style scoped>
.header {
  display: flex;
  justify-content: end;
  align-items: center;
  margin-block: 5px;
}
</style>
