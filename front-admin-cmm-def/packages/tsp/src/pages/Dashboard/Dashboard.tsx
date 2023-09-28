import React, {Fragment, useEffect, useState} from "react"
import {Stack, TableCell} from "@mui/material";
import {DashboardService} from "~/service/DashboardService";
import {CustomHeadCell, WithCustomRowData, TableComponents} from "shared/components/TableComponents";
import dayjs from "shared/libs/dayjs";
import {
  SubContents,
  TitleContents
} from "shared/components/LayoutComponents";
import {CustomButton} from "shared/components/ButtonComponents";
import {useNavigate} from "react-router-dom";
import {dayFormat, toTimeFormat} from "shared/utils/stringUtils";
import {NoticeInfoMgtService} from "~/pages/Operation/CustomerSupportMgt/Service/NoticeInfoMgtService";
import {CommonService} from "~/service/CommonService";
import {NoticeList} from "~/pages/Operation/CustomerSupportMgt/Model/Model";

const Dashboard = () => {
  const navigate = useNavigate()
  const information = DashboardService.DashboardData().data

  const handleClick1 = (title:string) => {
    if(title === '장비사용 신청관리')
      navigate('/tsp-admin/UseMgt/EquipmentMgt/EstimationMgt')
    else if(title === '장비사용 관리')
      navigate('/tsp-admin/UseMgt/EquipmentMgt/PeriodExtendMgt')
    else if(title === '컴퓨팅자원 사용관리')
      navigate('/tsp-admin/UseMgt/ResourceMgt/ApplyMgt')
  }

  const handleClick2 = (title:string) => {
    if(title === '장비사용 신청관리')
      navigate('/tsp-admin/UseMgt/EquipmentMgt/ApplyMgt')
    else if(title === '장비사용 관리')
      navigate('/tsp-admin/UseMgt/EquipmentMgt/ReportMgt')
    else if(title === '컴퓨팅자원 사용관리')
      navigate('/tsp-admin/UseMgt/ResourceMgt/UseMgt')
  }

  function DashboardBox(props: { title: string, cases: number, subTitle1: string, subTitle2: string, subNum1: number, subNum2: number }) {

    return (
      <div style={{
        width:'450px',
        margin: '20px',
        padding: '30px',
        border: 'solid 1px #d7dae6',
        borderRadius: '20px',
        color: '#222'
      }}>
        <div style={{fontSize: '20px', fontWeight: '600',textAlign:'center'}}>
          {props.title}
          <span style={{marginLeft: '10px', color: '#4063ec'}}>
          {props.cases}
            <span style={{color: '#222', fontSize: '16px', fontWeight: '400'}}>건</span>
        </span>
        </div>
        <div>
          <div style={{
            position: 'relative', display: 'flex',
            justifyContent: 'space-between',
            padding: '20px 20px',
            margin: '20px 0',
            backgroundColor: '#f5f5f5',
            borderRadius: '10px',
            textAlign: 'center',
            cursor:'pointer',
          }}
               onClick={() => handleClick1(props.title)}
          >
              <span style={{display:'flex', alignItems:'center', margin: 0, fontSize:'16px', color:'#707070'}}>{props.subTitle1}</span>
              <div style={{fontSize:'18px'}} className="num">
                <span style={{marginRight:'5px', fontSize:'30px', fontWeight:'700'}}>{props.subNum1}</span>건
              </div>
          </div>
          <div style={{
            position: 'relative', display: 'flex',
            justifyContent: 'space-between',
            padding: '20px 20px',
            margin: '20px 0',
            backgroundColor: '#f5f5f5',
            borderRadius: '10px',
            textAlign: 'center',
            cursor:'pointer',
          }}
               onClick={() => handleClick2(props.title)}
          >
            <span style={{display:'flex', alignItems:'center', margin: 0, fontSize:'16px', color:'#707070'}}>{props.subTitle2}</span>
            <div style={{fontSize:'18px'}}>
              <span style={{marginRight:'5px', fontSize:'30px', fontWeight:'700'}}>{props.subNum2}</span>건
            </div>
          </div>
        </div>
      </div>
    );
  }

  if(!information) return <></>

  return <TitleContents title={''}>
    <SubContents title={'미처리업무'} hideDivision maxHeight={'100%'} rightContent={<div
      style={{marginBottom: '20px', fontSize: '14px', alignSelf: 'flexEnd', color: '#707070'}}>{toTimeFormat(new Date().getTime())} 기준</div>}>
      <div style={{display:'flex', width:'100%', justifyContent:'space-around'}}>
      <DashboardBox title="장비사용 신청관리" cases={information.rentalTotal} subTitle1={'견적신청'} subNum1={information.estimateCount} subTitle2={'사용신청'} subNum2={information.useCount}/>
      <DashboardBox title="장비사용 관리" cases={information.usageTotal} subTitle1={'기간연장신청'} subNum1={information.extentionCount} subTitle2={'결과보고서 제출'} subNum2={information.reportCount}/>
      <DashboardBox title="컴퓨팅자원 사용관리" cases={information.resourceTotal} subTitle1={'사용신청'} subNum1={information.resourceCount} subTitle2={'반환요청'} subNum2={information.resourceReturnCount}/>
      </div>
    </SubContents>
    <SubContents
      hideDivision
      title={"공지사항"}
      maxHeight={'100%'}
      rightContent={
        <CustomButton
          type={"small"} color={"list"} label={"더 보기"}
          onClick={() => {
            navigate('/tsp-admin/Operation/About/Notice')
          }}/>
      }>
      <Notice/>
    </SubContents>
  </TitleContents>
}

