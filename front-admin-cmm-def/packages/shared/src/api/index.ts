import authentication from "../authentication";
import fetchRefreshToken from "../fetches"
import dayjs from "../libs/dayjs";
import axios from "../libs/axios";

export default async (req: any) => {
  // 토큰이 있다면..
  const certificate = authentication.get();
  const left = dayjs(certificate.accessTokenExpiresAt).diff(+new Date(), 's');

  if (
    //* 토큰이 유효하고,
    certificate.accessToken &&
    //* 남은 시간이 1초 이상이고,
    left > 0 &&
    //* 업데이트 한지 2초 이상이면,
    dayjs().diff(certificate.updateAt, 's') > 1
  ) {
    //* 토큰 갱신
    try {
      const res = await fetchRefreshToken();
      authentication.set(res.data);
    } catch (e) {}
  }

  return req.responseType === 'blob'
    ? axios(req)
    : axios(req).then((res) => res.data);
};
