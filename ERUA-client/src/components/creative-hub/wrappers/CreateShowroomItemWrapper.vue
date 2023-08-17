<script setup lang="ts">
import { toRefs } from 'vue';
import { VTextarea, VTextField, VFileInput } from 'vuetify/components';
import DialogWrapper from '~/components/wrappers/DialogWrapper.vue';
import FormWrapper from '~/components/wrappers/FormWrapper.vue';
import { TInputControl } from '~/types/props';
interface ICreateShowroomItemWrapperProps {
  open: boolean;
  title: string;
  controls: TInputControl[];
  data: { [key: string]: any };
  isSubmitLoading?: boolean;
}
const props = defineProps<ICreateShowroomItemWrapperProps>();

const emit = defineEmits(['close', 'create']);

const { data } = toRefs(props);

function handleCreate() {
  emit('create');
}

function handleClose() {
  emit('close');
}
</script>
<template>
  <DialogWrapper
    :open="open"
    @on-close="handleClose"
  >
    <template #title>
      {{ title }}
    </template>
    <FormWrapper
      :input-controls="controls"
      :is-button-loading="isSubmitLoading"
      @submit="handleCreate"
    >
      <template #formElement="{ control }">
        <VTextarea
          v-if="control.type === 'textarea'"
          v-model="data[control.name]"
          class="text-capitalize"
          color="primary"
          :label="control.label ?? control.name"
          :type="control.type"
          :required="control.required"
        />
        <VDatePicker
          v-else-if="control.type === 'date'"
          v-model="data[control.name]"
        >
          <template #default="{ inputValue, inputEvents }">
            <VTextField
              class="text-capitalize"
              color="primary"
              :model-value="inputValue"
              :label="control.label ?? control.name"
              :required="control.required"
              v-on="inputEvents"
            />
          </template>
        </VDatePicker>
        <VFileInput
          v-else-if="control.type === 'file'"
          v-model="data[control.name]"
          class="text-capitalize"
          color="primary"
          :label="control.label ?? control.name"
          :type="control.type"
          :required="control.required"
        />
        <VTextField
          v-else
          v-model="data[control.name]"
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
