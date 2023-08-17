import { ref } from 'vue';

export const useSearch = () => {
  const search = ref('');

  return {
    search,
  };
};
