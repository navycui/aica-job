import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {Box, TableBody, TableCell, TableContainer, TableRow, Stack} from "@mui/material";
import { TitleContents, VerticalInterval } from '~/../../shared/src/components/LayoutComponents';
import { CustomHeadCell, SearchTable, TableComponents, TableDateTermCell, TableSelectCell, TableSelectTextFieldCell, TableTextFieldCell } from '~/../../shared/src/components/TableComponents';
import { CustomButton, CustomIconButton } from '~/../../shared/src/components/ButtonComponents';
import {dayFormat, todayEndTime, todayTime} from '~/../../shared/src/utils/stringUtils';
import {
  UseResourceMgtData,
  UseResourceSearchParam
} from '~/service/Model';
import { WithCustomRowData } from '../../../../../../shared/src/components/TableComponents';
import {useNavigate} from "react-router-dom";
import {Icons} from "shared/components/IconContainer";
import {UseResourceService} from "~/service/UseMgt/Resource/UseResourceService";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 실증 자원 사용 관리 */
const UseResourceMgt = () => {
  const [useResourceSearchParam, setUseResourceSearchParam] = useState<UseResourceSearchParam>()

  return <TitleContents
      title={"컴퓨팅자원사용 관리"}>
    <Stack>
      <SearchBox setSearch={setUseResourceSearchParam}/>
      <VerticalInterval size={"30px"}/>
      <ListView useResourceSearchParam={useResourceSearchParam}/>
    </Stack>
  </TitleContents>
}

const SearchBox:React.FC<{
  setSearch: Dispatch<SetStateAction<UseResourceSearchParam | undefined>>
}> = props => {

  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_USAGE_ST"])
  const [useStatus, setUseStatus] = useState(['전체']);
  const [useResourceSearchParam, setUseResourceSearchParam] = useState<UseResourceSearchParam>()
  const [search, setSearch] = useState(false)
  const [searchText, setSearchText] = useState("")
  const [searchKeyword, setSearchKeyword] = useState("")
  const {addModal} = useGlobalModalStore();

  useEffect(() => {
    if (search) {
      props.setSearch(useResourceSearchParam!)
      setSearch(false)
    }
  }, [search])

  useEffect(() => {
    if (!!commtCode) {
      const state = CommtCodeNms(commtCode, 'EQPMN_RESOURCE_USAGE_ST').filter(f => f != '승인취소')
      if (state.length > 0) setUseStatus(['전체'].concat(state))
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
              division label={"사용 상태"}
              thWidth={"12%"} tdSpan={3}
              selectLabel={useStatus}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                const sttus = toCommtCode(commtCode, 'EQPMN_RESOURCE_USAGE_ST', selected)
                setUseResourceSearchParam({...useResourceSearchParam, useSttus: sttus})
              }}/>
          </TableRow>
          <TableRow>    
            <TableDateTermCell
                division type={"Date"} label={"사용일"} thWidth={"12%"}
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
                  setUseResourceSearchParam({...useResourceSearchParam, useBeginDt: todayTime(new Date(beginTime)), useEndDt: todayEndTime(new Date(endTime))})
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
                setUseResourceSearchParam({...useResourceSearchParam, entrprsNm: "", userNm: "", rceptNo: ""})
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
                if ((useResourceSearchParam?.useBeginDt && !useResourceSearchParam?.useEndDt) || (!useResourceSearchParam?.useBeginDt && useResourceSearchParam?.useEndDt)) {
                  addModal({open: true, isDist:true, content:'사용일자를 확인해주세요.'})
                  return
                }
                if (useResourceSearchParam?.useBeginDt && useResourceSearchParam?.useEndDt) {
                  if (useResourceSearchParam.useBeginDt > useResourceSearchParam.useEndDt) {
                    addModal({
                      open: true,
                      isDist: true,
                      type: 'normal',
                      content: `시작일 : ${dayFormat(Number(useResourceSearchParam.useBeginDt))}\n종료일 : ${dayFormat(Number(useResourceSearchParam.useEndDt))}\n사용일자를 다시 입력 바랍니다.`
                    })
                    return
                  }
                }
                if (searchKeyword === "사업자명/이름") {
                  setUseResourceSearchParam({...useResourceSearchParam, entrprsNm: searchText, userNm: searchText})
                }
                else if (searchKeyword === "접수번호") {
                  setUseResourceSearchParam({...useResourceSearchParam, rceptNo: searchText})
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
  useResourceSearchParam?: UseResourceSearchParam
  }
> = props => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0
  });
  const [rowList, setRowList] = useState<WithCustomRowData<UseResourceMgtData>[]>([]);
  const information = UseResourceService.getList({...pagination, ...props.useResourceSearchParam});
  const navigation = useNavigate();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_USAGE_ST", "MEMBER_TYPE"])

  useEffect(() => {
    if (!!information.data) {
      setRowList(information.data.list.map((m,) => {
        return {
          key: m.reqstId,
          ...m,
        }
      }));
      setPagination((state) => ({...state, rowCount: information.data.totalItems}))
    }
  }, [information.data])

  return <TableComponents<UseResourceMgtData>
      showTotal
      isLoading={information.isLoading || information.isFetching}
      rightContent={<Stack flexDirection={"row"}>
        <CustomIconButton
          startText={'엑셀저장'} icon={Icons.FileDownload}
          style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
          onClick={async () => {
            const res = await UseResourceService.getUseResourceHistInfoExcelDownload({...props.useResourceSearchParam})
            const blob = new Blob([res]);
            const fileObjectUrl = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = fileObjectUrl;
            link.setAttribute(
              "download",
              `컴퓨팅자원사용_리스트.xlsx`
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
        navigation('/tsp-admin/UseMgt/ResourceMgt/UseMgt/' + key);
      }}
      tableCell={(data) => {
        let useSttus = ''
        let mberDiv = ''
        if (commtCode && data)
          useSttus = toCommtCodeName(commtCode,"EQPMN_RESOURCE_USAGE_ST",data.useSttus)
          mberDiv = toCommtCodeName(commtCode,"MEMBER_TYPE",data.mberDiv)

        return (
            data ? <Fragment>
              <TableCell sx={{textAlign: 'center'}} key={"number-" + data.key}>{data.number}</TableCell>
              <TableCell key={"useSttus"} sx={{textAlign:'center'}}>{useSttus}</TableCell>
              <TableCell key={"mberDiv"} sx={{textAlign:'center'}}>{mberDiv}</TableCell>
              <TableCell key={"entrprsNm"} sx={{textAlign:'center'}}>{data.entrprsNm}</TableCell>
              <TableCell key={"userNm"} sx={{textAlign:'center'}}>{data.userNm}</TableCell>
              <TableCell key={"useBeginDt"} sx={{textAlign:'center'}}>{dayFormat(data.useBeginDt)}</TableCell>
              <TableCell key={"useEndDt"} sx={{textAlign:'center'}}>{dayFormat(data.useEndDt)}</TableCell>
              <TableCell key={"rceptNo"} sx={{textAlign:'center'}}>{data.rceptNo}</TableCell>
          </Fragment> : <></>
        )
      }}
  />
}


const headCells: CustomHeadCell<UseResourceMgtData>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'useSttus',
    align: 'center',
    label: '사용상태',
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
];


export default UseResourceMgt;