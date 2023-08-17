<script setup lang="ts">
import { VRow, VCol, VImg, VProgressCircular } from 'vuetify/components';
type ImageDataType = {
  title: string;
  url?: string;
};
defineProps<{ images: ImageDataType[] }>();
</script>
<template>
  <VRow :style="{ margin: 0 }">
    <VCol
      v-for="image in images"
      :key="image.url"
      class="d-flex child-flex flex-column text-center text-capitalize"
      cols="4"
    >
      <h3>{{ image.title }}</h3>
      <VImg
        :src="image.url"
        :lazy-src="image.url"
        aspect-ratio="1"
        cover
        class="bg-grey-lighten-2"
      >
        <template #placeholder>
          <VRow
            class="fill-height ma-0"
            align="center"
            justify="center"
          >
            <VProgressCircular
              indeterminate
              color="grey-lighten-5"
            />
          </VRow>
        </template>
      </VImg>
    </VCol>
  </VRow>
</template>
