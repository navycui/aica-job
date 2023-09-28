package aicluster.pms.common.util;

import bnet.library.util.CoreUtils.password;

public class Code {
	public static String[] week = {"","월","화","수","목","금","토","일"};

	public static class authorityId {
		public static final String 일반사용자 = "USER";
		public static final String 관리자 = "ADMIN";
	}

	public static class role {
		public static final String 미로그인 = "ROLE_ANONYMOUS";
		public static final String 일반사용자 = "ROLE_USER";
	}

	public static class memberType {
		public static final String CODE_GROUP = "MEMBER_TYPE";

		public static final String 개인사용자 = "INDIVIDUAL";
		public static final String 개인사업자 = "SOLE";
		public static final String 법인사업자 = "CORPORATION";
		public static final String 대학 = "UNIVERSITY";
		public static final String 내부사용자 = "INSIDER";
		public static final String 평가위원 = "EVALUATOR";
	}

	public static class memberSt {
		public static final String CODE_GROUP = "MEMBER_ST";

		public static final String 정상 = "NORMAL";
		public static final String 불량회원 = "BLACK";
		public static final String 휴면 = "SLEEP";
		public static final String 탈퇴 = "SECESSION";
	}

	public static class authorityPurpose {
		public static final String CODE_GROUP = "AUTHORITY_PURPOSE";

		public static final String 회원용 = "MEMBER";
		public static final String 내부자용 = "INSIDER";
		public static final String 시스템용 = "SYSTEM";
		public static final String 공통 = "COMMON";
	}

	public static class menuRelation {
		public static final String 자식 = "CHILD";
		public static final String 위 = "ABOVE";
		public static final String 아래 = "BELOW";
	}

	public static class menuDirection {
		public static final String 위 = "UP";
		public static final String 아래 = "DOWN";
	}

	public static class menuId {
		public static final String ROOT = "ROOT";
	}

	public static class prefix {
		public static final String ROLE = "ROLE_";
		public static final String MENU = "menu-";
		public static final String 공고 = "pblanc-";
		public static final String 공고상세 = "pblancdt-";
		public static final String 선정결과공고 = "slctn-";
		public static final String 선정결과공고상세 = "slctndt-";
		public static final String 지원분야 = "applyrm-";
		public static final String 지정과제 = "appntask-";
		public static final String 체크리스트 = "chklst-";
		public static final String 사전검토 = "exmnt-";
		public static final String 사전검토항목 = "exmntiem-";
		public static final String 비목 = "ioe-";
		public static final String 세목 = "taxitm-";
		public static final String 성과지표 = "rsltidx-";
		public static final String 성과지표세부항목 = "rsltidxdi-";
		public static final String 성과지표기준항목 = "rsltidxsi-";
		public static final String 성과 = "rslt-";
		public static final String 성과이력 = "rslthist-";
		public static final String 성과지표항목 = "rsltidxiem-";
		public static final String 성과지표항목내용 = "rsltidxiemcn-";
		public static final String 첨부파일 = "atchmnfl-";
		public static final String 공고신청자 = "pbapplcnt-";
		public static final String 공고신청자이력 = "applcnthist-";
		public static final String 전문가 = "expert-";
		public static final String 전문가학력 = "expertacd-";
		public static final String 전문가경력 = "expertcar-";
		public static final String 전문가분류 = "expertcl-";
		public static final String 전문가자격증 = "expertcrq-";
		public static final String 전문가신청처리이력 = "experthist-";
		public static final String 학력 = "acdmcr-";
		public static final String 직장경력 = "crr-";
		public static final String 기타경력 = "etccrr-";
		public static final String 자격증 = "crqfc-";
		public static final String 수상 = "wnpz-";
		public static final String 프로그램 = "progrm-";
		public static final String 보고서 = "reprt-";
		public static final String 보고서이력 = "reprthist-";
		public static final String 사업계획서 = "plandoc-";
		public static final String 사업계획서이력 = "plandochist-";
		public static final String 사업계획신청자 = "planapplcn-";
		public static final String 과제책임자 = "rspnber-";
		public static final String 과제참여기업 = "prtcmpny-";
		public static final String 과제참여자 = "partcpts-";
		public static final String 과제신청사업비 = "reqstwct-";
		public static final String 과제세목별사업비 = "taxitmwct-";
		public static final String 사업정산 = "excclc-";
		public static final String 사업협약 = "cnvn-";
		public static final String 사업협약참여기업서명 = "cnvnsign-";
		public static final String 평가선정이의신청 = "evlobjc-";
		public static final String 평가이의처리이력 = "evlobjchist-";
		public static final String 최종선정이의신청 = "lastobjc-";
		public static final String 최종선정이의처리이력 = "lastobjchist-";
		public static final String 사업협약해지이력 = "cnvntrmnathist-";
		public static final String 협약변경요청 = "cnvnchangereq-";
		public static final String 협약변경이력 = "bsnscnvnhist-";
		public static final String 협약수행기관신분변경이력 = "cnvnsclpsthist-";
		public static final String 협약과제정보변경이력 = "cnvntaskinfohist-";
		public static final String 과제참여기업정보변경이력 = "prtcmpnyinfohist-";
		public static final String 과제참여기업변경이력 = "prtcmpnyhist-";
		public static final String 과제참여자변경이력 = "partcptshist-";
		public static final String 과제신청사업비변경이력 = "reqstwcthist-";
		public static final String 과제세목별사업비변경이력 = "taxitmwcthist-";
		public static final String 협약신청자정보변경이력 = "cnvnapplcnthist-";
		public static final String 과제책임자변경이력 = "rspnberhist-";
		public static final String 최종선정대상 = "lslctntrget-";
		public static final String 사업선정대상 = "bslctntrget-";
		public static final String 최종선정처리이력 = "lslctnprochist-";
		public static final String 평가항목결과가감 = "evladsbtr-";
		public static final String 평가대상제출이력 = "evlpresentnhist-";
		public static final String 평가대상 = "evltrget-";
		public static final String 평가결과 = "evlresult-";
		public static final String 평가항목결과 = "evliemresult-";
		public static final String 문서중복 = "docdplct-";

	}

