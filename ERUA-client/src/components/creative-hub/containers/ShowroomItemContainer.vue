<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { IExhibitionResponse } from '~/types/responseData';
import {
  VCardActions,
  VImg,
  VCard,
  VCardTitle,
  VCardSubtitle,
} from 'vuetify/components';
import { VSkeletonLoader } from 'vuetify/labs/VSkeletonLoader';
import noImg from '../../../../assets/no-image.jpeg';
import { eruaMemberService } from '~/services/erua';
import { useRouter } from 'vue-router';
import { EXHIBITION } from '~/constants/routes';
import { useUserStore } from '~/pinia/modules/user';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';

const props = defineProps<{
  exhibition: IExhibitionResponse;
}>();
const emit = defineEmits(['delete']);

const img = ref<string>();
const loading = ref(false);

const router = useRouter();
async function fetchImage() {
  const firstImage = props.exhibition.exhibitionImageList[0];
  if (firstImage) {
    loading.value = true;
    const blob = await eruaMemberService.getExhibitionImage(
      props.exhibition.id,
      firstImage.id
    );
    if (blob) {
      const imgUrl = URL.createObjectURL(blob);
      img.value = imgUrl;
    }
  }
  loading.value = false;
}

function goToExhibition() {
  router.push({ name: EXHIBITION, params: { id: props.exhibition.id } });
}

const userStore = useUserStore();

const { id, isAdmin } = storeToRefs(userStore);
const hasDeleteAuthority = computed(() => {
  if (id) return props.exhibition.organizer.id === id.value || isAdmin.value;
  return isAdmin;
});
onMounted(fetchImage);
</script>
<template>
  <VCard
    v-if="loading"
    class="mx-4 my-4 border"
  >
    <VSkeletonLoader
      width="344"
      type="image"
    />
    <VSkeletonLoader
      width="344"
      type="article"
    />
  </VCard>

  <VCard
    v-else
    class="container"
    variant="flat"
    width="344"
  >
    <VImg
      height="200px"
      cover
      :src="img ?? noImg"
    />
    <VCardTitle>{{ exhibition.name }}</VCardTitle>
    <VCardSubtitle>Starts on: {{ exhibition.eventDate }}</VCardSubtitle>
    <VCardSubtitle>e-expo</VCardSubtitle>
    <template #actions>
      <VCardActions>
        <VBtn
          color="info"
          variant="flat"
          @click="goToExhibition"
        >
          Learn More
        </VBtn>
      </VCardActions>
      <VCardActions>
        <VBtn
          v-if="hasDeleteAuthority"
          color="error"
          variant="flat"
          @click="emit('delete')"
        >
          Delete
        </VBtn>
      </VCardActions>
    </template>
  </VCard>
</template>
<style scoped>
.container {
  display: flex;
  flex-flow: column;
  justify-content: end;
  margin: 10px;
  width: 344px;
}
</style>
