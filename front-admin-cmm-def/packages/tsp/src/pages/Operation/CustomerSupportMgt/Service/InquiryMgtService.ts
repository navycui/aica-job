import {UseQueryResult} from "react-query";
import {WithResponse} from "shared/utils/Model";
import {AxiosPut, GetQuery} from "shared/libs/axios";
import {WithPagination} from "~/service/Model";
import {OneByOneDetail, OneByOneMember, OneByOneQuest} from "~/pages/Operation/CustomerSupportMgt/Model/Model";

export class InquiryMgtService {
  static getList(param: {
    qnaId: string
    questStatus?: string //처리상태 코드
    categoryCd?: string //문의 구분
    title?: string //제목
    memberNm?: string //회원명
    page: number
    rowsPerPage: number
    rowCount: number
  }): UseQueryResult<WithResponse<WithPagination<OneByOneQuest>>, any> {
    return GetQuery(`/common/api/qna/${param?.qnaId}/quests`, {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage}, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getDetail(qnaId: string, id: string): UseQueryResult<WithResponse<OneByOneDetail>, any> {
    return GetQuery(`/common/api/qna/${qnaId}/quests/${id}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getMember(qnaId: string, id: string): UseQueryResult<WithResponse<OneByOneMember>, any> {
    return GetQuery(`common/api/qna/${qnaId}/quests/${id}/quester-info`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static updateAnswer(id: string, answer: FormData) : Promise<any> {
    return AxiosPut(`/common/api/qna/tsp-persnal/quests/${id}/ans`, answer, {baseURL:process.env.REACT_APP_DOMAIN})
  }
}