	public static class defaultString {
		public static final String 초기화비밀번호 = password.encode("aicA!234");
	}


	public static class validateMessage {
		public static final String 미로그인			= "로그인을 수행하세요.";
		public static final String 입력없음			= "%s을(를) 입력하세요.";
		public static final String 체크없음			= "%s을(를) 체크하세요.";
		public static final String 날짜없음			= "%s을(를) 지정하세요.";
		public static final String 유효오류			= "유효하지 않은 %s입니다.";
		public static final String 중복오류			= "사용 중인 %s 입니다.[%s]";
		public static final String 입력오류			= "잘못된 %s 입니다. %<s를 다시 확인하세요.";
		public static final String 접두어오류			= "%s은(는) '%s'(으)로 시작되어야 합니다.[%s]";
		public static final String 큰비교오류			= "%s이(가) %s보다 클 수 없습니다.";
		public static final String 크거나같은비교오류		= "%s이(가) %s보다 크거나 같을 수 없습니다.";
		public static final String 작은비교오류			= "%s이(가) %s보다 작을 수 없습니다.";
		public static final String 작거나같은비교오류		= "%s이(가) %s보다 작거나 같을 수 없습니다.";
		public static final String 허용불가			= "%s이(가) 허용된 값이 아닙니다.[%s]";
		public static final String 포함불가			= "유효하지 않은 %s이(가) 포함되어 있습니다.";
		public static final String 삭제불가			= "데이터를 삭제할 수 없습니다.[사유:%s]";
		public static final String 조회결과없음			= "%s이(가) 없습니다.";
		public static final String DB오류				= "%s 중 오류가 발생했습니다.";
		public static final String 일자형식오류			= "%s은 유효하지 않은 일자 형식입니다. 날짜는 8자리로 입력해주세요!";
		public static final String 이메일오류			= "%s은 유효하지 않은 이메일 형식입니다.";
	}

	public static class histMessage {
		public static final String 신규사용자 = "신규 사용자가 등록되었습니다.";
		public static final String 상태변경 = "상태 정보가 변경되었습니다. [%s => %s]";
		public static final String 비밀번호초기화 = "비밀번호가 초기화 되었습니다.";
		public static final String 비밀번호변경 = "비밀번호가 변경되었습니다.";
		public static final String 권한변경 = "권한 정보가 변경되었습니다. [%s => %s]";
		public static final String IP변경 = "IP정보가 변경되었습니다. [%s => %s]";
		public static final String 불량회원등록 = "불량회원 등록 처리되었습니다.\n\n ● 이용제한 기간 : [%s]~[%s]\n ● 등록사유 : [%s]\n ● 상세사유 : [%s]";
		public static final String 불량회원해제 = "불량회원 해제 처리되었습니다.";
		public static final String 회원탈퇴 = "회원 탈퇴 처리되었습니다.";
		public static final String 접수완료 = "접수완료 처리되었습니다.";
		public static final String 접수완료취소 = "접수완료취소 처리되었습니다.";
	}

