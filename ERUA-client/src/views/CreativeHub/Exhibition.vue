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
import { useUserStore } from '~/pinia/modules/user';
import { ComputedRef } from 'vue';

const { params } = useRoute();
const exhibition = ref<IExhibitionResponse>();
const imglist = ref<{ url: string; title: string; id: number }[]>([]);
const imagesCount = computed(() =>
  exhibition.value ? exhibition.value.exhibitionImageList.length : 0
);
const user = useUserStore();
const isOwner = computed(
  () => user.isAdmin || user.id === exhibition.value?.organizer.id
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
            imglist.value.push({ url, title: image.name, id: image.id });
          }
        })
      );
    }
  }
}

function handleDialogClose() {
  isCreateExibitDialogActive.value = false;
  exhibit.value = {
    name: '',
    file: null,
  };
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

async function deleteImage(id: number) {
  await eruaMemberService.deleteExhibitionImage(id);
  fetchExhibitionData();
}

const actions: ComputedRef<PageAction[]> = computed(() => [
  {
    name: 'Upload Exhibit',
    callback: () => (isCreateExibitDialogActive.value = true),
    color: 'primary',
    condition: isOwner.value,
  },
]);

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
      <ImageGridWrapper :images="imglist">
        <template
          v-if="isOwner"
          #actions="{ image }"
        >
          <VBtn
            color="error"
            variant="flat"
            @click="deleteImage(image.id)"
          >
            Delete
          </VBtn>
        </template>
      </ImageGridWrapper>
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
