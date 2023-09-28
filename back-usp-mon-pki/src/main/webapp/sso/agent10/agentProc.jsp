<%@page import="org.apache.commons.lang3.StringUtils"
%><%@page import="java.util.ResourceBundle"
%><%@page import="java.util.Base64"
%><%@ page language="java" contentType="text/html; charset=UTF-8"
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
	final String HOST_SAZ = resource.getString("host.saz");
	final String HOST_DXP = resource.getString("host.dxp");
	final String HOST_LMS = resource.getString("host.lms");
	final String HOST_LCMS = resource.getString("host.lcms");

    /**
     * agentProc - 인증이 완료된 후 호출 되는 페이지
     */

    String resultCode = (String)session.getAttribute("sso-resultCode");
    String resultMessage = (String)session.getAttribute("sso-resultMessage");
    String nextUrl = (String)session.getAttribute("sso-nextUrl");
    String loginId = (String)session.getAttribute("sso-loginId");

    if (CoreUtils.string.isBlank(nextUrl)) {
    	nextUrl = "";
    }

    if (!"000000".equals(resultCode)) {
		response.sendRedirect(HOST_FRONT + "/signin?agentId=" + agentId);
		return;
    }

    /**
     * 로그인 처리
     */
    String host = request.getServerName();
    // LCMS SSO 로그인 주소
	String url = HOST_LCMS + "/sso.do";

 	String key = CoreUtils.string.getNewId("");
 	String encLoginId = CoreUtils.aes256.encrypt(loginId, key);
%>
<!DOCTYPE html>
<html>
<head>
	<title>agentProc</title>
</head>
<body>
	<form id="secForm" action="<%=url%>" method="post">
		<input type="hidden" name="loginId" value="<%=encLoginId%>">
		<input type="hidden" name="resultCode" value="<%=resultCode%>">
		<input type="hidden" name="key" value="<%=key%>">
	</form>

	<script type="text/javascript">
		var form = document.getElementById("secForm");
		form.submit();
	</script>
</body>
</html>