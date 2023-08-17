<script setup lang="ts">
import EventsAndStoriesContainer from '~/components/creative-hub/containers/EventsAndStoriesContainer.vue';
import img from '../../../assets/events.jpeg';
import BackgroundImageWrapper from '~/components/wrappers/BackgroundImageWrapper.vue';
</script>
<template>
  <div>
    <BackgroundImageWrapper :image="img" />
    <EventsAndStoriesContainer />
  </div>
</template>
