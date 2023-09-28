import react, {Fragment, useEffect, useState} from "react"
import {SubContents} from "shared/components/LayoutComponents";
import {useParams} from "react-router-dom";
import {CustomHeadCell, TableComponents, WithCustomRowData} from "shared/components/TableComponents";
import {TableCell} from "@mui/material";
import React from "react";
import {PeriodExtendService} from "~/service/UseMgt/PeriodExtend/PeriodExtendService";
import {EqpmnsExtndHistList} from "~/service/Model";
import {toTimeFormat} from "shared/utils/stringUtils";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";

export const UseEquipmentPeriodExtendHistory = () => {
  return <SubContents title={"처리이력 조회"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams();
  const {commtCode} = useCommtCode(["EQPMN_EXTEND"])
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnsExtndHistList>[]>([]);
  const history = PeriodExtendService.getExtndHistList(id!.toString(), {...pagination});

  useEffect(() => {
    if (!history.isLoading && !history.isFetching) {
      if (!!history.data) {
        setRowList(history.data.list.map((m,) => {
          return {
            key: m.etReqstId,
            ...m
          }
        }));
        setPagination((state) => ({...state, rowCount: history.data.totalItems}))
      }
    }
  }, [history.data, history.isLoading, history.isFetching])

  return <TableComponents<EqpmnsExtndHistList>
    showTotal
    isLoading={history.isLoading || history.isFetching}
    // rightContent={<Stack flexDirection={"row"}>
    //     <CustomIconButton
    //         startText={'엑셀저장'} icon={Icons.FileDownload}
    //         style={{borderRadius: '5px', fontSize: '16px', fontWeight: 'bold'}}
    //         onClick={async () => {
    //             await EquipmentInformationService.getEquipmentsHistInfoExcelDownload(id!.toString()).then((res) => {
    //                 const blob = new Blob([res]);
    //                 const fileObjectUrl = window.URL.createObjectURL(blob);
    //                 const link = document.createElement("a");
    //                 link.href = fileObjectUrl;
    //                 link.setAttribute(
    //                     "download",
    //                     `처리이력_리스트.xlsx`
    //                 );
    //                 document.body.appendChild(link);
    //                 link.click();
    //             });
    //         }}/>
    // </Stack>}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    tableCell={(data) => {
      let processKnd = ''
      let processResn = ''
      let processmake = ''
      if (commtCode && data)
        for (let i=0; data.processResn.split("_",4).length > i ; i++ ){
          if (data.processResn.split("_",4).length - 1 === i){
            processmake = processmake.concat(data.processResn.split("_",4)[i])
          }else if (data.processResn.split("_",4).length - 2 === i){
            processmake = toCommtCodeName(commtCode, "EQPMN_EXTEND",processmake.concat(data.processResn.split("_",4)[i])).concat(' ')
          }else processmake = processmake.concat(data.processResn.split("_",4)[i]+'_')
        }
        processKnd = toCommtCodeName(commtCode,"EQPMN_EXTEND", data.processKnd)
        processResn = processmake
        // processResn = data.processResn.split("_",4).length === 1 ? data.processResn :
        //   data.processResn.split("_",4).length === 2 ?
        //     toCommtCodeName(commtCode, "EQPMN_EXTEND", data.processResn.split("_",4)[0]) +' '+ data.processResn.split("_",4)[1]:
        //     toCommtCodeName(commtCode, "EQPMN_EXTEND", data.processResn.split("_",4)[0] + '_' + data.processResn.split("_",4)[1] + '_' +data.processResn.split("_",4)[2]) +' '+ data.processResn.split("_",4)[3]

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
            {<div style={{maxHeight: '200px', overflow: 'auto', paddingRight: '10px'}}>{processResn}</div>}</TableCell>
          <TableCell sx={{textAlign:'center', width:'150px'}} key={"memberNm-" + data.key}>{data.mberNm}</TableCell>
          <TableCell sx={{textAlign:'center', width:'200px'}} key={"opetrId-" + data.key}>{data.opetrId}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

const headCells: CustomHeadCell<EqpmnsExtndHistList>[] = [
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