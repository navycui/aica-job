import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {EqpmnReportList, EqpmnUseReqstList, SearchParam} from "~/service/Model";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {
  CustomHeadCell,
  SearchTable, TableComponents,
  TableDateTermCell,
  TableRadioCell,
  TableSelectCell,
  TableSelectTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {dayFormat, todayEndTime, todayTime, toTimeFormat} from "shared/utils/stringUtils";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {useNavigate} from "react-router-dom";
import {Icons} from "shared/components/IconContainer";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 결과 보고서 관리 */
const UseEquipmentReportMgt = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()
  return <TitleContents title={"결과 보고서 관리"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_RESULT_REPORT_ST"])
  const [eqpmnStatus, setEqpmnStatus] = useState(['전체']);
  const [searchParam, setSearchParam] = useState<SearchParam>()
  const [searchKeyword, setSearchKeyword] = useState("")
  const [searchText, setSearchText] = useState("")
  const [search, setSearch] = useState(false)
  const {addModal} = useGlobalModalStore();

  useEffect(() => {
    if (search) {
      props.setSearch(searchParam!)
      setSearch(false)
    }
  }, [search])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'EQPMN_RESULT_REPORT_ST')
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
              division label={"처리 상태"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={eqpmnStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'EQPMN_RESULT_REPORT_ST', selected)
                setSearchParam({...searchParam, reprtSttus: sttus})
              }}/>
            <TableDateTermCell
              label={"제출일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime, endTime) => {
                if (endTime) {
                  if (beginTime > endTime) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(beginTime))}\n종료일 : ${dayFormat(Number(endTime))}\n제출일자를 다시 입력 바랍니다.`
                    })
                  }
                }
                setSearchParam({
                  ...searchParam,
                  creatBeginDt: todayTime(new Date(beginTime)),
                  creatEndDt: todayEndTime(new Date(endTime))
                })
              }}/>
          </TableRow>
          <TableRow>
            <TableSelectTextFieldCell
              label={"키워드검색"} tdSpan={3}
              selectLabel={["장비명", "사업자명/이름", '자산번호']}
              defaultLabel={searchText}
              onClick={(selected: string) => {
                setSearchKeyword(selected)
                setSearchText("")
                setSearchParam({...searchParam, eqpmnNmKorean: '', entrprsNm: '', assetsNo: ''})
              }}
              onChange={(text: string) => {
                setSearchText(text)
                if (100 <= searchText.length) {
                  const idx = (100 - searchText.length) < 0 ? 100 - searchText.length : -1;
                  setSearchText(searchText.slice(0, idx));
                  alert('입력한 검색어가 100글자를 초과하였습니다.\n검색어를 다시 입력해주세요.')
                }
              }}
            />
          </TableRow>
          <TableRow>
            <TableCell colSpan={4} style={{textAlign: "center", borderBottom: "none"}}>
              <CustomButton label={"검색"} onClick={() => {
                if ((searchParam?.creatBeginDt && !searchParam?.creatEndDt) || (!searchParam?.creatBeginDt && searchParam?.creatEndDt)) {
                  addModal({open: true, isDist:true, content:'제출일자를 확인해주세요.'})
                  return
                }
                if (searchParam?.creatBeginDt && searchParam?.creatEndDt) {
                  if (searchParam.creatBeginDt > searchParam.creatEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(searchParam.creatBeginDt))}\n종료일 : ${dayFormat(Number(searchParam.creatEndDt))}\n제출일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                switch (searchKeyword) {
                  case "장비명":
                    setSearchParam({...searchParam, eqpmnNmKorean: searchText})
                    break;
                  case "사업자명/이름":
                    setSearchParam({...searchParam, entrprsNm: searchText})
                    break;
                  case "자산번호":
                    setSearchParam({...searchParam, assetsNo: searchText})
                    break;
                  default:
                    break;
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
  searchParam?: SearchParam
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_RESULT_REPORT_ST", 'MEMBER_TYPE'])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnReportList>[]>([]);
  const information = EquipmentService.getReprtList({...pagination, ...props.searchParam});
  const navigation = useNavigate();

  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m) => {
          return {
            key: m.reprtId,
            ...m
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  return <TableComponents<EqpmnReportList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await EquipmentService.getReprtExcelDownload()
          const blob = new Blob([res]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.setAttribute(
            "download",
            `장비결과보고서_리스트.xlsx`
          );
          document.body.appendChild(link);
          link.click();
          // await EquipmentInformationService.getEquipmentListExcelDownload().then((res) => {
          //   const blob = new Blob([res]);
          //   const fileObjectUrl = window.URL.createObjectURL(blob);
          //   const link = document.createElement("a");
          //   link.href = fileObjectUrl;
          //   link.setAttribute(
          //     "download",
          //     `장비정보관리_리스트.xlsx`
          //   );
          //   document.body.appendChild(link);
          //   link.click();
          // });
        }}/>
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      navigation('/tsp-admin/UseMgt/EquipmentMgt/ReportMgt/' + key);
    }}
    tableCell={(data) => {
      let reprtSttus = ''
      let mberDiv = ''
      if (commtCode && data) {
        reprtSttus = toCommtCodeName(commtCode, "EQPMN_RESULT_REPORT_ST", data.reprtSttus)
        mberDiv = toCommtCodeName(commtCode, "MEMBER_TYPE", data.mberDiv)
      }

      return (
        <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"reqstSttus-" + data.key}>{reprtSttus}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"mberDiv-" + data.key}>{mberDiv}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"entrprsNm-" + data.key}>{data?.entrprsNm}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"assetsNo-" + data.key}>{data?.assetsNo}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"eqpmnNmKorean-" + data.key}>{data?.eqpmnNmKorean}</TableCell>
          <TableCell sx={{textAlign: 'center'}}
                     key={"useBeginDt-" + data.key}>{toTimeFormat(data?.useBeginDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useEndDt-" + data.key}>{toTimeFormat(data?.useEndDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"rceptNo-" + data.key}>{dayFormat(data?.creatDt)}</TableCell>
        </Fragment>
      )
    }}
  />
}

const headCells: CustomHeadCell<EqpmnReportList>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'reprtSttus',
    align: 'center',
    label: '처리상태',
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
    id: 'assetsNo',
    align: "center",
    label: '자산번호',
  },
  {
    id: 'eqpmnNmKorean',
    align: "center",
    label: '장비명',
  },
  {
    id: 'useBeginDt',
    align: "center",
    label: '사용시작일',
  },
  {
    id: 'useEndDt',
    align: "center",
    label: '사용종료일',
  },
  {
    id: 'creatDt',
    align: "center",
    label: '제출일',
  },
];

export default UseEquipmentReportMgt;