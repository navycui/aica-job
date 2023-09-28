# Configuration

## Router

[react-router-dom](https://reactrouter.com/docs/en/v6/getting-started/overview)

```typescript
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
