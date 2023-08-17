import { PANEL } from '~/constants/routes';

const AdminPanel = () => import('~/views/AdminPanel.vue');

export const adminRoutes = [
  { path: '/admin-panel', component: AdminPanel, name: PANEL },
];
