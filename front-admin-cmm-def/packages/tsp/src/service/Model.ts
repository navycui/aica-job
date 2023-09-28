import {BaseResponse} from "shared/utils/Model";

export interface BoardData {
  articleId: string
  boardId: string
  title: string
  notice: boolean
  attachmentGroupId: string | null
  imageGroupId: string | null
  categoryCd: string | null
  posting: boolean
  webEditor: boolean
  sharedUrl: string | null
  thumbnailFileId: string | null
  readCnt: number
  creatorId: string
  createdDt: number
  updaterId: string
  updatedDt: number
  number: number
}

export interface BoardDataResponse extends BaseResponse {
  page: number
  itemsPerPage: number
  totalItems: number
  list: BoardData[]
}

export interface EquipmentClassifyData {
  eqpmnClId: string /*장비분류 ID*/
  eqpmnClNm: string /*장비분류명*/
  eqpmnLclasId: string /*부모장비분류 ID*/
  ordr: number /*정렬 순서*/
  depth: number /*Depth*/
  useAt: boolean /*사용여부*/

  child?: EquipmentClassifyData[]
}

export interface EquipmentClassifyRequest {
  eqpmnClId?: string /*장비분류 ID*/
  eqpmnClNm?: string /*장비분류명*/
  eqpmnLclasId?: string /*부모장비분류 ID*/
  ordr?: number /*정렬 순서*/
  useAt?: boolean /*사용여부*/
}

export interface EquipmentClassifyRowData {
  id: string /*dataGride구분값*/
  eqpmnClId: string, /*장비분류 ID*/
  ordr: number, /*정렬 순서*/
  eqpmnClNm: string, /*장비분류명*/
  useAt: string,/*사용여부*/
}

export interface DashboardDataResponse extends BaseResponse {
  estimateCount: number
  useCount: number
  rentalTotal: number
  extentionCount: number
  reportCount: number
  usageTotal: number
  resourceCount: number
  resourceReturnCount: number
  resourceTotal: number
}

export interface EquipmentInformationData {
  eqpmnId: string
  assetsNo: string	/*자산번호*/
  disuseAt: boolean	/*불용여부*/
  eqpmnNmKorean: string	/*장비명(국문)*/
  eqpmnSttus: string	/*장비상태(G:EQPMN_ST)*/
  modelNm: string	/*모델명*/
  tkoutAt: boolean	/*반출여부*/
  eqpmnClNm: string /*장비분류*/
  creatDt: number /*등록일*/
  number: number
}

export interface EquipmentRegisterData {
  asChargerCttpc?: string /*AS연락처(전화)*/
  asChargerNm?: string /*AS담당자*/
  asEntrprsNm?: string /*AS업체*/
  assetsNo?: string /*자산번호*/
  asstnMhrls?: string /*보조기기*/
  creatDt?: number /*생성일시*/
  creatrId?: string /*생성자ID(CMMT_MEMBER.MEMBER_ID)*/
  eqpmnClId?: string /*장비분류ID*/
  eqpmnId?: string /*장비ID*/
  eqpmnNmEngl?: string /*장비명(영문)*/
  eqpmnNmKorean?: string /*장비명(국문)*/
  eqpmnStndrd?: string /*장비규격*/
  eqpmnSttus?: string /*장비상태(G:EQPMN_ST)*/
  hldyInclsAt?: boolean /*법정공휴일 휴일포함*/
  imageId?: string /*이미지파일ID*/
  legacyItlpc?: string /*기존설치장소*/
  makr?: string /*제조사*/
  mnlAt?: boolean /*매뉴얼유무*/
  modelNm?: string /*모델명*/
  nttkoutHldyInclsAt?: boolean /*미반출시 휴일포함*/
  pchrgAt?: boolean /*유료여부*/
  purchsDt?: number /*구입일*/
  purchsPc?: number /*구입가격(원)*/
  realmPrpos?: string /*분야/용도*/
  rntfeeHour?: number /*시간당 사용료*/
  spcmnt?: string /*특기사항*/
  specComposition?: string /*제원 및 주요구성품*/
  srcelct?: string /*전원*/
  strNm?: string /*구입처*/
  sumry?: string /*요약*/
  swAt?: boolean /*소프트웨어유무*/
  tkoutAt?: boolean /*반출여부*/
  tkoutHldyInclsAt?: boolean /*반출시 휴일포함*/
  updtDt?: number /*수정일시*/
  updusrId?: string /*수정자ID(CMMT_MEMBER.MEMBER_ID)*/
  useRateInctvSetupAt?: boolean /*사용율 저조 설정*/
  usefulBeginHour?: number /*1일 가용시작시간*/
  usefulEndHour?: number /*1일 가용종료시간*/
  dscntId?: string[] /*할인 ID*/

}

export type WithPagination<T> = T & {
  page: number
  itemsPerPage: number
  totalItems: number
  list: T[]
}

export interface EquipmentMgtInfoRequest {
  chckTrgetAt: boolean /*점검대상여부*/
  disuseAt: boolean /*불용여부*/
  crrcTrgetAt: boolean /*교정대상여부*/
  rntfeeHour: number /*시간당사용료(원)*/
  hldyInclsAt: boolean /*법정공휴일 휴일포함여부*/
  nttkoutHldyInclsAt: boolean /*미반출시 휴일포함여부*/
  tkoutHldyInclsAt: boolean /*반출시휴일포함여부*/
  useRateInctvSetupAt: boolean /*사용율저조설정여부*/
  usefulBeginHour: number
  usefulEndHour: number
  crrcCycle: number /*교정주기(일)*/
  eqpmnClId: string
  eqpmnClParam: EquipmentCategoryData[]
  // crrcCycle: number /*교정주기(개월)*/
  // correctParam: EquipmentCorrectData | null
}

