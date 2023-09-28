import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {SearchParam, UseEquipAnalysisList} from "~/service/Model";
import {
  CustomHeadCell,
  SearchTable, TableComponents, TableDateTermCell,
  TableSelectCell,
  WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {Icons} from "shared/components/IconContainer";
import {dayFormat, todayEndTime, todayTime} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {AnalysisService} from "~/service/AnalysisMgt/AnalysisService";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";

/* 분석 환경 이용신청 목록 */
const UseEquipmentAnalysisMgt = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()

  return <TitleContents title={"분석환경 이용신청 목록"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_USAGE_ST", 'ANALS_USE_ST'])
  const [eqpmnStatus, setEqpmnStatus] = useState(['전체']);
  const [searchParam, setSearchParam] = useState<SearchParam>()
  const [search, setSearch] = useState(false)
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (search) {
      props.setSearch(searchParam!)
      // if (props.onClick) props.onClick(searchParam!)
      setSearch(false)
    }
  }, [search])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'ANALS_USE_ST')/*.filter(f => f !== '대기중').filter(f => f !== '사용가능')*/
      if (state.length > 0) setEqpmnStatus(['전체'].concat(state))
    }
  }, [commtCode])

  return <Box sx={{
    border: "1px solid #d7dae6",
    borderRadius: "20px",
  }}>
    <TableContainer>
      <SearchTable>
        <TableBody>
          <TableRow>
            <TableSelectCell
              label={"신청상태"}
              thWidth={"12%"} tdWidth={"38%"} tdSpan={3}
              selectLabel={eqpmnStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'ANALS_USE_ST', selected)
                setSearchParam({...searchParam, useSttus: sttus})
              }}/>
          </TableRow>
          <TableRow>
            <TableDateTermCell
              division label={"사용일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime, endTime) => {
                if (endTime) {
                  if (beginTime > endTime) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(beginTime))}\n종료일 : ${dayFormat(Number(endTime))}\n사용일자를 다시 입력 바랍니다.`
                    })
                  }
                }
                setSearchParam({...searchParam, useBeginDt: todayTime(new Date(beginTime)), useEndDt: todayEndTime(new Date(endTime))})
              }}/>
            <TableDateTermCell
              label={"신청일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime, endTime) => {
                if (endTime) {
                  if (beginTime > endTime) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(beginTime))}\n종료일 : ${dayFormat(Number(endTime))}\n신청일자를 다시 입력 바랍니다.`
                    })
                  }
                }
                setSearchParam({...searchParam, creatBeginDt: todayTime(new Date(beginTime)), creatEndDt: todayEndTime(new Date(endTime))})
              }}/>
          </TableRow>
          <TableRow>
            <TableCell colSpan={4} style={{textAlign: "center", borderBottom: "none"}}>
              <CustomButton label={"검색"} onClick={() => {
                if ((searchParam?.creatBeginDt && !searchParam?.creatEndDt) || (!searchParam?.creatBeginDt && searchParam?.creatEndDt)) {
                  addModal({open: true, isDist:true, content:'신청일자를 확인해주세요.'})
                  return
                }
                if ((searchParam?.useBeginDt && !searchParam?.useEndDt) || (!searchParam?.useBeginDt && searchParam?.useEndDt)) {
                  addModal({open: true, isDist:true, content:'사용일자를 확인해주세요.'})
                  return
                }
                if (searchParam?.creatBeginDt && searchParam?.creatEndDt) {
                  if (searchParam.creatBeginDt > searchParam.creatEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(searchParam.creatBeginDt))}\n종료일 : ${dayFormat(Number(searchParam.creatEndDt))}\n신청일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                if (searchParam?.useBeginDt && searchParam?.useEndDt) {
                  if (searchParam.useBeginDt > searchParam.useEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(searchParam.useBeginDt))}\n종료일 : ${dayFormat(Number(searchParam.useEndDt))}\n사용일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                //addModal({open:true, isDist:true, content:'준비중입니다.'})
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
  searchParam?: SearchParam
}> = props => {
  const {commtCode} = useCommtCode(["ANALS_USE_ST", 'EQPMN_PAYMENT', 'MEMBER_TYPE', 'ANALS_UNT_DIV'])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<UseEquipAnalysisList>[]>([]);
  const information = AnalysisService.getList({...pagination, ...props.searchParam});
  const navigation = useNavigate();
  const {addModal} = useGlobalModalStore()

  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m) => {
          return {
            key: m.reqstID,
            ...m
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  return <TableComponents<UseEquipAnalysisList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          //addModal({open: true, isDist:true, content:'준비중입니다.'})
           const res = await AnalysisService.getEquipAnalsExcelDownload(props.searchParam!);
           const blob = new Blob([res]);
           const fileObjectUrl = window.URL.createObjectURL(blob);
           const link = document.createElement("a");
           link.href = fileObjectUrl;
           link.setAttribute(
             "download",
             '분석환경신청_리스트.xlsx'
           );
           document.body.appendChild(link);
           link.click();
        }}/>
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      navigation('/tsp-admin/UseMgt/AnalysisMgt/' + key);
    }}
    tableCell={(data) => {
      let useSttus = ''
      let mberDiv = ''
      let untDiv = ''
      if (commtCode && data) {
        useSttus = toCommtCodeName(commtCode, "ANALS_USE_ST", data.useSttus)
        mberDiv = toCommtCodeName(commtCode, "MEMBER_TYPE", data.mberDiv)
        untDiv = toCommtCodeName(commtCode, "ANALS_UNT_DIV", data.analsUntDiv)
      }

      return (
        <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useSttus-" + data.key}>{useSttus}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"mberDiv-" + data.key}>{mberDiv}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"entrprsNm-" + data.key}>{data?.entrprsNm}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"assetsNo-" + data.key}>{untDiv}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useBeginDt-" + data.key}>{dayFormat(data?.useBeginDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useEndDt-" + data.key}>{dayFormat(data?.useEndDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"creatDt-" + data.key}>{dayFormat(data?.creatDt)}</TableCell>
        </Fragment>
      )
    }}
  />
}

const headCells: CustomHeadCell<UseEquipAnalysisList>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'useSttus',
    align: 'center',
    label: '신청상태',
  },
  {
    id: 'mberDiv',
    align: "center",
    label: '구분',
  },
  {
    id: 'entrprsNm',
    align: "center",
    label: '사업자명/이름',
  },
  {
    id: 'analsUntDiv',
    align: "center",
    label: '이용신청 타입',
  },
  {
    id: 'useBeginDt',
    align: "center",
    label: '이용시작일',
  },
  {
    id: 'useEndDt',
    align: "center",
    label: '이용종료일',
  },
  {
    id: 'creatDt',
    align: "center",
    label: '신청일시',
  },
];

/*
const analysTempData: WithCustomRowData<UseEquipAnalysisList>[] =
  [
    {
      key: '1',
      reqstSttus: '신청',
      mberDiv: '기업',
      entrprsNm: '(주)블루레몬',
      reqstType: 'CPU 3core RAM',
      useBeginDt: 1670895000000,
      useEndDt: 1672477200000,
      creatDt: 1661847904098
    },
    {
      key: '2',
      reqstSttus: '신청',
      mberDiv: '기업',
      entrprsNm: '(주)블루레몬',
      reqstType: 'CPU 3core RAM',
      useBeginDt: 1670895000000,
      useEndDt: 1672477200000,
      creatDt: 1661847904098
    }
  ]
*/


export default UseEquipmentAnalysisMgt;