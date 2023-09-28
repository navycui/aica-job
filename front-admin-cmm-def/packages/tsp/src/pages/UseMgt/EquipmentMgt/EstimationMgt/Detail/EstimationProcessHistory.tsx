import React, {Fragment, useEffect, useState} from "react";
import {SubContents} from "shared/components/LayoutComponents";
import {CustomHeadCell, TableComponents, WithCustomRowData} from "shared/components/TableComponents";
import {TableCell} from "@mui/material";
import {SearchParam, TsptEqpmnEstmtReqstHist} from "~/service/Model";
import {EstimationService} from "~/service/UseMgt/Estimation/EstimationService";
import {toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {useParams} from "react-router-dom";

/* 견적요청관리 - 처리 이력*/
export const EstimationProcessHistory = () => {
  return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams()
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", "EQPMN_USE_HIST"])
  const [searchParam, setSearchParam] = useState<SearchParam>()
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<TsptEqpmnEstmtReqstHist>[]>([])
  const history = EstimationService.getTsptEqpmnEstmtReqstHist(id!.toString(), {...pagination, ...searchParam})
  useEffect(() => {
    if (!!history.data) {
      setRowList(history.data.list.map((m,) => {
        return {
          key: m.estmtId,
          ...m
        }
      }));
      setPagination((state) => ({...state, rowCount: history.data.totalItems}))
    }
  }, [history.data, history.isLoading, history.isFetching])

  return <TableComponents<TsptEqpmnEstmtReqstHist>
    showTotal
    /*rightContent={<Stack flexDirection={"row"}>
      <CustomIconButton
        startText={'엑셀저장'} icon={Icons.FileDownload}
        style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
        onClick={async () => {
          const res = await EquipmentInformationService.getEquipmentsHistInfoExcelDownload(id!.toString())
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
        }}/>
    </Stack>}*/
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      let processKnd = ''
      if (commtCode && data) {
        if (data.processKnd === "ACTL_USE_PAYMENT") {
          processKnd = toCommtCodeName(commtCode, "EQPMN_USE_HIST", data.processKnd)
        } else {
          processKnd = toCommtCodeName(commtCode, "EQPMN_REQST_ST", data.processKnd)
        }
      }

      return (
        data ? <Fragment>
          <TableCell sx={{textAlign: 'center', width: '250px'}} key={"creatDt-" + data.key}>{toTimeFormat(data.creatDt)}</TableCell>
          <TableCell sx={{textAlign: 'center', width: '150px'}} key={"processKnd-" + data.key}>{processKnd}</TableCell>
          <TableCell sx={{
            textAlign: 'center',
            maxWidth: '400px',
            whiteSpace: 'pre-wrap',
            tableLayout: 'fixed',
            wordWrap: 'break-word'
          }}
                     key={"processResn-" + data.key}>
            {<div style={{maxHeight: '200px', overflow: 'auto', paddingRight: '10px'}}>{data.processResn}</div>}</TableCell>
          <TableCell sx={{textAlign: 'center', width: '150px'}} key={"memberNm-" + data.key}>{data.mberNm}</TableCell>
          <TableCell sx={{textAlign: 'center', width: '200px'}} key={"opetrId-" + data.key}>{data.opetrId}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

const headCells: CustomHeadCell<TsptEqpmnEstmtReqstHist>[] = [
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