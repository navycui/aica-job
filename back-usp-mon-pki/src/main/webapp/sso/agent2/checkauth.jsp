<%@page import="bnet.library.exception.InvalidationException"%>
<%@page import="pki.sso.SsoCheckAuthResult"%>
<%@page import="bnet.library.exception.LoggableException"%>
<%@page import="kong.unirest.HttpResponse"%>
<%@page import="kong.unirest.Unirest"%>
<%@page import="pki.sso.CheckAuthResult"%>
<%@page import="bnet.library.util.CoreUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
%><%@ page import="org.apache.http.NameValuePair"
%><%@ page import="org.apache.http.client.ClientProtocolException"
%><%@page import="org.apache.http.client.methods.CloseableHttpResponse"
%><%@page import="org.apache.http.impl.client.CloseableHttpClient"
%><%@page import="org.apache.http.client.methods.HttpGet"
%><%@page import="org.apache.http.client.config.RequestConfig"
%><%@page import="org.apache.http.impl.client.HttpClientBuilder"
%><%@page import="org.apache.http.impl.client.HttpClients"
%><%@page import="org.apache.http.conn.ConnectTimeoutException"
%><%@page import="java.io.BufferedReader"
%><%@page import="org.json.simple.JSONObject"
%><%@page import="org.json.simple.parser.JSONParser"
%><%@page import="org.apache.http.message.BasicNameValuePair"
%><%@page import="java.util.ArrayList"
%><%@page import="org.apache.http.client.entity.UrlEncodedFormEntity"
%><%@page import="java.io.InputStreamReader"
%><%@page import="org.apache.http.client.methods.HttpPost"
%><%@page import="java.util.List"
%><%@page import="org.slf4j.Logger"
%><%@page import="org.slf4j.LoggerFactory"
%><%@page import="java.io.IOException"
%><%@ include file="agentInfo.jsp"%>
<%
    Logger log = LoggerFactory.getLogger("checkauth");
    /**
     * checkauth - SSO로부터 호출 되는 페이지
     *      토큰 검증 및 업데이트 진행
     */
    //logger.debug("[[[ checkauth page ]]]");

    String resultCode = request.getParameter("resultCode") == null ? "" : request.getParameter("resultCode");
    String secureToken = request.getParameter("secureToken") == null ? "" : request.getParameter("secureToken");
    String secureSessionId = request.getParameter("secureSessionId") == null ? "" : request.getParameter("secureSessionId");
    String clientIp = CoreUtils.webutils.getRemoteIp(request);

    String resultMessage = "";
    String returnUrl = "";
    String loginId = "";


    boolean checkResultCode = CoreUtils.string.equals(resultCode, "000000");
	boolean checkSecureToken = CoreUtils.string.isNotBlank(secureToken);
	if (!checkResultCode || !checkSecureToken) {
		// 비정상 호출 할 경우 Business 페이지로 리다이렉션 처리
		returnUrl = LOGOUT_PAGE;
	}
	else {

		/*
		 * Unirest 재설정
		 */
		Unirest.config().reset();
    	Unirest.config().connectTimeout(1000 * 5);
    	Unirest.config().socketTimeout(1000 * 5);

    	HttpResponse<String> res = Unirest.post(TOKEN_AUTHORIZATION_URL)
    			.field("secureToken", secureToken)
    			.field("secureSessionId", secureSessionId)
    			.field("requestData", requestData)
    			.field("agentId", agentId)
    			.field("clientIP", clientIp)
    			.asString();

    	if (res.getStatus() != 200) {
    		session.setAttribute("errorMessage", "SSO 서버와 연결하는데 실패하였습니다.");
			returnUrl = ERROR_PAGE;
    	}
    	String body = res.getBody();
		log.info("[httpResponse] : " + body);
		SsoCheckAuthResult scar = CoreUtils.json.toObject(body, SsoCheckAuthResult.class);

		resultCode = scar.getResultCode();
		resultMessage = scar.getResultMessage();
		// Return URL(인증서버에서 리다이렉션될 주소를 전달)
		returnUrl = scar.getReturnUrl();

		// 사용자 요청 정보
		SsoCheckAuthResult.User user = scar.getUser();
		// 요청 데이터 정보 추출
		if ("000000".equals(resultCode)) {
			loginId = user.getLogin_id();
			log.info(">>>loginId=" + loginId);
			if (loginId == null) {
				log.info(">>>> exception");
				session.setAttribute("errorMessage", "SSO 서버로부터 사용자 정보를 얻지 못했습니다.");
				returnUrl = ERROR_PAGE;
			}
			else {
				// 무조건 saveToken.html 호출
				returnUrl = SAVE_TOKEN_URL;
			}

		} else if ("310017".equals(resultCode) || "310012".equals(resultCode)) {
			// 서비스 접근 권한 실패(다른 서비스에 영향을 주어서는 안됨으로 로그아웃은 하지 않음)
			returnUrl = SERVICE_ERR_PAGE;
		} else {
			// SSO 검증 실패(로그아웃 필요)
			returnUrl = ERROR_PAGE;
		}
	}

	CheckAuthResult car = CheckAuthResult.builder()
			.agentId(agentId)
			.resultCode(resultCode)
			.resultMessage(resultMessage)
			.secureSessionId(secureSessionId)
			.returnUrl(returnUrl)
			.loginId(loginId)
			.build();

	session.setAttribute("sso-agentId", agentId);
	session.setAttribute("sso-resultCode", resultCode);
	session.setAttribute("sso-resultMessage", resultMessage);
	session.setAttribute("sso-secureSessionId", secureSessionId);
	session.setAttribute("sso-returnUrl", returnUrl);
	session.setAttribute("sso-loginId", loginId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>auth</title>
</head>
<body>
<form name="sendForm" method="post">
    <input type="hidden" name="agentId" value="<%=agentId%>" />
    <input type="hidden" name="resultCode" value="<%=resultCode%>" />
    <input type="hidden" name="secureSessionId" value="<%=secureSessionId%>" />
</form>

<script>
    var sendUrl = "<%=returnUrl%>";
    var sendForm = document.sendForm;
    sendForm.action = sendUrl;
    sendForm.submit();
</script>
</body>
</html>