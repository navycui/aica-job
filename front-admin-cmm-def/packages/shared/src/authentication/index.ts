import dayjs from 'shared/libs/dayjs';
import { mutate } from 'swr';
import axios from "axios";

export type AuthenticationType = {
  accessToken: string;
  accessTokenExpiresIn: number;
  accessTokenExpiresAt: number;
  grantType: string;
  refreshToken: string;
  refreshTokenExpiresIn: number;
  refreshTokenExpiresAt: number;
  updateAt: number;
};

let store: any = {};

type keys = keyof AuthenticationType;
export const RequestToken = async() => {
  const res = await axios({
    url: '/member/api/login/refresh-token/insider',
    method: 'post',
  });

  set(res.data);
}

export const get = (key?: keys) => {
  if (!!key) {
    return store[key];
  }
  return store as AuthenticationType;
};

export const getToken = (): AuthenticationType => {
  return get().accessToken;
};

export const set = (value: AuthenticationType) => {
  store = {
    ...value,
    updateAt: +new Date(),
    accessTokenExpiresAt: dayjs()
      .millisecond(value.accessTokenExpiresIn)
      .valueOf(),
    refreshTokenExpiresAt: dayjs()
      .millisecond(value.refreshTokenExpiresIn)
      .valueOf(),
  };

  // console.log(useSWRConfig());
  mutate('authentication', store);
};

export const remove = () => {
  store = {};
  mutate('authentication', store);
};

export default {
  get,
  set,
  remove,
  getToken,
};