export interface EquipmentMgtInfoData {
  eqpmnId: string /*장비ID*/
  chckTrgetAt: boolean /*점검대상여부*/
  eqpmnNmKorean: string
  // crrcId: string /*교정ID*/
  crrcTrgetAt: boolean /*교정대상여부*/
  crrcCycle: number /*교정주기(일)*/
  disuseAt: boolean /*불용여부*/
  eqpmnSttus: string /*장비상태(G:EQPMN_ST)*/
  hldyInclsAt: boolean /*법정공휴일 휴일포함*/
  nttkoutHldyInclsAt: boolean /*미반출시 휴일포함*/
  repairId: string /*수리ID*/
  tkoutAt: boolean /*반출여부*/
  tkoutHldyInclsAt: boolean /*반출시 휴일포함*/
  rntfeeHour: number /*시간당 사용료*/
  useRateInctvSetupAt: boolean /*사용율 저조설정*/
  usefulBeginHour: number /*1일 가용시작시간*/
  usefulEndHour: number /*1일 가용종료시간*/
  creatDt: number /*생성일시*/
  eqpmnClId:string
  eqpmnClNm:string
  eqpmnClParam: EquipmentCategoryData[]

  correctParam: EquipmentCorrectData | null
  repairParam: EquipmentRepairData | null
  tkoutParam: EquipmentTakoutData | null

}

export interface DiscountData {
  dscntId?: string /*할인 ID*/
  dscntRate: number /*할인률(%)*/
  dscntResn: string /*할인사유*/
  useSttus: string /*사용여부*/
}

export interface EquipmentTakoutData {
  cttpc: string	/*연락처*/
  email: string	/*email*/
  entrprsNm: string	/*업체명*/
  mberDiv: string /*구분(개인, 기업)*/
  ofcps: string	/*직위*/
  partcptnDiv: string /*AI직접 참여사업*/
  userNm: string	/*사용자*/
}

export interface EquipmentCorrectData {
  // crrcCycle: number	/*교정주기(일)*/
  crrcInstt?: string	/*교정기관*/
  manageResn?: string	/*관리사유*/
  lastCrrcDt?: number	/*마지막교정일*/
  manageBeginDt?: number	/*관리시작일*/
  manageEndDt?: number	/*관리종료일*/
}

export interface EquipmentCheckData {
  beginDay: string  /*수리시작일*/
  endDay: string /*수리종료일*/
  details: string /*수리내역*/
  eqpmntStateInfo?: EqpmntStateInfoData
}

export interface EquipmentRepairData {
  manageResn: string	/*관리사유*/
  manageBeginDt: number	/*관리시작일*/
  manageEndDt: number	/*관리종료일*/
}

export interface EquipmentCategoryData {
  eqpmnClId: string /*장비분류 ID*/
  eqpmnClNm: string /*장비분류명*/
  eqpmnLclasId: string /*부모장비분류 ID*/
  ordr: number /*정렬 순서*/
  depth: number /*Depth*/
  useAt?: boolean /*사용여부*/
}

// export interface EquipmentDiscountData {
//   eqpmnId?: number /*장비 ID*/
//   eqpmnDscntCndId?: string /*장비할인조건 ID*/
//   discountCnd: string /*할인조건*/
//   discountRate: string /*할인률(%)*/
// }

export interface EquipmentData {
  asChargerCttpc: string /*AS연락처(전화)*/
  asChargerNm: string /*AS담당자*/
  asEntrprsNm: string /*AS업체*/
  assetsNo: string /*자산번호*/
  asstnMhrls: string /*보조기기*/
  creatDt: number /*생성일시*/
  creatrId: string /*생성자ID(CMMT_MEMBER.MEMBER_ID)*/
  eqpmnClId: string /*장비분류ID*/
  eqpmnId: string /*장비ID*/
  eqpmnNmEngl: string /*장비명(영문)*/
  eqpmnNmKorean: string /*장비명(국문)*/
  eqpmnStndrd: string /*장비규격*/
  imageId: string /*이미지파일ID*/
  legacyItlpc: string /*기존설치장소*/
  makr: string /*제조사*/
  mnlAt: boolean /*매뉴얼유무*/
  modelNm: string /*모델명*/
  pchrgAt: boolean /*유료여부*/
  purchsDt: number /*구입일*/
  purchsPc: number /*구입가격(원)*/
  realmPrpos: string /*분야/용도*/
  spcmnt: string /*특기사항*/
  specComposition: string /*제원 및 주요구성품*/
  srcelct: string /*전원*/
  strNm: string /*구입처*/
  sumry: string /*요약*/
  swAt: boolean /*소프트웨어유무*/
  tkoutAt: boolean /*반출여부*/
  updtDt: number /*수정일시*/
  updusrId: string /*수정자ID(CMMT_MEMBER.MEMBER_ID)*/

  eqpmnClParam: EquipmentCategoryData[]
  detailDscntParam: DiscountData[]
  // tsptEqpmnClfc: EquipmentCategoryData[]
  // tsptEqpmnDscntCnd: EquipmentDiscountData[]
}

export interface EquipmentStateChangeModalData {
  manageId?: string /*관리ID*/
  manageDiv: string /*관리구분*/
  crrcInstt: string /*교정기관*/
  manageBeginDt: number /*관리시작일*/
  manageEndDt: number /*관리종료일*/

  manageResn?: string /*관리사유*/
  manageResult?: string /*관리결과*/
}

