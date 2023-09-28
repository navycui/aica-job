package aicluster.common.common.util;

import org.apache.commons.lang3.ArrayUtils;

import aicluster.framework.security.Code;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.string;

public class CodeExt extends Code {
    /**
     * 파일 업로드 제한 확장자 모음 클래스
     */
    public static class uploadExt {
        public static final String[] imageExt = {"jpg", "jpeg", "png", "gif"};
        public static final String[] genericExt = ArrayUtils.addAll(new String[]{"pdf", "docx", "doc", "xlsx", "xls", "pptx", "ppt", "hwp", "txt"}, imageExt);
    }

    /**
     * 회원유형 검증 모음 클래스
     */
	public static class memberTypeExt extends memberType {
		public static final String[] 사업자유형 = {개인사업자, 법인사업자, 대학};

		public static boolean isBzmn(String memberType) {
			return array.contains(사업자유형, memberType);
		}
		public static boolean isIndividual(String memberType) {
			return string.equals(memberType, 개인);
		}
	}

    /**
     * 검증오류 메시지 모음 확장 클래스(validationMessage 상속)
     */
	public static class validateMessageExt extends validateMessage {
		public static final String 선택없음				= "%s을(를) 선택하세요";
        public static final String 형식오류 = "%s이(가) 올바르지 않은 형식(%s)입니다.";
        public static final String 일시_작거나같은비교오류 = "%s이(가) %s보다 같거나 이전 %s일 수 없습니다.";
        public static final String 일시_작은비교오류 = "%s이(가) %s보다 이전 %s일 수 없습니다.";
        public static final String 일시_크거나같은비교오류 = "%s이(가) %s보다 같거나 이후 %s일 수 없습니다.";
        public static final String 일시_큰비교오류 = "%s이(가) %s보다 이후 %s일 수 없습니다.";
	}

	/**
	 * 검색 조건 중 검색유형 모음 클래스
	 */
	public static class searchType {
		public static final String CODE_GROUP = "";

		public static final String 제목 = "TITLE";
		public static final String 내용 = "CONTENTS";
		public static final String 전체 = "ALL";
	}

	/**
	 * 이벤트 게시 목록에 대한 정렬기준 모음 클래스
	 */
	public static class eventSortType {
		public static final String 생성일시 = "CREATED_DT";
		public static final String 조회건수 = "READ_CNT";
	}
}