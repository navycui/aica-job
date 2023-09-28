import {SubContents} from "shared/components/LayoutComponents";
import React, {Fragment, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useCommtCode} from "~/utils/useCommtCode";
import {CustomHeadCell, TableComponents, WithCustomRowData} from "shared/components/TableComponents";
import {UseReprtHistData} from "~/service/Model";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {TableCell} from "@mui/material";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {toTimeFormat} from "shared/utils/stringUtils";

export const ReportEquipmentHistory = () => {
  return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_RESULT_REPORT_ST"])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<UseReprtHistData>[]>([]);
  const history = EquipmentService.getReprtHistlist(id!.toString(),pagination);

  useEffect(() => {
    if (!!history.data) {
      setRowList(history.data.list.map((m,) => {
        return {
          key: m.histId,
          ...m
        }
      }));
      setPagination((state) => ({...state, rowCount: history.data.totalItems}))
    }
  }, [history.data])

  return <TableComponents<UseReprtHistData>
    showTotal
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      let processKnd = ''
      if (commtCode && data)
        processKnd = toCommtCodeName(commtCode,"EQPMN_RESULT_REPORT_ST", data.processKnd)

      return (
        data ? <Fragment>
          <TableCell sx={{textAlign:'center', width:'250px'}} key={"creatDt-" + data.key}>{toTimeFormat(data.creatDt)}</TableCell>
          <TableCell sx={{textAlign:'center', width:'150px'}} key={"processKnd-" + data.key}>{processKnd}</TableCell>
          <TableCell sx={{
            textAlign: 'center',
            maxWidth: '400px',
            whiteSpace: 'pre-wrap',
            tableLayout: 'fixed',
            wordWrap: 'break-word'
          }}
                     key={"processResn-" + data.key}>
            {<div style={{maxHeight: '200px', overflow: 'auto', paddingRight: '10px'}}>{data.processResn}</div>}</TableCell>
          <TableCell sx={{textAlign:'center', width:'150px'}} key={"memberNm-" + data.key}>{data.mberNm}</TableCell>
          <TableCell sx={{textAlign:'center', width:'200px'}} key={"opetrId-" + data.key}>{data.opetrId}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

const headCells: CustomHeadCell<UseReprtHistData>[] = [
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
  {
    id: 'mberNm',
    align: "center",
    label: '처리자명',
  },
  {
    id: 'opetrId',
    align: "center",
    label: '처리자ID',
  }
];