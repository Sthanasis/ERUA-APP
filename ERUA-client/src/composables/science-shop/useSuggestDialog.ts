import { ref } from 'vue';
import { IEruaMemberSolution, IStakeholderProblem } from '~/types/responseData';
import { eruaMemberService } from '~/services/erua';
import { stakeholderService } from '~/services/stakeholders';
import { useScienceShop } from '~/pinia/modules/scienceShop';

type SuggestionsType =
  | IEruaMemberSolution[]
  | IStakeholderProblem[]
  | undefined;
export const useSuggestDialog = () => {
  const isSuggestDialogActive = ref(false);
  const suggestions = ref<SuggestionsType>([]);
  const { isProblem, isSolution } = useScienceShop();

  async function fetchSuggestions(id: number) {
    let data: SuggestionsType = [];
    if (isSolution) {
      data = await eruaMemberService.getUnlinkedProblemsBySolution(id);
    }
    if (isProblem) {
      data = await stakeholderService.getUnlinkedSolutionsByProblem(id);
    }
    if (data) suggestions.value = data;
  }

  function closeSuggestionDialog() {
    isSuggestDialogActive.value = false;
    suggestions.value = [];
  }

  async function linkSuggestion(solutionId: number, problemId: number) {
    if (isProblem) {
      await eruaMemberService.addSolutionToProblem(solutionId, problemId);
    }
    if (isSolution) {
      await stakeholderService.addProblemToSolution(solutionId, problemId);
    }
    closeSuggestionDialog();
  }
  return {
    isSuggestDialogActive,
    suggestions,
    linkSuggestion,
    fetchSuggestions,
    closeSuggestionDialog,
  };
};
