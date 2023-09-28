import {useMutation, UseMutationResult, useQuery, UseQueryResult} from "react-query";
import {
  EquipmentClassifyData,
  EquipmentClassifyRequest
} from "~/service/Model";
import {AxiosDelete, AxiosGet, AxiosPost, AxiosPut, DeleteQuery, GetQuery, PostQuery} from "shared/libs/axios";
import {BaseResponse, WithListResponse, WithResponse} from "shared/utils/Model";

export class EquipmentCategoryService {
  static getRoot(): UseQueryResult<WithListResponse<EquipmentClassifyData>, any> {
    return GetQuery("/eqpmns/categories")
  }

  static getCategory(eqpmnClId: string): UseQueryResult<WithResponse<EquipmentClassifyData[]>, any> {
    return GetQuery("/eqpmns/categories/" + eqpmnClId)
  }

  static putCategory(eqpmnClfcId: string, req: EquipmentClassifyRequest[]): Promise<WithListResponse<EquipmentClassifyData>> {
    return AxiosPut("/eqpmns/categories/" + eqpmnClfcId, req)
  }

  static deleteCategory(eqpmnLclasId: string, req: EquipmentClassifyRequest[]): Promise<WithListResponse<EquipmentClassifyData>> {
    return AxiosDelete("/eqpmns/categories", req)
  }
}
