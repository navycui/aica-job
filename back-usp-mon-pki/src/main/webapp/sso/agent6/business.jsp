<%@page import="java.util.Base64"%>
<%@page import="bnet.library.util.CoreUtils"
%><%@page import="bnet.library.exception.LoggableException"
%><%@page import="pki.sso.AgentInfo"
%><%@page import="pki.sso.SsoCheckResult"
%><%@page import="kong.unirest.HttpResponse"
%><%@page import="kong.unirest.Unirest"
%><%@page language="java" contentType="text/html; charset=UTF-8"
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
%><%@page import="java.util.Map"
%><%@page import="java.io.InputStreamReader"
%><%@page import="org.apache.http.conn.HttpHostConnectException"
%><%@page import="org.slf4j.Logger"
%><%@page import="org.slf4j.LoggerFactory"
%><%@page import="java.io.IOException"
%><%@page import="java.text.ParseException"
%><%@include file="agentInfo.jsp"
%><%!
public String decode(String encodeUrl) {
	byte[] buf = Base64.getDecoder().decode(encodeUrl);
	String url = new String(buf);
	return url;
}
%><%
	/*
	 * Next URL이 있으면 Session에 저장한다.
	 */
	String nextUrl = request.getParameter("nextUrl");
	if (CoreUtils.string.isNotBlank(nextUrl)) {
		session.setAttribute("sso-nextUrl", nextUrl);
	}
	/*
	 * Unirest 재설정
	 */
	Unirest.config().reset();
	Unirest.config().connectTimeout(connectionTimeout);
	Unirest.config().socketTimeout(soTimeout);

	System.out.println("#### SESSION-VALUE:" + session.getAttribute("nextUrl"));

	/*
	 * SSO 서버 통신 체크
	 */
	HttpResponse<String> res = Unirest.get(CHECK_SERVER_URL).asString();
	if (res.getStatus() != 200) {
		System.out.println("-----------------");
		System.out.println("URL:" + CHECK_SERVER_URL);
		System.out.println("status:" + res.getStatus());
		System.out.println("-----------------");
		response.sendRedirect(NETWORK_ERROR_PAGE);
        return;
	}
	String body = res.getBody();
	SsoCheckResult result = CoreUtils.json.toObject(body, SsoCheckResult.class);
	if (!CoreUtils.string.equals(result.getResultCode(), "000000")) {
		System.out.println("-----------------");
		System.out.println("URL:" + CHECK_SERVER_URL);
		System.out.println("resultCode:" + result.getResultCode());
		System.out.println("-----------------");
		response.sendRedirect(NETWORK_ERROR_PAGE);
        return;
	}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>login</title>
</head>

<body>
    <form name="sendForm" method="post">
        <input type="hidden" name="agentId" value="<%=agentId%>" />
    </form>

    <script>
        var sendUrl = "<%=AUTH_LOGIN_PAGE%>";
        var sendForm = document.sendForm;

        sendForm.action = sendUrl;
        sendForm.submit();
    </script>

</body>
</html>
