import { TUserRole } from './userTypes';

export interface ILoginData {
  [key: string]: string;
  email: string;
  password: string;
}

export interface IRegisterData {
  [key: string]: string | undefined;
  email: string;
  password: string;
  name: string;
}

export interface ISignInResponse {
  accessToken: string;
  refreshToken: string;
}

export interface ISignInResponseTest {
  accessTokenTest: string;
  refreshTokenTest: string;
}

export interface IParsedJWT {
  exp: number;
  iss: string;
  roles: TUserRole[];
  sub: string;
  name: string;
  id: string;
}
