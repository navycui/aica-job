import {UseQueryResult} from "react-query";
import {WithResponse} from "shared/utils/Model";
import {
  AnalsReqstProcess, EqpmnUseReqstHistList,
  SearchParam, UseEquipAnalysHistListData,
  UseEquipAnalysisDetail,
  UseEquipAnalysisListData,
  WithPagination
} from "~/service/Model";
import {AxiosGet, AxiosPut, GetQuery} from "shared/libs/axios";

export class AnalysisService {
  // 분석환경 사용신청 정보 조회
  static getList(param: {
    page: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<UseEquipAnalysisListData>>, any> {
    return GetQuery("/anals",{...param,page: param.page +1, itemsPerPage:param.rowsPerPage})
  }

  // 분석환경 사용신청 상세정보 조회
  static getEquipAnalsDetail(reqstId: string) :UseQueryResult<UseEquipAnalysisDetail> {
    return GetQuery(`/anals/${reqstId}`)
  }

  // 분석환경 사용신청 상세정보 처리내역 조회
  static getEquipAnalsHistList(reqstId: string, param: { page: number, rowsPerPage: number }) : UseQueryResult<WithResponse<WithPagination<UseEquipAnalysHistListData>>,any> {
    return GetQuery(`/anals/hist-list/${reqstId}`, {...param, page:param.page +1, itemsPerPage:param.rowsPerPage})
  }

  // 분석환경 엑셀 다운로드
  static getEquipAnalsExcelDownload(param:SearchParam) {
    return AxiosGet("/anals/excel-dwld", param, {responseType: 'blob'});
  }

  // 분석환경 처리
  static putEquipAnalsProcess(param:AnalsReqstProcess) {
    return AxiosPut('/anals/process', param)
  }
}