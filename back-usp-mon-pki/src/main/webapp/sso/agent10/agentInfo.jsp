<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%!
	/**
	 * 설정정보
	 */
	ResourceBundle resource = ResourceBundle.getBundle("setting");
	final String HOST_SSO = resource.getString("host.sso");
	final String HOST_USP = resource.getString("host.usp");
	final String HOST_FRONT = resource.getString("host.front");
	final String HOST_SAZ = resource.getString("host.saz");
	final String HOST_DXP = resource.getString("host.dxp");
	final String HOST_LMS = resource.getString("host.lms");
	final String HOST_LCMS = resource.getString("host.lcms");

    /************************************************************
     *  Web-Agent 환경 설정   (** 수정)
     ************************************************************/
/**/final String AUTH_URL = HOST_SSO;     // ISign+ SSO URL
/**/final String agentId = "10";                          // 업무 시스템 고유 번호(관리자가 할당한 번호)
/**/final String requestData = "login_id";       // 요청 데이터(세션에 저장될 값)


    /************************************************************
     *  Agent Page
     ************************************************************/
    final String BUSINESS_PAGE = "business.jsp";     // SSO 에이전트 호출 시작 페이지
    final String LOGOUT_PAGE = "logout.jsp";         // SSO 에이전트 로그아웃 페이지
    final String ERROR_PAGE = "error.jsp";           // SSO 인증 실패시 호출 되는 페이지(에러 출력용)
    final String NETWORK_ERROR_PAGE = "notice.jsp";
    final String EXCEPTION_PAGE = "exception.jsp";               // SSO 네트워크 통신 실패 시 기존 업무 로그인 페이지 또는 에러 화면으로 리다이렉션


    /************************************************************
     *  HttpClient Timeout 설정
     ************************************************************/
    final int connectionTimeout = 5000;	    // 서버 응답 시간 한도 설정
    final int soTimeout = 5000;			    // 연결 후 Read 하는 동안 특정 시각동안 패킷이 없을 경우 Connection 종료
    final int maxTotalConnections = 500;     // keepalive 연결


    /************************************************************
     *  ISign+ 호출 URL
     ************************************************************/
    final String CHECK_SERVER_URL = AUTH_URL + "/openapi/checkserver";
    final String TOKEN_AUTHORIZATION_URL = AUTH_URL + "/token/authorization";
    final String SAVE_TOKEN_URL = AUTH_URL + "/token/saveToken.html";
    final String AUTH_LOGIN_PAGE = AUTH_URL + "/login.html";
    final String AUTH_LOGOUT_PAGE = AUTH_URL + "/logout.html";
    final String SERVICE_ERR_PAGE = AUTH_URL + "/error.html";
    final String GET_PUBLICKEY_URL = AUTH_URL + "/openapi/authentication/publickey/get";
    // Login Process
    final String IDPW_LOGIN_PROCESS = AUTH_URL + "/authentication/idpw/loginProcess";
    final String OAUTH_LOGIN_PROCESS = AUTH_URL + "/authentication/oauth/loginProcess";
    final String PKI_LOGIN_PROCESS = AUTH_URL + "/authentication/pki/loginProcess";
    final String ISSACWEB_LOGIN_PROCESS = AUTH_URL + "/authentication/issacweb/loginProcess";
    // After Login Process
    final String PORTAL_PAGE = AUTH_URL + "/user/portal/userPortal.html";
    final String PKI_REGIST_PAGE = AUTH_URL + "/authentication/pki/pkiRegister.html";

	// ISign+ WA Version
	final String ISIGN_PLUS_WA_VER = "3.0.19.1";
%>
