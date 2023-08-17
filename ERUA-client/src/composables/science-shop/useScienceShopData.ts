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
  const { isProblem, isSolution } = useScienceShop();
  async function fetchData(args?: any) {
    loading.value = true;
    try {
      let response;
      if (isProblem) {
        response = await stakeholderService.getAllProblems(args);
      }
      if (isSolution) {
        response = await eruaMemberService.getAllSolutions(args);
      }
      if (response?.solutions) {
        data.value = response?.solutions;
      }
    } catch (err) {}

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
