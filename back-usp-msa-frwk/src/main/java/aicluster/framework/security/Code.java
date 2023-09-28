package aicluster.framework.security;

import bnet.library.util.CoreUtils;

public class Code {
	public static class attachment {
		public static final String EXTS = "DOC/PPT/XLS/HWP/PDF/PNG/GIF/JPG/JPEG/ZIP";
	}

	public static class boardAuthority {
		public static final String CODE_GROUP = "BOARD_AUTHORITY";
		public static final String 권한없음 = "NONE";
		public static final String 읽기 = "READ";
		public static final String 쓰기 = "WRITE";
		public static final String 관리자 = "MANAGER";
	}

	public static class questSt {
		public static final String CODE_GROUP = "QUEST_STATUS";
		public static final String 요청 = "REQUEST";
		public static final String 접수 = "RECEIPT";
		public static final String 답변 = "ANSWER";
		public static final String 확인 = "CONFIRM";
	}

	public static class popupType {
		public static final String CODE_GROUP = "POPUP_TYPE";

		public static final String 메인화면 = "MAIN";
	}

	public static class approvalSt {
		public static final String CODE_GROUP = "APPROVAL_ST";

		public static final String 상신		= "REQUEST";
		public static final String 진행중		= "ING";
		public static final String 승인		= "APPROVAL";
		public static final String 반려		= "REJECT";
		public static final String 상신취소	= "CANCEL";
	}

	public static class systemId {
		public static final String CODE_GROUP = "SYSTEM_ID";

		public static final String 사용자지원포털	= "USER_PORTAL";
		public static final String 실증지원포털		= "PROOF_PORTAL";
		public static final String 인증서버		= "MEMBER";
		public static final String 공통서버		= "COMMON";
	}

	public static class apiSystemId {
		public static final String CODE_GROUP = "SYSTEM_ID";

		public static final String 인증API      = "API_MEMBER";
		public static final String 공통API      = "API_COMMON";
		public static final String 실증API      = "API_TSP";
		public static final String 사업관리API  = "API_PMS";
		public static final String 교육API      = "API_EDU";
		public static final String 입주API      = "API_MVN";
		public static final String 예약API      = "API_RSVT";
		public static final String 배치API      = "API_BATCH";
	}

	public static class surveyQuestionType {
		public static final String CODE_GROUP = "SURVEY_QUESTION_TYPE";

		public static final String 다중선택	= "CHECKBOX";
		public static final String 객관식		= "RADIO";
		public static final String 주관식		= "SHORT_ANSWER";
	}

	public static class surveyPurpose {
		public static final String CODE_GROUP = "SURVEY_PURPOSE";

		public static final String 일반 = "GENERAL";
		public static final String 분포털 = "ANALYSIS";
	}

	public static class memberSt {
		public static final String CODE_GROUP = "MEMBER_ST";

		public static final String 정상 = "NORMAL";
		public static final String 불량회원 = "BLACK";
		public static final String 휴면 = "DORMANT";
		public static final String 탈퇴 = "SECESSION";
	}

	public static class memberType {
		public static final String CODE_GROUP = "MEMBER_TYPE";
		private static final String[] MEMBERS = {
				memberType.개인
				,memberType.개인사업자
				,memberType.법인사업자
				,memberType.대학};
		public static final String 개인 = "INDIVIDUAL";
		public static final String 개인사업자 = "SOLE";
		public static final String 법인사업자 = "CORPORATION";
		public static final String 대학 = "UNIVERSITY";
		public static final String 내부사용자 = "INSIDER";
		public static final String 평가위원 = "EVALUATOR";

		/**
		 * memberType이 회원인지 검사
		 *
		 * @param memberType 회원구분
		 * @return true: 회원, false: 회원아님
		 */
		public static boolean isMember(String memberType) {
			return CoreUtils.array.contains(MEMBERS, memberType);
		}

		/**
		 * memberType이 내부사용자인지 검사
		 *
		 * @param memberType 회원구분
		 * @return true: 내부사용자, false: 내부사용자아님
		 */
		public static boolean isInsider(String memberType) {
			return CoreUtils.string.equals(내부사용자, memberType);
		}

		/**
		 * memberType이 평가위원인지 검사
		 *
		 * @param memberType 회원구분
		 * @return true: 평가위원, false: 평가위원아님
		 */
		public static boolean isEvaluator(String memberType) {
			return CoreUtils.string.equals(평가위원, memberType);
		}
	}

	public static class validateMessage {
		public static final String 미로그인				= "로그인을 수행하세요.";
		public static final String 입력없음				= "%s을(를) 입력하세요.";
		public static final String 체크없음				= "%s을(를) 체크하세요.";
		public static final String 날짜없음				= "%s을(를) 지정하세요.";
		public static final String 유효오류				= "유효하지 않은 %s입니다.";
		public static final String 중복오류				= "사용 중인 %s 입니다.[%s]";
		public static final String 입력오류				= "잘못된 %s 입니다. %<s를 다시 확인하세요.";
		public static final String 접두어오류			= "%s은(는) '%s'(으)로 시작되어야 합니다.[%s]";
		public static final String 큰비교오류			= "%s이(가) %s보다 클 수 없습니다.";
		public static final String 크거나같은비교오류	= "%s이(가) %s보다 크거나 같을 수 없습니다.";
		public static final String 작은비교오류			= "%s이(가) %s보다 작을 수 없습니다.";
		public static final String 작거나같은비교오류	= "%s이(가) %s보다 작거나 같을 수 없습니다.";
		public static final String 허용불가				= "%s이(가) 허용된 값이 아닙니다.[%s]";
		public static final String 포함불가				= "유효하지 않은 %s이(가) 포함되어 있습니다.";
		public static final String 삭제불가				= "데이터를 삭제할 수 없습니다.[사유:%s]";
		public static final String 조회결과없음			= "%s이(가) 없습니다.";
		public static final String 조회결과있음           = "%s이(가) 이미 있습니다.";
	}

	public static class termsType {
		public static final String CODE_GROUP = "TERMS_TYPE";
	}

	public static class possessTerm {
		public static final String CODE_GROUP = "POSSESS_TERM";

		public static final String 회원탈퇴 = "SECESSION";
		public static final String 준영구 = "PERMANENT";
	}

	public static class expiredCalType {
		public static final String 일단위 = "DAY";
		public static final String 월단위 = "MONTH";
	}

}