package aicluster.tsp.common.util;

import bnet.library.util.CoreUtils.password;

public class TspCode {

	public static class authorityId {
		public static final String 일반사용자 = "USER";
		public static final String 관리자 = "ADMIN";
	}

	public static class role {
		public static final String 미로그인 = "ROLE_ANONYMOUS";
		public static final String 일반사용자 = "ROLE_USER";
	}

	public static class mberTyCode {
		public static final String CODE_GROUP = "MEMBER_TYPE";

		public static final String 개인사용자 = "INDIVIDUAL";
		public static final String 개인사업자 = "SOLE";
		public static final String 법인사업자 = "CORPORATION";
		public static final String 대학 = "UNIVERSITY";
		public static final String 내부사용자 = "INSIDER";
		public static final String 평가위원 = "EVALUATOR";
	}

	public static class mberSt {
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
		public static final String 첨부파일 = "atchmnfl-";
		public static final String 공고신청자 = "pbapplcnt-";
		public static final String 공고신청자이력 = "applcnthist-";
		public static final String 과제참여자 = "partcpts-";
		public static final String 전문가 = "expert-";
		public static final String 전문가학력 = "expertacd-";
		public static final String 전문가경력 = "expertcar-";
		public static final String 전문가분류 = "expertcl-";
		public static final String 전문가자격증 = "expertcrq-";
		public static final String 전문가신청처리이력 = "experthis-";

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
	}

	public static class histMessage {
//		public static final String 신규사용자 = "신규 사용자가 등록되었습니다.";
//		public static final String 상태변경 = "상태 정보가 변경되었습니다. [%s => %s]";
//		public static final String 비밀번호초기화 = "비밀번호가 초기화 되었습니다.";
//		public static final String 비밀번호변경 = "비밀번호가 변경되었습니다.";
//		public static final String 권한변경 = "권한 정보가 변경되었습니다. [%s => %s]";
//		public static final String IP변경 = "IP정보가 변경되었습니다. [%s => %s]";
//		public static final String 불량회원등록 = "불량회원 등록 처리되었습니다.\n\n ● 이용제한 기간 : [%s]~[%s]\n ● 등록사유 : [%s]\n ● 상세사유 : [%s]";
//		public static final String 불량회원해제 = "불량회원 해제 처리되었습니다.";
//		public static final String 회원탈퇴 = "회원 탈퇴 처리되었습니다.";
//		public static final String 접수완료 = "접수완료 처리되었습니다.";
//		public static final String 접수완료취소 = "접수완료취소 처리되었습니다.";
		public static final String 등록 = "%s 등록";
		public static final String 등록사유 = "%s 등록 처리되었습니다.";
		public static final String 완료 = "%s 완료";
		public static final String 완료사유 = "%s 완료 처리되었습니다.";
		public static final String 처리이력 = "%s 처리되었습니다.";
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
		public static final String 대기중 = "EVST01";
		public static final String 진행중 = "EVST02";
		public static final String 평가완료 = "EVST03";
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
	}
	/** 사업유형 */
	public static class bsnsType {
		public static final String CODE_GROUP = "BSNS_TYPE";
		public static final String 지원사업 = "APPLY";
		public static final String 교육사업 = "EDC";
		public static final String 입주사업 = "MVN";
		public static final String 자원할당사업 = "ALREC";
	}

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
	public static final String RC_CODE_GROUP = recomendCl.CODE_GROUP;
	public static final String RC_BSR = recomendCl.사업분야;
	public static final String RC_EDU = recomendCl.교육분야;
	public static final String MBR_CODE_GROUP = mberTyCode.CODE_GROUP;
	public static final String PS_CODE_GROUP = pblancSttus.CODE_GROUP;
	public static final String RS_CODE_GROUP = rceptSttus.CODE_GROUP;
	public static final String TASK_CODE_GROUP = taskType.CODE_GROUP;
	public static final String HOPE_CODE_GROUP = "HOPE_DTY";
	public static final String CT_CODE_GROUP = "CMPNY_TYPE";
	public static final String IR_CODE_GROUP = "INDUST_REALM";
	public static final String NFP_CODE_GROUP = "NEW_FNTN_PLAN";
	public static final String FP_CODE_GROUP = "FOND_PLAN";


