<script setup lang="ts">
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import CreateShowroomItemWrapper from '../wrappers/CreateShowroomItemWrapper.vue';
import useShowroom from '~/composables/creative-hub/useShowroom';
import ShowroomItemContainer from './ShowroomItemContainer.vue';
import FlexboxWrapper from '~/components/wrappers/FlexboxWrapper.vue';

const {
  actions,
  createExhibitionControls,
  search,
  isCreateDialogActive,
  dialogTitle,
  createExhibitionData,
  exhibitions,
  createExhibition,
  closeCreateShowroomDialog,
  deleteExhibition,
} = useShowroom();
</script>
<template>
  <PageActionsWrapper
    :search="search"
    :actions="actions"
  />
  <CreateShowroomItemWrapper
    :open="isCreateDialogActive"
    :title="dialogTitle"
    :controls="createExhibitionControls"
    :data="createExhibitionData"
    @close="closeCreateShowroomDialog"
    @create="createExhibition"
  />
  <template v-if="exhibitions.length > 0">
    <FlexboxWrapper>
      <ShowroomItemContainer
        v-for="exhibition in exhibitions"
        :key="exhibition.id"
        :exhibition="exhibition"
        @delete="deleteExhibition(exhibition.id)"
      />
    </FlexboxWrapper>
  </template>
</template>
