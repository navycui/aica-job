import {WithCustomRowData} from "shared/components/TableComponents";
import {AttachmentParam} from "~/service/Model";

export interface SearchParam {
  분류?: string /*분류*/
  전시여부?: string /*전시여부*/
  제목?:string /*제목*/
  제목명?: string /*제목명*/
  처리상태?: string /*처리상태*/
  문의구분?: string /*문의구분*/
  entrprsNm?: string /*회원명*/
  useBeginDt?: number /*시작일*/
  useEndDt?: number /*종료일*/
  deptNm?: string
  memberNm?: string
}

export interface BoardsSearchParam {
  boardId?: string // 게시판 ID
  posting?: boolean // 게시여부
  beginDt?: number // 시작일시
  endDt?: number // 종료일시
  title?: string // 제목
  categoryCd?: string // 카테고리코드
  articleSrchCd?: string // 사용자 게시물 검색 코드
  articleSrchWord?: string // 사용자 게시물 검색 단어
  qnaId?: string // 게시판 ID
  questStatus?: string // 질문상태코드
  mberNm?: string // 회원명
  questBeginDay?: Date // 질의접수기간 시작일
  questEndDay?: Date // 질의접수기간 종료일
}

// 공지사항 게시글 등록
export interface AboutNoticeRegister {
  article?: {
    boardId?: string
    title?: string, // 제목
    notice?: boolean, //고정공지여부
    article?: string, // 내용
    categoryCd?: string, // 카테고리 코드
    posting?: boolean, // 공개여부
    webEditor?: boolean, // WYSIWYG 에디터 사용여부
    shareUrl?: string, // 공유 URL
    thumbnailAltCn?: string, // 썸네일 ALT 태그 내용
    questStatus?: string, // 질문분류
    articleCnList?: [{
      header: string,
      articleCn: string
    }],
    articleUrlList?: [{
      urlNm: string,
      url: string
    }]
  },
  pcThumbnailFile?: { // pc용 썸네일 파일
    MultipartFile: any
  },
  mobileThumbnailFile?: { // 모바일용 썸네일 파일
    MultipartFile: any
  },
  image?: [ // 이미지 첨부파일 array
    MultipartFile?: any
  ],
  attachment?: [ // 첨부파일 array
    MultipartFile: any
  ]
}

// 공지사항 목록 조회
export interface NoticeList {
  articleId: string,
  boardId: string,
  title: string,
  article: string,
  notice: boolean,
  attachmentGroupId: string,
  imageGroupId: string,
  categoryCd: string,
  posting: boolean,
  webEditor: boolean,
  sharedUrl: string,
  thumbnailFileId: string,
  thumbnailAltCn: string,
  readCnt: number,
  creatorId: string,
  createdDt: number,
  updaterId: string,
  updatedDt: number,
  rn: number
  questStatus?: string // 질문상태코드
}

// 자주묻는질문 검색파람
export interface AskedQutParam {
  boardId?: string // 화면분류값
  posting?: boolean //전시여부
  categoryCd?: string //분류
  categoryNm?: string //분류
  beginDt?: number //시작일
  endDt?: number //종료일
  title?: string
}

// 자주묻는질문 목록조회
export interface askedQutList {
  articleId: string
  title: string
  posting: boolean
  createdDt: number
  categoryCd: string
  categoryNm: string
  rn: number
}

// 자주묻는질문 상세 정보
export interface askedQutDetail {
  articleId: string
  createdDt: number
  posting: boolean
  title: string
  article: string
  categoryCd: string
  attachmentGroupId: string
  attachmentList: AttachmentParam[]
}

// 자주묻는질문 등록 정보
export interface AskedInsParam {
  boardId?: string
  categoryCd?: string
  title?: string
  article?: string
  notice?: boolean
  posting?: boolean
}

