<script setup lang="ts">
import { CSSProperties } from 'vue';

const iframeStyle: CSSProperties = {
  height: '100%',
  width: '100%',
};
</script>
<template>
  <iframe
    src="/maps/index.html?raw"
    frameborder="0"
    :style="iframeStyle"
  />
</template>
