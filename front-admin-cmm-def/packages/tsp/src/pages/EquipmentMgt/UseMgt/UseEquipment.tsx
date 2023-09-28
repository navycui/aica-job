import React, {Fragment, useEffect, useState} from 'react'
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {
  CustomHeadCell,
  SearchTable, TableComponents, TableDateTermCell, TableSelectCell,
  TableTextFieldCell, WithCustomRowData
} from "shared/components/TableComponents";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {Icons} from "shared/components/IconContainer";
import {TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {UseEquipmentService} from "~/service/EquipmentMgt/UseEquipmentService";
import {
  dayFormat,
  todayEndTime,
  todayTime,
  toStringForMinutes,
  toTimeFormat
} from "shared/utils/stringUtils";
import {UseEquipmentData} from "~/service/Model";
import {toCommtCode, toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useCommtCode} from "~/utils/useCommtCode";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import {CommonService} from "~/service/CommonService";

/* 장비 사용 현황*/
const UseEquipment = () => {
  const [useEquipmentData, setUseEquipmentData] = useState<UseEquipmentData | null>(null)
  return <TitleContents
    title={"장비사용현황"}>
    <SearchBox onClick={(data: UseEquipmentData | null) => {
      setUseEquipmentData(data);
    }}/>

    <VerticalInterval size={"30px"}/>
    <ListView useEquipmentData={useEquipmentData}/>
  </TitleContents>
}

const SearchBox: React.FC<{
  onClick: (data: UseEquipmentData | null) => void
}> = props => {
  const [useEquipmentData, setUseEquipmentData] = useState<UseEquipmentData | null>(null)
  const {addModal} = useGlobalModalStore();
  const [clList, setClList] = useState(['']);
  const getClList = CommonService.getClList()

  useEffect(() => {
      let list = ['전체']
      if (getClList) {
        if (getClList.data) {
          getClList.data.list.map((m: any) => {
            list.push(m.eqpmnClNm)
          })
        }
        setClList(list)
      }
    }, [getClList.data]
  )

  return <Box sx={{
    border: "1px solid #d7dae6",
    borderRadius: "20px",
  }}>
    <TableContainer>
      <SearchTable>
        <TableBody>
          <TableRow>
            <TableDateTermCell
              division type={"Date"} label={"사용기간"} thWidth={"12%"}
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
                setUseEquipmentData((e) => ({
                  ...e!,
                  useEndDt: todayEndTime(new Date(endTime)),
                  useBeginDt: todayTime(new Date(beginTime))
                }))
              }}
            />
            <TableTextFieldCell
              label={"사업자명"} wordCount={100}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(selected: string) => {
                setUseEquipmentData((e) => ({
                  ...e!,
                  userNm: selected
                }))
              }}/>
          </TableRow>
          <TableRow>
            <TableTextFieldCell
              division label={"장비명"} wordCount={100}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(selected: string) => {
                setUseEquipmentData((e) => ({
                  ...e!,
                  eqpmnNmKorean: selected
                }))
              }}/>
            <TableTextFieldCell
              label={"자산번호"} wordCount={100}
              thWidth={"12%"} tdWidth={"38%"}
              onChange={(selected: string) => {
                setUseEquipmentData((e) => ({
                  ...e!,
                  assetsNo: selected
                }))
              }}/>
          </TableRow>
          <TableRow>
            <TableSelectCell
              label={"장비 분야"}
              thWidth={"12%"} tdSpan={3}
              selectLabel={clList}
              defaultLabel={"전체"}
              medium
              onClick={(selected: string) => {
                setUseEquipmentData((e) => ({
                  ...e!,
                  eqpmnClNm: selected === '전체' ? undefined : selected
                }))
              }}/>
          </TableRow>
          <TableRow>
            <TableCell colSpan={4} style={{textAlign: "center", borderBottom: "none"}}>
              <CustomButton label={"검색"}
                            onClick={
                              () => {
                                if ((useEquipmentData?.useBeginDt && !useEquipmentData?.useEndDt) || (!useEquipmentData?.useBeginDt && useEquipmentData?.useEndDt)) {
                                  addModal({open: true, isDist:true, content:'사용기간을 확인해주세요.'})
                                  return
                                }
                                (useEquipmentData?.useEndDt! - useEquipmentData?.useBeginDt!) < 0 ? addModal({
                                    open: true,
                                    isDist: true,
                                    content: '사용기간 재확인 부탁드립니다.'
                                  }) :
                                  props.onClick(useEquipmentData);
                              }
                            }
              />
            </TableCell>
          </TableRow>
        </TableBody>
      </SearchTable>
    </TableContainer>
  </Box>
}