	/** 추천분류유형 그룹코드 */
	public static final String RCT_CODE_GROUP = "RECOMEND_CL_TYPE";
	/** 업무단계 그룹코드 */
	public static final String JS_CODE_GROUP = "JOB_STEP";
	/** 회원 코드형식 */
	public static final String MBR_CODE_TYPE = "PORTAL";
	/** 싱글 첨부파일 그룹ID */
	public static final String SINGLE_FILE_GROUP_ID = "single-file-group-id";
	/* ----------------------------------------------------------------------- */

	public static class taskType {
		public static final String CODE_GROUP = "TASK_TYPE";
		public static final String 자유과제 = "FREE";
		public static final String 지정과제 = "APPN";
		public static final String 자유지정과제 = "ALL";
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

	//출석상태
	public static class atendSttus {
		public static final String 대기중 = "ATTS01";
		public static final String 불참 = "ATTS02";
		public static final String 출석 = "ATTS03";
		public static final String 회피 = "ATTS04";
	}

	//평가구분코드 쿼리용
	public static final String evlDivGnrl = evlDiv.일반;
	public static final String evlDivAdd = evlDiv.가점;
	public static final String evlDivMinus = evlDiv.감점;

	//위원평가상태
	public static class mfcmmEvlSttus {
		public static final String 대기중 = "MFEV01";
		public static final String 임시저장 = "MFEV02";
		public static final String 평가완료 = "MFEV03";
	}

	//발송방법
	public static class sndngMth {
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

	public static final String splemntRequest = presnatnProcessDiv.보완요청;

	//장비사용
	public static class eqpmnSt {
		public static final String 가용 = "AVAILABLE";
		public static final String 교정 = "CORRECT";
		public static final String 수리 = "REPAIR";
		public static final String 점검 = "INSPECT";
		public static final String 사용 = "USE";
	}

	//장비견적신청상태
	public static class eqpmnEstmtSt {
		public static final String APPLY = "신청";
		public static final String 보완요청 = "SPM_REQUEST";
		public static final String 견적서발급 = "EST_APPROVE";
		public static final String 반려 = "REJECT";
		public static final String 신청취소 = "CANCEL";
	}

	// 신청자 구분
	public enum applcntMberDiv{
		INDIVIDUAL(0),	// 개인
		ENTERPRISE(1);	// 기업

		private int value;

		applcntMberDiv(int value) {
			this.value = value;
		}

		public Integer getValue(){
			return this.value;
		}
	}

	public static class eqpmnSttus1{
		public static final String 가용 = "USEFULNESS";
		public static final String 불용 = "DISUSE";
		public static final String 수리 = "REPAIR";
		public static final String 수리완료 = "REPAIR_COMPLETE";
		public static final String 교정 = "CORRECTION";
		public static final String 교정완료 = "CORRECTION_COMPLETE";
		public static final String 점검 = "CHECK";
		public static final String 점검완료 = "CHECK_COMPLETE";

	}
	// 장비상태
	public enum eqpmnSttus{
		AVAILABLE(1, "가용"),
		REPAIR(11, "수리"),
		REPAIR_MODIY(12, "수리내역"),
		CORRECTION(21, "교정"),
		INSPECT(31, "점검");

		private int value;
		private String title;


		eqpmnSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 장비사용상태
	public enum eqpmnUsage{
		WAITING(1, "대기중"),
		USE(2, "사용중"),
		END_USE(3, "사용종료");

