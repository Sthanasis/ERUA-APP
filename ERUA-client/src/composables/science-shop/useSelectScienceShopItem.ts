import { ref } from 'vue';
import { useScienceShop } from '~/pinia/modules/scienceShop';
import { IEruaMemberSolution, IStakeholderProblem } from '~/types/responseData';
import { eruaMemberService } from '~/services/erua';
import { stakeholderService } from '~/services/stakeholders';

export const useSelectScienceShopItem = () => {
  const selectedItem = ref<IEruaMemberSolution | IStakeholderProblem>();
  const linkedItemList = ref<IEruaMemberSolution[] | IStakeholderProblem[]>();
  const { isProblem, isSolution } = useScienceShop();
  async function fetchSelectedItem(
    item?: IEruaMemberSolution | IStakeholderProblem
  ) {
    selectedItem.value = item;
    if (item) {
      selectedItem.value = item;
      let list: IEruaMemberSolution[] | IStakeholderProblem[] | undefined;
      if (isSolution) {
        list = await eruaMemberService.getProblemsBySolution(item.id);
      }
      if (isProblem) {
        list = await stakeholderService.getSolutionsByProblem(item.id);
      }
      if (list?.length) linkedItemList.value = list;
    } else {
      linkedItemList.value = undefined;
    }
  }

  return {
    selectedItem,
    linkedItemList,
    fetchSelectedItem,
  };
};
