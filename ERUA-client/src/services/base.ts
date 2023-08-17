import { useSnackbarStore } from '~/pinia/modules/snackbar';
import { useUserStore } from '~/pinia/modules/user';

const baseUrl =
  process.env.NODE_ENV === 'production' ? '/api' : 'http://localhost:8080';

export const get = async <T>(endpoint: string): Promise<T | undefined> => {
  const token = useUserStore().accessToken;
  let headers;
  if (token) {
    headers = {
      Authorization: `Bearer ${token}`,
    };
  }
  try {
    const res = await fetch(`${baseUrl}${endpoint}`, {
      headers,
    });
    return await res.json();
  } catch (err) {
    console.log(err);
  }
};

export const getBlob = async (endpoint: string) => {
  const token = useUserStore().accessToken;
  let headers;
  if (token) {
    headers = {
      Authorization: `Bearer ${token}`,
    };
  }
  try {
    const res = await fetch(`${baseUrl}${endpoint}`, {
      headers,
    });
    return await res.blob();
  } catch (err) {
    console.log(err);
  }
};

export const getWithRefreshToken = async <T>(
  endpoint: string
): Promise<T | undefined> => {
  const token = useUserStore().refreshToken;

  try {
    const res = await fetch(`${baseUrl}${endpoint}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return await res.json();
  } catch (err) {
    console.log(err);
  }
};

export const post = async <T>(
  endpoint: string,
  data: T,
  isUrlEncoded?: boolean
) => {
  const token = useUserStore().accessToken;
  const snackbar = useSnackbarStore();
  const body = isUrlEncoded ? (data as URLSearchParams) : JSON.stringify(data);
  const contentType = isUrlEncoded
    ? 'application/x-www-form-urlencoded'
    : 'application/json';
  let authorizationHeaders;
  if (token) {
    authorizationHeaders = { Authorization: `Bearer ${token}` };
  }

  const res = await fetch(`${baseUrl}${endpoint}`, {
    method: 'POST',
    body,
    headers: {
      'Content-Type': contentType,
      ...authorizationHeaders,
    },
  });
  try {
    const json = await res.json();
    const hasError =
      res.status.toString().startsWith('4') ||
      res.status.toString().startsWith('5');

    if (!hasError) {
      return json;
    }

    snackbar.error(json.error_message ?? json.message);
  } catch (err) {}
};

export const postBlob = async (endpoint: string, data: Blob) => {
  const token = useUserStore().accessToken;
  const snackbar = useSnackbarStore();
  const formdata = new FormData();
  formdata.append('file', data);

  let authorizationHeaders;
  if (token) {
    authorizationHeaders = { Authorization: `Bearer ${token}` };
  }

  const res = await fetch(`${baseUrl}${endpoint}`, {
    method: 'POST',
    body: formdata,
    headers: {
      Accept: 'application/json, text/plain, */*',
      ...authorizationHeaders,
    },
  });

  const hasError =
    res.status.toString().startsWith('4') ||
    res.status.toString().startsWith('5');

  if (!hasError) {
    snackbar.success('File uploaded successfully');
    return;
  }
  const json = await res.json();

  snackbar.error(json.error_message ?? json.message);
};
export const deleteRequest = async <T>(
  endpoint: string
): Promise<T | undefined> => {
  const token = useUserStore().accessToken;

  try {
    const res = await fetch(`${baseUrl}${endpoint}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return await res.json();
  } catch (err) {
    console.log(err);
  }
};
