import { RouteRecordRaw } from 'vue-router';
import { HUB, PROFILE, TOOLS, EXHIBITION } from '~/constants/routes';

const CreativeHub = () => import('~/views/CreativeHub/CreativeHub.vue');
const UserProfile = () => import('~/views/UserProfile.vue');
const SupportingTools = () =>
  import('~/views/SupportingTools/SupportingTools.vue');
const Exhibition = () => import('~/views/CreativeHub/Exhibition.vue');

export const protectedRoutes: RouteRecordRaw[] = [
  {
    path: '/creative-hub',
    name: HUB,
    component: CreativeHub,
    meta: {
      canAccess: ['ADMIN', 'ERUA'],
    },
  },
  {
    path: '/supporting-tools',
    name: TOOLS,
    component: SupportingTools,
    meta: {
      canAccess: ['ADMIN', 'ERUA'],
    },
  },
  { path: '/user/:name', name: PROFILE, component: UserProfile },
  {
    path: '/exhibition/:id',
    name: EXHIBITION,
    component: Exhibition,
    meta: {
      canAccess: ['ADMIN', 'ERUA'],
    },
  },
];