export interface EqpmntStateInfoData {
  available: boolean
  eqpmnSt: string
}

export interface EquipmentHistoryInfoData {
  histId: string /*이력ID*/
  creatDt: number /*생성일시*/
  processKnd: string /*처리구분*/
  processResn: string /*처리사유*/
  opetrId: string /*처리자ID*/
}

// export interface UseEquipment {
//   list: UseEquipmentData[]
// }

export interface UseEquipmentData {
  assetsNo: string /*자산번호*/
  entrprsNm: string /*업체명*/
  eqpmnNmEngl: string /*장비명(영문)*/
  eqpmnNmKorean: string /*장비명(국문)*/
  mberDiv: string /*구분(기업, 개인, etc…)*/
  useBeginDt: number /*시작일*/
  useEndDt: number /*종료일*/
  userNm: string /*회원명/사업자명*/
  expectUsgtm: number /*예상사용기간*/
  eqpmnClId: string
  eqpmnClNm?: string
  number: number
}

export interface EquipmentMgtHistoryInfoData {
  eqpmnId: string /*장비ID*/
  crrcInstt: string	/*교정기관*/
  manageDiv: string	/*관리구분*/
  manageBeginDt: number	/*관리시작일*/
  manageEndDt: number	/*관리종료일*/
  manageResn: string	/*관리사유*/
  manageResult: string	/*관리결과*/
  mberNm: string /*회원명*/
  manageId: string	/*관리ID*/
  opetrId: string	/*처리자ID*/
  updusrId: string	/*수정자ID*/
}

export type CodeGroup =
  "EQPMN_AVAILABLE_ST"
  | "EQPMN_ESTMT_ST"
  | "EQPMN_RENTAL_REPORT_ST"
  | "EQPMN_RENTAL_ST"
  | "EQPMN_RESOURCE_REQST_ST"
  | "EQPMN_RESOURCE_USAGE_ST"
  | "EQPMN_ST"
  | 'ANALS_USE_ST'
  | "EQPMN_TKOUT_ST"
  | "EQPMN_TKIN_ST"
  | "EQPMN_USAGE_ST"
  | "EQPMN_EXTEND"
  | 'EQPMN_REQST_ST'
  | 'EQPMN_PAYMENT'
  | 'MEMBER_TYPE'
  | "EQPMN_RESOURCE_USAGE_ST"
  | "EQPMN_RESOURCE_REQST_ST"
  | 'EQPMN_USE_HIST'
  | 'EQPMN_RESULT_REPORT_ST'
  | 'EQPMN_EXTEND_ST'
  | 'CATEGORY_QNA'
  | 'ANALS_UNT_DIV'
  | 'QUEST_STATUS'
  | 'CATEGORY_PERSNAL'
  | 'PARTCPTN_DIV'
  | 'EQPMN_DSCNT_ST'

export interface Code {
  code: string
  codeNm: string
}

export interface CommonCode {
  // codeGroup: CodeKey<keyof CodeGroup>
  codeGroup: Record<CodeGroup, { codeGroup: Code[] }>
}

export interface SearchParam {
  eqpmnSttus?: string	/*장비상태(G:EQPMN_ST)*/
  eqpmnNmKorean?: string	/*장비명(국문)*/
  tkoutAt?: boolean /*반출여부*/
  tkinAt?: boolean	/*반입여부*/
  disuseAt?: boolean	/*불용여부*/
  pymntMth?: string, /*지불방법*/
  assetsNo?: string	/*자산번호*/
  modelNm?: string	/*모델명*/
  rceptNo?: string, /*접수번호*/
  entrprsNm?: string /*사업자명/이름*/
  reqstSttus?: string /*신청상태*/
  reprtSttus?: string /*결과보고서 상태*/
  useSttus?: string /*사용상태*/
  useBeginDt?: number /*사용시작일*/
  useEndDt?: number /*사용종료일*/
  BeginDt?: number /*사용시작일*/
  EndDt?: number /*사용종료일*/
  creatBeginDt?: number /*신청시작일*/
  creatEndDt?: number /*신청종료일*/
}

export interface EqpmnEstmtListData {
  reqstSttus: string // 신청상태
  mberDiv: string // 구분
  entrprsNm: string // 사업자명
  assetsNo: string // 자산번호
  eqpmnNmKorean: string // 장비명(국문)
  useBeginDt: number // 사용시작시간
  useEndDt: number // 사용종료시간
  pymntMth: string // 지불방법
  rceptNo: string // 접수번호
  creatDt: number // 생성일시
  estmtID: string // 장비 ID
  number: number
}

export interface EqpmnEstmtList {
  list: EqpmnEstmtListData[]
}

export interface EqpmnEstmtDetailData {
  assetsNo: string, /*자산번호*/
  atchmnflGroupId: string, /*첨부파일 ID*/
  attachmentList: AttachmentParam[]
  creatDt: number, /*생성일시*/
  cttpc: string, /*연락처*/
  dscntId: string /*할인 ID*/
  dscntRate: number /*할인률*/
  dscntResn: string /*할인 사유*/
  dscntList: DiscountData[], /*할인적용*/
  email: string, /*email*/
  entrprsNm: string, /*회원명/사업자명*/
  userNm: string, /*사용자명*/
  eqpmnNmEngl: string, /*장비명(영문)*/
  eqpmnNmKorean: string, /*장비명(국문)*/
  mberDiv: string, /*구분(기업, 개인, etc...)*/
  modelNm: string, /*모델명*/
  ofcps: string, /*직위*/
  partcptnDiv: string, /*ai직접단지 사업참여 여부*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  reqstSttus: string, /*신청상태*/
  rntfeeHour: number, /*1시간 사용료*/
  tkoutAdres: string, /*반출지 주소*/
  tkoutAt: boolean, /*반출신청 여부*/
  tkoutResn: string, /*반출 사유*/
  useBeginDt: number, /*사용시작시간*/
  useEndDt: number, /*사용종료시간*/
  usefulHour: number, /*1일 가용시간*/
  useprps: string, /*활용목적*/
  usgtm: number, /*조정 사용시간*/
  expectUsgtm: number /*예상사용시간*/
  expectRntfee: number, /*예상사용료*/
  reqstId: string /*장비사용ID*/
  rsndqf: string /*보완사유*/
}

