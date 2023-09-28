import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react"
import {LoadingProgress, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {Box, Stack, TableBody, TableCell, TableRow} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  CustomHeadCell,
  SearchTable, TableComponents,
  TableDateTermCell,
  TableSelectCell,
  TableTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {
  BoardsSearchParam,
  NoticeList,
  NoticeListData
} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {NoticeInfoMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/NoticeInfoMgtService";
import {dayFormat, todayEndTime, todayTime} from "shared/utils/stringUtils";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

function NoticeInfoMgt() {
  const [searchParam, setSearchParam] = useState<BoardsSearchParam>()

  return <TitleContents title={"공지사항 관리"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"40px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<BoardsSearchParam | undefined>>
}> = props => {
  const [exhibition, setExhibition] = useState(["전체","전시","전시안함"])
  const [searchParam, setSearchParam] = useState<BoardsSearchParam>()
  const [search, setSearch] = useState(false)
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (search) {
      props.setSearch(searchParam!)
      setSearch(false)
    }
  }, [search])

  return <Box sx={{
    border: "1px solid #d7dae6",
    borderRadius: "20px",
  }}>
    <TableContainer>
      <SearchTable>
        <TableBody>
          <TableRow>
            <TableSelectCell
              division medium label={"전시여부"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={exhibition}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                setSearchParam({...searchParam, posting: selected === '전체' ? undefined : selected === '전시'})
              }}/>
            <TableDateTermCell
              division label={"등록일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime, endTime) => {
                if (endTime) {
                  if (beginTime > endTime) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(beginTime))}\n종료일 : ${dayFormat(Number(endTime))}\n등록일자를 다시 입력 바랍니다.`
                    })
                  }
                }
                setSearchParam({...searchParam, beginDt: todayTime(new Date(beginTime)), endDt: todayEndTime(new Date(endTime))})
              }}/>
          </TableRow>
          <TableRow>
            <TableTextFieldCell
              label={"제목"} defaultLabel={""} tdSpan={3} wordCount={100}
              thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
                setSearchParam({...searchParam, title: text})
            }}
            />
          </TableRow>
          <TableRow>
            <TableCell colSpan={4} style={{textAlign: "center", borderBottom: "none"}}>
              <CustomButton label={"검색"} onClick={() => {
                if ((searchParam?.beginDt && !searchParam?.endDt) || (!searchParam?.beginDt && searchParam?.endDt)) {
                  addModal({open: true, isDist:true, content:'등록일을 확인해주세요.'})
                  return
                }
                setSearch(true);
              }}/>
            </TableCell>
          </TableRow>
        </TableBody>
      </SearchTable>
    </TableContainer>
  </Box>
}

const ListView: React.FC<{
  searchParam?: BoardsSearchParam
}> = props => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<NoticeList>[]>([]);
  const navigation = useNavigate();
  const information = NoticeInfoMgtService.getNoticeList({boardId:'tsp-notice', ...pagination, ...props.searchParam})

  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m,) => {
          return {
            key: m.articleId,
            ...m,
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  return <TableComponents<NoticeList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomButton
        label={"등록"}
        type={"small"}
        color={"list"}
        onClick={async () => {
          navigation('/tsp-admin/Operation/About/Notice/Register')
        }}
      />
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key) => {
      navigation(`/tsp-admin/Operation/About/Notice/${key}`);
    }}
    tableCell={(data:WithCustomRowData<NoticeList>) => {
      return <Fragment>
        <TableCell key={"number-" + data.key} sx={{textAlign:'center', width:'100px'}}>{data.notice ? '-' : data.rn}</TableCell>
        <TableCell key={"posting-" + data.key} sx={{textAlign:'center', width:'180px'}}>{data.posting ? '전시' : '전시안함'}</TableCell>
        <TableCell key={"title-" + data.key} sx={{textAlign:'center'}}>{data.title}</TableCell>
        <TableCell key={"notice-" + data.key} sx={{textAlign:'center', width:'180px'}}>{data.notice?"고정":"고정안함"}</TableCell>
        <TableCell key={"createdDt-" + data.key} sx={{textAlign:'center', width:'220px'}}>{dayFormat(data.createdDt)}</TableCell>
      </Fragment>
    }}
  />
}

const headCells: CustomHeadCell<NoticeListData & {count: number}>[] = [
  {
    id: 'rn',
    align: "center",
    label: '번호',
  },
  {
    id: 'posting',
    align: "center",
    label: '전시여부',
  },
  {
    id: 'title',
    align: "center",
    label: '제목',
  },
  {
    id: 'notice',
    align: "center",
    label: '고정여부',
  },
  {
    id: 'createDt',
    align: "center",
    label: '등록일',
  },
];
export default NoticeInfoMgt;