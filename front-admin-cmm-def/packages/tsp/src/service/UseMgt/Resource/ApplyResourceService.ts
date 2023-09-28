import {AxiosGet, AxiosPut, GetQuery} from "shared/libs/axios";
import {UseQueryResult} from "react-query";
import {
  ApplyResourceApproveProcess,
  ApplyResourceDetailData, ApplyResourceHistData, ApplyResourceRejectProcess, ApplyResourceSpmProcess, WithPagination
} from "~/service/Model";
import {WithResponse} from "shared/utils/Model";
import {ApplyResourceMgtData} from "~/service/Model";


export class ApplyResourceService {
  static getList(param: {
    page: number
    rowCount: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<ApplyResourceMgtData>>, any> {
    return GetQuery("/resrce",{...param,page: param.page +1, itemsPerPage:param.rowsPerPage})
  }

  static getApplyResourceMgtInfo(reqstId: string) : UseQueryResult<ApplyResourceDetailData, any> {
    return GetQuery("/resrce/" + reqstId)
  }

  static getProcessHistoryList(reqstId: string, param: {
    page: number,
    rowsPerPage: number,
  }) : UseQueryResult<WithResponse<WithPagination<ApplyResourceHistData>>, any> {
    return GetQuery('/resrce/' + reqstId + '/hist/', {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getApplyResourceHistInfoExcelDownload(param: {}) {
    return AxiosGet("/resrce/excel-dwld/", param ,{responseType: 'blob'});
  }
  //보완
  static putApplyResourceSpmCheck(data: ApplyResourceSpmProcess) : Promise<WithResponse<ApplyResourceDetailData>> {
    return AxiosPut('/resrce/' + data.reqstId + '/spm-request/', data);
  }
  //반려
  static putApplyResourceRejectCheck(data: ApplyResourceRejectProcess) : Promise<WithResponse<ApplyResourceDetailData>> {
    return AxiosPut('/resrce/' + data.reqstId + '/reject/', data);
  }

  static putApplyResourceApproveCheck(data: ApplyResourceApproveProcess) : Promise<WithResponse<ApplyResourceDetailData>> {
    return AxiosPut('/resrce/' + data.reqstId + '/approve/', data)
  }
}