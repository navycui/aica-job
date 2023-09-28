/* eslint-disable import/no-anonymous-default-export */
import axios, {AxiosRequestConfig, AxiosResponse, Canceler, ResponseType} from 'axios';
import authentication from '../authentication';
import async from 'async';
import {useInfiniteQuery, useMutation, useQuery} from "react-query";
import {BaseResponse, ErrorMessage} from '../utils/Model';
import dayjs from "../libs/dayjs";
import {UseInfiniteQueryOptions} from "react-query/types/react/types";
import {RefreshToken} from "../api/RefeshToken";

const q = async.queue(function (task: any, callback) {
  setTimeout(callback, 3000);
}, 2);
const xhr = axios.create();
xhr.defaults.withCredentials = true;
xhr.defaults.timeout = 1000 * 5 * 60; // 5분

const process: { [key: string]: any } = {};

export type {AxiosRequestConfig, AxiosResponse};

let config: any = null;
export const setup = (params: AxiosRequestConfig) => {
  config = {
    ...params,
    withCredentials: true
  };
  // config = params;
};
export const getBaseUrl = () => config.baseURL

//* 성공, 실패 모두 키값 삭제
function clear(res: any) {
  const key = res.config?.url;
  delete process[key];

  return res;
}

export default async (req: AxiosRequestConfig<any>) => {
  if (!config) {
    console.error('API Config 설정을 먼저 해야 합니다.');
  }
  const token = authentication.getToken();
  let headers:any = {};
  if (token) {
    headers = {
      Auth: 'Bearer ' + token,
    };
    headers['Front-Referer'] = window.location.href
  }

  //* 요청한 URL로 이미 진행 중인 API 가 있다면, 진행 중인 Promise 반환
  const key: any = req.url;
  if (process[key]) return process[key];

  process[key] = xhr({headers: headers, ...config, ...req});
  return process[key].then(clear).catch((e: any) => Promise.reject(clear(e)));
};

export type RequestCancelRef = (cancel: Canceler) => void
export type ContentType = "json" | "formData"
type RequestOptions = {
  contentType?: ContentType
  responseType?: ResponseType
  cancelRef?: RequestCancelRef
  baseURL?: string
}

const Headers = (type?: ContentType) => {
  const token = authentication.getToken();
  const headers: any = {
    'Content-Type': 'application/json;charset=UTF-8',
    'Front-Referer': window.location.href
  };
  if (type && type == "formData")
    headers['Content-Type'] = 'multipart/form-data'
  if (token) {
    headers['Auth'] = `Bearer ${token}`;
  }

  return headers;
}

const _request = async (
  type: 'GET' | 'POST' | 'DELETE' | 'PUT',
  url: string,
  data?: any,
  opts?: RequestOptions,
): Promise<any> => {
  let resData: BaseResponse | null = null;
  const headers = Headers(opts?.contentType)
  const start: Date = new Date(Date.now())

  // 토큰 갱신
  await RefreshToken();

  try {
    let res = null;

    if (type === 'GET') {
      res = await xhr.get(url, {
        ...config,
        baseURL: opts && opts.baseURL ? opts.baseURL : config.baseURL,
        headers: headers, params: data,
        responseType: opts && opts.responseType,
        paramsSerializer: paramsObj => {
          const params = new URLSearchParams()
          for ( const key in paramsObj) {
            if(paramsObj[key] !== undefined)
            params.append(key, paramsObj[key])
          }
          return params.toString();
        }
      })
    } else if (type === 'POST') {
      res = await xhr.post(url, data, {
        ...config,
        headers: headers,
        baseURL: opts && opts.baseURL ? opts.baseURL : config.baseURL,
        cancelToken: opts && opts.cancelRef && new axios.CancelToken(opts.cancelRef),
      })
    } else if (type === 'DELETE') {
      res = await xhr.delete(url, {
        ...config,
        data: data,
        baseURL: opts && opts.baseURL ? opts.baseURL : config.baseURL,
        headers: headers,
        cancelToken: opts && opts.cancelRef && new axios.CancelToken(opts.cancelRef),
      })
    } else if (type === 'PUT') {
      res = await xhr.put(url, data, {
        ...config,
        baseURL: opts && opts.baseURL ? opts.baseURL : config.baseURL,
        headers: headers,
        cancelToken: opts && opts.cancelRef && new axios.CancelToken(opts.cancelRef),
      })
    }

    resData = res!.data
    if (res!.status == 200) {
      if (resData) resData.success = true
      else resData = {success: true, headers: res!.headers.toString() || ''}
    }

  } catch (e) {
    clear(e);

    // cancelRef를 사용해서 요청을 취소시켰을 경우
    if (axios.isCancel(e)) {
      console.debug(['API CANCEL', {url}]);
      // @ts-ignore
      throw [{message: e.message, url}];
    }

    // status가 200이 아닌 모든 경우
    // @ts-ignore
    const {data, status} = e.response;
    if (status === 403 || status === 401) {
      alert(data.message)
      throw [{message: 'Access Denied', url} as ErrorMessage];
    }

    if (status == 500) alert(data.message)
    if (status == 400) alert(data.message)

    console.warn(['API ERROR', {url, status, data}]);
    throw [{message: 'ERROR', url} as ErrorMessage];
  }

  // 서버에서 반환된 에러 처리
  // GET은 서버 정의 에러가 없으며 문제가 발생한다면 null이 반환된다
  // if (type == 'POST' && resData && !resData.success) {
  //   throw resData.errors;
  // }
  return resData;
}

export const AxiosGet = async (url: string, param?: any, reqOpts?: RequestOptions) => {
  return await _request('GET', url, param, reqOpts);
}

export const AxiosPost = async (url: string, data?: any, reqOpts?: RequestOptions) => {
  return await _request('POST', url, data, reqOpts);
}

export const AxiosDelete = async (url: string, data?: any, reqOpts?: RequestOptions) => {
  return await _request('DELETE', url, data, reqOpts);
}

export const AxiosPut = async (url: string, data?: any, reqOpts?: RequestOptions) => {
  return await _request('PUT', url, data, reqOpts);
}

/* react-query를 사용해서 Axios통신. */
export const GetQuery = (url: string, data?: any, reqOpts?: RequestOptions, queryOpts?: any) => {
  return useQuery([url, data], async () => await _request('GET', url, data, reqOpts), queryOpts);
}

export const PostQuery = (url: string, data?: any, reqOpts?: RequestOptions, queryOpts?: any) => {
  return useQuery([url, data], () => _request('POST', url, data, reqOpts), queryOpts);
}

export const DeleteQuery = (url: string, data?: any, reqOpts?: RequestOptions, queryOpts?: any) => {
  return useQuery([url, data], () => _request('DELETE', url, data, reqOpts), queryOpts);
}
