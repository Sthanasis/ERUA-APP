<script setup lang="ts">
import { VTextField, VSelect, VCheckbox } from 'vuetify/components';
import { reactive } from 'vue';
import type { TInputControl } from '~/types/props';
import LoginRegisterWrapper from '~/components/wrappers/LoginRegisterWrapper.vue';
import { IRegisterData } from '~/types/authentication';
import { authService } from '~/services/authentication';
import { useRouter } from 'vue-router';
import { LOGIN } from '~/constants/routes';

interface IUserData {
  [key: string]: string | null | undefined | boolean;
  email: string | null;
  name: string | null;
  password: string | null;
  confirmPassword: string | null;
  role: string | undefined;
  hasAcceptedGTPR: boolean;
}

const router = useRouter();

const userData = reactive<IUserData>({
  email: null,
  name: null,
  password: null,
  confirmPassword: null,
  role: undefined,
  hasAcceptedGTPR: false,
});

const userRoles = ['ERUA', 'STAKEHOLDER'];

const inputControls: TInputControl[] = [
  { name: 'name', type: 'text', label: 'full name' },
  { name: 'email', type: 'text' },
  { name: 'password', type: 'password' },
  { name: 'confirmPassword', type: 'password', label: 'confirm Password' },
  { name: 'role', type: 'select' },
  {
    name: 'hasAcceptedGTPR',
    type: 'checkbox',
    label: 'I accept the terms and conditions',
  },
];

const registerUser = async () => {
  if (userData.password !== userData.confirmPassword) return;
  if (!userData.email || !userData.name || !userData.password) return;
  const data: IRegisterData = {
    email: userData.email,
    name: userData.name,
    password: userData.password,
  };
  if (userData.role === 'ERUA') {
    await authService.registerErua(data);
  } else {
    await authService.registerStakeholder(data);
  }
  router.push({ name: LOGIN });
};
</script>

<template>
  <LoginRegisterWrapper
    title="Sign Up"
    :input-controls="inputControls"
    :navigate-to="{ name: LOGIN }"
    :is-submit-disabled="!userData.hasAcceptedGTPR"
    nav-btn-label="Already have an account? Sign in"
    @submit="registerUser"
  >
    <template #formElement="{ control }">
      <VSelect
        v-if="control.type === 'select'"
        v-model="userData.role"
        class="text-capitalize"
        color="primary"
        :items="userRoles"
        :label="control.label ?? control.name"
      />
      <VCheckbox
        v-else-if="control.type === 'checkbox'"
        v-model="userData[control.name]"
        class="text-capitalize"
        color="primary"
        :label="control.label ?? control.name"
        :type="control.type"
        required
      />
      <VTextField
        v-else
        v-model="userData[control.name]"
        class="text-capitalize"
        color="primary"
        :label="control.label ?? control.name"
        :type="control.type"
        required
      />
    </template>
  </LoginRegisterWrapper>
</template>
