import { defineStore } from 'pinia';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

export const useLayoutStore = defineStore('layout', () => {
  const route = useRoute();

  const isRegisterOrLogin = computed(
    () => route.name === 'Login' || route.name === 'Register'
  );
  const getCurrentViewName = computed(() => route.name);
  return {
    isRegisterOrLogin,
    getCurrentViewName,
  };
});
