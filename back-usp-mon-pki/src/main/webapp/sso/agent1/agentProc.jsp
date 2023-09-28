<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Base64"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
%><%@page import="pki.sso.SsoParam"
%><%@page import="bnet.library.exception.LoggableException"
%><%@page import="pki.sso.LoginResult"
%><%@page import="bnet.library.util.CoreUtils"
%><%@page import="kong.unirest.Unirest"
%><%@page import="pki.sso.LoginParam"
%><%@page import="kong.unirest.HttpResponse"
%><%@ page import="java.util.Enumeration"
%><%@ include file="agentInfo.jsp"
%><%
	/**
	 * 설정정보
	 */
	ResourceBundle resource = ResourceBundle.getBundle("setting");
	final String HOST_SSO = resource.getString("host.sso");
	final String HOST_USP = resource.getString("host.usp");
	final String HOST_FRONT = resource.getString("host.front");

    /**
     * agentProc - 인증이 완료된 후 호출 되는 페이지
     */

    String resultCode = (String)session.getAttribute("sso-resultCode");
    String resultMessage = (String)session.getAttribute("sso-resultMessage");
    String nextUrl = (String)session.getAttribute("sso-nextUrl");
    String loginId = (String)session.getAttribute("sso-loginId");

//     System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
// 	System.out.println("sesson ID : ["+session.getId()+"]");
// 	for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements();) {
// 		String key = e.nextElement();
// 		System.out.println("==["+key+"]:["+session.getAttribute(key)+"]");
// 	}
// 	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    if (CoreUtils.string.isBlank(nextUrl)) {
    	nextUrl = "";
    }

    if (!"000000".equals(resultCode)) {
		response.sendRedirect(HOST_FRONT + "/signin");
		return;
    }

    /**
     * 로그인 처리
     */
	String url = HOST_USP + "/member/api/login/sso";
	Unirest.config().reset();
 	Unirest.config().connectTimeout(1000 * 500);
 	Unirest.config().socketTimeout(1000 * 500);

 	String key = CoreUtils.string.getNewId("");
 	String encLoginId = CoreUtils.aes256.encrypt(loginId, key);

 	SsoParam param = SsoParam.builder()
 			.loginId(encLoginId)
 			.resultCode(resultCode)
 			.key(key)
 			.build();

 	String paramBody = CoreUtils.json.toString(param);
 	HttpResponse<LoginResult> res = Unirest.post(url)
 			.accept("application/json")
 			.contentType("application/json")
 			.body(paramBody)
 			.asObject(LoginResult.class);

 	System.out.println("Unirest-res:" + res.toString());

  	if (res.getStatus() != 200) {
 		throw new LoggableException("로그인에 실패하였습니다." + res.getStatusText());
 		//System.out.println("로그인에 실패하였습니다." + res.getStatusText());
 		//return;
 	}

  	LoginResult result = res.getBody();
 	Cookie cookie = new Cookie("member-refresh-token", result.getRefreshToken());
 	cookie.setMaxAge(1800);
 	cookie.setSecure(false);
 	cookie.setHttpOnly(true);
 	cookie.setDomain("atops.or.kr");
 	cookie.setPath("/");
 	response.addCookie(cookie);

	response.setHeader("Set-AccessToken", result.getAccessToken());
	response.setHeader("Set-RefreshTokenExpiresIn", "" + result.getRefreshTokenExpiresIn());

	response.sendRedirect(HOST_FRONT + "/signin/bridge?nextUrl=" + nextUrl);
%>