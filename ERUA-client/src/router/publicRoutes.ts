import { HOME, KNOWLEDGE, NEWS, REGION, SHOP } from '~/constants/routes';
const Home = () => import('~/views/Home.vue');
const Shop = () => import('~/views/ScienceShop.vue');
const PlaceHolder = () => import('~/views/Placeholder.vue');
const Regions = () => import('~/views/Regions.vue');

export const publicRoutes = [
  { path: '/', component: Home, name: HOME },
  { path: '/science-shop', component: Shop, name: SHOP },
  { path: '/regions', component: Regions, name: REGION },
  { path: '/news', component: PlaceHolder, name: NEWS },
  { path: '/open-knowledge', component: PlaceHolder, name: KNOWLEDGE },
];
