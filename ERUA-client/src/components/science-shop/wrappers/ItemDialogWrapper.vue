<script setup lang="ts">
import { IEruaMemberSolution, IStakeholderProblem } from '~/types/responseData';
import DialogWrapper from '../../wrappers/DialogWrapper.vue';
import {
  VCardActions,
  VList,
  VListItem,
  VCardSubtitle,
  VListItemSubtitle,
} from 'vuetify/components';

interface IItemDialogProps {
  item?: IStakeholderProblem | IEruaMemberSolution;
  linkedWithList?: IStakeholderProblem[] | IEruaMemberSolution[];
  showActions?: boolean;
  canLink?: boolean;
}

defineProps<IItemDialogProps>();
defineEmits(['on-close', 'delete', 'suggest']);
</script>
<template>
  <DialogWrapper
    v-if="item"
    :open="!!item"
    @on-close="$emit('on-close')"
  >
    <template #title>
      <div>
        {{ item.name }}
      </div>
      <VCardSubtitle>Owner: {{ item.owner.name }}</VCardSubtitle>
      <VCardSubtitle>{{ item.owner.email }}</VCardSubtitle>
    </template>
    {{ item.description }}
    <template #footer>
      <VCardActions class="footer">
        <template v-if="canLink">
          <VBtn
            color="warning"
            variant="outlined"
            @click="$emit('suggest')"
          >
            Link
          </VBtn>
        </template>
        <template v-if="showActions">
          <VBtn
            color="error"
            variant="outlined"
            @click="$emit('delete')"
          >
            Delete
          </VBtn>
        </template>
      </VCardActions>
      <VList v-if="linkedWithList">
        <VListItem
          v-for="(listItem, i) in linkedWithList"
          :key="listItem.id"
          :active="i % 2 === 0"
        >
          {{ listItem.name }}
          <VListItemSubtitle>
            {{ listItem.owner.name }}
          </VListItemSubtitle>
        </VListItem>
      </VList>
    </template>
  </DialogWrapper>
</template>
<style scoped>
.footer {
  display: flex;
  justify-content: end;
  align-items: center;
  width: 100%;
}
</style>