const Notice = () => {
  const [pagination, setPagination] = useState({
    page: 0,
    rowsPerPage: 10,
    rowCount: 0,
  });
  const navigation = useNavigate();
  const [rowList, setRowList] = useState<WithCustomRowData<NoticeList>[]>([]);
  const information = NoticeInfoMgtService.getNoticeList({boardId:'tsp-notice', ...pagination})
  //const file = CommonService.getAttachmentIdInfo('attg-a75690a900c04a0c8757045f9fe756f3')
  // console.log(file)
  useEffect(() => {
    if (!information.isLoading || !information.isFetching) {
      if (!!information.data) {
        setRowList(information.data.list.map((m,) => {
          return {
            key: m.articleId,
            ...m,
          }
        }));
        setPagination((state) => ({...state, rowCount: information.data.totalItems}))
      }
    }
  }, [information.data, information.isLoading, information.isFetching])

  // const notice = DashboardService.Notice(pagination)

  // useEffect(() => {
  //   if (!!notice.data) {
  //     const data = notice.data.list.map((m, i) => {
  //       return {key: m.articleId, ...{title: m.title, createdDt: dayjs(m.createdDt).format('YYYY-MM-DD')}}
  //     });
  //
  //     setRowList(data);
  //     setPagination((state) => ({...state, rowCount: notice.data.totalItems}))
  //   }
  // }, [notice.data])

  return <TableComponents<NoticeList>
    hideRowPerPage isLoading={information.isLoading || information.isFetching}
    headCells={headCells}
    bodyRows={rowList}
    {...pagination}
    onChangePagination={(page, rowPerPage) => {
      setPagination((state) => ({...state, page: page, rowsPerPage: rowPerPage}))
    }}
    handleClick={(key: string) => {
      navigation(`/tsp-admin/Operation/About/Notice/${key}`);
    }}
    tableCell={(data) => {
      return (
        data ? <Fragment>
          <TableCell key={"number-" + data.key} align={"center"} width={'100px'}>{data.notice ? '-' : data.rn}</TableCell>
          <TableCell key={'title-' + data.key} align={"center"}>{data.title}</TableCell>
          <TableCell key={'createdDt-' + data.key} align={"center"} width={"220px"}>{dayFormat(data.createdDt)}</TableCell>
        </Fragment> : <></>
      )
    }}
  />
}

// const DashboardCount = () => {
//
//   const dashboardData: undefined | DashboardDataResponse = DashboardService.DashboardData().data
//   if (dashboardData === undefined)
//     return (<></>);
//
//   let dashboardMains: DashboardMain[];
//   let dashboardItems_1: DashboardItem[]
//   let dashboardItems_2: DashboardItem[]
//   let dashboardItems_3: DashboardItem[]
//
//   dashboardItems_1 = [
//     {
//       itemTitle: "견적신청",
//       itemSubtitle: "신청",
//       itemValue: dashboardData.estimateCount
//     },
//     {
//       itemTitle: "사용신청",
//       itemSubtitle: "신청",
//       itemValue: dashboardData.useCount
//     }
//   ]
//   dashboardItems_2 = [
//     {
//       itemTitle: "기간연장신청",
//       itemSubtitle: "신청",
//       itemValue: dashboardData.extentionCount
//     },
//     {
//       itemTitle: "결과보고서제출",
//       itemSubtitle: "제출",
//       itemValue: dashboardData.reportCount
//     }
//   ]
//   dashboardItems_3 = [
//     {
//       itemTitle: "사용신청",
//       itemSubtitle: "신청",
//       itemValue: dashboardData.resourceCount
//     },
//     {
//       itemTitle: "반환요청",
//       itemSubtitle: "요청",
//       itemValue: dashboardData.resourceReturnCount
//     }
//   ]
//
//   dashboardMains = [
//     {
//       title: "장비사용 신청관리",
//       value: dashboardData.rentalTotal,
//       items: dashboardItems_1
//     },
//     {
//       title: "장비 사용관리",
//       value: dashboardData.usageTotal,
//       items: dashboardItems_2
//     },
//     {
//       title: "컴퓨팅자원 사용관리",
//       value: dashboardData.resourceTotal,
//       items: dashboardItems_3
//     }
//   ]
//
//   return <>
//     <DashboardCountContents dashboardMain={dashboardMains}/>
//   </>
// }

const headCells: CustomHeadCell<NoticeList>[] = [
  {
    id: 'rn',
    align: "center",
    label: '번호',
  },
  {
    id: 'title',
    align: 'center',
    label: '제목',
  },
  {
    id: 'createdDt',
    align: "center",
    label: '등록일',
  },
];

export default Dashboard;
