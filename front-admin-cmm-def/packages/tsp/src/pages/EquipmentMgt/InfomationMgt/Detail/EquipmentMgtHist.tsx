import React, {Fragment, useEffect, useState} from 'react'
import {Stack, Table, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {LoadingProgress, SubContents, VerticalInterval} from "shared/components/LayoutComponents";
import {
  CustomHeadCell,
  TableComponents,
  TableRadioCell,
  WithCustomRowData
} from "shared/components/TableComponents";
import {CustomIconButton} from "shared/components/ButtonComponents";
import {Icons} from "shared/components/IconContainer";
import {useParams} from "react-router-dom";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import {dayFormat, toDayAndTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

/* 장비관리 이력 */
export const EquipmentMgtHist = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_ST"])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EquipmentMgtHistData>[]>([]);
  const [mgtStatus, setMgtStatus] = useState<string>("전체");
  const mgtHistory = EquipmentInformationService.getEquipmentsMgtHist(id!.toString(), mgtStatus, pagination);

  useEffect(() => {
    if (!mgtHistory.isLoading && !mgtHistory.isFetching) {
      if (!!mgtHistory.data) {
        setRowList(mgtHistory.data.list.map((m: EquipmentMgtHistData) => {
          return {
            key: m.eqpmnId,
            ...m
          }
        }));
        setPagination((state) => ({...state, rowCount: mgtHistory.data.totalItems}))
      }
    }
  }, [mgtHistory.data, mgtHistory.isLoading, mgtHistory.isFetching])

  // if (!mgtHistory.data) return <LoadingProgress/>

  return <Stack spacing={"40px"}>
    <SubContents title={"장비관리이력"}>
      <TableContainer style={{borderTop: "1px solid #d7dae6", width: "100%"}}>
        <Table>
          <TableBody>
            <TableRow>
              <TableRadioCell
                row label={"사용상태"}
                defaultLabel={mgtStatus}
                radioLabel={["전체", "수리", "교정", "점검"]}
                thWidth={"20%"}
                onClick={async (selected: string) => {
                  setMgtStatus(selected)
                }}
              />
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </SubContents>

    <VerticalInterval size={"30px"}/>
    <TableComponents<EquipmentMgtHistData>
      showTotal
      isLoading={mgtHistory.isLoading || mgtHistory.isFetching}
      rightContent={<Stack flexDirection={"row"}>
        <CustomIconButton
          startText={'엑셀저장'} icon={Icons.FileDownload}
          style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
          onClick={async () => {
            await EquipmentInformationService.getEquipmentManageHistExcelDownload(id!,mgtStatus).then((res) => {
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
      </Stack>}
      headCells={headCells}
      bodyRows={rowList}
      {...pagination}
      onChangePagination={(page, rowPerPage) => {
        setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
      }}
      tableCell={(data) => {
        let manageDiv = ''
        let manageResult = ''
        if (commtCode && data)
          manageDiv = toCommtCodeName(commtCode,"EQPMN_ST", data.manageDiv)
          manageResult = data.manageResult.split("_",2)[0] === "UNAVAILABLE" ? "불용 " + data.manageResult.split("_",2)[1] :data.manageResult
        return (
          data ? <Fragment>
            <TableCell key={"manageDiv-" + data.key} align={'center'}>{manageDiv}</TableCell>
            <TableCell key={"manageBeginDt-" + data.key} align={'center'}>{dayFormat(data.manageBeginDt)}</TableCell>
            <TableCell key={"manageEndDt-" + data.key} align={'center'}>{dayFormat(data.manageEndDt)}</TableCell>
            <TableCell key={"manageResult-" + data.key} align={'center'}>{manageResult}</TableCell>
            <TableCell key={"crrcInstt-" + data.key} align={'center'}>{data.crrcInstt.length > 0 ? data.crrcInstt : '-'}</TableCell>
            <TableCell key={"memberNm-" + data.key} align={'center'}>{data.mberNm}</TableCell>
            <TableCell key={"opetrId-" + data.key} align={'center'}>{data.opetrId}</TableCell>
          </Fragment> : <></>
        )
      }}
    />
  </Stack>
}

interface EquipmentMgtHistData {
  eqpmnId: string /*장비ID*/
  manageDiv: string	/*관리구분*/
  manageBeginDt: number	/*관리시작일*/
  manageEndDt: number	/*관리종료일*/
  // manageResn: string	/*관리사유*/
  manageResult: string	/*관리결과*/
  crrcInstt: string	/*교정기관*/
  mberNm: string /*처리자명*/
  opetrId: string	/*처리자ID*/
}

const headCells: CustomHeadCell<EquipmentMgtHistData>[] = [
  {
    id: 'manageDiv',
    align: "center",
    label: '구분',
  },
  {
    id: 'manageBeginDt',
    align: "center",
    label: '시작일',
  },
  {
    id: 'manageEndDt',
    align: "center",
    label: '종료일',
  },
  {
    id: 'manageResult',
    align: "center",
    label: '내역',
  },
  {
    id: 'crrcInstt',
    align: "center",
    label: '교정실시기관',
  },
  {
    id: 'mberNm',
    align: "center",
    label: '처리자명',
  },
  {
    id: 'opetrId',
    align: "center",
    label: '처리자ID',
  },
];
export default EquipmentMgtHist;