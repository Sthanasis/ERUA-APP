<script setup lang="ts">
import { VSheet } from 'vuetify/components';
import type { TInputControl } from '~/types/props';
import { VForm } from 'vuetify/components';
interface IProps {
  inputControls: TInputControl[];
  width?: number;
  isSubmitDisabled?: boolean;
  isButtonLoading?: boolean;
}
defineProps<IProps>();
defineEmits(['submit']);
</script>

<template>
  <VSheet :width="width">
    <VForm @submit.prevent>
      <slot
        v-for="control in inputControls"
        :key="control.name"
        v-bind="{ control }"
        name="formElement"
      />
      <VBtn
        type="submit"
        block
        class="mt-2 text-capitalize"
        color="secondary"
        :disabled="isSubmitDisabled"
        :loading="isButtonLoading"
        @click="$emit('submit')"
      >
        Submit
      </VBtn>
    </VForm>
  </VSheet>
</template>
