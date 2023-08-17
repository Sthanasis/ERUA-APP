<script setup lang="ts">
import { VTextarea, VTextField } from 'vuetify/components';
import { reactive, computed } from 'vue';
import { useScienceShopData } from '~/composables/science-shop/useScienceShopData';
import { eruaMemberService } from '~/services/erua';
import FormWrapper from '~/components/wrappers/FormWrapper.vue';
import ItemListWrapper from '../wrappers/ItemListWrapper.vue';
import SolutionItemCard from '../wrappers/SolutionItemCardWrapper.vue';

import { TInputControl } from '~/types/props';
import { useUserStore } from '~/pinia/modules/user';
import { ISolutionPayload, IStakeholderProblem } from '~/types/responseData';
import ItemDialogWrapper from '../wrappers/ItemDialogWrapper.vue';
import DialogWrapper from '~/components/wrappers/DialogWrapper.vue';
import { useDialog } from '~/composables/useDialog';
import { useSelectScienceShopItem } from '~/composables/science-shop/useSelectScienceShopItem';
import { useSearch } from '~/composables/useSearch';

import { useDebounce } from '~/composables/useDebounce';
import { useSuggestDialog } from '~/composables/science-shop/useSuggestDialog';
import { adminService } from '~/services/admin';
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { PageAction } from '~/types/utilities';
import LinkSuggestionsDialogWrapper from '../wrappers/LinkSuggestionsDialogWrapper.vue';

const { loading, data, fetchData } = useScienceShopData();

const { isErua, isAdmin, email, isStakeholder } = useUserStore();
const { isDialogVisible, setIsDialogVisible } = useDialog();
const { search } = useSearch();
const {
  selectedItem: selectedSolution,
  fetchSelectedItem: fetchSelectedSolution,
  linkedItemList: linkedProblems,
} = useSelectScienceShopItem();

const {
  fetchSuggestions,
  isSuggestDialogActive,
  suggestions,
  closeSuggestionDialog,
  linkSuggestion,
} = useSuggestDialog();

const showSelectedSolutionActions = computed(
  () => selectedSolution.value?.owner.email === email || isAdmin
);

const formData = reactive<{ [key: string]: string | null }>({
  name: null,
  description: null,
  typeOfSolution: null,
});

const inputControls: TInputControl[] = [
  { name: 'name', type: 'text', label: 'title' },
  { name: 'description', type: 'textarea' },
  { name: 'typeOfSolution', type: 'text', label: 'Type of Solution' },
];
const actions = computed<PageAction[]>(() => [
  {
    name: 'Create Solution',
    callback: () => setIsDialogVisible(true),
    condition: isErua,
    color: 'primary',
  },
]);
async function handleDeleteSolution() {
  if (selectedSolution.value) {
    const id = selectedSolution.value.id;
    if (isAdmin) await adminService.deleteSolution(id);
    else if (isErua) await eruaMemberService.deleteSolution(id);
    fetchSelectedSolution();
    await fetchData();
  }
}

async function handleSearch(v: string) {
  await fetchData(v);
}

const { debounceFunction: debounceSearch } = useDebounce(handleSearch);

function loadSuggestions() {
  if (selectedSolution.value) {
    fetchSuggestions(selectedSolution.value?.id);
    isSuggestDialogActive.value = true;
  }
}
async function handleLinkSuggestion(item: IStakeholderProblem) {
  if (selectedSolution.value) {
    await linkSuggestion(selectedSolution.value.id, item.id);
    await fetchSelectedSolution(selectedSolution.value);
  }
}
async function handleSubmit() {
  await eruaMemberService.createSolution(formData as ISolutionPayload);
  setIsDialogVisible(false);
  await fetchData();
}
</script>
<template>
  <PageActionsWrapper
    :search="search"
    :actions="actions"
    @search="debounceSearch"
  />
  <ItemListWrapper
    :loading="loading"
    :items="data"
    is-solution
  >
    <template #solution="{ item }">
      <SolutionItemCard
        :item="item"
        @click="fetchSelectedSolution(item)"
      />
    </template>
  </ItemListWrapper>
  <ItemDialogWrapper
    :item="selectedSolution"
    :linked-with-list="linkedProblems"
    :show-actions="showSelectedSolutionActions"
    :can-link="isStakeholder"
    @on-close="fetchSelectedSolution(undefined)"
    @suggest="loadSuggestions"
    @delete="handleDeleteSolution"
  />
  <LinkSuggestionsDialogWrapper
    :open="isSuggestDialogActive"
    :items="suggestions"
    @link="handleLinkSuggestion"
    @close="closeSuggestionDialog"
  />
  <DialogWrapper
    :open="isDialogVisible"
    @on-close="setIsDialogVisible(false)"
  >
    <template #title>New Solution</template>
    <FormWrapper
      :input-controls="inputControls"
      @submit="handleSubmit"
    >
      <template #formElement="{ control }">
        <VTextarea
          v-if="control.type === 'textarea'"
          v-model="formData[control.name]"
          class="text-capitalize"
          color="primary"
          :label="control.label ?? control.name"
          :type="control.type"
          :required="control.required"
        />
        <VTextField
          v-else
          v-model="formData[control.name]"
          class="text-capitalize"
          color="primary"
          :label="control.label ?? control.name"
          :type="control.type"
          :required="control.required"
        />
      </template>
    </FormWrapper>
  </DialogWrapper>
</template>
