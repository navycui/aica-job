import {AxiosGet, AxiosPut, GetQuery} from "shared/libs/axios";
import {UseQueryResult} from "react-query";
import {
  EqpmnExtndDetail,
  EqpmnExtndList, EqpmnsExtndDetailProcess, EqpmnsExtndHistList,
  WithPagination
} from "~/service/Model";
import {WithResponse} from "shared/utils/Model";

export class PeriodExtendService {
  // 기간연장신청 목록 조회
  static getList (param: {
    page:number
    rowsPerPage: number
  }) : UseQueryResult<WithResponse<WithPagination<EqpmnExtndList>>, any> {
    return GetQuery("/eqpmns/extnd", {...param, page: param.page+1, itemsPerPage: param.rowsPerPage})
  }

  // 기간연장신청 상세 조회
  static getExtndDetailInfo(extnReqstId: string) : UseQueryResult<EqpmnExtndDetail, any> {
    return GetQuery(`/eqpmns/extnd/${extnReqstId}`)
  }

  // 기간연장신청 엑셀다운로드
  static getExtndExcelDownload() {
    return AxiosGet(`/eqpmns/extnd/excel-dwld`, undefined, {responseType: 'blob'});
  }

  // 기간연장신청 처리이력 조회
  static getExtndHistList (extnReqstId:string, param: {
    page:number
    rowsPerPage: number
  }) : UseQueryResult<WithResponse<WithPagination<EqpmnsExtndHistList>>, any> {
    return GetQuery(`/eqpmns/extnd/hist/${extnReqstId}`, {...param, page: param.page+1, itemsPerPage: param.rowsPerPage})
  }

  // 기간연장신청 상세 처리
  static putExtndDetailProcess(extnReqstId:string, data:EqpmnsExtndDetailProcess) {
    return AxiosPut(`/eqpmns/extnd/${extnReqstId}`, data)
  }
}