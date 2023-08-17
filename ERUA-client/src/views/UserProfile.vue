<script setup lang="ts">
import type { IEruaMemberSolution, IUserResponse } from '~/types/responseData';
import HeroWrapper from '~/components/Hero/HeroWrapper.vue';
import { VCol } from 'vuetify/components';
import { useUserStore } from '~/pinia/modules/user';
import { toRefs } from 'vue';

import CardWrapper from '~/components/wrappers/CardWrapper.vue';
import NetworkCardWrapper from '~/components/user-profile/NetworkCardWrapper.vue';
import { onMounted } from 'vue';
import { ref } from 'vue';
import { eruaMemberService } from '~/services/erua';
import SolutionsCardWrapper from '~/components/user-profile/SolutionsCardWrapper.vue';
import { useRoute } from 'vue-router';

const userStore = useUserStore();
const { params } = useRoute();
const { id } = params;
console.log(id);
const { name, email, getUserRole, isErua } = toRefs(userStore);
const connections = ref<IUserResponse[]>([]);
const solutions = ref<IEruaMemberSolution[]>([]);
async function fetchConnections() {
  if (email.value) {
    const friends = await eruaMemberService.getFriendList();
    if (friends) connections.value = friends;
  }
}

async function fetchSolutions() {
  if (email.value) {
    const data = await eruaMemberService.getSolutionsByMember(email.value);
    if (data) solutions.value = data;
  }
}
onMounted(() => {
  if (isErua.value) {
    fetchConnections();
    fetchSolutions();
  }
});
</script>
<template>
  <div class="d-flex mt-10 px-2">
    <VCol
      cols="2"
      class="d-flex flex-column align-center"
    >
      <HeroWrapper size="100" />
      <div class="mt-4">
        <p class="text-h6 mt-1">{{ name }}</p>
        <p class="text-caption">{{ email }}</p>
        <p class="text-caption">{{ getUserRole }}</p>
      </div>
    </VCol>
    <VCol cols="6">
      <CardWrapper title="Interests">
        <div class="text-body-2">You have no interests</div>
      </CardWrapper>
      <NetworkCardWrapper :connections="connections" />
      <SolutionsCardWrapper :solutions="solutions" />
    </VCol>
    <VCol cols="4">
      <div>
        <div class="text-h5-2">Bio</div>
        <div class="text-caption">
          Lorem ipsum dolor sit amet consectetur adipisicing elit. A officiis,
          nemo ex veritatis sequi soluta provident similique ad dolores.
          Consequuntur sed perspiciatis cupiditate exercitationem aspernatur
          natus enim voluptate ad ex.
        </div>
      </div>
    </VCol>
  </div>
</template>
