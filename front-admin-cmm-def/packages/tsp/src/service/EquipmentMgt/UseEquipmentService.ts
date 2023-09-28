import {UseQueryResult} from "react-query";
import {
  UseEquipmentData,
  WithPagination,
} from "~/service/Model";
import {AxiosDelete, AxiosGet, AxiosPost, AxiosPut, GetQuery} from "shared/libs/axios";
import {BaseResponse, WithResponse} from "shared/utils/Model";


export class UseEquipmentService {
  // 장비 정보 리스트 조회
  static getList(param: { page: number; rowCount: number; rowsPerPage: number }): UseQueryResult<WithResponse<WithPagination<UseEquipmentData>>, any> {
    return GetQuery("/eqpmns/mngms/status/", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getEquipmentListExcelDownload(param: {}) {
    return AxiosGet("/eqpmns/mngms/status/excel-dwld", param, {responseType: 'blob'});
  }
}
