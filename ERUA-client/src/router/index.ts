import { createRouter, createWebHistory } from 'vue-router';
import { protectedRoutes } from './protectedRoutes';
import { publicRoutes } from './publicRoutes';
import { useUserStore } from '~/pinia/modules/user';
import { authenticationRoutes } from './authenticationRoutes';
import { HOME, LOGIN } from '~/constants/routes';
import { adminRoutes } from './adminRoutes';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...adminRoutes,
    ...authenticationRoutes,
    ...protectedRoutes,
    ...publicRoutes,
    { path: '/:pathMatch(.*)*', redirect: { name: HOME } },
  ],
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = useUserStore().isAuthenticated;
  const navigatesToProtectedRoute = protectedRoutes.some(
    (r) => to.name === r.name
  );
  const navigatesToAuthRoute = authenticationRoutes.some(
    (r) => to.name === r.name
  );
  if (!isAuthenticated && navigatesToProtectedRoute) {
    next({ name: LOGIN });
  } else if (isAuthenticated && navigatesToAuthRoute) next({ name: HOME });
  else next();
});

router.beforeEach((to, from, next) => {
  const isAdmin = useUserStore().isAdmin;
  const isAdministrativeRoute = adminRoutes.some((r) => r.name === to.name);
  if (!isAdmin && isAdministrativeRoute) {
    next({ name: HOME });
  } else {
    next();
  }
});

router.beforeEach((to, from, next) => {
  const store = useUserStore();
  if (to.meta.canAccess && store.getUserRole) {
    const hasAccessToRoute = to.meta.canAccess.includes(store.getUserRole);
    if (hasAccessToRoute) {
      next();
    } else {
      next({ name: HOME });
    }
  } else {
    next();
  }
});

export default router;
