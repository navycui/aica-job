package aicluster.mvn.common.util;

import org.apache.commons.lang3.ArrayUtils;

import aicluster.framework.security.Code;
import bnet.library.util.CoreUtils.string;

public class CodeExt extends Code {
    /**
     * 파일 업로드 제한 확장자 모음 클래스
     */
    public static class uploadExt {
        public static final String[] imageExt = {"jpg", "jpeg", "png", "gif"};
        public static final String[] genericExt = ArrayUtils.addAll(new String[]{"pdf", "docx", "doc", "xlsx", "xls", "pptx", "ppt", "hwp"}, imageExt);
    }

    /**
     * 검증오류 메시지 모음 확장 클래스(validationMessage 상속)
     */
    public static class validateMessageExt extends validateMessage {
        public static final String 선택없음 = "%s을(를) 선택하시기 바랍니다.";
        public static final String 체크없음 = "%s을(를) 체크하시기 바랍니다.";
        public static final String 형식오류 = "%s이(가) 올바르지 않은 형식(%s)입니다.";
        public static final String 일시_작거나같은비교오류 = "%s이(가) %s보다 같거나 이전 %s일 수 없습니다.";
        public static final String 일시_작은비교오류 = "%s이(가) %s보다 이전 %s일 수 없습니다.";
        public static final String 일시_크거나같은비교오류 = "%s이(가) %s보다 같거나 이후 %s일 수 없습니다.";
        public static final String 일시_큰비교오류 = "%s이(가) %s보다 이후 %s일 수 없습니다.";
        public static final String 행위오류 = "%s이(가) %s할 수 있습니다.";
        public static final String 행위상태오류 = "%s 상태인 경우에만 %s할 수 있습니다.";
        public static final String 권한없음 = "권한이 없습니다.";
        public static final String 재선택권유 = "유효하지 않는 값입니다. 다시 선택하세요.";
    }

    /**
     * 처리이력 메시지 모음 클래스
     */
    public static class prcsWorkMessage {
    	public static final String 처리완료 = "%s 처리되었습니다.";
    }

    /**
     * 상수 선언 모음 클래스
     */
    public static class criteria {
    	public static final int 연장신청_기한일수 = 60;
    }

    /**
     * 테이블 PK 값 생성을 위한 UUID prefix 선언 모음 클래스
     */
    public static class prefixId {
    	public static final String 이력ID = "hist-";
    	public static final String 입주ID = "mvn-";
    	public static final String 시설ID = "mvnfc-";
    	public static final String 퇴실신청ID = "chkout-";
    	public static final String 연장신청ID = "etreq-";
    	public static final String 자원ID = "rsrc-";
    	public static final String 자원할당ID = "alrs-";
    }

	public static class histMessage {
		public static final String 신규사용자 = "신규 사용자가 등록되었습니다.";
	}

	public static class workTypeNm {
		public static final String 재고변경 = "재고변경";
		public static final String 삭제 = "타입삭제";
		public static final String 추가 = "타입추가";
	}

	public static class searchType {
		public static final String 시설명 = "FC_NM";
		public static final String 사업자명 = "MEMBER_NM";
	}

    public static class mvnFcType {
        public static final String CODE_GROUP = "MVN_FC_TYPE";

        public static final String 사무실 = "OFFICE";
        public static final String 공유시설 = "SHARE";
    }

    public static class mvnSt {
        public static final String CODE_GROUP = "MVN_ST";

        public static final String 공실 = "EMPTY";
        public static final String 입주 = "MVN";
        public static final String 사용불가 = "DISABLE";
    }

    public static class mvnCmpnySt {
        public static final String CODE_GROUP = "MVN_CMNPY_ST";

        public static final String 대기중 = "STNDB";
        public static final String 입주중 = "GOING";
        public static final String 입주종료 = "CLOSE";
    }

    public static class mvnAllocateSt {
        public static final String CODE_GROUP = "MVN_ALLOCATE_ST";

        public static final String 대기중 = "STNDB";
        public static final String 배정완료 = "CMPTN";
    }

    public static class checkoutReqSt {
        public static final String CODE_GROUP = "CHECKOUT_REQ_ST";

        public static final String 신청 = "APLY";
        public static final String 보완 = "SPLMNT";
        public static final String 승인 = "APRV";
        public static final String 신청취소 = "RTRCN";
    }

    public static class reserveType {
    	public static final String CODE_GROUP = "RESERVE_TYPE";

    	public static final String 즉시예약형 = "IMME";
        public static final String 승인형 = "APRV";
    }

    public static class reserveSt {
        public static final String CODE_GROUP = "RESERVE_ST";

        public static final String 신청 = "APLY";
        public static final String 반려 = "RJCT";
        public static final String 승인 = "APRV";
        public static final String 예약취소 = "RSVT_RTRCN";
        public static final String 승인취소 = "APRV_RTRCN";
        public static final String 이용종료 = "CLOSE";
        public static final String 이용대기 = "STNDB";
        public static final String 이용중 = "GOING";
    }

    public static class mvnEtReqSt {
        public static final String CODE_GROUP = "MVN_ET_REQ_ST";

        public static final String 신청 = "APLY"  ;
        public static final String 보완 = "SPLMNT";
        public static final String 접수완료 = "RCPT"  ;
        public static final String 연장반려 = "RJCT"  ;
        public static final String 연장승인 = "APRV"  ;
        public static final String 취소 = "RTRCN" ;
	}

    public static class rsrcGroup {
    	public static final String CODE_GROUP = "RSRC_GROUP";
    }

    public static class rsrcTypeUnit {
    	public static final String CODE_GROUP = "RSRC_TYPE_UNIT";
    }

    public static class procType {
    	public static final String 등록 = "C";
    	public static final String 수정 = "M";
    	public static final String 삭제 = "D";
    }

    public static class alrsrcSt {
    	public static final String CODE_GROUP = "ALRSRC_ST";

    	public static final String 이용대기 = "STNDB";
    	public static final String 이용중 = "GOING";
    	public static final String 이용종료 = "CLOSE";

    	public static boolean isModifyStatus(String alrsrcSt) {
    		if (!string.equals(alrsrcSt, 이용중) && !string.equals(alrsrcSt, 이용종료)) {
    			return false;
    		}
    		return true;
    	}
    }
}