const ListView: React.FC<{
  useEquipmentData: UseEquipmentData | null
}> = props => {
  const [rowList, setRowList] = useState<WithCustomRowData<UseEquipmentData>[]>([]);
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0
  });

  const useEqpmn = UseEquipmentService.getList({
    ...pagination,
    ...props.useEquipmentData
  });
  const navigation = useNavigate();
  const {commtCode} = useCommtCode(["MEMBER_TYPE"])

  useEffect(() => {
    if (!useEqpmn.isLoading && !useEqpmn.isFetching) {
      if (!!useEqpmn.data) {
        setRowList(useEqpmn.data.list.map((m,) => {
          return {
            key: m.assetsNo.concat(m.userNm, String(m.useBeginDt), String(m.useEndDt)),
            ...m,
          }
        }));
        setPagination((state) => ({...state, rowCount: useEqpmn.data.totalItems}))
      }
    }
  }, [useEqpmn.data, useEqpmn.isLoading, useEqpmn.isFetching])

  return <TableComponents<UseEquipmentData>
    showTotal isLoading={useEqpmn.isLoading || useEqpmn.isFetching}
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await UseEquipmentService.getEquipmentListExcelDownload({
            ...props.useEquipmentData
          });

          const blob = new Blob([res]);
          const fileObjectUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = fileObjectUrl;
          link.setAttribute(
            "download",
            `장비사용현황_리스트.xlsx`
          );
          document.body.appendChild(link);
          link.click();
        }}>
      </CustomIconButton>
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      // let mberDiv = ''
      // if (commtCode && data) {
      //   mberDiv = toCommtCodeName(commtCode,"MEMBER_TYPE", data.mberDiv)
      // }

      return (
        data ? <Fragment>
          <TableCell sx={{textAlign: 'center'}} key={"rn-" + data.key}>{data.number}</TableCell>
          <TableCell key={"entrprsNm-" + data.key} sx={{textAlign: 'center'}}>{data.entrprsNm}</TableCell>
          <TableCell key={"eqpmnNmKorean-" + data.key} sx={{textAlign: 'center'}}>{data.eqpmnNmKorean}</TableCell>
          <TableCell key={"eqpmnClNm-" + data.key} sx={{textAlign: 'center'}}>{data.eqpmnClNm}</TableCell>
          <TableCell key={"useBeginDt-" + data.key} sx={{textAlign: 'center'}}>{toTimeFormat(data.useBeginDt)}</TableCell>
          <TableCell key={"useEndDt-" + data.key} sx={{textAlign: 'center'}}>{toTimeFormat(data.useEndDt)}</TableCell>
          <TableCell key={"expectUsgtm-" + data.key} sx={{textAlign: 'center'}}>{toStringForMinutes(data.expectUsgtm)}</TableCell>
          {/*<TableCell key={"assetsNo-" + data.key} sx={{textAlign:'center'}}>{data.assetsNo}</TableCell>
          <TableCell key={"mberDiv-" + data.key} sx={{textAlign:'center'}}>{mberDiv}</TableCell>
          <TableCell key={"userNm-" + data.key} sx={{textAlign:'center'}}>{data.userNm}</TableCell>*/}
        </Fragment> : <></>
      )
    }}
  />
}

const headCells: CustomHeadCell<UseEquipmentData>[] = [
  {
    id: 'number',
    align: 'center',
    label: '번호',
  },
  {
    id: 'entrprsNm',
    align: "center",
    label: '사업자명',
  },
  /*{
    id: '장비분야',
    align: "center",
    label: '장비분야',
  },*/
  {
    id: 'eqpmnNmKorean',
    align: "center",
    label: '장비명',
  },
  {
    id: 'eqpmnClNm',
    align: "center",
    label: '장비분류',
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
    id: 'expectUsgtm',
    align: "center",
    label: '사용시간',
  },
  /*{
    id: 'assetsNo',
    align: 'center',
    label: '자산번호',
  },
  {
    id: 'mberDiv',
    align: "center",
    label: '구분',
  },
  {
    id: 'userNm',
    align: "center",
    label: '대표자명',
  },*/
];

export default UseEquipment;