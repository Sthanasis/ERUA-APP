<script setup lang="ts">
import { onMounted } from 'vue';
import Layout from './components/Layout';
import { useUserStore } from './pinia/modules/user';

const user = useUserStore();

onMounted(() => {
  user.authenticateUser();
});
</script>

<template>
  <Layout>
    <router-view />
  </Layout>
</template>
