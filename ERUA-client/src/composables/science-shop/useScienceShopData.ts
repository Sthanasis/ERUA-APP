import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';
import { useScienceShop } from '~/pinia/modules/scienceShop';
import { eruaMemberService } from '~/services/erua';
import { stakeholderService } from '~/services/stakeholders';
import type {
  IEruaMemberSolution,
  IStakeholderProblem,
} from '~/types/responseData';

type DataType = IEruaMemberSolution[] | IStakeholderProblem[];
export const useScienceShopData = () => {
  const data = ref<DataType>([]);
  const loading = ref(false);
  const store = useScienceShop();
  const { isProblem, isSolution } = storeToRefs(store);
  async function fetchSolutions(search?: string) {
    const response = await eruaMemberService.getAllSolutions(search);
    if (response?.solutions) data.value = response?.solutions;
  }
  async function fetchProblems(search?: string) {
    const response = await stakeholderService.getAllProblems(search);
    if (response?.solutions) data.value = response?.solutions;
  }
  async function fetchData(args?: string) {
    loading.value = true;

    try {
      if (isProblem.value) await fetchProblems(args);
      if (isSolution.value) await fetchSolutions(args);
    } catch (err) {
      console.error(err);
    }

    loading.value = false;
  }

  onMounted(() => {
    fetchData();
  });
  return {
    data,
    loading,
    fetchData,
  };
};
