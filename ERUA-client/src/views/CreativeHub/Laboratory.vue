<script setup lang="ts">
import BackgroundImageWrapper from '~/components/wrappers/BackgroundImageWrapper.vue';
import img from '../../../assets/laboratory.png';
import LaboratoryContainer from '~/components/creative-hub/containers/LaboratoryContainer.vue';
</script>
<template>
  <div>
    <BackgroundImageWrapper :image="img" />
    <LaboratoryContainer />
    No data found
  </div>
</template>
