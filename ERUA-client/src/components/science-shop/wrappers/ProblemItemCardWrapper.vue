<script setup lang="ts">
import type { IStakeholderProblem } from '~/types/responseData';
import { VImg, VCardTitle, VCardSubtitle, VCard } from 'vuetify/components';
import problemImage from '../../../../assets/problem.jpeg';

interface IProps {
  item: IStakeholderProblem;
}

defineProps<IProps>();
defineEmits(['click']);
</script>
<template>
  <VCard
    class="mx-4 my-10"
    width="344"
    :elevation="4"
    @click="$emit('click', item)"
  >
    <VImg
      height="200px"
      cover
      :src="problemImage"
    />

    <VCardTitle> {{ item.name }} </VCardTitle>
    <VCardSubtitle> {{ item.owner.name }} </VCardSubtitle>
    <VCardSubtitle> {{ item.owner.email }} </VCardSubtitle>

    <div class="my-5">
      <VCardSubtitle> {{ item.description }} </VCardSubtitle>
      <VCardSubtitle>
        <strong>keywords:</strong> {{ item.keywordList.join(' ') }}
      </VCardSubtitle>
    </div>
  </VCard>
</template>
