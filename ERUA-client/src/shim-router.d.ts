import { TUserRole } from '~/types/userTypes';
// This can be directly added to any of your `.ts` files like `router.ts`
// It can also be added to a `.d.ts` file, in which case you will need to add an export
// to ensure it is treated as a module
export {};

import 'vue-router';

declare module 'vue-router' {
  // eslint-disable-next-line no-unused-vars
  interface RouteMeta {
    // must be declared by every route
    canAccess: TUserRole[];
  }
}