	public static class workTypeNm{
		public static final String BLACK = "불량회원등록";
		public static final String UNBLACK = "불량회원해제";
		public static final String AUTHORITY = "권한변경";
		public static final String MEMBERST = "회원상태변경";
		public static final String SECESSION = "회원탈퇴";
	}

	//평가구분코드
	public static class evlDiv {
		public static final String 일반 = "EVLD01";
		public static final String 가점 = "EVLD02";
		public static final String 감점 = "EVLD03";
	}

	//평가방식코드
	public static class evlMthd {
		public static final String 배점형 = "EVMT01";
		public static final String 척도형 = "EVMT02";
		public static final String 서술형 = "EVMT03";
	}

	//평가상태코드
	public static class evlSttus {
		public static final String CODE_GROUP = "EVL_STTUS";
		public static final String 대기중 = "EVST01";
		public static final String 진행중 = "EVST02";
		public static final String 평가완료 = "EVST03";
	}

	//평가단계상태코드
	public static class evlStepSttus {
		public static final String 대기중 = "ESTE01";
		public static final String 진행중 = "ESTE02";
		public static final String 평가완료 = "ESTE03";
	}

	/** 공고상태코드 */
	public static class pblancSttus {
		public static final String CODE_GROUP = "PBLANC_STTUS";
		public static final String 접수대기 = "WAIT";
		public static final String 접수중 = "ING";
		public static final String 접수중단 = "DSCNTC";
		public static final String 접수마감 = "CLOS";
	}

	/** 추천분류 */
	public static class recomendCl {
		public static final String CODE_GROUP = "RECOMEND_CL";
		public static final String 사업분야 = "BSR";
		public static final String 교육분야 = "EDU";
		public static final String 창업분야 = "FNTN";
	}
	/** 사업유형 */
	public static class bsnsType {
		public static final String CODE_GROUP = "BSNS_TYPE";
		public static final String 지원사업 = "APPLY";
		public static final String 교육사업 = "EDC";
		public static final String 입주사업 = "MVN";
		public static final String 자원할당사업 = "ALREC";
	}

	/** 사업담당자권한 */
	public static final String 사업담당자_수정권한 = "WRITE";
	public static final String 사업담당자_조회권한 = "READ";

	/** 사업연도형식 */
	public static class bsnsYearType {
		public static final String CODE_GROUP = "BSNS_YEAR_TYPE";
		public static final String 연간 = "YEAR";
		public static final String 반기 = "HALF";
		public static final String 분기 = "QUARTER";
	}
	/** 사업연도상세 */
	public static class bsnsYearDetail {
		public static final String CODE_GROUP = "BSNS_YEAR_DETAIL";
		public static final String 상반기 = "FIRST_HALF";
		public static final String 하반기 = "SECOND_HALF";
		public static final String 분기1 = "1ST";
		public static final String 분기2 = "2ND";
		public static final String 분기3 = "3RD";
		public static final String 분기4 = "4TH";
	}
	/** 접수상태 */
	public static class rceptSttus {
		public static final String CODE_GROUP = "RCEPT_STTUS";
		public static final String 임시저장 = "TEMP";
		public static final String 신청 = "REQST";
		public static final String 반려 = "REJECT";
		public static final String 보완요청 = "MAKEUP";
		public static final String 접수완료 = "COMPT";
		public static final String 신청취소 = "CANCEL";
	}

