import {UseQueryResult} from "react-query";
import {
  EquipmentStateChangeModalData,
  EquipmentData,
  EquipmentMgtInfoData,
  EquipmentMgtInfoRequest,
  WithPagination,
  EquipmentRegisterData, EquipmentHistoryInfoData, EquipmentMgtHistoryInfoData, EquipmentInformationData,
} from "~/service/Model";
import {AxiosDelete, AxiosGet, AxiosPost, AxiosPut, GetQuery} from "shared/libs/axios";
import {BaseResponse, WithValueResponse, WithResponse} from "shared/utils/Model";
import {UseInfiniteQueryResult} from "react-query/types/react/types";


export class EquipmentInformationService {

  // 장비 등록
  static postEquipment(data: any): Promise<WithResponse<EquipmentRegisterData>> {
    return AxiosPost("/eqpmns", data)
  }

  // 장비 관리이력 조회
  static getEquipmentsMgtHist(eqpmnId: string, status: string, param: {
    page: number,
    rowsPerPage: number,
    rowCount: number
  }): UseQueryResult<WithResponse<WithPagination<EquipmentMgtHistoryInfoData>>, any> {
    let st = ""
    if (status === '수리') st = 'REPAIR'
    else if (status === '교정') st = 'CORRECTION'
    else if (status === '점검') st = 'INSPECT'
    // const url = status === "전체" || "" ? "/equipments/info/managementhistories/" + eqpmnId : "/equipments/info/managementhistories/" + eqpmnId + "?eqpmnMgtType=" + status;

    return GetQuery("/eqpmns/mngms/mngmhistlist/" + eqpmnId, {
      manageDiv: st,
      page: param.page + 1,
      itemsPerPage: param.rowsPerPage
    })
  }

  // 장비 정보 리스트 조회
  static getList(param: {
    eqpmnSt?: string
    eqpmnNm?: string
    tkout?: boolean
    modelNm?: string
    assetNo?: string
    page: number
    rowCount: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<EquipmentInformationData>>, any> {
    return GetQuery("/eqpmns", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  // 관리 정보 조회
  static getEquipmentsMgtInfo(eqpmnId: string): UseQueryResult<EquipmentMgtInfoData, any> {
    return GetQuery("/eqpmns/mngms/" + eqpmnId)
  }

  // 상세 정보 조회
  static getEquipmentsDetailsInfo(eqpmnId: string): UseQueryResult<WithResponse<EquipmentData>, any> {
    return GetQuery("/eqpmns/mngms/details/" + eqpmnId)
  }

  // 상세 정보 저장
  static putEquipmentDetailInfo(eqpmnId: string, data: any): Promise<WithResponse<EquipmentData>> {
    return AxiosPut("/eqpmns/mngms/details/" + eqpmnId, data)
  }

  static getEquipmentsCorrect(eqpmnId: string): Promise<EquipmentStateChangeModalData> {
    return AxiosGet("/equipments/info/" + eqpmnId + "/correct")
  }

  static postEquipmentsCorrect(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<EquipmentMgtInfoData> {
    return AxiosPost("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  static getEquipmentsCorrectFinish(eqpmnId: string, manageDiv: string): Promise<EquipmentStateChangeModalData> {
    return AxiosGet("/eqpmns/mngms/procs/" + eqpmnId, {eqpmnId: eqpmnId, manageDiv: manageDiv})
  }

  static putEquipmentsCorrectFinish(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<EquipmentMgtInfoData> {
    return AxiosPut("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  static getEquipmentManageHistExcelDownload(eqpmnId: string,mgtStatus: string) {
    const param: {manageDiv?: string} = {}
    if (mgtStatus === '교정') param.manageDiv = 'CORRECTION'
    else if (mgtStatus === '수리') param.manageDiv = 'REPAIR'
    else if (mgtStatus === '점검') param.manageDiv = 'INSPECT'

    return  AxiosGet("/eqpmns/mngms/mngmhistList/excel-dwld/" + eqpmnId, param, {responseType: 'blob'});
  }

  static getEquipmentListExcelDownload(param?:{
    eqpmnSt?: string
    eqpmnNm?: string
    tkout?: boolean
    modelNm?: string
    assetNo?: string}) {
    return AxiosGet("/eqpmns/excel-dwld", param, {responseType: 'blob'});
  }

  static getEquipmentsHistInfo(eqpmnId: string, param: {
    page: number,
    rowsPerPage: number,
    rowCount: number
  }): UseQueryResult<WithResponse<WithPagination<EquipmentHistoryInfoData>>, any> {
    return GetQuery("/eqpmns/mngms/prohistlist/" + eqpmnId, {page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getEquipmentsHistInfoExcelDownload(eqpmnId: string) {
    return AxiosGet("/equipments/info/histories/excel-dwld/" + eqpmnId, undefined, {responseType: 'blob'});
  }

  static deleteEquipment(eqpmnId: string): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosDelete("/eqpmns/mngms/" + eqpmnId)
  }

  static putEquipmentDisuseAt(eqpmnId: string, disuseAt: boolean): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/unavilable/" + eqpmnId, {param: disuseAt.toString()})
  }

  static putEquipmentMgtInfo(eqpmnId: string, data: EquipmentMgtInfoRequest): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/" + eqpmnId, data)
  }

  // 점검 조회
  static getEquipmentsEndCheck(eqpmnId: string, manageDiv: string): Promise<EquipmentStateChangeModalData> {
    return AxiosGet("/eqpmns/mngms/procs/" + eqpmnId, {eqpmnId: eqpmnId, manageDiv: manageDiv})
  }

  // 장비 점검 등록
  static postEquipmentsCheck(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPost("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 점검 완료
  static putEquipmentsEndCheck(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 점검 수정
  static putEquipmentsCheckContent(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 수리 완료 조회
  static getEquipmentsEndRepair(eqpmnId: string, manageDiv: string): Promise<EquipmentStateChangeModalData> {
    return AxiosGet("/eqpmns/mngms/procs/" + eqpmnId, {eqpmnId: eqpmnId, manageDiv: manageDiv})
  }

  // 장비 수리 등록
  static postEquipmentsRepair(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPost("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 수리 완료
  static putEquipmentsEndRepair(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 수리 내역등록
  static putEquipmentsRepairContent(eqpmnId: string, data: EquipmentStateChangeModalData): Promise<WithResponse<EquipmentMgtInfoData>> {
    return AxiosPut("/eqpmns/mngms/procs/" + eqpmnId, data)
  }

  // 장비 상세정보 이미지 업로드
  static postEquipmentsDetailUploadImage(eqpmnId: string, data: any): Promise<WithValueResponse<string>> {
    return AxiosPost("/tsp-api/api/admin/eqpmns/mngms/details/" + eqpmnId + "/image-upload", data, {contentType: "formData", baseURL: process.env.REACT_APP_DOMAIN})
  }

  // 장비 등록 이미지 업로드
  static postEquipmentsUploadImage(data: any): Promise<WithValueResponse<string>> {
    return AxiosPost("/tsp-api/api/admin/eqpmns/image-upload", data, {contentType: "formData", baseURL: process.env.REACT_APP_DOMAIN})
  }
}
