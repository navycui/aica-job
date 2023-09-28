import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {EqpmnUseList, SearchParam, StatusState} from "~/service/Model";
import {
  CustomHeadCell,
  SearchTable, TableComponents,
  TableDateTermCell, TableRadioCell,
  TableSelectCell,
  TableSelectTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {dayFormat, todayEndTime, todayTime} from "shared/utils/stringUtils";
import {useNavigate} from "react-router-dom";
import {Icons} from "shared/components/IconContainer";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 장비 사용 관리 */
const UseEquipmentMgt = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()

  return <TitleContents title={"장비 사용 관리"}>
    <SearchBox setSearch={setSearchParam}/>

    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>

  </TitleContents>
}
const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_USAGE_ST", 'EQPMN_PAYMENT'])
  const [eqpmnStatus, setEqpmnStatus] = useState(["전체"]);
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
      const state = CommtCodeNms(commtCode, 'EQPMN_USAGE_ST').filter(f => f !== '대기중')
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
              label={"사용상태"}
              thWidth={"12%"} tdWidth={"38%"} tdSpan={3}
              selectLabel={eqpmnStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'EQPMN_USAGE_ST', selected)
                setSearchParam({...searchParam, useSttus: sttus})
              }}/>
          </TableRow>
          <TableRow>
            <TableDateTermCell
              division type={"Date"} label={"사용일"} thWidth={"12%"} tdWidth={"38%"}
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
              }}
            />
            <TableRadioCell
              row label={"반출여부"} radioLabel={["전체", "반출", "미반출"]}
              thWidth={"12%"} tdWidth={"38%"} defaultLabel={"전체"}
              onClick={(selected) => {
                let tkout: boolean | undefined;
                if (selected === "반출") tkout = true
                else if (selected === "미반출") tkout = false
                else tkout = undefined
                setSearchParam({...searchParam, tkoutAt: tkout})
              }}/>
          </TableRow>
          <TableRow>
            <TableRadioCell
              division row label={"반입여부"} radioLabel={["전체", "반입전", "반입완료"]}
              thWidth={"12%"} tdWidth={"38%"} defaultLabel={"전체"}
              onClick={(selected) => {
                let tkin: boolean | undefined;
                if (selected === "반입전") tkin = false
                else if (selected === "반입완료") tkin = true
                else tkin = undefined
                setSearchParam({...searchParam, tkinAt: tkin})
              }}/>
            <TableRadioCell
              row label={"지불방법"} radioLabel={pymnt}
              thWidth={"12%"} tdWidth={"38%"} defaultLabel={"전체"}
              onClick={(selected) => {
                const pymnt = toCommtCode(commtCode, 'EQPMN_PAYMENT', selected)
                setSearchParam({...searchParam, pymntMth: pymnt})
              }}/>
          </TableRow>

          {/*<TableDateTermCell
                type={"Date"} label={"사용종료일"} thWidth={"12%"} tdWidth={"38%"}
                onChange={(beginTime, endTime) => {
                }}
            />*/}
          <TableRow>
            <TableSelectTextFieldCell
              label={"키워드검색"} tdSpan={3}
              selectLabel={['장비명', '사업자명/이름', '접수번호', '자산번호']}
              defaultLabel={searchText}
              onClick={(selected: string) => {
                setSearchKeyword(selected)
                setSearchText("")
                setSearchParam({...searchParam, assetsNo: "", entrprsNm: "", rceptNo: '', eqpmnNmKorean: ''})
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
                if ((searchParam?.useBeginDt && !searchParam?.useEndDt) || (!searchParam?.useBeginDt && searchParam?.useEndDt)) {
                  addModal({open: true, isDist:true, content:'사용일자를 확인해주세요.'})
                  return
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
                  case "자산번호":
                    setSearchParam({...searchParam, assetsNo: searchText})
                    break;
                  case "사업자명/이름":
                    setSearchParam({...searchParam, entrprsNm: searchText})
                    break;
                  case "접수번호":
                    setSearchParam({...searchParam, rceptNo: searchText})
                    break;
                  case "장비명":
                    setSearchParam({...searchParam, eqpmnNmKorean: searchText})
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
  const {commtCode} = useCommtCode(["EQPMN_USAGE_ST", 'EQPMN_PAYMENT', 'MEMBER_TYPE', 'EQPMN_REQST_ST'])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnUseList>[]>([]);

  // const clone = {...props.searchParam};
  // const {tkoutAt, ...rest} = clone;
  // const queries = {...rest, ...tkoutAt !== undefined && {tkoutAt}};

  const information = EquipmentService.getEquipUseList({...pagination, ...props.searchParam});
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
//
  return <TableComponents<EqpmnUseList>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await EquipmentService.getUseExcelDownload(props.searchParam!);
          const blob = new Blob([res]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.setAttribute(
            "download",
            `장비사용관리_리스트.xlsx`
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
      navigation('/tsp-admin/UseMgt/EquipmentMgt/UseMgt/' + key);
    }}
    tableCell={(data) => {
      let useSttus = ''
      let tkoutDlbrtResult = ''
      let dlbrtResult = ''
      // let pymntMth = ''
      // let mberDiv = ''
      if (commtCode && data) {
        useSttus = toCommtCodeName(commtCode, "EQPMN_USAGE_ST", data.useSttus)
        tkoutDlbrtResult = toCommtCodeName(commtCode, "EQPMN_REQST_ST", data.tkoutDlbrtResult)
        // pymntMth = toCommtCodeName(commtCode, "EQPMN_PAYMENT", data.pymntMth)
        // mberDiv = toCommtCodeName(commtCode, "MEMBER_TYPE", data.mberDiv)
        if (data.tkoutAt) {
          dlbrtResult = '반출'
          if (tkoutDlbrtResult === '승인') {
            dlbrtResult = '반출'
          } else if (tkoutDlbrtResult === '반려') {
            dlbrtResult = '미반출'
          }
        } else {
          dlbrtResult = '미반출'
        }
      }

      return (
        <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"entrprsNm-" + data.key}>{data?.entrprsNm}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"eqpmnNmKorean-" + data.key}>{data?.eqpmnNmKorean}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useSttus-" + data.key}>{useSttus}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useBeginDt-" + data.key}>{dayFormat(data?.useBeginDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"useEndDt-" + data.key}>{dayFormat(data?.useEndDt)}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"tkoutAt-" + data.key}>{dlbrtResult}</TableCell>
          <TableCell sx={{textAlign: 'center'}}
                     key={"tkinAt-" + data.key}>{data?.tkinAt ? '반입완료' : '반입전'}</TableCell>
          {/*<TableCell sx={{textAlign: 'center'}} key={"mberDiv-" + data.key}>{mberDiv}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"assetsNo-" + data.key}>{data?.assetsNo}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"pymntMth-" + data.key}>{pymntMth}</TableCell>
          <TableCell sx={{textAlign: 'center'}} key={"rceptNo-" + data.key}>{data?.rceptNo}</TableCell>*/}
        </Fragment>
      )
    }}
  />
}

const headCells: CustomHeadCell<EqpmnUseList>[] = [
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
    id: 'useSttus',
    align: 'center',
    label: '사용상태',
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
    id: 'tkinAt',
    align: "center",
    label: '반입여부',
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
  }*/
];

export default UseEquipmentMgt;