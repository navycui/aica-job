<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@ include file="agentInfo.jsp"%>
<%
    Logger logger = LoggerFactory.getLogger("logout");
    /**
     * logout - SSO 전체 로그아웃을 진행
     * 		이 프로그램을 실행하기 전에 개별 시스템 로그아웃을 먼저 선행해 주세요.
     */
    try {
        session.invalidate();
    } catch (IllegalStateException e) {
        logger.error("failed to session invalidate");
    }

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>logout</title>
</head>
<body>
<form name="sendForm" method="post" action="<%=AUTH_LOGOUT_PAGE%>">
    <input type="hidden" name="agentId" value="<%=agentId%>" />
</form>

<script>
    var sendForm = document.sendForm;
    sendForm.submit();
</script>
</body>
</html>