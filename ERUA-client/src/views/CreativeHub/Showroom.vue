<script setup lang="ts">
import ShowroomContainer from '~/components/creative-hub/containers/ShowroomContainer.vue';
import BackgroundImageWrapper from '~/components/wrappers/BackgroundImageWrapper.vue';
import img from '../../../assets/art-canvas.png';
</script>
<template>
  <div>
    <BackgroundImageWrapper :image="img" />
    <ShowroomContainer />
  </div>
</template>
