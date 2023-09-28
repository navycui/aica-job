import {useParams} from "react-router-dom";
import { Stack, TableCell } from "@mui/material";
import React, {Fragment, useEffect, useState} from "react";
import { CustomHeadCell, TableComponents, WithCustomRowData } from "~/../../shared/src/components/TableComponents";
import {ApplyResourceDetailData, ApplyResourceHistData, ApplyResourceSearchParam, SearchParam} from "~/service/Model";
import {SubContents} from "~/../../shared/src/components/LayoutComponents";
import {ApplyResourceService} from "~/service/UseMgt/Resource/ApplyResourceService";
import {toDayAndTimeFormat, toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

export const ApplyResourcehistinfo = () => {
  return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_RESOURCE_REQST_ST","EQPMN_RESOURCE_USAGE_ST"])
  const [applyResourceSearchParam, setApplyResourceSearchParam] = useState<ApplyResourceSearchParam>()
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<ApplyResourceHistData>[]>([]);
  const history = ApplyResourceService.getProcessHistoryList(id!.toString(),{...pagination, ...applyResourceSearchParam});

  useEffect(() => {
    if (!!history.data) {
      setRowList(history.data.list.map((m:ApplyResourceHistData) => {
        return {
          key: m.histId,
          ...m
        }
      }));
      setPagination((state) => ({...state, rowCount: history.data.totalItems}))
    }
  }, [history.data])


  return <TableComponents<ApplyResourceHistData>
    isLoading={history.isLoading || history.isFetching}
    showTotal
    rightContent={<Stack flexDirection={"row"}>
    </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      let processKnd = ''
      if (commtCode && data)
        processKnd = data.processKnd === "APPROVE_CANCEL" ? toCommtCodeName(commtCode,"EQPMN_RESOURCE_USAGE_ST", data.processKnd) : toCommtCodeName(commtCode,"EQPMN_RESOURCE_REQST_ST", data.processKnd)
      
      return (
        data ? <Fragment>
          <TableCell align={'center'} key={"creatDt-" + data.key}>{toTimeFormat(data.creatDt)}</TableCell>
          <TableCell align={'center'} key={"processKnd-" + data.key}>{processKnd}</TableCell>
          <TableCell align={'center'} sx={{
            maxWidth: '400px',
            whiteSpace: 'pre-wrap',
            tableLayout: 'fixed',
            wordWrap: 'break-word'
          }}
                     key={"processResn-" + data.key}>
            {<div style={{maxHeight: '200px', overflow: 'auto', paddingRight: '10px'}}>{data.processResn}</div>}</TableCell>
          <TableCell align={'center'} key={"memberNm-" + data.key}>{data.mberNm}</TableCell>
          <TableCell align={'center'} key={"opetrId-" + data.key}>{data.opetrId}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}


export const headCells: CustomHeadCell<ApplyResourceDetailData>[] = [
  {
    id: 'creatDt',
    align: "center",
    label: '처리일시',
  },
  {
    id: 'processKnd',
    align: "center",
    label: '처리구분',
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
  },
];

