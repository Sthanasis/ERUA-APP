<script setup lang="ts">
import BackgroundImageWrapper from '~/components/wrappers/BackgroundImageWrapper.vue';
import CreativeNetworkContainer from '~/components/creative-hub/containers/CreativeNetworkContainer.vue';
import img from '../../../assets/social.jpeg';
</script>
<template>
  <div>
    <BackgroundImageWrapper :image="img" />
    <CreativeNetworkContainer />
    No data found
  </div>
</template>
