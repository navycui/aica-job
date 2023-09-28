import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {HorizontalInterval, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {CommonService} from '~/service/CommonService';
import {EqpmnUseReqstList, SearchParam} from '~/service/Model';
import {
  CustomHeadCell,
  SearchTable,
  TableComponents,
  TableDateTermCell,
  TableRadioCell,
  TableSelectCell,
  TableSelectTextFieldCell,
  WithCustomRowData
} from '~/../../shared/src/components/TableComponents';
import {CustomButton, CustomIconButton} from '~/../../shared/src/components/ButtonComponents';
import {useNavigate} from 'react-router-dom';
import {Icons} from '~/../../shared/src/components/IconContainer';
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {dayFormat, todayEndTime, todayTime, toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 장비 신청 관리 */
const UseEquipmentApplyMgt = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()
  return <TitleContents title={"장비 신청 관리"}>
    <SearchBox setSearch={setSearchParam}/>
    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}


const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'EQPMN_PAYMENT'])
  const [eqpmnStatus, setEqpmnStatus] = useState(['전체']);
  const [pymnt, setPymnt] = useState(['전체']);
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
      const state = CommtCodeNms(commtCode, 'EQPMN_REQST_ST').filter(f => f !== '견적서발급')
      const pymnt = CommtCodeNms(commtCode, 'EQPMN_PAYMENT')
      if (state.length > 0) setEqpmnStatus(['전체'].concat(state))
      if (pymnt.length > 0) setPymnt(['전체'].concat(pymnt))
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
              label={"신청 상태"}
              thWidth={"12%"} tdWidth={"38%"} tdSpan={3}
              selectLabel={eqpmnStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'EQPMN_REQST_ST', selected)
                setSearchParam({...searchParam, reqstSttus: sttus})
              }}/>

          </TableRow>
          <TableRow>
            <TableDateTermCell
              division label={"신청일"}
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
                setSearchParam({
                  ...searchParam,
                  creatBeginDt: todayTime(new Date(beginTime)),
                  creatEndDt: todayEndTime(new Date(endTime))
                })
              }}/>
            <TableDateTermCell
              label={"사용일"} tdSpan={3}
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
                setSearchParam({
                  ...searchParam,
                  useBeginDt: todayTime(new Date(beginTime)),
                  useEndDt: todayEndTime(new Date(endTime))
                })
              }}/>
            {/*<TableDateTermCell
              label={"사용종료일"}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(beginTime,endTime) => {
                // setSearchParam({...searchParam, eqpmnNm: selected})
              }}/>*/}
          </TableRow>
          <TableRow>
            <TableRadioCell
              division row label={"반출여부"} radioLabel={["전체", "반출", "미반출"]}
              thWidth={"12%"} tdWidth={"38%"} defaultLabel={"전체"}
              onClick={(selected: string) => {
                let tkout: boolean | undefined = undefined;
                if (selected === "반출") tkout = true
                else if (selected === "미반출") tkout = false
                setSearchParam({...searchParam, tkoutAt: tkout})
              }}/>
            <TableRadioCell
              row label={"지불방법"} radioLabel={pymnt}
              thWidth={"12%"} tdWidth={"38%"} defaultLabel={"전체"}
              onClick={(selected) => {
                const pymnt = toCommtCode(commtCode, 'EQPMN_PAYMENT', selected)
                setSearchParam({...searchParam, pymntMth: pymnt})
              }}/>
          </TableRow>

          <TableRow>
            <TableSelectTextFieldCell
              label={"키워드검색"} tdSpan={3}
              selectLabel={["장비명", "사업자명/이름", "접수번호", "자산번호"]}
              defaultLabel={searchText}
              onClick={(selected: string) => {
                setSearchKeyword(selected)
                setSearchText("")
                setSearchParam({...searchParam, eqpmnNmKorean: '', entrprsNm: '', assetsNo: '', rceptNo: ''})
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
                  case "접수번호":
                    setSearchParam({...searchParam, rceptNo: searchText})
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
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'EQPMN_PAYMENT', 'MEMBER_TYPE'])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnUseReqstList>[]>([]);
  const information = EquipmentService.getEquipApplyList({...pagination, ...props.searchParam});
  const navigation = useNavigate();

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

  return <TableComponents<EqpmnUseReqstList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await EquipmentService.getApplyEquipExcelDownload(props.searchParam!)
          const blob = new Blob([res]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.setAttribute(
            "download",
            `장비신청관리_리스트.xlsx`
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
      navigation('/tsp-admin/UseMgt/EquipmentMgt/ApplyMgt/' + key);
    }}
    tableCell={(data) => {
      let reqstSttus = ''
      let pymntMth = ''
      let mberDiv = ''
      if (commtCode && data) {
        reqstSttus = toCommtCodeName(commtCode, "EQPMN_REQST_ST", data.reqstSttus)
        pymntMth = toCommtCodeName(commtCode, "EQPMN_PAYMENT", data.pymntMth)
        mberDiv = toCommtCodeName(commtCode, "MEMBER_TYPE", data.mberDiv)
      }

      return (
        <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"entrprsNm-" + data.key}>{data?.entrprsNm}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"eqpmnNmKorean-" + data.key}>{data?.eqpmnNmKorean}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useBeginDt-" + data.key}>{dayFormat(data?.useBeginDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useEndDt-" + data.key}>{dayFormat(data?.useEndDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"tkout-" + data.key}>{data.tkoutAt ? '반출' : '미반출'}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"reqstSttus-" + data.key}>{reqstSttus}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"creatDt-" + data.key}>{dayFormat(data?.creatDt)}</TableCell>
          {/*<TableCell sx={{textAlign:'center'}} key={"mberDiv-" + data.key}>{mberDiv}</TableCell>
          <TableCell sx={{textAlign:'center'}} key={"assetsNo-" + data.key}>{data?.assetsNo}</TableCell>
          <TableCell sx={{textAlign:'center'}} key={"pymntMth-" + data.key}>{pymntMth}</TableCell>
          <TableCell sx={{textAlign:'center'}} key={"rceptNo-" + data.key}>{data?.rceptNo}</TableCell>*/}
        </Fragment>
      )
    }}
  />
}

const headCells: CustomHeadCell<EqpmnUseReqstList>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'entrprsNm',
    align: "center",
    label: '사업자명/이름',
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
    id: 'tkoutAt',
    align: "center",
    label: '반출여부',
  },
  {
    id: 'reqstSttus',
    align: 'center',
    label: '신청상태',
  },
  {
    id: 'creatDt',
    align: "center",
    label: '신청일시',
  },
  /*{
    id: 'mberDiv',
    align: "center",
    label: '구분',
  },
  {
    id: 'assetsNo',
    align: "center",
    label: '자산번호',
  },
  {
    id: 'pymntMth',
    align: "center",
    label: '지불방법',
  },
  {
    id: 'rceptNo',
    align: "center",
    label: '접수번호',
  },*/
];

export default UseEquipmentApplyMgt;