// 공지사항 상세
export interface NoticeDetail {
  articleId: string,
  boardId: string,
  title: string,
  article: string,
  notice: boolean,
  attachmentGroupId: string,
  imageGroupId: string,
  categoryCd: string,
  posting: boolean,
  webEditor: boolean,
  sharedUrl: string,
  pcThumbnailFileId: string,
  mobileThumbnailFileId: string,
  thumbnailAltCn: string,
  readCnt: number,
  creatorId: string,
  creatorNm: string,
  createdDt: number,
  updaterId: string,
  updaterNm: string,
  updatedDt: number,
  cmmtBoard: {
    boardId: string,
    systemId: string,
    boardNm: string,
    articleCnt: number,
    enabled: boolean,
    noticeAvailable: boolean,
    commentable: boolean,
    category: boolean,
    categoryCodeGroup: string,
    attachable: boolean,
    attachmentSize: number,
    attachmentExt: string,
    useSharedUrl: boolean,
    useThumbnail: boolean
    useForm: boolean,
    allReadable: boolean
  },
  articleCnList: [
    {
      articleCnId: string,
      articleId: string,
      sortOrder: number,
      header: string,
      articleCn: string
    },
  ],
  articleUrlList: [
    {
      urlId: string,
      articleId: string,
      sortOrder: number,
      urlNm: string,
      url: string,
    },
  ],
  attachmentList: AttachmentParam[]
  imageList: [
    {
      attachmentId?: string,
      attachmentGroupId?: string,
      fileNm?: string,
      contentType?: string,
      fileSize?: number,
      downloadCnt?: number,
      fileDeleted?: boolean
    },]
}

export interface FAQListData {
  id: string
  questStatus: string
  title: string
  posting: boolean
  createDt: string
}

export interface NoticeListData {
  id:string
  rn:number
  posting:string
  notice: boolean
  title:string
  createDt:string
}

export interface articleCnList {
  header: string
  articleCn: string
}

export interface articleUrlList {
  urlNm: string
  url: string
}

// 1:1 문의 관리
export interface OneByOneSearchParam {
  qnaId: string
  questStatus?: string //처리상태 코드
  categoryCd?: string //문의 구분
  title?: string //제목
  memberNm?: string //회원명
}

// 1:1문의 리스트
export interface OneByOneQuest{
  questId:string
  questSt:string
  questStNm:string
  question:string
  title:string
  questionerNm:string
  answererNm:string
  questCreatedDt:number
  categoryCd:string
  categoryNm:string
  rn: number
}

// 1:1문의 상세 정보
export interface OneByOneDetail {
  questCreatedDt:number
  questSt:string
  questStNm:string
  answererNm:string
  questioner: questioner
  categoryCd:string
  categoryNm:string
  questAttachmentList: AttachmentParam[]
  questAttachmentGroupId: string
  title:string
  question:string
  answer:string
}

//질문자 정보
export interface questioner {
  email:string
  loginId:string
  memberNm:string
  memberType:string
  mobileNo:string
  rn: number
}

export interface OneByOneMember {
  loginId: string
  memberNm: string
  memberType: string
  memberTypeNm: string
  positionNm: string
  chargerNm: string
  mobileNo: string
  email: string
}

export interface OneByAnswer {
  qnaId?: string
  questId?: string
  answer?: string
}

export interface 전문가분류관리_담당자 {
  id: string /*id*/
  부서명: string /*부서명*/
  이름: string /*이름*/
  직급: string /*직급*/
}

export interface UsptExpertClModel {
  id?: string;                /** 전문가분류ID */
  expertClId?: string;        /** 전문가분류ID */
  parntsExpertClId?: string;  /** 부모전문가분류ID */
  expertClNm?: string;        /** 전문가분류명 */
  sortOrdrNo?: number;        /** 정렬순서번호 */
  enabled?: boolean;          /** 전문가분류ID */
  useAt?: string;             /** 트리메뉴 레벨 */
  level?: string;             /** 트리메뉴 레벨 */
  path?: string;              /** 경로 */
  cycle?: string;             /** cycle */
  flag?: string;              /** I:등록, U:수정, D:삭제 */
  reload?: boolean;           /** 전문가분류ID */
  children?: UsptExpertClModel[];
}

export interface UsptExpertClChargerModel {
  expertClId ?: string;        /** 전문가분류ID*/
  memberId ?: string;          /** 회원ID : 담당자ID*/
  memberNm ?: string;          /** 전문가분류ID */
  deptNm ?: string;            /** 부서명 */
  clsfNm ?: string;            /** 직급명*/
  flag ?: string;              /** I:등록, U:수정, D:삭제 */
  flags ?: string;             /** I:등록, U:수정, D:삭제 */
}