// 견적요청 상세 - 신청정보 - 사용금액 재설정
export interface EqpmnEstmtModifyPrice {
  dscntId: string,
  reqstId: string,
  updusrId?: string,
  usgtm: number
}

export interface TsptEqpmnEstmtReqstHist {
  creatDt: number, /*생성일시*/
  estmtId: string, /*견적 ID*/
  histId?: string, /*이력 ID*/
  mberId?: string, /*회원 ID*/
  mberNm: string, /*회원명*/
  opetrId: string, /*처리자ID*/
  processKnd: string, /*처리구분*/
  processResn: string /*처리사유*/
}

export interface EqpmnReqstProcess {
  estmtId?: string /*장비 ID*/
  reqstId?: string /*장비신청 ID*/
  reqstSttus?: string /*신청 상태 (보완, 반려)*/
  rsndqf?: string /*사유*/
  updtDt?: number /*수정일시*/
  updusrId?: string /*수정자 ID*/
}

export interface EqpmnApplyReqstProcess {
  reqstId: string /*장비견적신청 ID*/
  reqstSttus: string /*신청 상태 (보완, 반려)*/
  rceptNo?: string, /*접수번호*/
  rsndqf?: string /*사유*/
  updtDt?: string /*수정일시*/
  updusrId?: string /*수정자 ID*/
}

export interface EqpmnUseReqstList {
  assetsNo: string /*자산번호*/
  creatDt: number, /*신청일시*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmKorean: string, /*장비명*/
  mberDiv: string, /*구분*/
  tkoutAt: boolean, /*반출여부*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  reqstID: string, /*신청 ID*/
  reqstSttus: string, /*신청상태*/
  useBeginDt: number, /*사용시작일*/
  useEndDt: number /*사용종료일*/
  number: number
}

/*장비사용관리 - 장비신청관리 - 디테일 신청정보*/
export interface EqpmnUseReqstDetail {
  assetsNo: string /*자산번호*/
  atchmnflGroupId: string, /*첨부파일 그룹 ID*/
  attachmentList: AttachmentParam[]
  creatDt: number, /*신청일시*/
  cttpc: string, /*연락처*/
  dscntId: string, /*할인 ID*/
  dscntRate: number, /*할인률*/
  dscntResn: string, /*할인사유*/
  dscntList: DiscountData[], /*할인리스트*/
  email: string, /*이메일*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmEngl: string, /*장비명(영문)*/
  eqpmnNmKorean: string, /*장비명(국문)*/
  mberDiv: string, /*구분*/
  modelNm: string, /*모델명*/
  npyResn: string, /*미납사유*/
  ofcps: string, /*직위*/
  partcptnDiv: string, /*AI 직업단지 사업참여 여부*/
  promsAtchmnflId: string /*서약서 파일 ID*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  reqstSttus: string, /*사용상태*/
  rntfee: number, /*할인적용금액*/
  rntfeeHour: number, /*1시간 사용료*/
  tkoutAdres: string, /*반출지 주소*/
  tkoutAt: boolean, /*반출 여부*/
  tkoutDlbrtCn: string /*반출심의 내용*/
  tkoutDlbrtResult: string /*반출심의 결과*/
  tkoutResn: string, /*사유(용도)*/
  useBeginDt: number, /*시작일*/
  useEndDt: number, /*종료일*/
  usefulHour: number, /*1일 가용시간*/
  useprps: string, /*활용목적*/
  usgtm: number /*사용시간*/
  expectRntfee: number /*예상사용료*/
  expectUsgtm: number /*예상사용시간*/
}

/*장비사용관리 - 장비신청관리 - 디테일 신청정보*/
export interface EqpmnUseReqstHistList {
  number: number, /*번호*/
  creatDt: number, /*생성일시*/
  histId: string, /*이력 ID*/
  mberId?: string, /*회원 ID*/
  mberNm: string, /*회원명 (사업자명)*/
  opetrId: string, /*처리자 ID*/
  processKnd: string, /*처리구분*/
  processResn: string, /*처리사유*/
  reqstId?: string /*신청 ID*/
}

// 사용관리 - 장비사용관리 - 기간연장신청관리 리스트
export interface EqpmnExtndList {
  assetsNo: string, /*자산번호*/
  creatDt: number, /*생성일시*/
  entrprsNm: string, /*업체명*/
  eqpmnNmKorean: string, /*장비명(국문)*/
  etReqstId: string, /*연장신청 ID*/
  mberDiv: string, /*구분(개인,기업)*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  reqstSttus: string, /*신청상태*/
  useBeginDt: number, /*사용시작일*/
  useEndDt: number, /*사용종료일*/
  userNm: string /*사용자명*/
  number: number
}

