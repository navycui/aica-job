import {AxiosGet, AxiosPut, GetQuery} from "shared/libs/axios";
import {UseQueryResult} from "react-query";
import {
  AditRntfee, EqpmnApplyReqstProcess, EqpmnEstmtModifyPrice, EqpmnReportDetail, EqpmnReportList,
  EqpmnReqstProcess, EqpmnsUseExtnd,
  EqpmnUseDetail, EqpmnUseExtendPeriod, EqpmnUseHistlist,
  EqpmnUseList,
  EqpmnUseReqstDetail,
  EqpmnUseReqstHistList,
  EqpmnUseReqstList, EqpmnUseRntfeeHist,
  NpyProcess, RcpmnyGdcc, SearchParam, StatisticsList,
  TkoutDlbrt, UseReprtProcess,
  WithPagination
} from "~/service/Model";
import {WithResponse} from "shared/utils/Model";

export class EquipmentService {
  static getEquipApplyList(param: {
    page: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<EqpmnUseReqstList>>, any> {
    return GetQuery("/eqpmns/use-reqst", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getUseEquipListExcelDownload() {
    return AxiosGet("/eqpmns/estmts/use-reqst/excel-dwld", undefined, {responseType: 'blob'});
  }

  static getUseEquipListApplyInfo(reqstId: string): UseQueryResult<EqpmnUseReqstDetail, any> {
    return GetQuery("/eqpmns/use-reqst/" + reqstId)
  }

  // 사용신청 처리내역 조회
  static getApplyEquipDetailHistList(reqstId: string, param: { page: number, rowsPerPage: number }) : UseQueryResult<WithResponse<WithPagination<EqpmnUseReqstHistList>>,any> {
    return GetQuery("eqpmns/use-reqst/hist-list/" + reqstId, {...param, page:param.page +1, itemsPerPage:param.rowsPerPage})
  }

  static getApplyEquipExcelDownload(param:SearchParam) {
    return AxiosGet("/eqpmns/use-reqst/excel-dwld", param, {responseType: 'blob'});
  }

  // 장비사용관리 정보 조회
  static getEquipUseList(param: {
    page: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<EqpmnUseList>>, any> {
    return GetQuery("/eqpmns/use", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  // 장비사용관리 상세정보 조회
  static getEquipUseDetail(reqstId: string): UseQueryResult<EqpmnUseDetail, any> {
    return GetQuery("/eqpmns/use/" + reqstId)
  }

  // 장비사용관리 추가금액설정
  static putUseAditRntfee(data:AditRntfee) {
    return AxiosPut(`/eqpmns/use/adit-rntfee`, data)
  }

  // 장비사용관리 엑셀 다운로드
  static getUseExcelDownload(param:SearchParam) {
    return AxiosGet(`/eqpmns/use/excel-dwld`, param, {responseType:'blob'})
}

// 장비사용관리 사용료 부과내역
static getUseRntfee(reqstId:string): UseQueryResult<WithResponse<WithPagination<EqpmnUseRntfeeHist>>, any> {
    return GetQuery(`/eqpmns/use/rntfee/${reqstId}`)
}

// 장비사용관리 기간연장관리 조회
  static getUseExtnd(reqstId: string, param: {page: number, rowsPerPage: number}) : UseQueryResult<WithResponse<WithPagination<EqpmnUseExtendPeriod>>,any> {
    return GetQuery(`eqpmns/use/extnd/${reqstId}`, {...param, page:param.page +1, itemsPerPage:param.rowsPerPage})
  }

// 장비사용관리 사용신청 처리내역 조회
  static getUseHistlist(reqstId:string, param: { page: number, rowsPerPage: number }) : UseQueryResult<WithResponse<WithPagination<EqpmnUseHistlist>>,any> {
    return GetQuery(`eqpmns/use/hist-list/${reqstId}`, {...param, page:param.page +1, itemsPerPage:param.rowsPerPage})
  }

  // 장비사용관리 미납처리
  static putUseNpyProcess(data: NpyProcess) : Promise<WithResponse<NpyProcess>> {
    return AxiosPut('/eqpmns/use/npy-process', data)
}

  // 장비사용관리 기간연장
  static putUseExtnd(data:EqpmnsUseExtnd) {
    return AxiosPut('eqpmns/use/extnd', data)
  }

  // 장비사용관리 반입 완료처리
  static putUseTkin(reqstId: string) {
    return AxiosPut('/eqpmns/use/tkin', {param:reqstId})
  }

  // 장비사용 할인 적용
  static putUseDscnt(data:EqpmnEstmtModifyPrice) {
    return AxiosPut('/eqpmns/use/dscnt', data)
  }

  // 장비사용관리 장비사용처리
  static putUseReqstProcess(data: EqpmnReqstProcess) {
    return AxiosPut('/eqpmns/use/process', data)
  }

  // 장비사용관리 입금 안내문 발송
  static putUseRcpmnyGdcc(data: RcpmnyGdcc) :Promise<WithResponse<RcpmnyGdcc>> {
    return AxiosPut('/eqpmns/use/rcpmny-gdcc', data)
  }

  static putUseReqstCancel(reqstId: string) {
    return AxiosPut(`/eqpmns/use/reqst-cancel/${reqstId}`)
  }

  // 사용신청 승인 처리
  static putUseReqstConsent(reqstId: string) {
    return AxiosPut('/eqpmns/use-reqst/consent', {param:reqstId})
  }

  // 사용신청 할인 적용
  static putUseReqstDscnt(data: EqpmnEstmtModifyPrice) {
    return AxiosPut('/eqpmns/use-reqst/dscnt', data)
  }

  // 사용신청 보완,반려 처리
  static putUseEquipEstmtCheck(data: EqpmnApplyReqstProcess) : Promise<WithResponse<EqpmnUseReqstDetail>> {
    return AxiosPut('/eqpmns/use-reqst/process', data)
  }

  // 사용신청 반출심의 처리
  // put 보내고 리턴값 받는지 작동여부 확인필요(!!금성!!)
  static putUseReqstTkout(data :TkoutDlbrt ): Promise<WithResponse<EqpmnUseReqstDetail>> {
    return AxiosPut('/eqpmns/use-reqst/tkout', data)
  }

  // 결과보고서 리스트 조회
  static getReprtList(param: {
    page: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<EqpmnReportList>>, any> {
    return GetQuery("/eqpmns/reprt", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  // 결과보고서 관리 엑셀 다운로드
  static getReprtExcelDownload() {
    return AxiosGet(`/eqpmns/reprt/excel-dwld`, undefined, {responseType: 'blob'})
  }

  // 결과보고서 보고서정보 상세리스트
  static getEquipReprtDetail(reprtId: string): UseQueryResult<EqpmnReportDetail, any> {
    return GetQuery("/eqpmns/reprt/" + reprtId)
  }

  // 결과보고서 처리내역 조회
  static getReprtHistlist(reprtId:string, param: { page: number, rowsPerPage: number }) : UseQueryResult<WithResponse<WithPagination<EqpmnUseHistlist>>,any> {
    return GetQuery(`eqpmns/reprt/hist-list/${reprtId}`, {...param, page:param.page +1, itemsPerPage:param.rowsPerPage})
  }

  // 결과보고서 처리
  static putEquipReprtProcess(data: UseReprtProcess) {
    return AxiosPut('/eqpmns/reprt/process', data)
  }

  // 통계 조회
  static getStatisticsList(data:SearchParam): UseQueryResult<StatisticsList> {
    return GetQuery('/statistics',data)
  }

  // 통계 엑셀 다운로드
  static getStatisticsExcelDownload() {
    return AxiosGet('statistics/excel-dwld', undefined, {responseType:'blob'})
  }
}