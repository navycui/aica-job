import * as R from 'ramda';
import React, {Suspense, useEffect, useState} from 'react';
import {useRoutes, useLocation} from 'react-router-dom';
import NotFound from 'shared/NotFound';
import {Loader, middleware, RouteData, RouteType} from "./utils/RouteUtiles";
import {useRouteStore} from "./Store/RouteConfig";
import {useQuery, useQueryClient} from "react-query";
import axios, {AxiosGet, GetQuery} from "./libs/axios";

type PortalType = "PORTAL_UAM" | "PORTAL_TSP" | "PORTAL_TAM";

interface IDynamicRouter {
  addRoutes?: RouteType[],
  portalType: PortalType,
}

interface Root {
  children: RouteData[]
}

export function DynamicRouter({addRoutes, portalType}: IDynamicRouter) {
  const url = `/member/api/auth/menus/${portalType}/me`
  // const memberUrl = portalType == "PORTAL_TAM"?  "http://3.38.179.210:8082" :
  //   `${process.env.REACT_APP_DOMAIN_MEMBER_BNET}`
  const memberUrl = `${process.env.REACT_APP_DOMAIN}`
  const client = useQueryClient()
  const portal = GetQuery(url, undefined, {
    baseURL: memberUrl
  },{
    cacheTime: Infinity
  })
  const [isLoading, setIsLoading] = useState(true)
  const routeStore = useRouteStore()

  useEffect(() => {
    if (!portal.isLoading && !portal.isFetching){
      if (!!portal.data){
        const record: RouteData[] = R.pipe(JSON.stringify, JSON.parse)(portal.data.list);
        const routes = hierarchy(portal.data.list);
        routeStore.setRecord(record)
        routeStore.setRoutes(routes)
        if (isLoading) setIsLoading(false)
      }
    }
  }, [portal.data, portal.isLoading, portal.isFetching])



  if (isLoading ) return  <div/>
  return <Routes addRoutes={addRoutes} routes={routeStore.routes}/>;
}

const flatten = (routes: any) => {
  const res = routes.reduce((a: any, b: any) => {
    const {children = []} = b;
    const c = R.omit(['children'])(b);

    const d = flatten(children);

    return [...a, c, ...d];
  }, []);
  return res;
};

const adaptor = (routes: any) => {
  return routes.map((route: any) => {
    const {label, path} = route;
    const config = useRouteStore();
    const tspPath = "/tsp-admin"+path;

    return {
      label,
      path: `${tspPath}`,
      element: (
        <Loader
          route={middleware({
            layout: config.defaultLayout,

            //* path 컴포넌트
            element: React.lazy(() => import(`~/pages${path}`)),
          }, [])}
        />
      ),
    };
  });
};


function Routes({routes, addRoutes}: any) {
  const menus = R.pipe(flatten, adaptor)(routes);

  const nav = useRoutes([
    ...addRoutes,
    ...menus,
    {
      path: '*',
      element: <NotFound/>,
    },
  ]);

  return nav;
}

//* 1차원 배열을 계층 구조로 변환
function hierarchy(list: any) {
  //* 객체 복사
  const record: RouteData[] = R.pipe(JSON.stringify, JSON.parse)(list);

  // mutate('route://record', record);
  let map = record.reduce((a, b, i) => ({...a, [b.menuId]: i}), {});
  let root: Root = { children: []};

  //* 계층 생성
  record.forEach((item) => {
    item.path = `${item.url}`.replace(/\/\//g, '/');
    item.label = item.menuNm;
    if (item.parentMenuId === "ROOT") {
      root.children.push(item)
    } else if (!R.isNil(item.parentMenuId)){
      // @ts-ignore
      const el = record[map[item.parentMenuId]];

      if (R.isNil(el.children)) el.children = [];

      el.children.push(item);
    }
  });

  return root.children;
}
