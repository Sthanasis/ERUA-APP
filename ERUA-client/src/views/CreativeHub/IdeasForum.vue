<script setup lang="ts">
import BackgroundImageWrapper from '~/components/wrappers/BackgroundImageWrapper.vue';
import IdeasForumContainer from '~/components/creative-hub/containers/IdeasForumContainer.vue';
import img from '../../../assets/ideas.png';
</script>
<template>
  <div>
    <BackgroundImageWrapper :image="img" />
    <IdeasForumContainer />
    No ideas found
  </div>
</template>
