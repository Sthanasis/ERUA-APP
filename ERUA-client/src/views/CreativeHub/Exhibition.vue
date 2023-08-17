<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { IExhibitionResponse } from '~/types/responseData';
import { eruaMemberService } from '~/services/erua';
import { VSkeletonLoader } from 'vuetify/labs/VSkeletonLoader';
import { VCard } from 'vuetify/components';
import FlexboxWrapper from '~/components/wrappers/FlexboxWrapper.vue';
import PageActionsWrapper from '~/components/wrappers/PageActionsWrapper.vue';
import { PageAction } from '~/types/utilities';
import CreateShowroomItemWrapper from '~/components/creative-hub/wrappers/CreateShowroomItemWrapper.vue';
import { TInputControl } from '~/types/props';
import ImageGridWrapper from '~/components/creative-hub/wrappers/ImageGridWrapper.vue';

const { params } = useRoute();
const exhibition = ref<IExhibitionResponse>();
const imglist = ref<{ url: string; title: string }[]>([]);
const imagesCount = computed(() =>
  exhibition.value ? exhibition.value.exhibitionImageList.length : 0
);

const isUploading = ref(false);

const createExhibitControls: TInputControl[] = [
  {
    name: 'name',
    type: 'text',
  },
  {
    name: 'file',
    type: 'file',
    label: 'Image',
  },
];

const exhibit = ref({
  name: '',
  file: null,
});

const isCreateExibitDialogActive = ref(false);
async function fetchExhibitionData() {
  const { id } = params;
  imglist.value = []; // in case of refetching re init the image list
  if (id) {
    const data = await eruaMemberService.getExhibition(id as string);
    if (data) {
      exhibition.value = data;
      data.exhibitionImageList.forEach((image) =>
        eruaMemberService.getExhibitionImage(data.id, image.id).then((blob) => {
          if (blob) {
            const url = URL.createObjectURL(blob);
            imglist.value.push({ url, title: image.name });
          }
        })
      );
    }
  }
}

function handleDialogClose() {
  isCreateExibitDialogActive.value = false;
}

async function uploadImage() {
  if (exhibition.value && exhibit.value.file) {
    isUploading.value = true;
    await eruaMemberService.uploadImageToExhibition(
      exhibition.value.id,
      exhibit.value.name,
      exhibit.value.file[0]
    );
    isUploading.value = false;
  }
  handleDialogClose();
  fetchExhibitionData();
}
const actions: PageAction[] = [
  {
    name: 'Upload Exhibit',
    callback: () => (isCreateExibitDialogActive.value = true),
    color: 'primary',
  },
];

onMounted(fetchExhibitionData);
</script>
<template>
  <PageActionsWrapper :actions="actions">
    <h1>{{ exhibition?.name }}</h1>
    <p>{{ exhibition?.description }}</p>
  </PageActionsWrapper>
  <FlexboxWrapper>
    <template v-if="imglist.length === 0 && imagesCount > 0">
      <VCard
        v-for="c in imagesCount"
        :key="c"
        class="mx-3 my-3 border"
        variant="flat"
      >
        <VSkeletonLoader
          width="320"
          type="image"
        />
        <VSkeletonLoader
          width="320"
          type="heading"
        />
      </VCard>
    </template>
    <template v-else>
      <ImageGridWrapper :images="imglist" />
    </template>
  </FlexboxWrapper>
  <CreateShowroomItemWrapper
    title="Add Exhibit"
    :open="isCreateExibitDialogActive"
    :controls="createExhibitControls"
    :data="exhibit"
    :is-submit-loading="isUploading"
    @close="handleDialogClose"
    @create="uploadImage"
  />
</template>
