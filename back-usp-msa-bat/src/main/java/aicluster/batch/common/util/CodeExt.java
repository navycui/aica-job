package aicluster.batch.common.util;

import aicluster.framework.security.Code;

public class CodeExt extends Code {

    /**
     * 테이블 PK 값 생성을 위한 UUID prefix 선언 모음 클래스
     */
    public static class prefixId {
    	public static final String 이력ID = "hist-";
    }

    /**
     * API 시스템ID 모음 클래스
     */
    public static class apiSystemId {
		public static final String CODE_GROUP = "API_SYSTEM_ID";
	}

    /**
     * 입주관리 - 입주업체상태코드 모음 클래스
     */
    public static class mvnCmpnySt {
        public static final String CODE_GROUP = "MVN_CMNPY_ST";

        public static final String 대기중 = "STNDB";
        public static final String 입주중 = "GOING";
        public static final String 입주종료 = "CLOSE";
    }

    /**
     * 입주관리 - 입주상태코드 모음 클래스
     */
    public static class mvnSt {
        public static final String CODE_GROUP = "MVN_ST";

        public static final String 공실 = "EMPTY";
        public static final String 입주 = "MVN";
        public static final String 사용불가 = "DISABLE";
    }
}