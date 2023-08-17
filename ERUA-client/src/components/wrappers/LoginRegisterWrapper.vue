<script setup lang="ts">
import FormWrapper from '~/components/wrappers/FormWrapper.vue';
import CenteredContainerWrapper from '~/components/wrappers/CenteredContainerWrapper.vue';
import CardWrapper from '~/components/wrappers/CardWrapper.vue';
import type { TInputControl } from '~/types/props';
import { RouteLocationRaw } from 'vue-router';

interface IProps {
  inputControls: TInputControl[];
  navigateTo: RouteLocationRaw;
  navBtnLabel: string;
  title: string;
  isSubmitDisabled?: boolean;
}

defineProps<IProps>();
defineEmits(['submit']);
</script>

<template>
  <CenteredContainerWrapper>
    <CardWrapper :title="title">
      <FormWrapper
        :input-controls="inputControls"
        :is-submit-disabled="isSubmitDisabled"
        :width="400"
        @submit="$emit('submit')"
      >
        <template #formElement="{ control }">
          <slot
            name="formElement"
            v-bind="{ control }"
          />
        </template>
      </FormWrapper>
      <CenteredContainerWrapper>
        <VBtn
          color="primary"
          variant="text"
          class="mt-2 text-capitalize"
          :to="navigateTo"
        >
          {{ navBtnLabel }}
        </VBtn>
      </CenteredContainerWrapper>
    </CardWrapper>
  </CenteredContainerWrapper>
</template>
