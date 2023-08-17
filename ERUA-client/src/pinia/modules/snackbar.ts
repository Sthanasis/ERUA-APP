import { defineStore } from 'pinia';
import { ref } from 'vue';
import { IPiniaSnackbar, ISnackbar } from '~/types/props';

export const useSnackbarStore = defineStore('snackbar', () => {
  const snackbars = ref<IPiniaSnackbar[]>([]);

  function remove(id: number) {
    snackbars.value = snackbars.value.filter((s) => s.id !== id);
  }

  function add(snackbar: ISnackbar) {
    const s = { id: Math.random(), ...snackbar };
    snackbars.value.push(s);
    setTimeout(() => {
      if (s.id) remove(s.id);
    }, s.timeout || 5000);
  }

  function success(text: string) {
    const s: IPiniaSnackbar = { id: Math.random(), text, variant: 'success' };
    add(s);
  }

  function error(text: string) {
    const s: IPiniaSnackbar = { id: Math.random(), text, variant: 'error' };
    add(s);
  }

  function info(text: string) {
    const s: IPiniaSnackbar = { id: Math.random(), text, variant: 'info' };
    add(s);
  }

  return {
    snackbars,
    remove,
    success,
    error,
    info,
  };
});
