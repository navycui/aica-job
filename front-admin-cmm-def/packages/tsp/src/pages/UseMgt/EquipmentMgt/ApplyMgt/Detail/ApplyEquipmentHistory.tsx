import { Stack, TableCell } from "@mui/material";
import React, { Fragment, useEffect, useState } from "react"
import { useParams } from "react-router-dom";
import { CustomIconButton } from "~/../../shared/src/components/ButtonComponents";
import { Icons } from "~/../../shared/src/components/IconContainer";
import { SubContents } from "~/../../shared/src/components/LayoutComponents";
import { CustomHeadCell, TableComponents, WithCustomRowData } from "~/../../shared/src/components/TableComponents";
import { EquipmentInformationService } from "~/service/EquipmentMgt/EquipmentInformationService";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {EqpmnUseReqstHistList} from "~/service/Model";
import {dayFormat, toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

export const ApplyEquipmentHistory = () => {
    return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST",'EQPMN_USE_HIST','EQPMN_USAGE_ST', 'EQPMN_TKIN_ST'])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnUseReqstHistList>[]>([]);
  const history = EquipmentService.getApplyEquipDetailHistList(id!.toString(),pagination);

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

  return <TableComponents<EqpmnUseReqstHistList>
    showTotal
    rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          await EquipmentInformationService.getEquipmentsHistInfoExcelDownload(id!.toString()).then((res) => {
            const blob = new Blob([res]);
            const fileObjectUrl = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = fileObjectUrl;
            link.setAttribute(
              "download",
              `처리이력_리스트.xlsx`
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
      let processKnd = ''
      let processKnd2 = ''
      let processKnd3 = ''
       if (commtCode && data) {
         processKnd = toCommtCodeName(commtCode, "EQPMN_REQST_ST", data.processKnd)
         processKnd2 = toCommtCodeName(commtCode, "EQPMN_USE_HIST", data.processKnd)
         processKnd3 = toCommtCodeName(commtCode, "EQPMN_USAGE_ST", data.processKnd)
       }
      return (
        data ? <Fragment>
          <TableCell sx={{textAlign:'center', width:'250px'}} key={"creatDt-" + data.key}>{toTimeFormat(data.creatDt)}</TableCell>
          <TableCell sx={{textAlign:'center', width:'150px'}} key={"processKnd-" + data.key}>{processKnd || processKnd2 || processKnd3}</TableCell>
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

const headCells: CustomHeadCell<EqpmnUseReqstHistList>[] = [
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