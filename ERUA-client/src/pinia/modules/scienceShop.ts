import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export enum ScienceShopItem {
  SOLUTION = 'solution',
  PROBLEM = 'problem',
}

export const useScienceShop = defineStore('scienceShop', () => {
  const sienceShopItem = ref<ScienceShopItem>(ScienceShopItem.SOLUTION);

  const isSolution = computed(
    () => sienceShopItem.value === ScienceShopItem.SOLUTION
  );
  const isProblem = computed(
    () => sienceShopItem.value === ScienceShopItem.PROBLEM
  );
  function setSienceShopItem(item: ScienceShopItem) {
    sienceShopItem.value = item;
  }

  return { sienceShopItem, isSolution, isProblem, setSienceShopItem };
});
