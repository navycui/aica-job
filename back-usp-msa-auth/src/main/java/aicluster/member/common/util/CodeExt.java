package aicluster.member.common.util;

import java.util.List;

import aicluster.framework.security.Code;
import aicluster.member.common.entity.CmmtPasswordHist;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.string;

public class CodeExt extends Code {
	public static class authorityId {
		public static final String 일반사용자 = "USER";
		public static final String 관리자 = "ADMIN";
	}

	public static class role {
		public static final String 미로그인 = "ROLE_ANONYMOUS";
		public static final String 일반사용자 = "ROLE_USER";
	}

	public static class memberTypeExt extends memberType {
		public static final String[] 사업자유형 = {개인사업자, 법인사업자, 대학};

		public static boolean isBzmn(String memberType) {
			return array.contains(사업자유형, memberType);
		}
		public static boolean isIndividual(String memberType) {
			return string.equals(memberType, 개인);
		}
	}

	public static class memberSt {
		public static final String CODE_GROUP = "MEMBER_ST";

		public static final String 정상 = "NORMAL";
		public static final String 불량회원 = "BLACK";
		public static final String 휴면 = "DORMANT";
		public static final String 탈퇴 = "SECESSION";
		public static final String 잠김 = "LOCK";
		public static final String 중지 = "STOP";
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

	public static class gender {
		public static final String CODE_GROUP = "GENDER";

		public static final String 남성 = "M";
		public static final String 여성 = "F";
		public static final String 없음 = "X";
	}

	public static class prefix {
		public static final String ROLE = "ROLE_";
		public static final String MENU = "menu-";
		public static final String 이력ID = "hist-";
		public static final String 회원ID = "member-";
		public static final String 가입세선ID = "joinset-";
		public static final String 나이스인증ID = "niceid-";
		public static final String 계정인증ID = "accntid-";
		public static final String 공동인증ID = "pkiceid-";
		public static final String 개인정보조회로그ID = "ilog-";
		public static final String 사업자전환세션ID= "bzmnset-";
		public static final String 비밀번호찾기세션ID = "findpwd-";
		public static final String 이메일인증세션ID = "emailcert-";
		public static final String SMS인증세션ID = "phonecert-";
		public static final String 비밀번호확인세션ID = "pwd-";
	}

	public static class validateMessageExt extends validateMessage {
		public static final String 선택없음		= "%s을(를) 선택하세요";
		public static final String 수정권한없음 = "%s만 수정 할 수 있습니다.";
		public static final String 아이디적합성오류 = "아이디는 4 ~ 12자리 영문 대소문자와 숫자로 입력하세요.";
        public static final String 일시_작거나같은비교오류 = "%s이(가) %s보다 같거나 이전 %s일 수 없습니다.";
        public static final String 일시_작은비교오류 = "%s이(가) %s보다 이전 %s일 수 없습니다.";
        public static final String 일시_크거나같은비교오류 = "%s이(가) %s보다 같거나 이후 %s일 수 없습니다.";
        public static final String 일시_큰비교오류 = "%s이(가) %s보다 이후 %s일 수 없습니다.";
	}

	public static class histMessage {
		public static final String 신규사용자 = "신규 사용자가 등록되었습니다.";
		public static final String 상태변경 = "상태 정보가 변경되었습니다. [%s => %s]";
		public static final String 비밀번호초기화 = "비밀번호가 초기화 되었습니다.";
		public static final String 비밀번호변경 = "비밀번호가 변경되었습니다.";
		public static final String 권한변경 = "권한 정보가 변경되었습니다. [%s => %s]";
		public static final String IP변경 = "IP정보가 변경되었습니다. [%s => %s]";
		public static final String IP등록 = "IP주소가 등록되었습니다. [%s]";
		public static final String 불량회원등록 = "불량회원 등록 처리되었습니다.\n\n ● 이용제한 기간 : [%s]~[%s]\n ● 등록사유 : [%s]\n ● 상세사유 : [%s]";
		public static final String 불량회원해제 = "불량회원 해제 처리되었습니다.";
		public static final String 회원탈퇴 = "회원 탈퇴 처리되었습니다.";
		public static final String 정보변경 = "강사여부가 변경되었습니다.";
	}

