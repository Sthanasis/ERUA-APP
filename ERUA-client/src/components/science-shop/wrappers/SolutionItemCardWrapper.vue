<script setup lang="ts">
import type { IEruaMemberSolution } from '~/types/responseData';
import { VImg, VCardTitle, VCardSubtitle, VCard } from 'vuetify/components';
import solutionImage from '../../../../assets/solution.png';

interface IProps {
  item: IEruaMemberSolution;
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
      :src="solutionImage"
    />

    <VCardTitle> {{ item.name }} </VCardTitle>
    <VCardSubtitle>{{ item.owner.name }}</VCardSubtitle>
    <VCardSubtitle>{{ item.owner.email }}</VCardSubtitle>

    <div class="my-5">
      <VCardSubtitle> {{ item.description }} </VCardSubtitle>
      <VCardSubtitle>
        <strong>Solution type:</strong> {{ item.typeOfSolution }}
      </VCardSubtitle>
    </div>
  </VCard>
</template>
