<script setup lang="ts">
import { reactive } from 'vue';
import { VTextField } from 'vuetify/components';

import type { ILoginData } from '~/types/authentication';
import type { TInputControl } from '~/types/props';
import LoginRegisterWrapper from '~/components/wrappers/LoginRegisterWrapper.vue';
import { authService } from '~/services/authentication';
import { useUserStore } from '~/pinia/modules/user';
import { useRouter } from 'vue-router';
import { HOME, REGISTER } from '~/constants/routes';

const store = useUserStore();
const router = useRouter();
const credentials = reactive<ILoginData>({
  email: '',
  password: '',
});

const inputControls: TInputControl[] = [
  { name: 'email', type: 'text' },
  { name: 'password', type: 'password' },
];

const signInUser = async () => {
  const urlEncoded = new URLSearchParams();
  urlEncoded.append('email', credentials.email);
  urlEncoded.append('password', credentials.password);
  const tokens = await authService.login(urlEncoded);
  if (tokens) {
    store.storeUser(tokens);
    store.refreshUser();
    router.push({ name: HOME });
  }
};
</script>
<template>
  <LoginRegisterWrapper
    title="Sign In"
    :input-controls="inputControls"
    :navigate-to="{ name: REGISTER }"
    nav-btn-label="Not registered? Sign up"
    @submit="signInUser"
  >
    <template #formElement="{ control }">
      <VTextField
        v-model="credentials[control.name]"
        class="text-capitalize"
        color="primary"
        :label="control.name"
        :type="control.type"
        required
      />
    </template>
  </LoginRegisterWrapper>
</template>
