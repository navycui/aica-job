import {UseQueryResult} from "react-query";
import {AxiosGet, getBaseUrl, GetQuery} from "shared/libs/axios";
import {AttachmentParam, CodeGroup, CommonCode, ymdList} from "~/service/Model";
import {WithListResponse, WithResponse} from "shared/utils/Model";
import api from "shared/api";

// EQPMN_REQST_ST - 장비상태
export class CommonService {
  static CommonCode(groupName: CodeGroup[]): UseQueryResult<CommonCode, any> {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    return GetQuery("/code", {codeGroup: groupName}, {baseURL: newBase})
  }

  // 첨부 파일 한개 다운로드
  static AttachmentFile(attachmentId: string) {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    return AxiosGet("/file-dwld-contentType/" + attachmentId, undefined, {responseType: 'blob', baseURL: newBase})
  }

  // 첨부파일 한개 정보 가져오기
  static getAttachmentIdInfo(attachmentId: string) : UseQueryResult<WithResponse<AttachmentParam>, any> {
    return GetQuery(`/tsp-api/api/atchmnfl/${attachmentId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  // 첨부파일groupId 한개 정보 가져오기
  static getAttachmentGroupIdInfo(attachmentGroupId: string) : UseQueryResult<WithListResponse<AttachmentParam>> {
    // if (attachmentGroupId === '' ){return}
    // return GetQuery(`/tsp/api/atchmnflInfo/${attachmentGroupId}`, undefined, {baseURL:"http://localhost:8083"})
    return GetQuery(`/tsp/api/atchmnflInfo/${attachmentGroupId}`, undefined, {baseURL:process.env.REACT_APP_DOMAIN})
  }

  static getAttachmentGroupIdInfo1(attachmentGroupId:string) : Promise<AttachmentParam> {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    return api( {
      url: `${newBase}/atchmnflInfo/${attachmentGroupId}`,
      method:'get',
    })
  }

  static getYmd(): UseQueryResult<WithListResponse<ymdList>, any> {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0,index)
    return GetQuery('/ymd', undefined, {baseURL:newBase})
  }

  static getClList() {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    return GetQuery('/cl-list',undefined, {baseURL:newBase})
  }

  // 첨부 파일 그룹 다운로드
  static AttachmentGroupFile(attachmentGroupId: string) {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    return AxiosGet("/file-dwld-groupfiles/" + attachmentGroupId, undefined, {responseType: 'blob', baseURL: newBase})
  }

  static async DownloadFiles(attachmentId: string) {
    const index = getBaseUrl().indexOf('/admin')
    const newBase = getBaseUrl().substring(0, index)
    const res = await AxiosGet("/file-dwld-contentType/" + attachmentId, undefined, {responseType: 'blob', baseURL: newBase})

    const {headers, data} = res;
    const content = headers['content-disposition'] || '';
    const attrs = /(\w+)=([^\s]+)/.exec(content) || [];
    console.log("attrs", attrs)
    const [, , name = ''] = attrs;

    if (!!name) {
      const filename = decodeURI(name.replace(/['"\\]/g, ''));
      download(data, filename, headers['content-type']);
      return Promise.resolve({data: filename});
    } else {
      return Promise.reject(new Error('Error file'));
    }
  } catch(e: any) {
    if (e.response.status === 400) {
      return Promise.reject({...e, message: '파일이 없습니다.'});
    }
  }
}

function download(data: any, filename: any, mime: string) {
  const blobData = [data];
  const blob = new Blob(blobData, {
    type: mime || 'application/octet-stream',
  });
  const blobURL =
    window.URL && window.URL.createObjectURL
      ? window.URL.createObjectURL(blob)
      : window.webkitURL.createObjectURL(blob);

  const element = document.createElement('a');
  element.style.display = 'none';
  element.href = blobURL;
  element.setAttribute('download', filename);

  if (typeof element.download === 'undefined') {
    element.setAttribute('target', '_blank');
  }

  document.body.appendChild(element);
  element.click();

  // 0.2초 뒤에 요소 삭제
  setTimeout(function () {
    document.body.removeChild(element);
    window.URL.revokeObjectURL(blobURL);
  }, 200);
}