	/* -----------------------------------------------------------------------
	 * 쿼리사용 코드
	 */
	public static final String HOPE_CODE_GROUP = "HOPE_DTY";
	public static final String MSVC_CODE_GROUP = "MSVC_TYPE";
	public static final String GRDTN_CODE_GROUP = "GRDTN_DIV";
	public static final String CT_CODE_GROUP = "CMPNY_TYPE";
	public static final String IR_CODE_GROUP = "INDUST_REALM";
	public static final String NFP_CODE_GROUP = "NEW_FNTN_PLAN";
	public static final String FP_CODE_GROUP = "FOND_PLAN";
	public static final String UNIT_CODE_GROUP = "IEM_UNIT";
	public static final String DEPT_CODE_GROUP = "DEPT_CD";
	public static final String RC_CODE_GROUP = recomendCl.CODE_GROUP;
	public static final String RC_BSR = recomendCl.사업분야;
	public static final String RC_FNTN = recomendCl.창업분야;
	public static final String RC_EDU = recomendCl.교육분야;
	public static final String MBR_CODE_GROUP = memberType.CODE_GROUP;
	public static final String MBR_IND_CODE_GROUP = memberType.개인사용자;
	public static final String PS_CODE_GROUP = pblancSttus.CODE_GROUP;
	public static final String PS_ING_CODE = pblancSttus.접수중;
	public static final String PS_WAIT_CODE = pblancSttus.접수대기;
	public static final String RS_CODE_GROUP = rceptSttus.CODE_GROUP;
	public static final String RS_COMPT_CODE = rceptSttus.접수완료;
	public static final String RS_CANCEL_CODE = rceptSttus.신청취소;
	public static final String TASK_CODE_GROUP = taskType.CODE_GROUP;
	public static final String REPRT_CODE_GROUP = reprtSttus.CODE_GROUP;
	public static final String RSLT_CODE_GROUP = rsltSttus.CODE_GROUP;
	public static final String RSLT_PS = rsltSttus.제출;
	public static final String REPRT_TYPE_CODE_GROUP = reprtType.CODE_GROUP;
	public static final String JOBSTEP_TASK = jobStep.과제관리;
	public static final String JOBSTEP_RSLT = jobStep.성과관리;
	public static final String JOBSTEP_EVL = jobStep.평가선정;
	public static final String CNVN_STTS_CMP = cnvnSttus.협약완료;
	public static final String CNVN_CODE_GROUP = cnvnSttus.CODE_GROUP;
	public static final String BT_CODE_GROUP = bsnsType.CODE_GROUP;
	public static final String BT_EDC_CODE_GROUP = bsnsType.교육사업;
	public static final String LSOPS_CODE_GROUP = lastSlctnObjcProcessSttus.CODE_GROUP;
	public static final String LSOPS_02_CODE_GROUP = lastSlctnObjcProcessSttus.반려;
	public static final String LSOPS_03_CODE_GROUP = lastSlctnObjcProcessSttus.접수완료;
	public static final String EVL_STTUS_CODE_GROUP = evlSttus.CODE_GROUP;
	public static final String EVL_STTUS_03_CODE = evlSttus.평가완료;
	public static final String EVL_TYPE_CODE_GROUP = evlType.CODE_GROUP;
	public static final String EVL_TYPE_01_CODE = evlType.선정평가;
	public static final String EVL_STEP_03_CODE = evlStepSttus.평가완료;
	public static final String BSNS_TYPE_MVN_CODE = bsnsType.입주사업;
	public static final String RSLT_STTUS_RC_CODE = rsltSttus.접수완료;
	public static final String LSPD_CODE_GROUP = lastSlctnProcessDiv.CODE_GROUP;
	public static final String splemntRequest = presnatnProcessDiv.보완요청;

	public static final String evlDivGnrl = evlDiv.일반;
	public static final String evlDivAdd = evlDiv.가점;
	public static final String evlDivMinus = evlDiv.감점;
	public static final String MES_CODE_GROUP = mfcmmEvlSttus.CODE_GROUP;
	public static final String ATEND_STTUS_03_CODE = atendSttus.출석;
	public static final String LSN_RESULT_04_CODE = lsnResult.승낙;
	public static final String BSNS_TYPE_APPLY = bsnsType.지원사업;


	/** 추천분류유형 그룹코드 */
	public static final String RCT_CODE_GROUP = "RECOMEND_CL_TYPE";
	/** 업무단계 그룹코드 */
	public static final String JS_CODE_GROUP = "JOB_STEP";
	/** 회원 코드형식 */
	public static final String MBR_CODE_TYPE = "PORTAL";
	/** 내부사용자 코드형식 */
	public static final String INSIDER_CODE_TYPE = "INSIDER";
	/** 싱글 첨부파일 그룹ID */
	public static final String SINGLE_FILE_GROUP_ID = "single-file-group-id";
	/* ----------------------------------------------------------------------- */

