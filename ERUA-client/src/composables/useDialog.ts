import { ref } from 'vue';

export const useDialog = () => {
  const isDialogVisible = ref(false);
  function setIsDialogVisible(value: boolean) {
    isDialogVisible.value = value;
  }
  return {
    isDialogVisible,
    setIsDialogVisible,
  };
};
