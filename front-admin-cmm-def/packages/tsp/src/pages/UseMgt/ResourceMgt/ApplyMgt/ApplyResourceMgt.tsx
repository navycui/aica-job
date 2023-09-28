import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, TableBody, TableCell, TableContainer, TableRow, Stack} from "@mui/material";
import { TitleContents, VerticalInterval } from '~/../../shared/src/components/LayoutComponents';
import {
  CustomHeadCell,
  SearchTable,
  TableComponents,
  TableDateTermCell,
  TableSelectCell,
  TableSelectTextFieldCell,
  TableTextFieldCell,
  WithCustomRowData
} from '~/../../shared/src/components/TableComponents';
import {todayEndTime, todayTime, toStringFullDayFormat} from '~/../../shared/src/utils/stringUtils';
import {ApplyResourceMgtData, ApplyResourceSearchParam, SearchParam} from '~/service/Model';
import { Icons } from '~/../../shared/src/components/IconContainer';
import {useNavigate} from "react-router-dom";
import {dayFormat} from "shared/utils/stringUtils";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {ApplyResourceService} from "~/service/UseMgt/Resource/ApplyResourceService";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useGlobalModalStore} from "~/store/GlobalModalStore";


/* 실증 자원 신청 관리 */
const ApplyResourceMgt = () => {
  const [applyResourceSearchParam, setApplyResourceSearchParam] = useState<ApplyResourceSearchParam>()

  return <TitleContents
    title={"컴퓨팅자원신청 관리"}>
    <Stack>
      <SearchBox setSearch={setApplyResourceSearchParam}/>
      <VerticalInterval size={"30px"}/>
      <ListView applyResourceSearchParam={applyResourceSearchParam}/>
    </Stack>
  </TitleContents>
}