export interface EqpmnExtndDetail {
  creatDt: number, /*생성일시*/
  cttpc: string, /*연락처*/
  dscntRate: number, /*할인률*/
  detailDscntParam: DiscountData[], /*할인*/
  dscntId: string, /*할인 ID*/
  dscntResn: string, /*할인사유*/
  email: string, /*이메일*/
  entrprsNm: string, /*업체명*/
  eqpmnId: string, /*장비 ID*/
  etReqstId: string, /*연장신청 ID*/
  mberDiv: string, /*구분(개인, 기업)*/
  npyResn: string, /*미납사유*/
  ofcps: string, /*직위*/
  oldUseBeginDt: number, /*사용시작일*/
  oldUseEndDt: number, /*사용종료일*/
  partcptnDiv: string, /*AI 직접단지 참여사업*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  rcpmnyGdcc: string, /*입금안내문*/
  rcpmnyGdccDt: number, /*입급안내일시*/
  reqstId: string, /*사용신청 ID*/
  reqstSttus: string, /*신청상태*/
  rntfee: number, /*사용료*/
  rntfeeHour: number, /*시간당 사용료*/
  useBeginDt: number, /*사용시작시간*/
  useEndDt: number, /*사용종료시간*/
  userNm: string, /*사용자명*/
  usgtm: number /*사용시간*/
  expectUsgtm: number /*예상사용시간*/
  expectRntfee: number /*예상사용료*/
  dscntAmount: number /*할인금액*/
  useSttus: string /*사용상태*/
}

//사용관리 - 장비사용관리 - 장비사용관리 리스트
export interface EqpmnUseList {
  assetsNo: string, /*자산번호*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmKorean: string, /*장비명*/
  exUseBeginDt: string,
  exUseEndDt: string,
  mberDiv: string, /*구분*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  reqstID: string, /*신청 ID*/
  useSttus: string, /*사용상태*/
  tkinAt: boolean, /*반입여부*/
  tkoutAt: boolean, /*반출여부*/
  useBeginDt: number, /*사용시작일*/
  useEndDt: number /*사용종료일*/
  tkoutDlbrtResult: string /*반출심의결과*/
  number: number
}

//사용관리 - 장비사용관리 - 장비사용관리 디테일
export interface EqpmnUseDetail {
  aditRntfee: number, /*추가금액*/
  assetsNo: string, /*자산번호*/
  useSttus: string, /*사용상태*/
  atchmnflGroupId: string, /*첨부파일 그룹 ID*/
  attachmentList: AttachmentParam[]
  creatDt: number, /*신청일시*/
  cttpc: string, /*연락처*/
  dscntId: string, /*할인 ID*/
  dscntRate: number, /*할인률*/
  dscntResn: string, /*할인사유*/
  dscntList: DiscountData[],
  email: string, /*이메일*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmEngl: string, /*장비명(영문)*/
  eqpmnNmKorean: string, /*장비명(국문)*/
  mberDiv: string, /*구분*/
  modelNm: string, /*모델명*/
  npyResn: string, /*미납사유*/
  ofcps: string, /*직위*/
  partcptnDiv: string, /*AI 직업단지 사업참여 여부*/
  pymntMth: string, /*지불방법*/
  rceptNo: string, /*접수번호*/
  rcpmnyGdccDt: number, /*입금안내일시*/
  reqstSttus: string, /*사용상태*/
  rntfee: number, /*할인적용금액*/
  rntfeeHour: number, /*1시간 사용료*/
  rqestResn: string, /*추가금액 사유*/
  tkinAt: boolean, /*반입 여부*/
  tkoutAdres: string, /*반출지 주소*/
  tkoutAt: boolean, /*반출 여부*/
  tkoutResn: string, /*사유(용도)*/
  useBeginDt: number, /*시작일*/
  useEndDt: number, /*종료일*/
  usefulHour: number, /*1일 가용시간*/
  useprps: string, /*활용목적*/
  usgtm: number /*사용시간*/
  expectRntfee: number /*예상사용료*/
  expectUsgtm: number /*예상사용시간*/
  tkoutDlbrtResult: string
  tkoutDlbrtCn: string
}

//사용관리 - 장비사용관리 - 장비사용관리 - 추가금액등록
export interface AditRntfee {
  aditRntfee: number
  reqstId?: string
  rqestResn?: string
  updurId?: string
}

// 반출 심의
export interface TkoutDlbrt {
  reqstId: string, /*장비견적신청 ID*/
  tkoutDlbrtCn?: string, /*반출심의 내용*/
  tkoutDlbrtResult?: string, /*반출심의 결과*/
  updtDt?: number, /*수정일시*/
  updusrId?: string /*수정자*/
}

// 미납 처리
export interface NpyProcess {
  npyResn: string, /*미납사유*/
  reqstId: string, /*장비견적신청 ID*/
  updtDt?: number, /*수정일시*/
  updusrId?: string /*수정자 ID*/
}

// 입금 안내문
export interface RcpmnyGdcc {
  rcpmnyGdcc: string, /*입금 안내문 내용*/
  reqstId: string, /*장비견적신청 ID*/
  updusrId?: string /*수정자ID*/
}

// 장비사용상세 사용료부과내역
export interface UsageChargeDetailProps {
  number: string; // 번호
  useDivision: string; // 사용구분
  division: string; // 구분 (날짜 및 청구사유 출력)
  fare: number; // 금액
}

// 장비사용관리 사용신청 기간연장
export interface EqpmnsUseExtnd {
  dscntAmount?: number, /*할인금액*/
  expectRntfee?: number, /*예상사용료*/
  expectUsgtm?: number, /*예상사용시간*/
  reqstId?: string, /*장비신청 ID*/
  rntfee?: number, /*사용료*/
  rsndqf: string, /*사유*/
  updusrId?: string, /*수정자*/
  useBeginDt: number, /*사용시작시간*/
  useEndDt: number, /*사용종료시간*/
  usgtm: number /*사용시간*/
}