	public static class workTypeNm {
		public static final String BLACK = "불량회원등록";
		public static final String UNBLACK = "불량회원해제";
		public static final String AUTHORITY = "권한변경";
		public static final String MEMBERST = "회원상태변경";
		public static final String SECESSION = "회원탈퇴";
		public static final String INSTR = "정보변경";
	}

	public static class snsUrl {
		public static final String NAVER_ME_URL = "https://openapi.naver.com/v1/nid/me";
		public static final String GOOGLE_ME_URL = "https://people.googleapis.com/v1/people/me";
		public static final String KAKAO_ME_URL = "https://kapi.kakao.com/v2/user/me";
		public static final String KAKAO_OAUTH_URL = "https://kauth.kakao.com/oauth/token";
	}

	public static class passwd {
		/**
		 * 비밀번호 적합성 검사
		 *
		 * @param passwd 비밀번호
		 * @param birthday 생년월일
		 * @param passwdHistList 비밀번호변경이력 목록
		 */
		public static void checkValidation(String passwd, String birthday, List<CmmtPasswordHist> passwdHistList) {
			if (!CoreUtils.password.isValid(passwd, 9)) {
				throw new InvalidationException("비밀번호는 숫자, 영문, 특수문자를 포함하여 9자리 이상이어야 합니다.");
			}
			if (string.isNotBlank(birthday)) {
				if (CoreUtils.string.contains(passwd, birthday)) {
					throw new InvalidationException("비밀번호에 생년월일이 포함되어 있습니다. 다른 비밀번호를 입력하세요.");
				}
				birthday = CoreUtils.string.substring(birthday, 4);
				if (CoreUtils.string.contains(passwd, birthday)) {
					throw new InvalidationException("비밀번호에 생년월일이 포함되어 있습니다. 다른 비밀번호를 입력하세요.");
				}
			}
			for(CmmtPasswordHist hist : passwdHistList) {
				if (CoreUtils.password.compare(passwd, hist.getEncPasswd())) {
					throw new InvalidationException("비밀번호가 이전에 사용한 비밀번호와 동일합니다. 다른 비밀번호를 입력하세요.");
				}
			}
		}

		/**
		 * 비밀번호 적합성 검사
		 *
		 * @param passwd 비밀번호
		 * @param birthday 생년월일
		 */
		public static void checkValidation(String passwd, String birthday) {
			if (!CoreUtils.password.isValid(passwd, 9)) {
				throw new InvalidationException("비밀번호는 숫자, 영문, 특수문자를 포함하여 9자리 이상이어야 합니다.");
			}
			if (string.isNotBlank(birthday)) {
				if (CoreUtils.string.contains(passwd, birthday)) {
					throw new InvalidationException("비밀번호에 생년월일이 포함되어 있습니다. 다른 비밀번호를 입력하세요.");
				}
				birthday = CoreUtils.string.substring(birthday, 4);
				if (CoreUtils.string.contains(passwd, birthday)) {
					throw new InvalidationException("비밀번호에 생년월일이 포함되어 있습니다. 다른 비밀번호를 입력하세요.");
				}
			}
		}
	}


	public static class loginId {
		/**
		 * 로그인ID 적합성 검증
		 * (4 ~ 12자리 영문 대소문자)
		 *
		 * @param loginId	로그인ID
		 * @return true/false
		 */
		public static boolean isValid(String loginId)
		{
        	// 로그인ID 자리수 검증
        	if ( 4 > string.length(loginId) || string.length(loginId) > 12 ) {
        		return false;
        	}
			// 로그인ID 영문 대소문자와 숫자를 제외한 문자가 포함되어 있는지 검증
        	if ( loginId.matches(".*[^a-zA-Z0-9]+.*") ) {
        		return false;
        	}
        	return true;
		}
	}
}
