import { UseQueryResult } from "react-query";
import {WithResponse} from "shared/utils/Model";
import {AxiosDelete, AxiosPost, AxiosPut, GetQuery} from "shared/libs/axios";
import {UsptExpertClModel} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {WithPagination} from "~/service/Model";

export class ExpertClassificationMgtService {
  /**
   * 내담당목록 트리 조회
   * @return List<UsptExpertCl>
   */

  static getExpertClfcMyTreeList(): UseQueryResult<WithResponse<any>, any> {
    return GetQuery(`/pms/api/expert-clfc/mytree`, { expertClId: '' }, undefined, { retry: false });
  }

  /**
   * 전문가분류 등록,수정
   * @param
   * @param expertClfcParamList
   * @return
   */
  static modifyExpertClfc(list: UsptExpertClModel[]): Promise<any> {
    return AxiosPut(`/pms/api/expert-clfc/save`, list)
  }

  /**
   * 전문가분류 삭제
   * @param expertClId(조회용)
   * @param expertClfcParamList
   * @return List<ExpertClDto>
   */
  static removeExpertClfc(list: UsptExpertClModel[]): Promise<any> {
    return AxiosDelete(`/pms/api/expert-clfc/delete`, list)
  }

  /**
   * 전문가단 담당자 목록 조회
   * @param expertClId
   * @return List<ExpertClMapngDto>
   */
  static getExpertClChargerList(expertClId: string): UseQueryResult<WithResponse<any>, any> {
    return GetQuery(`/pms/api/expert-clfc/expertcl-charger`, { expertClId }, undefined, { retry: false });
  }

  /**
   * 전문가단 담당자 등록
   * @param usptExpertClChargerList
   * @return
   */
  static addExpertClCharger(list: UsptExpertClModel[]): Promise<any> {
    return AxiosPost(`/pms/api/expert-clfc/expertcl-charger/save`, list)
  }

  /**
   * 전문가단 담당자 삭제
   * @param usptExpertClChargerList
   * @return
   */
  static removeExpertClCharger(list: UsptExpertClModel[]): Promise<any> {
    return AxiosDelete(`/pms/api/expert-clfc/expertcl-charger/delete`, list)
  }

  /**
   * 전문가단 담당자 추가 목록 조회(팝업)
   * @param wrcNm
   * @param expertNm
   * @return List<ExpertClMapngDto>
   */
  static getExpertClChargerPopupList(param: {
    deptNm?: string /**부서명**/
    memberNm?: string /**담당자명**/
    page: number
    rowCount: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<any>>, any> {
    return GetQuery(`/pms/api/expert-clfc/popup`, {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage});
  }


}