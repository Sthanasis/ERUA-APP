import { LOGIN, REGISTER } from '~/constants/routes';

const Login = () => import('~/views/Login.vue');
const Register = () => import('~/views/Register.vue');

export const authenticationRoutes = [
  { path: '/login', component: Login, name: LOGIN },
  { path: '/signup', component: Register, name: REGISTER },
];
