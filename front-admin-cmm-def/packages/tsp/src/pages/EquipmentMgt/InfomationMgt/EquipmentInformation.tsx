import React, {Dispatch, Fragment, SetStateAction, useEffect, useState} from 'react'
import {
  Box, Icon, Stack,
  Table, TableBody,
  TableCell,
  TableContainer,
  TableRow
} from "@mui/material";
import {
  CustomHeadCell,
  WithCustomRowData, SearchTable,
  TableComponents, TableDateTermCell, TableTextFieldCell,
  TableRadioCell,
  TableSelectCell, TableSelectTextFieldCell
} from "shared/components/TableComponents";
import {BlockContents, HorizontalInterval, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import dayjs from "shared/libs/dayjs";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import {Icons} from "shared/components/IconContainer";
import {useNavigate} from "react-router-dom";
import styled from "@emotion/styled";
import {CommonService} from "~/service/CommonService";
import {EquipmentInformationData, SearchParam} from "~/service/Model";
import {dayFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {CommtCodeNms, toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useGlobalModalStore} from "~/store/GlobalModalStore";

/* 장비 정보 관리 */
const EquipmentInformation = () => {
  const [searchParam, setSearchParam] = useState<SearchParam>()

  // const {clear} = useEquipmentDetailStore();
  // useEffect(() => {
  //   return () => clear()
  // })

  return <TitleContents
    title={"장비정보관리"}>
    <SearchBox setSearch={setSearchParam}/>

    <VerticalInterval size={"30px"}/>
    <ListView searchParam={searchParam}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  setSearch: Dispatch<SetStateAction<SearchParam | undefined>>
}> = props => {
  // const common = CommonService.CommonCode(["EQPMN_ST"])

  const {commtCode} = useCommtCode(["EQPMN_ST"])
  const [eqpmnStatus, setEqpmnStatus] = useState(["전체"]);
  const [searchParam, setSearchParam] = useState<SearchParam>()
  const [searchKeyword, setSearchKeyword] = useState("")
  const [searchText, setSearchText] = useState("")
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
      const state = CommtCodeNms(commtCode, 'EQPMN_ST').filter(f => f != '수리내역')
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
              division medium label={"장비 상태"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={eqpmnStatus}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                setSearchParam({
                  ...searchParam,
                  eqpmnSttus: selected === '전체' ? undefined : toCommtCode(commtCode, 'EQPMN_ST', selected)
                })
              }}/>
            <TableTextFieldCell
              label={"장비명"} wordCount={100}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(s: string) => {
                setSearchParam({...searchParam, eqpmnNmKorean: s})
              }}/>
          </TableRow>
          <TableRow>
            <TableSelectCell
              division medium label={"반출 상태"}
              thWidth={"12%"} tdWidth={"38%"}
              selectLabel={["전체", "미반출", "반출중"]}
              defaultLabel={"전체"}
              onClick={(selected: string) => {
                let tkout: boolean | undefined = undefined;
                if (selected === "반출중") tkout = true
                else if (selected === "미반출") tkout = false
                setSearchParam({...searchParam, tkoutAt: tkout})
              }}/>
            <TableRadioCell
              row label={"불용여부"} defaultLabel={"정상"}
              radioLabel={["정상", "불용"]}
              thWidth={"12%"} tdWidth={"38%"}
              onClick={(select: string) => {
                let disuse: boolean | undefined;
                if (select === '불용') disuse = true
                else if (select === '정상') disuse = false
                else disuse = undefined
                setSearchParam({...searchParam, disuseAt: disuse})
              }}
            />
          </TableRow>
          <TableRow>
            <TableSelectTextFieldCell
              label={"키워드검색"} tdSpan={3}
              selectLabel={["모델명", "자산번호"]}
              defaultLabel={searchText}
              onClick={(selected: string) => {
                setSearchKeyword(selected)
                setSearchText("")
                setSearchParam({...searchParam, assetsNo: "", modelNm: ""})
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
                if (searchKeyword === "자산번호") {
                  setSearchParam({...searchParam, assetsNo: searchText})
                } else if (searchKeyword === "모델명") {
                  setSearchParam({...searchParam, modelNm: searchText})
                }
                // props.onClick(searchParam!)
                // searchParam을 검색시 적용하기 떄문에 useEffect에서 검색되도록 함.
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
  const {commtCode} = useCommtCode(["EQPMN_ST"])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EquipmentInformationData>[]>([]);
  const information = EquipmentInformationService.getList({...pagination, ...props.searchParam});

  const navigation = useNavigate();
  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m,) => {
          return {
            key: m.eqpmnId,
            ...m,
            creatDt: m.creatDt
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])
  return <TableComponents<EquipmentInformationData>
    showTotal
    isLoading={information.isLoading || information.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          await EquipmentInformationService.getEquipmentListExcelDownload({...props.searchParam}).then((res) => {
            const blob = new Blob([res]);
            const fileObjectUrl = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = fileObjectUrl;
            link.setAttribute(
              "download",
              `장비정보관리_리스트.xlsx`
            );
            document.body.appendChild(link);
            link.click();
          });
        }}/>
      <HorizontalInterval size={"10px"}/>
      <CustomButton
        type={"small"} color={"list"} label={"등록"}
        onClick={() => {
          navigation('/tsp-admin/EquipmentMgt/EquipmentRegist')
        }}/>
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      navigation('/tsp-admin/EquipmentMgt/InfomationMgt/' + key);
    }}
    tableCell={(data: WithCustomRowData<EquipmentInformationData>) => {
      let eqpmnSttus = ''
      if (commtCode && data)
        eqpmnSttus = toCommtCodeName(commtCode, "EQPMN_ST", data.eqpmnSttus)
      return (
        <Fragment key={data.key}>
          <TableCell sx={{textAlign:'center'}} key={"rn-" + data.key}>{data.number}</TableCell>
          <TableCell key={"eqpmnSt-" + data.key} sx={{textAlign:'center'}}>{data.disuseAt ? '불용' : eqpmnSttus}</TableCell>
          <TableCell key={"assetNo-" + data.key} sx={{textAlign:'center'}}>{data?.assetsNo}</TableCell>
          <TableCell key={"eqpmnNmKo-" + data.key} sx={{textAlign:'center'}}>{data?.eqpmnNmKorean}</TableCell>
          <TableCell key={"eqpmnClfcId-" + data.key} sx={{textAlign:'center'}}>{data?.eqpmnClNm}</TableCell>
          <TableCell key={"modelNm-" + data.key} sx={{textAlign:'center'}}>{data?.modelNm}</TableCell>
          <TableCell key={"tkout-" + data.key} sx={{textAlign:'center'}}>{data?.tkoutAt ? '반출' : '미반출'}</TableCell>
          <TableCell key={"disuseAt-" + data.key} sx={{textAlign:'center'}}>{data?.disuseAt ? '불용' : '정상'}</TableCell>
          <TableCell key={"createdDt-" + data.key} sx={{textAlign:'center'}}>{dayFormat(data?.creatDt)}</TableCell>
        </Fragment>
      )
    }}

  />
}

// interface NoticeData {
//   eqpmnId: string
//   assetsNo: string
//   eqpmnSttus: string
//   eqpmnNmKorean: string
//   eqpmnClNm: string
//   modelNm: string
//   tkoutAt: boolean
//   disuseAt: boolean
//   creatDt: string
// }

const headCells: CustomHeadCell<EquipmentInformationData>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'eqpmnSttus',
    align: 'center',
    label: '장비상태',
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
    id: 'eqpmnClNm',
    align: "center",
    label: '분류',
  },
  {
    id: 'modelNm',
    align: "center",
    label: '모델명',
  },
  {
    id: 'tkoutAt',
    align: "center",
    label: '반출상태',
  },
  {
    id: 'disuseAt',
    align: "center",
    label: '불용여부',
  },
  {
    id: 'creatDt',
    align: "center",
    label: '등록일',
  },
];


export default EquipmentInformation;