	/** 과제유형 */
	public static class taskType {
		public static final String CODE_GROUP = "TASK_TYPE";
		public static final String 자유과제 = "FREE";
		public static final String 지정과제 = "APPN";
		public static final String 자유지정과제 = "ALL";
	}

	/** 보고서 유형 */
	public static class reprtType {
		public static final String CODE_GROUP = "REPRT_TYPE";
		public static final String 중간보고서 = "I";
		public static final String 결과보고서 = "R";
	}

	/** 보고서 상태 */
	public static class reprtSttus {
		public static final String CODE_GROUP = "REPRT_STTUS";
		public static final String 제출요청 = "PR";
		public static final String 제출 = "PS";
		public static final String 보완요청 = "SR";
		public static final String 접수완료 = "RC";
	}

	/** 성과 상태 */
	public static class rsltSttus {
		public static final String CODE_GROUP = "RSLT_STTUS";
		public static final String 제출요청 = "PR";
		public static final String 제출 = "PS";
		public static final String 보완요청 = "SR";
		public static final String 접수완료 = "RC";
	}

	public static class flag {
		public static final String 등록 = "I";
		public static final String 수정 = "U";
		public static final String 삭제 = "D";
	}

	//성별
	public static class gender {
		public static final String 남성 = "M";
		public static final String 여성 = "F";
		public static final String 없음 = "X";
	}

	//섭외결과
	public static class lsnResult {
		public static final String 대기중 = "LIRE01";
		public static final String 부재중 = "LIRE02";
		public static final String 거부 = "LIRE03";
		public static final String 승낙 = "LIRE04";
	}

	//추가배제옵션
	public static class aditExclCnd {
		public static final String 조건1 = "EXCL01";
		public static final String 조건2 = "EXCL02";
		public static final String 조건3 = "EXCL03";
		public static final String 조건4 = "EXCL04";
	}

	//참여제한
	public static class partcptnLmttCnd {
		public static final String 조건1 = "LMT01";
		public static final String 조건2 = "LMT02";
		public static final String 조건3 = "LMT03";
	}

	//출석상태
	public static class atendSttus {
		public static final String 대기중 = "ATTS01";
		public static final String 불참 = "ATTS02";
		public static final String 출석 = "ATTS03";
		public static final String 회피 = "ATTS04";
	}

	//위원평가상태
	public static class mfcmmEvlSttus {
		public static final String CODE_GROUP = "MFCMM_EVL_STTUS";
		public static final String 대기중 = "MFEV01";
		public static final String 임시저장 = "MFEV02";
		public static final String 평가완료 = "MFEV03";
	}

	//발송방법
	public static class sndngMth {
		public static final String ALL = "SNMT00";
		public static final String SMS = "SNMT01";
		public static final String 이메일 = "SNMT02";
	}

	//발표자료처리구분
	public static class presnatnProcessDiv {
		public static final String 제출요청 = "PRPR01";
		public static final String 제출 = "PRPR02";
		public static final String 보완요청 = "PRPR03";
		public static final String 접수완료 = "PRPR04";
		public static final String 접수완료취소 = "PRPR05";
	}

	//업무단계
	public static class jobStep {
		public static final String 공고접수 = "PBLANC_RCEPT";
		public static final String 평가선정 = "EVL_SLCTN";
		public static final String 협약 = "AGREM";
		public static final String 과제관리 = "TASK";
		public static final String 성과관리 = "RSLT";
	}

	//사업계획제출상태
	public static class planPresentnSttus {
		public static final String CODE_GROUP 	= "PLAN_PRESENTN_STTUS";
		public static final String 미제출 	= "PLPR01";
		public static final String 제출 		= "PLPR02";
		public static final String 보완요청 	= "PLPR03";
		public static final String 승인		= "PLPR04";
		public static final String 승인취소	= "PLPR05";
	}