const SearchBox:React.FC<{
  setSearch: Dispatch<SetStateAction<ApplyResourceSearchParam | undefined>>
}> = props => {

  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST"])
  const [rescStatus, setRescStatus] = useState(['전체']);
  const [applyResourceSearchParam, setApplyResourceSearchParam] = useState<ApplyResourceSearchParam>()
  const [search, setSearch] = useState(false)
  const [searchText, setSearchText] = useState("")
  const [searchKeyword, setSearchKeyword] = useState("")
  const {addModal} = useGlobalModalStore();

  useEffect(() => {
    if (search) {
      props.setSearch(applyResourceSearchParam!)
      // if (props.onClick) props.onClick(searchParam!)
      setSearch(false)
    }
  }, [search])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'EQPMN_RESOURCE_REQST_ST')
      if (state.length > 0) setRescStatus(['전체'].concat(state))
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
              division label={"신청 상태"}
              thWidth={"12%"} tdSpan={3}
              selectLabel={rescStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'EQPMN_RESOURCE_REQST_ST', selected)
                setApplyResourceSearchParam({...applyResourceSearchParam, reqstSttus: sttus})
              }}/>
          </TableRow>
          <TableRow>
            <TableDateTermCell
              division type={"Date"} label={"신청일"} thWidth={"12%"}
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
                setApplyResourceSearchParam({...applyResourceSearchParam, creatBeginDt: todayTime(new Date(beginTime)), creatEndDt: todayEndTime(new Date(endTime))})
              }}/>

            <TableDateTermCell
              division type={"Date"} label={"사용일"} thWidth={"12%"} tdSpan={3}
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
                setApplyResourceSearchParam({...applyResourceSearchParam, useBeginDt: todayTime(new Date(beginTime)), useEndDt: todayEndTime(new Date(endTime))})
              }}/>
          </TableRow>
          <TableRow>
            <TableSelectTextFieldCell
              label={"키워드검색"} tdSpan={3}
              selectLabel={["사업자명/이름","접수번호"]}
              defaultLabel={searchText}
              onClick={(selected: string) => {
                setSearchKeyword(selected)
                setSearchText("")
                setApplyResourceSearchParam({...applyResourceSearchParam, entrprsNm: "", userNm: "", rceptNo: ""})
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
                if ((applyResourceSearchParam?.creatBeginDt && !applyResourceSearchParam?.creatEndDt) || (!applyResourceSearchParam?.creatBeginDt && applyResourceSearchParam?.creatEndDt)) {
                  addModal({open: true, isDist:true, content:'신청일자를 확인해주세요.'})
                  return
                }
                if ((applyResourceSearchParam?.useBeginDt && !applyResourceSearchParam?.useEndDt) || (!applyResourceSearchParam?.useBeginDt && applyResourceSearchParam?.useEndDt)) {
                  addModal({open: true, isDist:true, content:'사용일자를 확인해주세요.'})
                  return
                }
                if (applyResourceSearchParam?.creatBeginDt && applyResourceSearchParam?.creatEndDt) {
                  if (applyResourceSearchParam.creatBeginDt > applyResourceSearchParam.creatEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(applyResourceSearchParam.creatBeginDt))}\n종료일 : ${dayFormat(Number(applyResourceSearchParam.creatEndDt))}\n신청일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                if (applyResourceSearchParam?.useBeginDt && applyResourceSearchParam?.useEndDt) {
                  if (applyResourceSearchParam.useBeginDt > applyResourceSearchParam.useEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(applyResourceSearchParam.useBeginDt))}\n종료일 : ${dayFormat(Number(applyResourceSearchParam.useEndDt))}\n사용일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                if (searchKeyword === "사업자명/이름") {
                  setApplyResourceSearchParam({...applyResourceSearchParam, entrprsNm: searchText, userNm: searchText})
                }
                else if (searchKeyword === "접수번호") {
                  setApplyResourceSearchParam({...applyResourceSearchParam, rceptNo: searchText})
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


const ListView:React.FC<{
  applyResourceSearchParam?: ApplyResourceSearchParam
}
  > = props => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0
  });
  const [rowList, setRowList] = useState<WithCustomRowData<ApplyResourceMgtData>[]>([]);
  const information = ApplyResourceService.getList({...pagination, ...props.applyResourceSearchParam});
  const navigation = useNavigate();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST", "MEMBER_TYPE"])

  useEffect(() => {
    if (!!information.data) {
      setRowList(information.data.list.map((m:ApplyResourceMgtData) => {
        return {
          key: m.reqstId,
          ...m,
        }
      }));
      setPagination((state) => ({...state, rowCount: information.data.totalItems}))
    }
  }, [information.data])

  return <TableComponents<ApplyResourceMgtData>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await ApplyResourceService.getApplyResourceHistInfoExcelDownload({...props.applyResourceSearchParam})
          const blob = new Blob([res]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.setAttribute(
            "download",
            `컴퓨팅자원신청_리스트.xlsx`
          );
          document.body.appendChild(link);
          link.click();
          ;
        }}/>
    </Stack>}

    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      navigation('/tsp-admin/UseMgt/ResourceMgt/ApplyMgt/' + key);
    }}
    tableCell={(data) => {
      let reqstSttus = ''
      let mberDiv = ''
      if (commtCode && data)
        reqstSttus = toCommtCodeName(commtCode,"EQPMN_RESOURCE_REQST_ST",data.reqstSttus)
        mberDiv = toCommtCodeName(commtCode,"MEMBER_TYPE",data.mberDiv)

      return (
        data ? <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
          <TableCell key={"reqstSttus"} sx={{textAlign:'center'}}>{reqstSttus}</TableCell>
          <TableCell key={"mberDiv"} sx={{textAlign:'center'}}>{mberDiv}</TableCell>
          <TableCell key={"entrprsNm"} sx={{textAlign:'center'}}>{data.entrprsNm}</TableCell>
          <TableCell key={"userNm"} sx={{textAlign:'center'}}>{data.userNm}</TableCell>
          <TableCell key={"useBeginDt"} sx={{textAlign:'center'}}>{dayFormat(data.useBeginDt)}</TableCell>
          <TableCell key={"useEndDt"} sx={{textAlign:'center'}}>{dayFormat(data.useEndDt)}</TableCell>
          <TableCell key={"rceptNo"} sx={{textAlign:'center'}}>{data.rceptNo}</TableCell>
          <TableCell key={"creatDt"} sx={{textAlign:'center'}}>{dayFormat(data.creatDt)}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

const headCells: CustomHeadCell<ApplyResourceMgtData>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'reqstSttus',
    align: "center",
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
    label: '사업자명',
  },
  {
    id: 'userNm',
    align: "center",
    label: '이름',
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
    id: 'rceptNo',
    align: "center",
    label: '접수번호',
  },
  {
    id: 'creatDt',
    align: 'center',
    label: '신청일시',
  },

];


export default ApplyResourceMgt;