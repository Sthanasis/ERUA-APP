import { RouteLocationRaw } from 'vue-router';
import {
  HOME,
  HUB,
  KNOWLEDGE,
  NEWS,
  REGION,
  SHOP,
  TOOLS,
} from '~/constants/routes';
import { TUserRole } from '~/types/userTypes';

export type TNavTab = {
  icon: string;
  value: string;
  title: string;
  to: RouteLocationRaw;
  canAccess?: TUserRole[];
};

const navTabs: TNavTab[] = [
  {
    title: HOME,
    icon: 'mdi-account-group',
    value: 'home',
    to: '/',
  },

  {
    title: SHOP,
    icon: 'mdi-cart',
    value: 'scienceShop',
    to: '/science-shop',
  },
  {
    title: REGION,
    icon: 'mdi-earth',
    value: 'regions',
    to: '/regions',
  },
  {
    title: HUB,
    icon: 'mdi-hubspot',
    value: 'creativeHub',
    to: '/creative-hub',
    canAccess: ['ERUA', 'ADMIN'],
  },
  {
    title: TOOLS,
    icon: 'mdi-tools',
    value: 'supportingTools',
    to: '/supporting-tools',
    canAccess: ['ERUA', 'ADMIN'],
  },
  {
    title: NEWS,
    icon: 'mdi-newspaper',
    value: 'newsAndEvents',
    to: '/news',
  },
  {
    title: KNOWLEDGE,
    icon: 'mdi-bookshelf',
    value: 'openKnowledge',
    to: '/open-knowledge',
  },
];

export default navTabs;