	/** 협약상태 코드 */
	public static class cnvnSttus {
		public static final String CODE_GROUP 	= "CNVN_STTUS";
		public static final String 대기중 	=  "CNST01";
		public static final String 협약생성  	=  "CNST02";
		public static final String 서명요청 	=  "CNST03";
		public static final String 서명완료 	=  "CNST04";
		public static final String 협약완료 	=  "CNST05";
		public static final String 협약해지 	=  "CNST06";
		public static final String 협약해지취소	=  "CNST07";
		public static final String 서명재요청 =  "CNST08";
		public static final String 서명요청취소 =  "CNST09";
	}

	/** 협약해지구분 코드 */
	public static class cnvnTrmnatDiv {
		public static final String CODE_GROUP 	= "CNVN_TRMNAT_DIV";
		public static final String 협약 	=  "CNTR01";
		public static final String 해지  	=  "CNTR02";
	}

	/** 협약해지사유구분 코드 */
	public static class cnvnTrmnatResnCnDiv {
		public static final String CODE_GROUP 	= "CNVN_TRMNAT_RESN_CN_DIV";
		public static final String 사업중도포기 	=  "CTRN01";
		public static final String 중간평가탈락  	=  "CTRN02";
		public static final String 기타  	=  "CTRN03";
	}

	/** 협약해지처리구분 코드(이력) */
	public static class cnvnTrmnatProcessDiv {
		public static final String CODE_GROUP 	= "CNVN_TRMNAT_PROCESS_DIV";
		public static final String 해지등록 	=  "CNTP01";
		public static final String 저장  		=  "CNTP02";
		public static final String 해지취소  	=  "CNTP03";
	}

	/** 최종선정이의처리상태 */
	public static class lastSlctnObjcProcessSttus {
		public static final String CODE_GROUP 	= "LAST_SLCTN_OBJC_PROCESS_STTUS";
		public static final String 신청		= "LSLC01";
		public static final String 반려		= "LSLC02";
		public static final String 접수완료	= "LSLC03";
		public static final String 심의완료	= "LSLC04";
		public static final String 신청취소	= "LSLC05";
	}

	/** 협약변경항목구분 */
	public static class changeIemDiv {
		public static final String CODE_GROUP = "CHANGE_IEM_DIV";
		public static final String 수행기관신분	= "CHIE01";
		public static final String 과제정보      = "CHIE02";
		public static final String 참여기업      = "CHIE03";
		public static final String 참여인력      = "CHIE04";
		public static final String 사업비         = "CHIE05";
		public static final String 비목별사업비  = "CHIE06";
		public static final String 신청자정보    = "CHIE07";
		public static final String 과제책임자    = "CHIE08";
	}

	/** 협약변경상태 */
	public static class cnvnChangeSttus {
		public static final String CODE_GROUP 	= "CNVN_CHANGE_STTUS";
		public static final String 신청   		= "CCHS01";
		public static final String 신청취소 	= "CCHS02";
		public static final String 반려   		= "CCHS03";
		public static final String 보완요청 	= "CCHS04";
		public static final String 승인   		= "CCHS05";

	}

	/** 협약변경유형 */
	public static class cnvnChangeType {
		public static final String CODE_GROUP 	= "CNVN_CHANGE_TYPE";
		public static final String 승인   		= "APPROVAL";
		public static final String 통보   		= "NOTIFICATION";
	}

	/** 개인사업자구분코드 */
	public static class indvdlBsnmDiv {
		public static final String CODE_GROUP 	= "INDVDL_BSNM_DIV";
		public static final String 개인   		= "INDV01";
		public static final String 사업자   		= "INDV02";
	}

	/** 평가상태코드 */
	public static class evlType {
		public static final String CODE_GROUP = "EVL_TYPE";
		public static final String 선정평가 = "EVTY01";
		public static final String 중간평가 = "EVTY02";
		public static final String 최종평가 = "EVTY03";
		public static final String 심의 = "EVTY04";
	}

	/** 최종선정처리구분코드 */
	public static class lastSlctnProcessDiv {
		public static final String CODE_GROUP = "LAST_SLCTN_PROCESS_DIV";
		public static final String 대기 = "LSPD01";
		public static final String 최종선정 = "LSPD02";
		public static final String 통보 = "LSPD03";
	}

	/** 전문가신청처리상태코드 */
	public static class expertReqstProcessSttus {
		public static final String CODE_GROUP = "EXPERT_REQST_PROCESS_STTUS";
		public static final String 신청 = "ERPS01";
		public static final String 승인 = "ERPS02";
		public static final String 반려 = "ERPS03";
	}
}