		private int value;
		private String title;
		eqpmnUsage(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 견적_장비_기간연장_실증자원 신청상태
	public enum reqstSttus{
		APPLY(1, "신청"),
		APPROVE(2 ,"승인"),
		SPM_REQUEST(3, "보완요청"),
		REJECT(4, "반려"),
		CANCEL(5, "신청취소"),
		EST_APPROVE(6, "견적서발급");

		private int value;
		private String title;

		reqstSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 실증자원사용상태
	public enum reqUsage{
		WAITING(1, "대기중"),
		USE(2, "사용중"),
		REQ_RETURN(3, "반환요청"),
		REQ_COMPLETE(4, "반환완료"),
		APPROVE_CANCEL(5, "승인취소");

		private int value;
		private String title;
		reqUsage(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 지불방법
	public enum pymntMth{
		PRE_PAYMENT(1, "선납"),
		AFTER_PAYMENT(2, "후납");

		private int value;
		private String title;
		pymntMth(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 장비결과보고서상태
	public enum reprtSttus{
		SUBMIT(1, "제출"),
		APPROVE(2, "승인"),
		SPM_REQUEST(3, "보완");

		private int value;
		private String title;

		reprtSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 장비반출심의 결과
	public enum tkoutDlbrtResult{
		APPLY(1, "신청"),
		APPROVE(2, "승인"),
		REJECT(3, "불가");

		private int value;
		private String title;
		tkoutDlbrtResult(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 장비사용료구분
	public enum eqpmnFeeDiv{
		ADD_PAYMENT(1, "추가금액"),
		EXTEND(2, "기간연장"),
		RNTFEE(3, "사용금액");

		private int value;
		private String title;
		eqpmnFeeDiv(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 기간연장처리구분
	public enum extndSttus{
		APPLY(1, "신청"),
		APPROVE(2 ,"승인"),
		SPM_REQUEST(3, "보완요청"),
		REJECT(4, "반려"),
		CANCEL(5, "신청취소"),
		DEPINFO(6, "입금안내"),
		NONPAYMENT(7, "미납처리");

		private int value;
		private String title;
		extndSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 장비사용처리이력
	public enum eqpmnUseHist{
		END_USE(1, "사용종료"),
		ACTL_USE_PAYMENT(2, "사용금액재설정"),
		ADD_PAYMENT(3, "추가금액"),
		DEPINFO(4, "입금안내"),
		NONPAYMENT(5, "미납처리"),
		CANCEL(6, "신청취소"),
		TKOUT_DLBRT_RESULT(7, "반출심의결과");

		private int value;
		private String title;
		eqpmnUseHist(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 불용여부
	public enum eqpmnAvailable{
		AVAILABLE(1, "정상"),
		UNAVAILABLE(2, "불용");

		private int value;
		private String title;
		eqpmnAvailable(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 반출상태
	public enum eqpmnTkoutSttus{
		TAKEN(1, "반출"),
		TAKENING(2, "반출중"),
		NOT_TAKEN(3, "미반출"),
		TKIN(4, "반입완료");

		private int value;
		private String title;
		eqpmnTkoutSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 결과보고서 처리상태
	public enum eqpmnDscntUseSttus{
		USE(1, "사용중"),
		NOT_USE(2, "미사용");

		private int value;
		private String title;
		eqpmnDscntUseSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}
	public enum eqpmnResultReportSt{
		SUBMIT(1, "제출"),
		SPM_REQUEST(2, "보완"),
		APPROVE(3, "승인"),
		TEMP(4, "임시저장"),;

		private int value;
		private String title;
		eqpmnResultReportSt(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}


	public enum processKnd{
		APPLY(1, "신청"),
		APPROVE(2 ,"승인"),
		SPM_REQUEST(3, "보완요청"),
		REJECT(4, "반려"),
		CANCEL(5, "신청취소"),
		DEPINFO(6, "입금안내"),
		NONPAYMENT(7, "미납처리"),
		DSCNT(8, "할인"),
		TAKEN(9, "반출"),
		NOT_TAKEN(10, "반출불가"),
		ADD_PAYMENT(11, "추가금등록"),
		TKIN(12, "반입완료"),
		END_USE(13, "사용종료"),
		ACTL_USE_PAYMENT(14, "사용금액 재설정"),
		EST_APPROVE(15, "견적서 발급"),
		WAITING(16, "대기중"),
		USE(17, "사용가능");
		private int value;
		private String title;

		processKnd(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 통계
	public enum statistics{
		Type1(0, "견적요청"),
		Type2(1, "장비신청"),
		Type3(2, "장비사용"),
		Type4(3, "기간연장"),
		//Type5(5, "실증자원신청"),
		Type5(4, "장비상태");

		private int value;
		private String title;
		statistics(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}

	}

	// 분석도구 신청상태
	public enum analsUseSttus{
		APPLY(1, "신청"),
		APPROVE(2 ,"승인"),
		WAITING(3, "대기중"),
		USE(4, "이용가능"),
		END_USE(5, "사용종료"),
		REJECT(6, "반려"),
		CANCEL(7, "신청취소");

		private int value;
		private String title;

		analsUseSttus(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	// 분석도구 타입
	public enum analsUntDiv{
		BNET(1, "BNET"),
		DSR(2, "DSR"),
		GPU(3, "GPU");

		private int value;
		private String title;

		analsUntDiv(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

	public enum mberDiv{

		INDIVIDUAL(1, "개인사용자"),
		SOLE(2, "개인사업자"),
		CORPORATION(3, "법인사업자"),
		UNIVERSITY(4, "대학"),
		INSIDER(5, "내부사용자"),
		EVALUATOR(6, "평가위원");
		private int value;
		private String title;

		mberDiv(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public Integer getValue(){
			return this.value;
		}
		public String getTitle(){
			return this.title;
		}
	}

}

