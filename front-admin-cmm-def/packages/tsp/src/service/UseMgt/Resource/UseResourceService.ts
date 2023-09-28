import {AxiosGet, AxiosPut, GetQuery} from "shared/libs/axios";
import {UseQueryResult} from "react-query";
import {
  UseResourceCancelProcess,
  UseResourceDetailData,
  UseResourceHistData,
  UseResourceMgtData, UseResourceReqReturnProcess, UseResourceReturnProcess,
  WithPagination
} from "~/service/Model";
import {WithResponse} from "shared/utils/Model";


export class UseResourceService {
  static getList(param: {
    page: number
    rowCount: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<UseResourceMgtData>>, any> {
    return GetQuery("/resrce/resrce-use/",{...param,page: param.page +1, itemsPerPage:param.rowsPerPage})
  }

  static getUseResourceMgtInfo(reqstId: string) : UseQueryResult<UseResourceDetailData, any> {
    return GetQuery("/resrce/resrce-use/" + reqstId)
  }

   static putUseResourceApplyCancel(data: UseResourceCancelProcess): Promise<WithResponse<UseResourceDetailData>> {
     return AxiosPut("/resrce/resrce-use/" + data.reqstId + "/cancel", data)
   }

  static putUseResourceApplyReturn(data: UseResourceReturnProcess): Promise<WithResponse<UseResourceDetailData>> {
    return AxiosPut("/resrce/resrce-use/" + data.reqstId + "/return", data)
  }

  static putUseResourceApplyReqReturn(data: UseResourceReqReturnProcess): Promise<WithResponse<UseResourceDetailData>> {
    return AxiosPut("/resrce/resrce-use/" + data.reqstId + "/req-return", data)
  }

  static getProcessHistoryList(reqstId: string, param: {
    page: number,
    rowsPerPage: number,
  }) : UseQueryResult<WithResponse<WithPagination<UseResourceHistData>>, any> {
    return GetQuery('/resrce/resrce-use/' + reqstId + '/hist/', {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getUseResourceHistInfoExcelDownload(param: {}) {
    return AxiosGet("/resrce/resrce-use/excel-dwld", param ,{responseType: 'blob'});
  }
}