import type {
  IRegisterData,
  ISignInResponse,
  ISignInResponseTest,
} from '~/types/authentication';
import { getWithRefreshToken, post } from './base';
import { useSnackbarStore } from '~/pinia/modules/snackbar';

const login = async (data: URLSearchParams) => {
  try {
    const { accessToken, refreshToken } = (await post(
      '/login',
      data,
      true
    )) as ISignInResponse;
    return { accessToken: accessToken, refreshToken: refreshToken };
  } catch (err) {
    const snackbar = useSnackbarStore();
    snackbar.error('Invalid username or password');
  }
};

const testlogin = async (data: URLSearchParams) => {
  const { accessTokenTest, refreshTokenTest } = (await post(
    '/login',
    data,
    true
  )) as ISignInResponseTest;

  return { accessToken: accessTokenTest, refreshToken: refreshTokenTest };
};

const registerStakeholder = async (data: IRegisterData) => {
  return await post('/stakeholder', data);
};

const registerErua = (data: IRegisterData) => {
  return post('/eruamember', data);
};

const refreshAccessToken = () => {
  return getWithRefreshToken<ISignInResponse>('/user/token/refresh');
};

export const authService = Object.freeze({
  login,
  registerStakeholder,
  registerErua,
  refreshAccessToken,
  testlogin,
});
