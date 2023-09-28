import 'shared/styles/global.scss';
import { setup } from 'shared/libs/axios';

// @ts-ignore
const config = JSON.parse(localStorage.getItem("serverConfig"))

const ENV = {
  dev: {
    baseURL: config? config.url : 'http://dev-portal.atops.or.kr/tsp-api/api/admin'
  },
  prd: {
    baseURL: process.env.REACT_APP_API
  }
}

// const api = {
//   baseURL: 'http://api.bnet.com',
// };
// @ts-ignore
const api = process.env.REACT_APP_MODE === 'prd'? ENV['prd'] : ENV['dev'];

setup(api);
export default { config: { api } };

// 3.37.6.42
