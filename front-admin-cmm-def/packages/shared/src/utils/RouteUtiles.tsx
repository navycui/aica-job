import React, {Suspense, useEffect} from "react";
import Layout, {LayoutType} from "../layout";
import {useRouteStore} from "../Store/RouteConfig";
import {useNavigate} from "react-router-dom";
import {LinearProgress} from "@mui/material";

export type RouteType = {
  label?: string;
  menuId?: any;
  layout?: LayoutType;
  element?: any;
  path?: string;
  index?: boolean;
  middleware?: string[];
  children?: RouteType[];
};

export interface RouteData {
  systemId: string
  menuId: string
  menuNm: string
  url: string
  newWindow: boolean
  parentMenuId: string
  sortOrder: number
  creatorId: string
  createdDt: number
  updaterId: string
  updatedDt: number
  path: string
  label: string
  children?: RouteData[]
}

export type MiddlewareType = 'auth' | 'factor';

export function middleware(route: RouteType, type: MiddlewareType[]) {
  const middleware = [...(route.middleware || []), ...type];
  return Object.assign(route, {middleware});
}

export function Loader({route}: any) {
  const config = useRouteStore()
  const navi = useNavigate()
  const View = route.element;

  useEffect(() => {
    config.setLayout(route.layout)
    config.setMiddleware(route.middleware || [])
  }, [navi])

  return (
      <Suspense
        fallback={<LinearProgress />}
      >
        <View/>
      </Suspense>
  );
}