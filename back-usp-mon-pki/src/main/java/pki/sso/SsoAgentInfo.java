package pki.sso;

public class SsoAgentInfo {
    /************************************************************
     *  Web-Agent 환경 설정   (** 수정)
     ************************************************************/
    /** 유영민: 설정을 만들어 사용 */
/**/public static final String AUTH_URL = "http://dev-sso.atops.or.kr";     // ISign+ SSO URL
/**/public static final String agentId = "3";                          // 업무 시스템 고유 번호(관리자가 할당한 번호)
/**/public static final String requestData = "login_id";       // 요청 데이터(세션에 저장될 값)


    /************************************************************
     *  Agent Page
     ************************************************************/
    public static final String BUSINESS_PAGE = "business.jsp";     // SSO 에이전트 호출 시작 페이지
    public static final String LOGOUT_PAGE = "logout.jsp";         // SSO 에이전트 로그아웃 페이지
    public static final String ERROR_PAGE = "error.jsp";           // SSO 인증 실패시 호출 되는 페이지(에러 출력용)
    public static final String NETWORK_ERROR_PAGE = "notice.jsp";
    public static final String EXCEPTION_PAGE = "exception.jsp";               // SSO 네트워크 통신 실패 시 기존 업무 로그인 페이지 또는 에러 화면으로 리다이렉션


    /************************************************************
     *  HttpClient Timeout 설정
     ************************************************************/
    public static final int connectionTimeout = 5000;	    // 서버 응답 시간 한도 설정
    public static final int soTimeout = 5000;			    // 연결 후 Read 하는 동안 특정 시각동안 패킷이 없을 경우 Connection 종료
    public static final int maxTotalConnections = 500;     // keepalive 연결


    /************************************************************
     *  ISign+ 호출 URL
     ************************************************************/
     /** Class에 저장하여 사용 */
    public static final String CHECK_SERVER_URL = AUTH_URL + "/openapi/checkserver";
    public static final String TOKEN_AUTHORIZATION_URL = AUTH_URL + "/token/authorization";
    public static final String SAVE_TOKEN_URL = AUTH_URL + "/token/saveToken.html";
    public static final String AUTH_LOGIN_PAGE = AUTH_URL + "/login.html";
    public static final String AUTH_LOGOUT_PAGE = AUTH_URL + "/logout.html";
    public static final String SERVICE_ERR_PAGE = AUTH_URL + "/error.html";
    public static final String GET_PUBLICKEY_URL = AUTH_URL + "/openapi/authentication/publickey/get";
    // Login Process
    public static final String IDPW_LOGIN_PROCESS = AUTH_URL + "/authentication/idpw/loginProcess";
    public static final String PKI_LOGIN_PROCESS = AUTH_URL + "/authentication/pki/loginProcess";
    public static final String ISSACWEB_LOGIN_PROCESS = AUTH_URL + "/authentication/issacweb/loginProcess";
    // After Login Process
    public static final String PORTAL_PAGE = AUTH_URL + "/user/portal/userPortal.html";
    public static final String PKI_REGIST_PAGE = AUTH_URL + "/authentication/pki/pkiRegister.html";

	// ISign+ WA Version
	public static final String ISIGN_PLUS_WA_VER = "3.0.19.1";
}
