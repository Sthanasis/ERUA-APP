<script setup lang="ts">
import type { IUserResponse } from '~/types/responseData';
import ProfileCardWrapper from './ProfileCardWrapper.vue';
import { VList, VListItem, VListSubheader } from 'vuetify/components';
import { computed } from 'vue';

const props = defineProps<{ connections: IUserResponse[] }>();
const length = computed(() => props.connections.length);
</script>
<template>
  <ProfileCardWrapper title="Network">
    <template v-if="length > 0">
      <VList>
        <VListItem
          v-for="(connection, index) in connections"
          :key="connection.id"
          :value="connection.email"
          :active="index % 2 === 0"
          append-icon="mdi-account-circle"
          color="grey"
        >
          {{ connection.name }}
        </VListItem>
      </VList>
      <VListSubheader>Total: {{ length }}</VListSubheader>
    </template>
    <template v-else>
      <div>You have no connections</div>
    </template>
  </ProfileCardWrapper>
</template>
