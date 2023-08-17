<script setup lang="ts">
import { onMounted, ref } from 'vue';
import AdminUserTableWrapper from '~/components/AdminUserTable/StakeholderTableWrapper.vue';
import { TColumn } from '~/types/table';
import { stakeholderService } from '~/services/stakeholders';
import { adminService } from '~/services/admin';
import { IStakeholder } from '~/types/responseData';

const stakeholders = ref<IStakeholder[]>([]);
const loading = ref(false);
const cols: TColumn[] = [
  {
    key: 'email',
    title: 'Email',
  },
  {
    key: 'name',
    title: 'Name',
  },
  {
    key: 'authorized',
    title: 'Activated',
  },
  {
    key: 'actions',
    title: 'Actions',
  },
];
const loadStakeholders = async () => {
  loading.value = true;
  const stakeholderData = await stakeholderService.getAllStakeholders();
  if (stakeholderData) {
    stakeholders.value = stakeholderData;
  }
  loading.value = false;
};

const handleActivateStakeholder = async (id: string) => {
  const stakeholder = stakeholders.value.find((s) => s.id === id);
  if (stakeholder) {
    await adminService.activateStakeholder(stakeholder.email);
    loadStakeholders();
  }
};

const handleDeactivateStakeholder = async (id: string) => {
  const stakeholder = stakeholders.value.find((s) => s.id === id);
  if (stakeholder) {
    await adminService.deactivateStakeholder(stakeholder.email);
    loadStakeholders();
  }
};

onMounted(loadStakeholders);
</script>
<template>
  <AdminUserTableWrapper
    :loading="loading"
    :rows="stakeholders"
    :columns="cols"
    @activate="handleActivateStakeholder"
    @deactivate="handleDeactivateStakeholder"
  />
</template>
