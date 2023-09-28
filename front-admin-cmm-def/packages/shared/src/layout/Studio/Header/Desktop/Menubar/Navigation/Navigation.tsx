import useSWR from 'swr';
import clsx from 'clsx';
import {useEffect, useState} from 'react';
import {useLocation} from 'react-router-dom';
import {RouteType} from "../../../../../../utils/RouteUtiles";

function Navigation(args: any) {
  const location = useLocation();
  const [selected, setSelected] = useState(location.pathname);

  const {data: routes = []} = useSWR('route://service');
  const syncLocation = () => {
    setSelected(() => location.pathname);
  };

  //* location 시 변경 selected 초기화
  useEffect(syncLocation, [location]);

  const handleClick = (route: any) => {
    setSelected(route.path);
  };
  return (
    <nav className="top_gnbMenu" role="navigation">
      <ul>
        {routes.map((row: RouteType, i: number) => {
          const isActive = selected.indexOf(row.path!) > -1;
          return (
            <li key={i} className={clsx([!!isActive && 'active'])}>
              <button type="button" onClick={handleClick.bind(null, row)}>
                {row.label}
              </button>
              {/* <ul>
                {(row.children || []).map((col: RouteType, k: number) => {
                  return (
                    <li key={k}>
                    <button type="button" onClick={handleClick.bind(null, col)}>
                      {col.label}
                    </button>
                    <ul>
                      {(col.children || []).map((col2: RouteType, k2: number) => {
                        return (
                          <li key={k2}>
                            <NavLink to={`/${row.path}/${col.path}/${col2.path}`} replace>
                              {col2.label}
                            </NavLink>
                          </li>
                        );
                      })}
                    </ul>
                  </li>
                  );
                })}
              </ul> */}
            </li>
          );
        })}
      </ul>
    </nav>
  );
}

export default Navigation;
