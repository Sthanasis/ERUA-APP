<script setup lang="ts"></script>
<template>
  <div class="flexbox">
    <slot />
  </div>
</template>
<style scoped>
.flexbox {
  display: flex;
  flex-wrap: wrap;
}
</style>
