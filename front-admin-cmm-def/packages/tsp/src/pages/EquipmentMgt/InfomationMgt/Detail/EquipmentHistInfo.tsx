import {useNavigate, useParams} from "react-router-dom";
import {EquipmentInformationService} from "~/service/EquipmentMgt/EquipmentInformationService";
import React, {Fragment, useEffect, useState} from "react";
import {
  CustomHeadCell,
  SearchTable,
  TableComponents,
  TableDateTermCell,
  TableRadioCell,
  TableSelectCell,
  TableTextFieldCell,
  WithCustomRowData
} from "shared/components/TableComponents";
import {Box, Stack, TableBody, TableCell, TableContainer, TableRow} from "@mui/material";
import {HorizontalInterval, SubContents, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {useEquipmentDetailStore} from "~/store/EquipmentMgt/EquipmentDetailStore";
import dayjs from "shared/libs/dayjs";
import {Icons} from "shared/components/IconContainer";
import {dayFormat} from "shared/utils/stringUtils";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useCommtCode} from "~/utils/useCommtCode";

/* 장비 정보 관리 상세 - 상세 정보 */
export const EquipmentHistInfo = () => {
  const {clear} = useEquipmentDetailStore();
  useEffect(() => {
    return () => clear()
  })
  return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}
const ListView = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_ST"])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<HistData>[]>([]);
  const history = EquipmentInformationService.getEquipmentsHistInfo(id!.toString(), pagination);

  useEffect(() => {
    if (!history.isLoading && !history.isFetching) {
      if (!!history.data) {
        setRowList(history.data.list.map((m,) => {
          return {
            key: m.histId,
            ...m
          }
        }));
        setPagination((state) => ({...state, rowCount: history.data.totalItems}))
      }
    }
  }, [history.data, history.isLoading, history.isFetching])
  return <TableComponents<HistData>
    showTotal
    isLoading={history.isLoading || history.isFetching}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      // console.log(data.processResn.split(" ",3)[0])
      // console.log(data.processResn.split(" ",3))
      let processKnd = ''
      let processResn = ''
      if (commtCode && data) {
        processKnd = data.processKnd === "SAVE" ? "저장" : data.processKnd === "UNAVAILABLE" ? "불용" : data.processKnd.split(" ").length > 1 ? toCommtCodeName(commtCode, "EQPMN_ST", data.processKnd.split(" ",2)[0]) + data.processKnd.split(" ",2)[1] : toCommtCodeName(commtCode, "EQPMN_ST", data.processKnd.split(" ",2)[0])
        // processResn = data.processResn
        processResn = data.processResn.split(" ",3)[0] === "저장" ? data.processResn : data.processResn.split(" ",3)[0] === "UNAVAILABLE" ? "불용 " + data.processResn.split(" ",3)[1] : data.processResn.split(" ",3).length === 2 ? toCommtCodeName(commtCode, "EQPMN_ST", data.processResn.split(" ",3)[0]) + " " + data.processResn.split(" ",3)[1] : toCommtCodeName(commtCode, "EQPMN_ST", data.processResn.split(" ",3)[0]) + " " + data.processResn.split(" ",3)[1] + " " + data.processResn.split(" ",3)[2]
      }
//
      return (
        data ? <Fragment>
          <TableCell key={"creatDt-" + data.key} align={'center'}>{dayFormat(data.creatDt)}</TableCell>
          <TableCell key={"processKnd-" + data.key} align={'center'}>{processKnd}</TableCell>
          <TableCell sx={{
            textAlign: 'center',
            maxWidth: '400px',
            whiteSpace: 'pre-wrap',
            tableLayout: 'fixed',
            wordWrap: 'break-word'
          }}
                     key={"processResn-" + data.key}>
            {<div style={{maxHeight: '200px', overflow: 'auto', paddingRight: '10px'}}>{processResn}</div>}</TableCell>
          <TableCell key={"opetrId-" + data.key} align={'center'}>{data.opetrId}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

interface HistData {
  creatDt: number /*생성일시*/
  processKnd: string /*처리구분*/
  processResn: string /*처리사유*/
  opetrId: string /*처리자ID*/
}

const headCells: CustomHeadCell<HistData>[] = [
  {
    id: 'creatDt',
    align: 'center',
    label: '처리일시',
  },
  {
    id: 'processKnd',
    align: "center",
    label: '구분',
  },
  {
    id: 'processResn',
    align: "center",
    label: '사유',
  },
  // {
  //   id: 'workerNm',
  //   align: "center",
  //   label: '처리자명',
  // },
  {
    id: 'opetrId',
    align: "center",
    label: '처리자ID',
  }
];