import axios from "../libs/axios";

export default () =>
  axios({
    url: '/member/api/login/refresh-token/insider',
    method: 'post',
    baseURL: `${process.env.REACT_APP_DOMAIN}`
  });
