<script setup lang="ts">
import { reactive, computed } from 'vue';
import { VTextField, VTextarea, VCombobox } from 'vuetify/components';
import { stakeholderService } from '~/services/stakeholders';
import { useScienceShopData } from '~/composables/science-shop/useScienceShopData';
import { TInputControl } from '~/types/props';
import { useUserStore } from '~/pinia/modules/user';
import { IEruaMemberSolution, IProblemPayload } from '~/types/responseData';
import { useDialog } from '~/composables/useDialog';
import ItemListWrapper from '../wrappers/ItemListWrapper.vue';
import ItemDialogWrapper from '../wrappers/ItemDialogWrapper.vue';
import ProblemItemCardWrapper from '../wrappers/ProblemItemCardWrapper.vue';
import FormWrapper from '~/components/wrappers/FormWrapper.vue';
import DialogWrapper from '~/components/wrappers/DialogWrapper.vue';
import { useSelectScienceShopItem } from '~/composables/science-shop/useSelectScienceShopItem';
import { useDebounce } from '~/composables/useDebounce';
import { useSearch } from '~/composables/useSearch';
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { adminService } from '~/services/admin';
import { PageAction } from '~/types/utilities';
import { useSuggestDialog } from '~/composables/science-shop/useSuggestDialog';
import LinkSuggestionsDialogWrapper from '../wrappers/LinkSuggestionsDialogWrapper.vue';

const { loading, data, fetchData } = useScienceShopData();
const { isStakeholder, isAdmin, email, isErua } = useUserStore();

const { isDialogVisible, setIsDialogVisible } = useDialog();
const { search } = useSearch();
const formData = reactive<{ [key: string]: string | string[] | null }>({
  name: null,
  description: null,
  keywordList: [],
});

const {
  selectedItem: selectedProblem,
  fetchSelectedItem: fetchSelectedProblem,
  linkedItemList: solutionsList,
} = useSelectScienceShopItem();

const {
  fetchSuggestions,
  isSuggestDialogActive,
  suggestions,
  closeSuggestionDialog,
  linkSuggestion,
} = useSuggestDialog();

const showSelectedItemActions = computed(
  () => selectedProblem.value?.owner.email === email || isAdmin
);

const inputControls: TInputControl[] = [
  { name: 'name', type: 'text', label: 'title' },
  { name: 'description', type: 'textarea' },
  { name: 'keywordList', type: 'select', label: 'keywords' },
];

async function handleSearch(v: string) {
  await fetchData(v);
}
const { debounceFunction: debounceSearch } = useDebounce(handleSearch);

const actions = computed<PageAction[]>(() => [
  {
    name: 'Create Problem',
    callback: () => setIsDialogVisible(true),
    condition: isStakeholder,
    color: 'primary',
  },
]);
async function handleSubmit() {
  await stakeholderService.createProblem(formData as IProblemPayload);
  setIsDialogVisible(false);
  await fetchData();
}
function loadSuggestions() {
  if (selectedProblem.value) {
    fetchSuggestions(selectedProblem.value?.id);
    isSuggestDialogActive.value = true;
  }
}

async function handleLinkSuggestion(item: IEruaMemberSolution) {
  if (selectedProblem.value) {
    await linkSuggestion(item.id, selectedProblem.value.id);
    await fetchSelectedProblem(selectedProblem.value);
  }
}
async function handleDeleteProblem() {
  if (selectedProblem.value) {
    const id = selectedProblem.value.id;
    if (isAdmin) await adminService.deleteProblem(id);
    else if (isStakeholder) await stakeholderService.deleteProblem(id);
    fetchSelectedProblem();
    await fetchData();
  }
}
</script>
<template>
  <PageActionsWrapper
    :search="search"
    :actions="actions"
    @search="debounceSearch"
  />

  <ItemListWrapper
    :items="data"
    :loading="loading"
  >
    <template #problem="{ item }">
      <ProblemItemCardWrapper
        :item="item"
        @click="fetchSelectedProblem(item)"
      />
    </template>
  </ItemListWrapper>
  <ItemDialogWrapper
    :item="selectedProblem"
    :linked-with-list="solutionsList"
    :show-actions="showSelectedItemActions"
    :can-link="isErua"
    @on-close="fetchSelectedProblem(undefined)"
    @suggest="loadSuggestions"
    @delete="handleDeleteProblem"
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
    <template #title> New Problem </template>
    <FormWrapper
      :input-controls="inputControls"
      @submit="handleSubmit"
    >
      <template #formElement="{ control }">
        <VCombobox
          v-if="control.type === 'select'"
          v-model="formData[control.name]"
          class="text-capitalize"
          color="primary"
          hint="Press enter to add a new keyword"
          :label="control.label ?? control.name"
          :multiple="(true as any)"
          hide-selected
          persistent-hint
          chips
        />
        <VTextarea
          v-else-if="control.type === 'textarea'"
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
