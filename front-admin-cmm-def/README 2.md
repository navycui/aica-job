# Frontend stack

## Framework

- React or Nextjs
- Typescript

## Repository

- lerna

## Stylesheet

- scss
- mui
- emotion
- polished

## Stage management

- swr
- react-query

## XHR

- axios

## Testing

- Storybook

## Lint

- eslint
- prettier

## Library

- dayjs
- react-device-detect
- swiper
- chart.js

## Mode

- develop
- stage
- prod





## 폴더 구조

```
ai-common-front/
├─ server: local server

├─ shared: 공통 컴포넌트 및 기능
├─ ├─ .storybook: 스토리북 설정
├─ ├─ src
├─ ├─ NotFound: 페이지 없을 때 에러 처리
├─ ├─ InternalServerError: react 및 javascript 오류 처리
├─ ├─ ├─ authentication: 사용자 토큰 관리
├─ ├─ ├─ components: 공통 컴포넌트
├─ ├─ ├─ libs: library 공통 설정
├─ ├─ ├─ stories: 스토리북
├─ ├─ ├─ styles: 공통 스타일
├─ ├─ ├─ theme: Mui 태마 설정
├─ ├─ ├─ utils: 공통 유틸

├─ usp: 사용자 지원 포털
├─ ├─ src
├─ ├─ Router: 라우터 관리 (폴더 내 README.md 참조)
├─ ├─ ├─ api: 리프레시 토큰 관리 등과 같은 api 포털내 곷통 처리
├─ ├─ ├─ components: 포털내 컴포넌트
├─ ├─ ├─ fetches: api 연동
├─ ├─ ├─ layouts: 레이아웃 관리(폴더 내 README.md 참조)
├─ ├─ ├─ pages: 페이지 모음
├─ ├─ ├─ styles: 포털 스타일
```



## Start

```bash
# /etc/hosts 수정
$ sudo vi /etc/hosts
# 127.0.0.1 pc.bnet.com
# 3.37.6.42 api.bnet.com

# mac 사용자 지원 포털
$ yarn usp start

# windows 사용자 지원 포털
$ yarn usp craco start

# 스토리북
$ yarn storybook
```



## Bootstrap(포털 별 초기 로딩 설정)

```typescript
// ai-common-front/packages/usp/src/bootstrap.ts
// 공통 스타일
import 'shared/styles/global.scss';
import { setup } from 'shared/libs/axios';
const api = {
  baseURL: 'http://api.bnet.com',
};
// api 설정
setup(api);
export default { config: { api } };

```



## Router(포털 별 경로 관리)

```typescript
// ai-common-front/packages/usp/src/Router.tsx
// Route 설정 예시
const routes: Route[] = [
  {
    label: '회원',
    path: 'user',
    children: [
      {
        label: '사용자',
        path: ':id',
        element: (
          <Loader
            route={{
              label: 'User',
              layout: 'studio',
              element: React.lazy(() => import('~/pages/User')),
            }}
          />
        ),
      },
      {
        label: '마이페이지',
        path: 'mypage',
        element: (
          <Loader
            route={middleware({
              label: 'mypage',
              layout: 'studio',
              element: React.lazy(() => import('~/pages/Mypage')),
            }, ['auth'])}
          />
        ),
      }, // 토큰이 없다면, login 페이지로 redirect
    ],
  },
];
```



## Authentication(JWT)

```typescript
import authentication, {AuthenticationType} from 'shared/authentication';
import useSWR from 'swr';
// 사용자 정보 설정
authentication.set(토큰 데이터);

// jwt 가져오기
authentication.get();

// 토큰 가져오기
authentication.get('accessToken');

// 삭제
authentication.remove();

// react hook 으로 토큰 가져오기
const { data } = useSWR<AuthenticationType>('authentication');
```



## Dev server( Webpack )

```javascript
// ai-common-front/craco.config.js
module.exports = {
  devServer: {
    host: 'pc.bnet.com',
    port: 5500,
  },
  .. 이하 생략
}
```



## 참고하면 좋은 파일

- ai-common-front/packages/usp/src/layout/Studio/Header/Desktop/Menubar/Navigation/Navigation.tsx: 라우터 활용
- ai-common-front/packages/usp/src/layout/Studio/Header/Header.tsx: 모바일, 데스크톱 구분
- ai-common-front/packages/usp/src/layout/index.tsx: 토큰 활용
- ai-common-front/packages/usp/src/pages/Board/Board.tsx: 게시판 샘플
- ai-common-front/packages/usp/src/pages/Factor/Factor.tsx: 비밀번호 확인