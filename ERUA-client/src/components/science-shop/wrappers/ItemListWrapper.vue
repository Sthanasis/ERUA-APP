<script setup lang="ts">
import { IEruaMemberSolution, IStakeholderProblem } from '~/types/responseData';
import { VProgressCircular } from 'vuetify/components';
import { computed } from 'vue';
import CenteredContainerWrapper from '../../wrappers/CenteredContainerWrapper.vue';
import FlexboxWrapper from '~/components/wrappers/FlexboxWrapper.vue';
interface IProps {
  items: IStakeholderProblem[] | IEruaMemberSolution[];
  isSolution?: boolean;
  loading?: boolean;
}

const props = defineProps<IProps>();
const isEmpty = computed(() => props.items?.length === 0);
</script>
<template>
  <CenteredContainerWrapper v-if="loading">
    <VProgressCircular
      color="primary"
      size="60"
      indeterminate
      class="center"
    />
  </CenteredContainerWrapper>
  <FlexboxWrapper v-else>
    <div v-if="isEmpty">No Items</div>
    <template v-else>
      <template v-if="isSolution">
        <slot
          v-for="item in items"
          :key="item.id"
          name="solution"
          v-bind="{ item:item as IEruaMemberSolution }"
        />
      </template>
      <template v-else>
        <slot
          v-for="item in items"
          :key="item.id"
          name="problem"
          v-bind="{ item:item as IStakeholderProblem }"
        />
      </template>
    </template>
  </FlexboxWrapper>
</template>
<style scoped>
.center {
  margin-top: 30%;
}
</style>
