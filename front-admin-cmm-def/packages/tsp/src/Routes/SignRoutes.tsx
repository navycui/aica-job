import {Loader, middleware, RouteType} from "shared/utils/RouteUtiles";
import React from "react";

export const SignRoutes: RouteType[] = [
  {
    path: '/tsp-admin/signin',
    label: '로그인',
    element: (
      <Loader
        route={
          {
            label: 'signin',
            layout: 'empty',
            element: React.lazy(() => import("../pages/Sign/SignIn")),
          }}
      />
    ),
  },{
    path: '/tsp-admin/signout',
    label: '로그아웃',
    element: (
      <Loader
        route={{
          label: 'signout',
          layout: 'empty',
          element: React.lazy(() => import('../pages/Sign/SignOut')),
        }}
      />
    ),
  }
].
map((route: Partial<RouteType>) => ({
  ...route,
  layout: 'space',
})) as RouteType[];