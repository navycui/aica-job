import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react"
import {LoadingProgress, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {Box, Stack, TableBody, TableCell, TableRow} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  CustomHeadCell,
  SearchTable, TableComponents,
  TableSelectCell,
  TableTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {AskedQutParam, askedQutList} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {FrequentlyAskedQutService} from "~/pages/Operation/CustomerSupportMgt/Service/FrequentlyAskedQutService";
import {dayFormat} from "shared/utils/stringUtils";
import {sort} from "ramda";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useCommtCode} from "~/utils/useCommtCode";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

function FrequentlyAskedQut() {
  const [searchParam, setSearchParam] = useState<AskedQutParam>()

  return <TitleContents title={"자주묻는 질문"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"40px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<AskedQutParam | undefined>>
}> = props => {
  const [classify, setClassify] = useState(["전체"]);
  const [exhibition, setExhibition] = useState(["전체","전시","전시안함"])
  const [searchParam, setSearchParam] = useState<AskedQutParam>()
  const [search, setSearch] = useState(false)
  const {commtCode} = useCommtCode(['CATEGORY_QNA'])
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'CATEGORY_QNA')
      if (state.length > 0) setClassify(['전체'].concat(state))
    }
  }, [commtCode])

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
              division medium label={"분류"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={classify}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                const category = toCommtCode(commtCode, 'CATEGORY_QNA', selected)
                setSearchParam({...searchParam, categoryCd: category})
              }}/>
            <TableSelectCell
              medium label={"전시여부"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={exhibition}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                let select = selected === '전체'? undefined: selected === '전시'
                setSearchParam({...searchParam, posting: select})
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
  searchParam?: AskedQutParam
}> = props => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const {commtCode} = useCommtCode(['CATEGORY_QNA'])
  const [rowList, setRowList] = useState<WithCustomRowData<askedQutList>[]>();
  const navigation = useNavigate();
  const information = FrequentlyAskedQutService.getList({boardId:'tsp-qna', ...pagination, ...props.searchParam})

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

  if(!rowList) return <></>

  return <TableComponents<askedQutList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomButton
        label={"등록"}
        type={"small"}
        color={"list"}
        onClick={async () => {
          navigation('/tsp-admin/Operation/About/FAQ/Register')
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
      navigation(`/tsp-admin/Operation/About/FAQ/${key}`);
    }}
    tableCell={(data:WithCustomRowData<askedQutList>) => {
      let categoryCd = ''
      if (commtCode && data) {
        categoryCd = toCommtCodeName(commtCode, 'CATEGORY_QNA', data.categoryCd)
      }
      return <Fragment>
        <TableCell key={"number-" + data.key} sx={{textAlign: "center", width:'100px'}}>{data.rn}</TableCell>
        <TableCell key={"categoryCd-" + data.key} sx={{textAlign: "center", width:'180px'}}>{categoryCd}</TableCell>
        <TableCell key={"title-" + data.key} sx={{textAlign: "center"}}>{data.title}</TableCell>
        <TableCell key={"posting-" + data.key} sx={{textAlign: "center", width:'180px'}}>{data.posting ? '전시' : '전시안함'}</TableCell>
        <TableCell key={"createdDt-" + data.key} sx={{textAlign: "center", width:'220px'}}>{dayFormat(data.createdDt)}</TableCell>
      </Fragment>
    }}
  />
}

const headCells: CustomHeadCell<askedQutList & {count: number}>[] = [
  {
    id: 'rn',
    align: 'center',
    label: '번호',
  },
  {
    id: 'categoryCd',
    align: "center",
    label: '분류',
  },
  {
    id: 'title',
    align: "center",
    label: '제목',
  },
  {
    id: 'posting',
    align: "center",
    label: '전시여부',
  },
  {
    id: 'createdDt',
    align: "center",
    label: '등록일',
  },
];
export default FrequentlyAskedQut;