// 기간연장신청 처리이력 조회
export interface EqpmnsExtndHistList {
  creatDt: number, /*생성일시,처리일시*/
  etReqstId: string, /*연장신청 ID*/
  histId?: string, /*이력 ID*/
  mberId: string, /*처리자 ID*/
  mberNm?: string, /*처리자이름*/
  opetrId?: string, /*처리자계정*/
  processKnd: string, /*처리구분*/
  processResn: string /*처리사유*/
}

// 기간연장신청 상세 처리
export interface EqpmnsExtndDetailProcess {
  dscntId?: string, /*할인 ID*/
  npyResn?: string, /*미납처리 내용*/
  rcpmnyGdcc?: string, /*입금안내문*/
  reqstSttus?: string, /*신청상태 APPLY[신청], CANCEL[신청취소], APPROVE[승인], SPM_REQUEST[보완요청], REJECT[반려], [견적서발급]*/
  rntfee?: number, /*사용료*/
  rsndqf?: string, /*사유(보완,반려)*/
  usgtm?: number /*사용시간*/
  expectRntfee?: number /*예상사용료*/
  dscntAmount?: number /*할인금액*/
}


//사용관리 - 실증인프라 - 컴퓨팅자원신청
export interface ApplyResourceMgtData {
  creatBeginDt: number /*신청시작일(검색)*/
  creatEndDt: number /*신청시작일(검색)*/
  creatDt: number /*신청일*/
  entrprsNm: string /*사업자명*/
  mberDiv: string /*구분*/
  rceptNo: string /*접수번호*/
  reqstId: string /*신청ID*/
  reqstSttus: string /*신청상태*/
  useBeginDt: number /*사용시작일(검색))*/
  useEndDt: number /*사용종료일(검색)*/
  userNm: string /*사용자명*/
  number: number
}

export interface ApplyResourceDetailData {
  atchmnflGroupId: string /*첨부파일*/
  cpuCo: number /*CPU 갯수*/
  creatDt: number /*신청일, 처리일시*/
  cttpc: string /*연락처*/
  dataStorgeAt: boolean /*데이터저장소 사용여부*/
  email: string /*이메일*/
  entrprsNm: string /*사업자명*/
  gpuAt: boolean /*GPU 사용여부*/
  mberDiv: string /*구분*/
  ofcps: string /*직위*/
  partcptnDiv: string /*AI 집적단지 사업자명 여부*/
  rceptNo: string /*접수번호*/
  reqstSttus: string /*신청상태*/
  useprps: string /*활용목적*/
  userNm: string /*이름*/
  histId: string /*이력ID*/
  mberId: string /*회원ID*/
  mberNm: string /*회원명(사업자명)*/
  opetrId: string /*처리자ID*/
  processKnd: string /*처리구분*/
  processResn: string /*처리사유*/
  reqstId: string /*신청ID*/
}

export interface ApplyResourceHistData {
  creatDt: number,
  histId: string,
  mberId: string,
  mberNm: string,
  opetrId: string,
  processKnd: string,
  processResn: string,
  reqstId: string
}

export interface ApplyResourceSearchParam {
  creatDt?: number /*신청시작일(검색)*/
  entrprsNm?: string /*사업자명*/
  mberDiv?: string /*구분*/
  rceptNo?: string /*접수번호*/
  reqstId?: string /*신청ID*/
  reqstSttus?: string /*신청상태*/
  creatBeginDt?: number  /*신청시작일(검색))*/
  creatEndDt?: number   /*신청종료일(검색))*/
  useBeginDt?: number /*사용시작일(검색))*/
  useEndDt?: number /*사용종료일(검색)*/
  userNm?: string /*사용자명*/
}

// 첨부파일 관련
export interface AttachmentParam {
  fileNm: string
  attachmentId: string
  fileSize: number
  contentType: string
  fileInfo?: File
  key?: string
}

export interface ymdList {
  ymd: string /*휴일*/
}

export interface ApplyResourceSpmProcess {
  reqstId: string /*신청 ID*/
  param?: string /*보완사유*/
}

export interface ApplyResourceRejectProcess {
  reqstId: string
  param?: string /*반려사유*/
}

export interface ApplyResourceApproveProcess {
  reqstId: string
}

export interface UseResourceMgtData {
  creatBeginDt: number /*신청시작일(검색)*/
  creatEndDt: number /*신청종료일(검색)*/
  entrprsNm: string /*사업자명*/
  itemsPerPage: number /**/
  mberDiv: string /*구분*/
  page: number /**/
  rceptNo: string /*접수번호*/
  reqstId: string /*신청ID*/
  reqstSttus: string /*신청상태*/
  useBeginDt: number /*사용시작일(검색)*/
  useEndDt: number /*사용종료일(검색)*/
  useSttus: string /*사용상태*/
  userNm: string /*사용자명*/
  number: number
}

export interface UseResourceSearchParam {
  creatBeginDt?: number /*신청시작일(검색)*/
  creatEndDt?: number /*신청종료일(검색)*/
  entrprsNm?: string /*사업자명*/
  itemsPerPage?: number /**/
  mberDiv?: string /*구분*/
  page?: number /**/
  rceptNo?: string /*접수번호*/
  reqstId?: string /*신청ID*/
  reqstSttus?: string /*신청상태*/
  useBeginDt?: number /*사용시작일(검색)*/
  useEndDt?: number /*사용종료일(검색)*/
  useSttus?: string /*사용상태*/
  userNm?: string /*사용자명*/
}

