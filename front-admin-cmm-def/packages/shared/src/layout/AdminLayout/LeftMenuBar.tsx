import * as R from 'ramda';
import useSWR from 'swr';
import {useLocation, useNavigate} from 'react-router-dom';
import Box from '@mui/material/Box';
import {RouteData, RouteType} from "../../utils/RouteUtiles";
import {Collapse, List, ListSubheader} from "@mui/material";
import {LnbListItem} from "../../components/LayoutComponents";
import React, {Fragment, useEffect, useState} from 'react'
import './AdminLayout.scss';
import {useRouteStore} from "../../Store/RouteConfig";
import {useQueryClient} from "react-query";

export const LeftMenuBar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [selectedIds, setSelectedIds] = useState<string[]>([])
  const [currentPageId, setCurrentPageId] = useState<string>("")
  const client = useQueryClient()
  // const record = client.getQueryData<RouteData[]>('record') || []
  // const routes = client.getQueryData<RouteData[]>('routes') || []
  const {record,routes} = useRouteStore()

  const FindParent = (route: RouteData[], parentId: string, selectedIds: string[]) => {
    const find = route.find(f => f.menuId == parentId)
    if (find) {
      FindParent(route, find.parentMenuId, [...selectedIds, find.menuId])
    } else if (!find || parentId == "ROOT") {
      // 중복값 제거해서 set
      setSelectedIds(selectedIds.filter((f,i) => selectedIds.indexOf(f) == i))
    }
  }

  const FindLeftMenu = (pathName: string) => {
    const find = record.find(f => f.url == pathName)

    if (find) {
      FindParent(record, find.parentMenuId, [...selectedIds])
      setCurrentPageId(find.menuId)
      return true;
    }

    return false;
  }

  useEffect(() => {
    if (location) {
      if (location.pathname != '/') {
        const result = FindLeftMenu(location.pathname)

        if (!result){
          let path = location.pathname.split('/');
          let count = path[path.length - 1].length;
          const pathName = location.pathname.slice(0, (count + 1) * -1)

          FindLeftMenu(pathName)
        }
      } else if (location.pathname == '/') {
        setCurrentPageId("")
      }
    }
  }, [location])

  // const {data: record = []} = useSWR<RouteData[]>('route://record');
  // const {data: routes = []} = useSWR('route://service');
  // const {routes,record} = useRouteStore()
  const nav = R.find((v: RouteType) =>
    new RegExp(`^${v.path}`).test(location.pathname)
  )(routes);

  const handlerNavigate = (url: string) => {
    navigate(url);
  };

  const handlerCheck = (id: string) => {
    const selectedIndex = selectedIds.indexOf(id);
    let newSelected: string[] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selectedIds, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selectedIds.slice(1));
    } else if (selectedIndex === selectedIds.length - 1) {
      newSelected = newSelected.concat(selectedIds.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selectedIds.slice(0, selectedIndex),
        selectedIds.slice(selectedIndex + 1),
      );
    }
    setSelectedIds(newSelected);
  }

  //* 메뉴 아이템에 active class 추가
  return <Box className='leftMenu'>
    <List
      sx={{width: "100%", padding: 0}}
      component="nav"
    >
      <Item
        key={"Routes - Items"}
        routes={routes}
        depth={1}
        selectedIds={selectedIds}
        currentPageId={currentPageId}
        onClick={(id, url, isChildren) => {
          const path = "/tsp-admin"+url
          if (isChildren) {
            handlerCheck(id)
          } else {
            handlerNavigate(path)
            setCurrentPageId(id)
          }
        }}
      />
    </List>
  </Box>
}

const Item: React.FC<{
  routes: any,
  depth: number,
  selectedIds: string[],
  currentPageId: string,
  onClick: (id: string, url: string, isChildren: boolean) => void
}> = props => {

  return (
    //@ts-ignore
    props.routes && props.routes.map((m, i) => {
      const isActive = props.selectedIds.includes(m.menuId)
      const last = !m.children
      let collapseStyleClass = undefined;
      if (!last)
        collapseStyleClass = m.children.at(0).children ? "Collapse-middle" : "Collapse-last"

      return <Fragment key={i}>
        {/*<LnbArrow isActive={false}/>*/}
        <LnbListItem
          key={m.menuId}
          active={isActive || props.currentPageId == m.menuId}
          id={m.menuId}
          url={m.url}
          isChild={!!m.children}
          label={m.menuNm}
          depth={props.depth}
          onClick={props.onClick}
        />
        {
          !last && <Collapse
            key={"collapse - " + m.menuId}
            className={collapseStyleClass}
            in={props.selectedIds.includes(m.menuId)}
            timeout="auto" unmountOnExit>
            <Item
              key={m.children}
              currentPageId={props.currentPageId}
              routes={m.children}
              depth={props.depth + 1}
              selectedIds={props.selectedIds}
              onClick={props.onClick}/>
          </Collapse>
        }
      </Fragment>
    })
  )
}
