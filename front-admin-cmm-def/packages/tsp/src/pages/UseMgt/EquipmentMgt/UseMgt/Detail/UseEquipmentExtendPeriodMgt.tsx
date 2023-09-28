import React, {Fragment, useEffect, useState} from 'react'
import {LoadingProgress, SubContents} from "shared/components/LayoutComponents";
import {CustomHeadCell, TableComponents, WithCustomRowData} from "shared/components/TableComponents";
import {TableCell} from "@mui/material";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate, useParams} from "react-router-dom";
import UsePeriodSelect from "~/pages/UseMgt/EquipmentMgt/UsePeriodSelect";
import {useCommtCode} from "~/utils/useCommtCode";
import {toCommtCodeName} from "~/utils/CommtCodeUtil";
import {EqpmnUseDetail, EqpmnUseExtendPeriod, StatusState} from "~/service/Model";
import {EquipmentService} from "~/service/UseMgt/Equipment/EquipmentService";
import {dayFormat, toTimeFormat} from "shared/utils/stringUtils";
import {CommonService} from "~/service/CommonService";

export const UseEquipmentExtendPeriodMgt: React.FC<{
  state?: EqpmnUseDetail
}> = props => {
  const {commtCode} = useCommtCode(["EQPMN_REQST_ST", 'EQPMN_PAYMENT'])
  const {id} = useParams()
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const navigation = useNavigate();
  const [extendPeriodView, setExtendPeriodView] = useState(false)
  const information = EquipmentService.getUseExtnd(id!.toString(), {...pagination})
  const applyInformation = EquipmentService.getEquipUseDetail(id!.toString())
  const [rowList, setRowList] = useState<WithCustomRowData<EqpmnUseExtendPeriod>[]>([]);
  const [state, setState] = useState<EqpmnUseDetail>();
  const [date, setDate] = useState<string[]>([])
  const [startTime, setStartTime] = useState<number>(0)

  useEffect(() => {
    if (!information.isLoading && !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m, i) => {
          return {
            key: m.etReqstId,
            ...m,
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  useEffect(() => {
    if (!applyInformation.isLoading && !applyInformation.isFetching) {
      if (!!applyInformation.data) {
        setState({
          ...applyInformation.data
        });
      }
    }
  }, [applyInformation.data, applyInformation.isLoading, applyInformation.isFetching])

  useEffect(() => {
    if (rowList.length > 0) {
      let result: string[] = []
      for (let i = 0; i < rowList.length; ++i) {
        if (rowList[i].reqstSttus === StatusState.APPROVE) {
          result = result.concat(getDatesStartToLast(rowList[i].useBeginDt, rowList[i].useEndDt))
        }
      }
      setDate(date.concat(result));
    }
    else if (state) {
      let result: string[] = []
      result = getDatesStartToLast(state.useBeginDt, state.useEndDt)
      setDate(result)
    }
  }, [rowList, state])

  const getDatesStartToLast = (startDate: number, lastDate: number) => {
    let result: string[] = [];
    let curDate = new Date(startDate);
    let endDate = new Date(lastDate);
    const beginDt = new Date().setMonth((new Date().getMonth() - 1), 1)
    const endDt = new Date().setMonth(new Date().getMonth(), (new Date().getDate() - 1))

    let dateBegin = new Date(beginDt);
    let dateEnd = new Date(endDt);
    for (; dayFormat(dateBegin.getTime()) <= dayFormat(dateEnd.getTime());) {
      result.push(dayFormat(dateBegin.getTime()))
      dateBegin.setDate(dateBegin.getDate() + 1);
    }
    if(lastDate > startTime) setStartTime(lastDate)
    curDate.setHours(0); curDate.setMinutes(0)
    endDate.setDate(endDate.getDate() - 1); endDate.setHours(0); endDate.setMinutes(0)
    while (curDate <= endDate) {
      result.push(dayFormat(curDate.getTime()));
      curDate.setDate(curDate.getDate() + 1);
    }
    return result
  }
  return <SubContents
    title={"기간연장"}
    maxHeight={"100%"}
    rightContent={
      <Fragment>
        {
          (props.state!.tkoutAt && !extendPeriodView) && <CustomButton
                label={"사용기간연장"} type={"small"} color={"list"}
                onClick={() => {
                  setExtendPeriodView(true)
                }}/>
        }
      </Fragment>
    }>
    {
      extendPeriodView ? <UsePeriodSelect
        onClickSave={() => {
          setExtendPeriodView(false)
        }}
        onClickList={() => setExtendPeriodView(false)}
        startTime={startTime === 0 ? Number(props.state?.useEndDt) : startTime}
        disabled={date}
      /> : <TableComponents<EqpmnUseExtendPeriod>
        showTotal
        isLoading={information.isLoading || information.isFetching}
        headCells={headCells}
        bodyRows={rowList}
        {...pagination}
        onChangePagination={(page, rowsPerPage) => {
          setPagination((state) => ({...state, page: page, rowsPerPage: rowsPerPage}))
        }}
        tableCell={(data) => {
          let reqstSttus = ''
          let pymntMth = ''
          if (commtCode && data) {
            reqstSttus = toCommtCodeName(commtCode, "EQPMN_REQST_ST", data.reqstSttus)
            pymntMth = toCommtCodeName(commtCode, "EQPMN_PAYMENT", data.pymntMth)
          }

          return data ? <Fragment>
            <TableCell key={"reqstSttus-" + data.key} align={"center"}>{reqstSttus}</TableCell>
            <TableCell key={"period-" + data.key} align={"center"}>{`${toTimeFormat(data.useBeginDt)} ~ ${toTimeFormat(data.useEndDt)}`}</TableCell>
            <TableCell key={"usgtm-" + data.key} align={"center"}>{`${data.usgtm || 0}시간`}</TableCell>
            <TableCell key={"rntfeeHour-" + data.key} align={"center"}>{`${data.rntfeeHour.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')}원`}</TableCell>
            <TableCell key={"rntfee-" + data.key} align={"center"}>{`${data.rntfee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')}원`}</TableCell>
            <TableCell key={"dscntAmount-" + data.key} align={"center"}>{`${data.dscntAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') || 0}원`}</TableCell>
            <TableCell key={"pymntMth-" + data.key} align={"center"} width={'100px'}>{pymntMth}</TableCell>
            <TableCell key={"detail-" + data.key} align={"center"}>
              <CustomButton key={'button-' + data.key} label={"상세보기"} type={"small"} color={"list"} onClick={() => {
                // 해당 기간연장
                navigation('/tsp-admin/UseMgt/EquipmentMgt/PeriodExtendMgt/' + data.etReqstId);
              }}/>
            </TableCell>
          </Fragment> : <></>
        }}
      />
    }
  </SubContents>
}

const headCells: CustomHeadCell<EqpmnUseExtendPeriod>[] = [
  {
    id: 'reqstSttus',
    align: 'center',
    label: '신청상태',
  },
  {
    id: 'useBeginDt',
    align: "center",
    label: '연장 신청기간',
  },
  /*{
    id: 'usingTime',
    align: "center",
    label: '사용 시간기간',
  },*/
  {
    id: 'usgtm',
    align: "center",
    label: '사용시간',
  },
  {
    id: 'rntfeeHour',
    align: "center",
    label: '1시간 사용료',
  },
  {
    id: 'rntfee',
    align: "center",
    label: '사용금액',
  },
  {
    id: 'dscntAmount',
    align: "center",
    label: '할인적용금액',
  },
  {
    id: 'pymntMth',
    align: "center",
    label: '지불방법',
  },
  {
    id: 'detail',
    align: "center",
    label: '상세내역',
  },
];
