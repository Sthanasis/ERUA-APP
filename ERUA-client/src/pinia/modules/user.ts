import { defineStore } from 'pinia';
import { authService } from '~/services/authentication';
import { ISignInResponse } from '~/types/authentication';
import { TUserRole } from '~/types/userTypes';
import { getTokenDifference, isTokenExpired, parseJWT } from '~/utils/methods';

interface IUserState {
  accessToken: string | null;
  refreshToken: string | null;
  email: string | null;
  roles: TUserRole[] | null;
  name: string | null;
  id: string | null;
}
// TODO REFACTOR TO COMPOSITION API
export const useUserStore = defineStore('user', {
  state: (): IUserState => ({
    accessToken: localStorage.getItem('token'),
    refreshToken: localStorage.getItem('refreshToken'),
    email: null,
    roles: null,
    name: null,
    id: null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.accessToken,
    isAdmin: (state) => !!state.roles && state.roles.includes('ADMIN'),
    isErua: (state) => !!state.roles && state.roles.includes('ERUA'),
    isStakeholder: (state) =>
      !!state.roles && state.roles.includes('STAKEHOLDER'),
    getUserRole: (state) => {
      if (state.roles) {
        if (state.roles.includes('ADMIN')) {
          return 'ADMIN';
        }
        const [role] = state.roles;
        return role;
      }
    },
  },
  actions: {
    storeToken(token: string) {
      this.accessToken = token;
      localStorage.setItem('token', token);
    },
    storeRefreshToken(token: string) {
      this.refreshToken = token;
      localStorage.setItem('refreshToken', token);
    },
    logout() {
      this.accessToken = null;
      this.refreshToken = null;
      this.email = null;
      this.roles = null;
      this.name = null;
      this.id = null;
      localStorage.clear();
    },
    storeUser({ accessToken, refreshToken }: ISignInResponse) {
      const { roles, sub, name, id } = parseJWT(accessToken);
      this.email = sub;
      this.roles = roles;
      this.name = name;
      this.id = id;
      this.storeToken(accessToken);
      this.storeRefreshToken(refreshToken);
    },
    async authenticateUser() {
      if (this.accessToken && this.refreshToken) {
        // logout the user if refresh token has expired
        const refreshTokenExpired = isTokenExpired(this.refreshToken);
        if (refreshTokenExpired) {
          this.logout();
          return;
        }
        // refresh access token if refresh token is valid
        const accessTokenExpired = isTokenExpired(this.accessToken);
        if (accessTokenExpired) {
          const tokens = await authService.refreshAccessToken();
          if (tokens) {
            const { accessToken, refreshToken } = tokens;
            this.storeUser({
              accessToken,
              refreshToken,
            });
          }
        } else {
          this.storeUser({
            accessToken: this.accessToken,
            refreshToken: this.refreshToken,
          });
        }
        this.refreshUser();
      }
    },
    refreshUser() {
      if (this.accessToken) {
        const diff = getTokenDifference(this.accessToken);
        setTimeout(() => this.authenticateUser(), diff);
      }
    },
  },
});
