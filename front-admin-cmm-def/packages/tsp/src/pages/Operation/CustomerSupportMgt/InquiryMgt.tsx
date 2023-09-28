import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from "react";
import {TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {
  OneByOneQuest,
  OneByOneSearchParam
} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {Box, TableBody, TableCell, TableRow} from "@mui/material";
import TableContainer from "@mui/material/TableContainer";
import {
  CustomHeadCell,
  SearchTable,
  TableComponents,
  TableSelectCell,
  TableTextFieldCell,
  WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {dayFormat} from "shared/utils/stringUtils";
import {InquiryMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/InquiryMgtService";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

function InquiryMgt() {
  const [searchParam, setSearchParam] = useState<OneByOneSearchParam>({qnaId:'tsp-persnal'} as OneByOneSearchParam)

  return <TitleContents title={'1:1문의 관리'}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={'40px'}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<OneByOneSearchParam>>
}> = props => {
  const [classify, setClassify] = useState(["전체"]);
  const [exhibition, setExhibition] = useState(["전체"])
  const [searchParam, setSearchParam] = useState<OneByOneSearchParam>({qnaId:'tsp-persnal'} as OneByOneSearchParam)
  const [search, setSearch] = useState(false)
  const {commtCode} = useCommtCode(['QUEST_STATUS','CATEGORY_PERSNAL'])
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'QUEST_STATUS')
      const state1 = CommtCodeNms(commtCode, 'CATEGORY_PERSNAL')
      if (state.length > 0) setClassify(['전체'].concat(state))
      if( state1.length > 0) setExhibition(['전체'].concat(state1))
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
              division medium label={"처리상태"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={classify}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                const category = toCommtCode(commtCode, 'QUEST_STATUS', selected)
                setSearchParam({...searchParam, questStatus: category})
              }}/>
            <TableSelectCell
              medium label={"문의구분"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={exhibition}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                const category = toCommtCode(commtCode, 'CATEGORY_PERSNAL', selected)
                setSearchParam({...searchParam, categoryCd: category})
              }}/>
          </TableRow>
          <TableRow>
            <TableTextFieldCell
              division label={"제목"} defaultLabel={""} wordCount={100}
              thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
              setSearchParam({...searchParam, title: text})
            }}/>
            <TableTextFieldCell
              label={"회원명"} defaultLabel={""} wordCount={100}
              thWidth={"13%"} tdWidth={"21%"} onChange={(text) => {
              setSearchParam({...searchParam, memberNm: text})
            }}/>
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
  searchParam: OneByOneSearchParam
}> = props => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const {commtCode} = useCommtCode(['QUEST_STATUS','CATEGORY_PERSNAL'])
  const [rowList, setRowList] = useState<WithCustomRowData<OneByOneQuest>[]>();
  const navigation = useNavigate();
  const information = InquiryMgtService.getList({...props.searchParam, ...pagination})

  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m,) => {
          return {
            key: m.questId,
            ...m,
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  if(!rowList) return <></>

  return <TableComponents<OneByOneQuest>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key) => {
      navigation(`/tsp-admin/Operation/About/Inquiry/${key}`);
    }}
    tableCell={(data:WithCustomRowData<OneByOneQuest>) => {
      let categoryCd = ''
      if (commtCode && data) {
        categoryCd = toCommtCodeName(commtCode, 'CATEGORY_PERSNAL', data.categoryCd)
      }
      return <Fragment>
        <TableCell align={'center'} key={"rn-" + data.key}>{data.rn}</TableCell>
        <TableCell align={'center'} key={"questStNm-" + data.key}>{data.questStNm}</TableCell>
        <TableCell align={'center'} key={"categoryNm-" + data.key}>{categoryCd}</TableCell>
        <TableCell align={'center'} key={"title-" + data.key}>{data.title}</TableCell>
        <TableCell align={'center'} key={"questionerNm-" + data.key}>{data.questionerNm}</TableCell>
        <TableCell align={'center'} key={"answererNm-" + data.key}>{data.answererNm || '-'}</TableCell>
        <TableCell align={'center'} key={"questCreatedDt-" + data.key}>{dayFormat(data.questCreatedDt)}</TableCell>
      </Fragment>
    }}
  />
}

const headCells: CustomHeadCell<OneByOneQuest>[] = [
  {
    id: 'rn',
    align: 'center',
    label: '번호',
  },
  {
    id: 'questSt',
    align: "center",
    label: '처리상태',
  },
  {
    id: 'question',
    align: "center",
    label: '문의구분',
  },
  {
    id: 'title',
    align: "center",
    label: '제목',
  },
  {
    id: 'questionerNm',
    align: "center",
    label: '회원명',
  },
  {
    id: 'answererNm',
    align: "center",
    label: '담당자',
  },
  {
    id: 'questCreatedDt',
    align: "center",
    label: '접수일',
  },
];

export default InquiryMgt