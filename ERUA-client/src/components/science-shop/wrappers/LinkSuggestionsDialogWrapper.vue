<script setup lang="ts">
import { VList, VListItem, VListItemSubtitle } from 'vuetify/components';
import DialogWrapper from '~/components/wrappers/DialogWrapper.vue';
import { IEruaMemberSolution, IStakeholderProblem } from '~/types/responseData';
interface ILinkSuggestionsDialogProps {
  items?: IEruaMemberSolution[] | IStakeholderProblem[];
  open: boolean;
}

defineProps<ILinkSuggestionsDialogProps>();
defineEmits(['link', 'close']);
</script>
<template>
  <DialogWrapper
    v-if="open"
    :open="open"
    @on-close="$emit('close')"
  >
    <template #title> Link Suggestions </template>
    <VList v-if="items && items.length > 0">
      <VListItem
        v-for="(listItem, i) in items"
        :key="listItem.id"
        :active="i % 2 === 0"
        @click="$emit('link', listItem)"
      >
        {{ listItem.name }}
        <VListItemSubtitle>
          {{ listItem.owner.name }}
        </VListItemSubtitle>
      </VListItem>
    </VList>
    <div v-else>No suggestions available</div>
  </DialogWrapper>
</template>
