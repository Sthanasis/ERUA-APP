import { onMounted, ref } from 'vue';
import { PageAction } from '~/types/utilities';
import { TInputControl } from '~/types/props';
import { useSearch } from '../useSearch';
import { ICreateExhibitionBody } from '~/types/postData';
import { eruaMemberService } from '~/services/erua';
import { IExhibitionResponse } from '~/types/responseData';

const createExhibitionControls: TInputControl[] = [
  {
    name: 'name',
    type: 'text',
    label: 'Exhibition Title',
    required: true,
  },
  {
    name: 'description',
    type: 'textarea',
    label: 'Exhibition Description',
    required: true,
  },
  {
    name: 'eventDate',
    type: 'date',
    label: 'Event Date',
    required: true,
  },
];

export default function useShowroom() {
  const { search } = useSearch();
  const exhibitions = ref<IExhibitionResponse[]>([]);
  const isCreateDialogActive = ref(false);
  const dialogTitle = ref('');

  const createExhibitionData = ref<ICreateExhibitionBody>({
    name: '',
    description: '',
    eventDate: '',
  });

  function openCreateExhibitionDialog() {
    dialogTitle.value = 'New Exhibition';
    isCreateDialogActive.value = true;
  }

  function closeCreateShowroomDialog() {
    isCreateDialogActive.value = false;
    createExhibitionData.value = {
      name: '',
      description: '',
      eventDate: '',
    };
  }

  async function fetchExhibitions() {
    const data = await eruaMemberService.getExhibitions();
    if (data && data.length > 0) {
      exhibitions.value = data;
    }
  }

  async function createExhibition() {
    const day = new Date(createExhibitionData.value.eventDate).getDate();
    const month = new Date(createExhibitionData.value.eventDate).getMonth() + 1;
    const year = new Date(createExhibitionData.value.eventDate).getFullYear();
    createExhibitionData.value = {
      ...createExhibitionData.value,
      eventDate: `${day}-${month}-${year}`,
    };
    await eruaMemberService.createExhibition(createExhibitionData.value);
    closeCreateShowroomDialog();
    fetchExhibitions();
  }
  async function deleteExhibition(id: number) {
    await eruaMemberService.deleteExhibition(id);
    fetchExhibitions();
  }
  const actions: PageAction[] = [
    {
      name: 'Create Exhibition',
      callback: openCreateExhibitionDialog,
      color: 'primary',
    },
    {
      name: 'Create Gallery',
      callback: () => console.log('yo'),
      color: 'secondary',
    },
  ];

  onMounted(fetchExhibitions);

  return {
    search,
    createExhibitionControls,
    actions,
    isCreateDialogActive,
    dialogTitle,
    createExhibitionData,
    exhibitions,
    closeCreateShowroomDialog,
    createExhibition,
    deleteExhibition,
  };
}