export interface UseResourceDetailData {
  atchmnflGroupId: string /*첨부파일*/
  cpuCo: number /*CPU 갯수*/
  creatDt: number /*신청일*/
  cttpc: string /*연락처*/
  dataStorgeAt: boolean /*데이터저장소 사용여부*/
  email: string /*이메일*/
  entrprsNm: string /*사업자명*/
  gpuAt: boolean /*GPU 사용여부*/
  mberDiv: string /*구분*/
  ofcps: string /*직위*/
  partcptnDiv: string /*AI 집적단지 사업참여 여부*/
  rceptNo: string /*접수번호*/
  reqstSttus: string /*신청상태*/
  useRturnDt: number /*반환요청일*/
  useSttus: string /*사용상태*/
  useprps: string /*활용목적*/
  userNm: string /*이름*/
}

export interface UseResourceHistData {
  creatDt: number /*처리일시*/
  histId: string /*이력ID*/
  mberId: string /*회원ID*/
  mberNm: string /*회원명(사업자명)*/
  opetrId: string /*처리자ID*/
  processKnd: string /*처리구분*/
  processResn: string /*처리사유*/
  reqstId: string /*신청ID*/
}

export interface UseResourceCancelProcess {
  reqstId: string /*신청 ID*/
}

export interface UseResourceReturnProcess {
  reqstId: string /*신청 ID*/
}

export interface UseResourceReqReturnProcess {
  reqstId: string /*신청 ID*/
}

// 장비사용관리 사용료 부과내역
export interface EqpmnUseRntfeeHist {
  creatDt: number /*일자*/
  rntfee: number /*금액*/
  rqestResn: string /*사유*/
  useDiv: string /*구분*/
}

// 장비사용관리 처리이력 조회
export interface EqpmnUseHistlist {
  creatDt: number, /*생성일시*/
  histId: string, /*이력 ID*/
  mberId: string, /*회원 ID*/
  mberNm: string, /*회원명(사업자명)*/
  opetrId: string, /*처리자 ID*/
  processKnd: string, /*처리구분*/
  processResn: string, /*처리사유*/
  reqstId: string /*신청 ID*/
}

// 장비사용관리 기간연장관리
export interface EqpmnUseExtendPeriod {
  dscntAmount: number, /*할인금액*/
  etReqstId: string, /*연장신청 ID*/
  pymntMth: string, /*지불방법*/
  reqstSttus: string, /*신청상태*/
  rntfee: number, /*사용료*/
  rntfeeHour: number, /*1시간 사용료*/
  useBeginDt: number, /*사용시작일*/
  useEndDt: number, /*사용종료일*/
  usgtm: number, /*사용시간*/
  detail: string /*상세내역*/
}

export interface EqpmnReportList {
  assetsNo: string, /*자산번호*/
  creatDt: number, /*제출일*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmKorean: string, /*장비명*/
  exUseBeginDt: string,
  exUseEndDt: string,
  mberDiv: string, /*구분*/
  reprtId: string, /*신청 ID*/
  reprtSttus: string, /*신청상태*/
  useBeginDt: number, /*사용 시작일*/
  useEndDt: number /*사용 종료일*/
  number: number
}

// 결과보고서 상세
export interface EqpmnReportDetail {
  achiv: string, /*활용목적달성*/
  assetsNo: string, /*자산번호*/
  atchmnflGroupId: string, /*첨부파일 그룹 ID*/
  cmtExpectEra: string, /*기술 제품출시 예상시기*/
  creatDt: number, /*신청일시*/
  cttpc: string, /*연락처*/
  detailPrcuse: string, /*상세활용내역*/
  dffrnc: string, /*계획대비차이점*/
  dscntRate: string, /*할인률*/
  dscntResn: string, /*할인사유*/
  email: string, /*이메일*/
  entrprsNm: string, /*사업자명/이름*/
  eqpmnNmEngl: string, /*장비명 (영문)*/
  eqpmnNmKorean: string, /*장비명 (국문)*/
  expcEffect: string, /*장비활용기대효과*/
  expectSalamt: string, /*기술 매출 예상액*/
  mberDiv: string, /*구분*/
  mnnst: string, /*주관기관*/
  modelNm: string, /*모델명*/
  ofcps: string, /*직위*/
  partcptnDiv: string, /*AI 집적단지 사업참여 여부*/
  prcuseDtls: string, /*장비활용내역*/
  prcuseNeed: string, /*장비활용 필요성*/
  prcusePlan: string, /*장비활용계획*/
  prcusePrpos: string, /*장비활용용도*/
  prcusePurps: string, /*장비활용 활용목적*/
  prcuseRealm: string, /*활용분야*/
  pymntMth: string, /*지불방법*/
  reprtSttus: string, /*사용상태*/
  rntfee: string, /*할인적용금액*/
  rntfeeHour: number, /*1시간 사용료*/
  strength: string, /*주관기관 장비활용 시 좋았던 점*/
  trgetEqpmn: string, /*대상장비*/
  useBeginDt: number, /*시작일*/
  useEndDt: number, /*종료일*/
  usgtm: number, /*사용시간*/
  weakness: string /*주관기관 장비활용 시 아쉬웠던 점*/
}

// 결과보고서 처리이력
export interface UseReprtHistData {
  creatDt: number /*처리일시*/
  histId: string /*이력 ID*/
  mberId: string /*회원 ID*/
  mberNm: string /*회원명(사업자명)*/
  opetrId: string /*처리자 ID*/
  processKnd: string /*처리구분*/
  processResn: string /*처리사유*/
  reprtId?: string /*보고서 ID*/
}

