import * as R from 'ramda';
import useSWR from 'swr';
import clsx from 'clsx';
import {useLocation, useNavigate} from 'react-router-dom';
import TreeView from '@mui/lab/TreeView';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import Typography from '@mui/material/Typography';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import TreeItem from '@mui/lab/TreeItem';
import Box from '@mui/material/Box';
import {RouteType} from "../../../../../utils/RouteUtiles";
import {Collapse, List, ListSubheader} from "@mui/material";
import {LnbListItem} from "../../../../../components/LayoutComponents";
import React, {Fragment, useState} from 'react'

function LNB() {
  const location = useLocation();
  const navigate = useNavigate();
  const [selectedIds, setSelectedIds] = useState<string[]>([])
  const [currentPageId, setCurrentPageId] = useState<string>("")

  const {data: routes = []} = useSWR('route://service');
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
  return <Box className="leftMenu">
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
          if (isChildren) {
            handlerCheck(id)
          } else {
            handlerNavigate(url)
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
      return <Fragment key={i}>
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
          m.children?.length > 0 &&
          <Collapse
            key={"collapse - " + m.menuId}
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


// routes ? (
//   <Box className="leftMenu">
//     <div className="m_tit">교육관리</div>
//     <Accordion>
//       <AccordionSummary
//         expandIcon={<ExpandMoreIcon />}
//         aria-controls="panel1a-content"
//         id="panel1a-header"
//       >
//         <Typography>사업정보관리</Typography>
//       </AccordionSummary>
//       <AccordionDetails>
//         <TreeView
//           aria-label="file system navigator"
//           defaultCollapseIcon={<RemoveIcon />}
//           defaultExpandIcon={<AddIcon />}
//           sx={{ flexGrow: 1, maxWidth: 400, overflowY: 'auto' }}
//         >
//           <TreeItem nodeId="1" label="사업정보관리">
//             <TreeItem nodeId="2" label="사업정보관리" />
//             <TreeItem nodeId="3" label="기준사업관리" />
//             <TreeItem nodeId="4" label="기준사업정보관리" />
//             <TreeItem nodeId="5" label="기준사업분류관리" />
//             <TreeItem nodeId="6" label="사업비비모관리" />
//           </TreeItem>
//           <TreeItem nodeId="7" label="사업현황관리">
//             <TreeItem nodeId="8" label="업체현황조회" />
//           </TreeItem>
//         </TreeView>
//       </AccordionDetails>
//     </Accordion>
//     <Accordion>
//       <AccordionSummary
//         expandIcon={<ExpandMoreIcon />}
//         aria-controls="panel2a-content"
//         id="panel2a-header"
//       >
//         <Typography>사업현황관리</Typography>
//       </AccordionSummary>
//       <AccordionDetails>
//         <TreeView
//           aria-label="file system navigator"
//           defaultCollapseIcon={<RemoveIcon />}
//           defaultExpandIcon={<AddIcon />}
//           sx={{ flexGrow: 1, maxWidth: 400, overflowY: 'auto' }}
//         >
//           <TreeItem nodeId="1" label="업체현황조회"></TreeItem>
//         </TreeView>
//       </AccordionDetails>
//     </Accordion>
//     {/*<TreeView
//         defaultCollapseIcon={<ExpandMoreIcon />}
//         defaultExpandIcon={<ChevronRightIcon />}
//         // tobe ... start
//         defaultExpanded={['menu-ADM010000', 'menu-ADM010100', 'menu-ADM010101']}
//         defaultSelected={['menu-ADM010101']}
//         // expanded={expanded}
//         // selected={selected}
//         // onNodeToggle={handleToggle}
//         // onNodeSelect={handleSelect}
//         // tobe ... end
//       >
//         {routes.map((item: RouteType, i: number) => {
//           return (
//             <TreeItem
//               key={item.path}
//               nodeId={item.menuId}
//               label={item.label}
//               className={clsx(nav?.path === item.path && 'active')}
//               sx={{ mt: 1 }}
//             >
//               {(item.children || []).map((item1: RouteType, k: number) => {
//                 return (
//                   <TreeItem
//                     key={item1.path}
//                     nodeId={item1.menuId}
//                     label={item1.label}
//                     className={clsx(nav?.path === item1.path && 'active')}
//                   >
//                     {(item1.children || []).map(
//                       (item2: RouteType, y: number) => {
//                         return (
//                           <TreeItem
//                             key={item2.path}
//                             nodeId={item2.menuId}
//                             label={item2.label}
//                             className={clsx(
//                               location?.pathname === item2.path && 'active'
//                             )}
//                             onClick={handleClick.bind(null, item2.path!)}
//                           ></TreeItem>
//                         );
//                       }
//                     )}
//                   </TreeItem>
//                 );
//               })}
//             </TreeItem>
//           );
//         })}
//       </TreeView>
//       */}
//   </Box>
// ) : null;

export default LNB;
