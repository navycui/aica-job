import {UseQueryResult} from "react-query";
import {WithResponse} from "shared/utils/Model";
import {AxiosDelete, AxiosPost, AxiosPut, GetQuery} from "shared/libs/axios";
import {
  NoticeDetail,
  NoticeList,
} from "~/pages/Operation/CustomerSupportMgt/Model/Model";
import {WithPagination} from "~/service/Model";

export class NoticeInfoMgtService {
  // 공지사항 게시글 목록 조회
  static getNoticeList(param : {
    boardId?:string
    posting?: boolean
    title?: string
    beginDt?: number
    endDt?: number
    page: number
    rowCount: number
    rowsPerPage: number
  }) : UseQueryResult<WithResponse<WithPagination<NoticeList>>, any> {
    //return GetQuery(`/common/api/boards/${param.boardId}/articles`,{...param, page:param.page + 1, itemsPerPage:param.rowsPerPage}, {baseURL : "http://172.30.1.10:8081"})
    return GetQuery(`/common/api/boards/${param.boardId}/articles`,{...param, page:param.page + 1, itemsPerPage:param.rowsPerPage}, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  // 공지사항 게시글 등록
  static postNoticeData(boardId:string, data: FormData) : Promise<any> {
    //return AxiosPost(`/common/api/boards/${boardId}/articles`, data, {baseURL:"http://172.30.1.10:8081",contentType: "formData", responseType: 'blob'})
    return AxiosPost(`/common/api/boards/${boardId}/articles`, data, {baseURL:process.env.REACT_APP_DOMAIN, contentType:'formData', responseType:'blob'})
  }

  // 공지사항 상세 조회
  static getNoticeDetail(boardId:string, articleId: string) : UseQueryResult<NoticeDetail> {
    //return GetQuery(`/common/api/boards/${boardId}/articles/${articleId}`, undefined, {baseURL : "http://172.30.1.10:8081"})
    return GetQuery(`/common/api/boards/${boardId}/articles/${articleId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  // 공지사항 수정
  static putNoticeModify(boardId: string, articleId: string, data: FormData) : Promise<any> {
    return AxiosPut(`/common/api/boards/${boardId}/articles/${articleId}`, data, {baseURL:process.env.REACT_APP_DOMAIN, contentType:'formData', responseType:'blob'})
  }

  // 공지사항 삭제
  static deleteNotice(boardId:string, articleId: string) {
    return AxiosDelete(`/common/api/boards/${boardId}/articles/${articleId}`,undefined,{baseURL:process.env.REACT_APP_DOMAIN})
  }

  // 공지사항 파일 삭제
  static deleteNoticeBoardAttachment(boardId:string, articleId:string, attachmentId:string) : Promise<any> {
    return AxiosDelete(`/common/api/boards/${boardId}/articles/${articleId}/attachments/${attachmentId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN});
  }

  // 공지사항 전시 상태 변환
  static putNoticePosting(boardId:string, articleId: string, data: FormData) {
    return AxiosPut(`/common/api/boards/${boardId}/articles/${articleId}/posting`, data, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getAttactmentExtsn(bbsId:string):UseQueryResult<{atchmnflExtsnSet:string}> {
    return GetQuery(`/common/api/board/extsn/${bbsId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }
}