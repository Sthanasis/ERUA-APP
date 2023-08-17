<script setup lang="ts">
import { VDialog, VCard, VCardTitle, VCardText } from 'vuetify/components';

interface IItemDialogProps {
  open?: boolean;
  title?: string;
}
defineProps<IItemDialogProps>();
defineEmits(['on-close']);
</script>
<template>
  <VDialog
    :model-value="open"
    max-width="800"
    @update:model-value="$emit('on-close')"
  >
    <VCard class="dialog">
      <VCardTitle>
        <slot name="title" />
      </VCardTitle>
      <VCardText class="content">
        <slot />
      </VCardText>

      <slot name="footer" />
    </VCard>
  </VDialog>
</template>
<style scoped>
.dialog {
  width: auto;
  padding: 10px 0;
}

.content {
  overflow: auto;
  max-height: 500px;
}
</style>