// 결과보고서 처리
export interface UseReprtProcess {
  reprtId: string, /*보고서 ID*/
  reprtSttus: string, /*보고서 상태*/
  rsndqf?: string, /*사유*/
  updusrId?: string /*수정자*/
}

// 분석환경 이용신청 목록
export interface UseEquipAnalysisList {
  useSttus: string, /*사용상태*/
  mberDiv: string, /*구분*/
  entrprsNm: string /*회원명(사업자명)*/
  analsUntDiv: string /*이용신청타입*/
  useBeginDt: number /*이용시작일*/
  useEndDt: number /*이용종료일*/
  creatDt: number /*신청일시*/
  reqstID: string /*신청 ID*/
  resrceId: string /*리소스 ID*/
  number: number
}

export interface UseEquipAnalysisListData {
  list: UseEquipAnalysisList[]
}

// 분석환경 이용신청 상세 조회
export interface UseEquipAnalysisDetail {
  analsUntDiv: string, // 분석도구 구분
  creatDt: number, // 신청일시
  cttpc: string, // 연락처
  email: string, // 이메일
  entrprsNm: string, // 사업자명/이름
  mberDiv: string, // 구분
  ofcps: string, // 직위
  partcptnDiv: string, // AI 집적단지 사업참여 여부
  reqstID: string, // 신청 ID
  reqstResn: string, // 신청사유
  resrceId: string, //리소스 ID
  useBeginDt: number, // 사용시작일
  useEndDt: number, // 사용종료일
  useSttus: string // 사용상태
}

// 분석환겨 이용신청 처리내역 조회
export interface UseEquipAnalysHistListData {
  creatDt: number, // 생성일시
  histId: string, // 이력 ID
  mberId: string, // 회원 ID
  mberNm: string, // 회원명 (사업자명)
  opetrId: string, // 처리자 ID
  processKnd: string, // 처리구분
  processResn: string, // 처리사유
  reqstId: string // 신청 ID
}

// 견적서 관련 정보 조회
export interface ozReportPersonInfo {
  applcnt: { /*신청자 정보*/
    adres: string, /*주소*/
    bsnlcnsFile: {
      attachmentId: string, /*첨부파일ID*/
      contentType: string, /*첨부파일 타입*/
      fileNm: string, /*첨부파일 이름*/
      fileSize: number /*첨부파일 크기*/
    },
    bsnlcnsFileId: string, /*사업자등록증파일ID*/
    cttpc: string, /*연락처*/
    email: string, /*이메일*/
    entrprsNm: string, /*사업자명/이름*/
    mberDiv: string, /*구분*/
    ofcps: string, /*직위*/
    partcptnDiv: string, /*AI직접단지 참여사엽 참여 여부*/
    userNm: string /*신청자 이름*/
  },
  clsfNm: string, /*담당자직급*/
  creatDt: number, /*견적서 발급일자*/
  email: string, /*담당자이메일*/
  loginId: string, /*담당자 로그인 Id*/
  mberNm: string, /*담당자명*/
  telno: string /*담당자자리번호*/
}

// 견적서 발급
export interface EST {
  fileName?: string,
  ozrName?: string,
  jsonData: {
    user?: [{
      entrprsNm?: string,
      chargerNm?: string,
      chargerCttpc?: string,
      adres?: string,
      creatDt?: string,
      validate?: string
    }],
    admin?: [{
      entrprsNm?: string,
      chargerNm?: string,
      chargerCttpc?: string,
      adres?: string,
      homepage?: string,
      email?: string
    }],
    estmtDetail?: [{
      assetsNo?: string,
      eqpmnNm?: string,
      modelNm?: string,
      amount?: string,
      rntfee?: string
    }],
    etc?: [{
      place: string
      remarks: string
      account: string
    }]
  }
}

export interface StatisticsList {
  statistics: [{
    title: string,
    rate: number
  }]
  entrprsCount: number
}

export interface AnalsReqstProcess {
  reqstId: string
  rsndqf: string
  useSttus: string
}

export interface DashboardCount {
  estimateCount: number, // 견적신청
  extentionCount: number, // 기간연장신청
  rentalTotal: number, // 장비사용 신청관리
  reportCount: number, // 결과보고서제출
  resourceCount: number, // 실증 사용신청
  resourceReturnCount: number, // 반환요청
  resourceTotal: number, // 컴퓨팅자원사용관리
  usageTotal: number, // 장비사용관리
  useCount: number // 사용신청
}

export enum StatusState {
  APPLY = "APPLY", /*신청*/
  CANCEL = "CANCEL", /*신청취소*/
  SPM_REQUEST = "SPM_REQUEST", /*보완요청*/
  APPROVE = "APPROVE", /*승인*/
  REJECT = "REJECT", /*반려*/
  EST_APPROVE = 'EST_APPROVE', /*견적서발급*/
  DEPINFO = 'DEPINFO', /*입금안내*/
  NONPAYMENT = 'NONPAYMENT', /*미납처리*/
  USE = 'USE', /*사용중*/
  END_USE = 'END_USE', /*사용종료*/
  WAITING = 'WAITING', /*대기중*/
  ACTL_USE_PAYMENT = 'ACTL_USE_PAYMENT', /*사용금액 재설정*/
  ADD_PAYMENT = 'ADD_PAYMENT' /*추가금액*/
}

export enum PaymentState {
  PRE_PAYMENT = 'PRE_PAYMENT', /*선납*/
  AFTER_PAYMENT = 'AFTER_PAYMENT' /*후납*/
}