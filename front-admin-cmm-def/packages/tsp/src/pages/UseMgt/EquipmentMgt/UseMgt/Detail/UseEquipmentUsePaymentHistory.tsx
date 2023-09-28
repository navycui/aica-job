import {useEffect, useState} from 'react'
import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  TableHead,
  tableCellClasses,
  LinearProgress
} from "@mui/material";
import {SubContents} from "shared/components/LayoutComponents";
import {CustomHeadCell, WithCustomRowData} from "shared/components/TableComponents";
import {styled} from "@mui/material/styles";
import {EqpmnUseRntfeeHist, UsageChargeDetailProps} from "~/service/Model";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {dayFormat, toTimeFormat} from "shared/utils/stringUtils";
import {useParams} from "react-router-dom";

export const UseEquipmentUsePaymentHistory = () => {
  return <SubContents title={"사용료 부과내역"} maxHeight={"100%"}>
    <ListView/>
  </SubContents>
}

const ListView = () => {
  const {id} = useParams()
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnUseRntfeeHist>[]>([]);
  const history = EquipmentService.getUseRntfee(id!.toString());
  let arr: string[] = [''];
  for (let i = 0; i < rowList.length; ++i) {
    if(rowList[i].rntfee !== null)
      arr[i] = rowList[i].rntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    else arr[i] = '0'
  }

  function subtotal(items: readonly EqpmnUseRntfeeHist[]) {
    return items.map(({rntfee}) => rntfee).reduce((sum, i) => sum + i, 0);
  }

  const subtotal1 = subtotal(rowList).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');

  useEffect(() => {
    if (!history.isLoading && !history.isFetching) {
      if (!!history.data) {
        setRowList(history.data.list.map((m, i) => {
          return {
            key: i.toString(),
            ...m,
            // rntfee 값이 null 일경우 오류
          }
        }));
      }
    }
  }, [history.data, history.isLoading, history.isFetching])

  return <>
    {(history.isLoading || history.isFetching) && <LinearProgress/>}
    <TableContainer sx={{borderTop: "1px solid #4063ec", width: "100%"}}>
      <Table sx={{minWidth: 700}}>
        <TableHead>
          <TableRow sx={{borderLeft: '1px solid #d7dae6', borderRight: '1px solid #d7dae6',}}>
            <StyledTableCell><span>번호</span></StyledTableCell>
            <StyledTableCell><span>일자</span></StyledTableCell>
            <StyledTableCell><span>구분</span></StyledTableCell>
            <StyledTableCell><span>사유</span></StyledTableCell>
            <TableCell align={'center'} sx={{fontWeight: 'bold'}}>
              <span>금액</span></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {
            rowList.map((m, i) => {
              return <TableRow key={i} sx={{borderLeft: '1px solid #d7dae6', borderRight: '1px solid #d7dae6',}}>
                <StyledTableCell sx={{minWidth: '61px'}}><span>{i + 1}</span></StyledTableCell>
                <StyledTableCell sx={{minWidth: '90px'}}><span>{`${dayFormat(m.creatDt)}`}</span></StyledTableCell>
                <StyledTableCell sx={{minWidth: '90px'}}><span>{`${m.useDiv}`}</span></StyledTableCell>
                <StyledTableCell sx={{minWidth: '210px'}}><span>{`${m.rqestResn}`}</span></StyledTableCell>
                <TableCell sx={{minWidth: '90px', textAlign: 'center'}}><span>{`${arr[i]}원`}</span></TableCell>
              </TableRow>
            })
          }
          <TableRow sx={{borderLeft: '1px solid #d7dae6', borderRight: '1px solid #d7dae6',}}>
            <StyledTableCell colSpan={4} sx={{minWidth: '61px'}}> {/*backgroundColor: '#eff1f8'}}>*/}
              <span>총 사용료</span></StyledTableCell>
            <TableCell sx={{
              minWidth: '90px',
              //backgroundColor: '#eff1f8',
              textAlign: 'center'
            }}><span>{`${subtotal1}원`}</span></TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  </>


  /*<TableComponents
      showTotal
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
      bodyRows={testdata}
      {...pagination}
      onChangePagination={(page, rowPerPage) => {
          setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
      }}

      tableCell={(index: number) => {
          const data = testdata.at(index) as any
          return (
              data ? <Fragment>
                  <TableCell key={"PaymentDate"} align={"center"}>{data.PaymentDate}</TableCell>
                  <TableCell key={"PaymentType"} align={"center"}>{data.PaymentType}</TableCell>
                  <TableCell key={"PaymentRange"} align={"left"}>{data.PaymentRange}</TableCell>
                  <TableCell key={"PaymentMuch"} align={"center"}>{data.PaymentMuch}</TableCell>
              </Fragment> : <></>
          )
      }}
  />*/
}
export const StyledTableCell = styled(TableCell)(() => ({
  [`&.${tableCellClasses.head}`]: {
    //backgroundColor: '#f5f5f5',
    borderRight: '1px solid #d7dae6',
    fontWeight: 'bold',
    textAlign: 'center'
  },
  [`&.${tableCellClasses.body}`]: {
    borderRight: '1px solid #d7dae6',
    textAlign: 'center'
  },
}));

export const usageTempData: UsageChargeDetailProps[] = [
  {
    number: '1',
    useDivision: '사용금액',
    division: '2021-11-01',
    fare: 3000
  },
  {
    number: '2',
    useDivision: '기간연장',
    division: '2021-11-01',
    fare: 1000
  },
  {
    number: '3',
    useDivision: '추가금액',
    division: '2021-11-01',
    fare: 1000
  },
]
/*
export const testdata: WithCustomRowData<NoticeData>[] = [
  {
    key: "123",
    assetNo: "1",
    PaymentDate: "2021-10-31",
    PaymentType: "추가금액",
    PaymentRange: "청구사유가 출력됩니다",
    PaymentMuch: "1000",
  },
]
*/


/*export interface NoticeData {
  assetNo?: string
  PaymentDate: string
  PaymentType: string
  PaymentRange: string
  PaymentMuch: string
}

export const headCells: CustomHeadCell<NoticeData>[] = [
  {
    id: 'PaymentDate',
    align: 'center',
    label: '일자',
  },
  {
    id: 'PaymentType',
    align: "center",
    label: '구분',
  },
  {
    id: 'PaymentRange',
    align: "center",
    label: '청구사유',
  },
  {
    id: 'PaymentMuch',
    align: "center",
    label: '금액',
  },
];*/

