import authentication from "../authentication";
import axios from "../libs/axios";
import dayjs from "../libs/dayjs";
import React from 'react'
import {useNavigate} from "react-router-dom";

export const RefreshToken = async (memberType?: string) => {
  const data = authentication.get()
  // 최소 1초에 한번씩만 호출되도록 예외처리
  if (!!data && data!.updateAt && dayjs().diff(data!.updateAt, 's') <= 1) {
    return true
  }

  try {
    const res = await axios({
      url: `/member/api/login/refresh-token/${!memberType ? 'insider' : memberType}`,
      method: 'post',
      baseURL: process.env.REACT_APP_DOMAIN,
    })
    if (res && res.data) {
      authentication.set(res.data)
      return true
    }
  }catch (e:any) {
    const {data} = e.response;

    if (!window.location.href.includes('signin') && data?.status == 401 && data?.error == "Unauthorized") {
      authentication.remove()
      const addPath = window.location.href.includes('tsp-admin')? '/tsp-admin' : ''
      const nextUrl = window.btoa(window.location.href)

      window.location.href = `${window.location.protocol}//${window.location.host}${addPath}/signin?nextUrl=${nextUrl}`
      return false
    }
  }

  return false
}