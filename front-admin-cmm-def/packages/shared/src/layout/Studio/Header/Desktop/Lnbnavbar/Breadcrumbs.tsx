// import * as R from 'ramda';
import useSWR from 'swr';
import { useLocation, NavLink } from 'react-router-dom';
import {RouteType} from "../../../../../utils/RouteUtiles";

function getActivateRoutes(
  pathname: string,
  items: RouteType[],
  selected = []
): any {
  return items.reduce((a: any, b: RouteType) => {
    const regexp = new RegExp(`^${b.path!}`);
    if (regexp.test(pathname)) a.push(b);
    return getActivateRoutes(pathname, b.children || [], a);
  }, selected);
}

function Breadcrumbs() {
  const location = useLocation();
  const { data: routes } = useSWR('route://service');

  if (!routes) return <div />;

  const items = getActivateRoutes(location.pathname, routes);
  return (
    <ul>
      <li>홈</li>
      {items.map((route: RouteType) => {
        return (
          <li key={route.path}>
            <NavLink to={route.path!}>{route.label}</NavLink>
          </li>
        );
      })}
    </ul>
  );
}

export default Breadcrumbs;
