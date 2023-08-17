import { IParsedJWT } from '~/types/authentication';

export const parseJWT = (token: string): IParsedJWT => {
  const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    window
      .atob(base64)
      .split('')
      .map((c) => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join('')
  );

  return JSON.parse(jsonPayload);
};

export const getTokenDifference = (token: string) => {
  const { exp } = parseJWT(token);
  const tokenExpireTimestamp = exp * 1000;
  const nowTimestamp = new Date().getTime();
  return tokenExpireTimestamp - nowTimestamp;
};

export const isTokenExpired = (token: string) => {
  const diff = getTokenDifference(token);
  return diff < 0;
};
