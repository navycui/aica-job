import {UseQueryResult} from "react-query";
import {BaseResponse, WithListResponse, WithResponse} from "shared/utils/Model";
import {DiscountData, EquipmentClassifyData, EquipmentClassifyRequest, WithPagination} from "~/service/Model";
import {AxiosDelete, AxiosPost, AxiosPut, GetQuery} from "shared/libs/axios";

export class DiscountService {
  static getDscnt(param: {
    dscntResn?: string
    page: number,
    rowsPerPage: number,
    rowCount: number
  }): UseQueryResult<WithResponse<WithPagination<DiscountData>>, any> {
    return GetQuery("/eqpmns/dscnt", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static getDscntApply(param: {
    dscntResn?: string
    page: number,
    rowsPerPage: number,
    rowCount: number
  }): UseQueryResult<WithResponse<WithPagination<DiscountData>>, any> {
    return GetQuery("/eqpmns/dscnt/apply", {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }

  static postDscnt(data: DiscountData): Promise<WithResponse<DiscountData>> {
    return AxiosPost("/eqpmns/dscnt", data)
  }

  static putDscnt(data: DiscountData[]): Promise<WithListResponse<DiscountData>> {
    return AxiosPut("/eqpmns/dscnt", data)
  }
}
