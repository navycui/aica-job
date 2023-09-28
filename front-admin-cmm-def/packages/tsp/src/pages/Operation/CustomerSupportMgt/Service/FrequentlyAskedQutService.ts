import {UseQueryResult} from "react-query";
import {WithListResponse, WithResponse} from "shared/utils/Model";
import {AxiosDelete, AxiosPost, AxiosPut, DeleteQuery, GetQuery} from "shared/libs/axios";
import {askedQutDetail, askedQutList} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {WithPagination} from "~/service/Model";

export class FrequentlyAskedQutService {
  static getList(param: {
    boardId?: string
    categoryCd?: string
    categoryNm?: string
    title?: string
    posting?: boolean
    page: number
    rowCount: number
    rowsPerPage: number
  }): UseQueryResult<WithResponse<WithPagination<askedQutList>>, any> {
    //return GetQuery(`/common/api/boards/${param.boardId}/articles`, {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage}, {baseURL:"http://172.30.1.10:8081"})
    return GetQuery(`/common/api/boards/${param.boardId}/articles`, {...param, page: param.page + 1, itemsPerPage: param.rowsPerPage}, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getDetail(boardId: string, id: string): UseQueryResult<WithResponse<askedQutDetail>,any> {
    //return GetQuery(`/common/api/boards/${boardId}/articles/${id}`, undefined, {baseURL:"http://172.30.1.10:8081"})
    return GetQuery(`/common/api/boards/${boardId}/articles/${id}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  // static insertAsked(article: FormData) : Promise<any> {
  static insertAsked(form: FormData) : Promise<any> {
    //return AxiosPost('/common/api/boards/tsp-qna/articles', form, {baseURL:"http://172.30.1.10:8081",contentType: "formData", responseType: 'blob'})
    return AxiosPost('/common/api/boards/tsp-qna/articles', form, {baseURL:process.env.REACT_APP_DOMAIN,contentType: "formData",responseType: 'blob'})
  }

  static updateAsked(form: FormData, id: string) : Promise<any> {
    //return AxiosPut(`/common/api/boards/tsp-qna/articles/${id}`, form, {baseURL:"http://172.30.1.10:8081",contentType: "formData", responseType: 'blob'})
    return AxiosPut(`/common/api/boards/tsp-qna/articles/${id}`, form, {baseURL:process.env.REACT_APP_DOMAIN,contentType: "formData",responseType: 'blob'})
  }

  // 자주묻는질문 삭제
  static deleteQNA(boardId:string, articleId: string) {
    return AxiosDelete(`/common/api/boards/${boardId}/articles/${articleId}`,undefined,{baseURL:process.env.REACT_APP_DOMAIN})
  }

  static deleteBoardAttachment(id:string, attachmentId:string) : Promise<any> {
    //return AxiosDelete(`/common/api/boards/tsp-qna/articles/${id}/attachments/${attachmentId}`, undefined, {baseURL:"http://172.30.1.10:8081"});
    return AxiosDelete(`/common/api/boards/tsp-qna/articles/${id}/attachments/${attachmentId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN});
  }

  // 자주묻는질문 전시 상태 변환
  static putQNAPosting(boardId:string, articleId: string, data: FormData) {
    //return AxiosPut(`/common/api/boards/${boardId}/articles/${articleId}/posting`, data, {baseURL:"http://172.30.1.10:8081",})
    return AxiosPut(`/common/api/boards/${boardId}/articles/${articleId}/posting`, data, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getAttactmentExtsn(qnaId:string):UseQueryResult<{atchmnflExtsnSet:string}> {
    return GetQuery(`/common/api/board/extsn/${qnaId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  // static getHistInfo(experMgtId: string, param: {
  //   page: number,
  //   rowsPerPage: number,
  //   rowCount: number
  // }): UseQueryResult<WithResponse<WithPagination<전문가신청처리이력>>, any> {
  //   return GetQuery("/" + experMgtId, {page: param.page + 1, itemsPerPage: param.rowsPerPage})
  // }
}
