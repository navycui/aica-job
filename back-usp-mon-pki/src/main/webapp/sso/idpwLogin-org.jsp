﻿<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@include file="agentInfo.jsp"%>
<%
    Logger logger = LoggerFactory.getLogger("idpwLogin");

    String clientIp = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");

    logger.debug("[Auth url] : " + AUTH_URL);
    logger.debug("[agent id] : " + agentId);
    logger.debug("[clientIp] : " + clientIp);
    logger.debug("[userAgent] : " + userAgent);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>auth</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="jquery-3.1.1.min.js"></script>
    <script type="text/javascript">
        $(function() {

            var props = {
                constants : {},
                elements:{
                    $formlogin : $('#form-login'),
                    $btnLogin : $('#btn-login')
                }
            };

            $("#btn-login").click(function(){
                var id = $('#id').val();
                var pw = $('#pw').val();
                var agentId = $('#agentId').val();

                if (id == undefined || id.length == 0) {
                    alert("id를 입력하세요.");
                    return;
                }
                if (pw == undefined || pw.length == 0) {
                    alert("비밀번호를 입력하세요.");
                    return;
                }

                //var action = "<%=IDPW_LOGIN_PROCESS%>";
                var action = "<%=OAUTH_LOGIN_PROCESS%>";
                props.elements.$formlogin.attr('action', action);
                props.elements.$formlogin.attr('method', 'post');
                props.elements.$formlogin.submit();
            });

            $("#pw").keydown(function(key) {
                if(key.keyCode == 13) {
                    $("#btn-login").click();
                }

            });
        });
    </script>
</head>

<body>

<h1>idpw login page</h1>
<form id="form-login">
    <input type="hidden" id="agentId" name="agentId" value="<%=agentId%>">
    <input type="text" id="id"name="id" placeholder="loginId">
    <input type="password" id="pw" name="pw" placeholder="loginPw">
    <button type="button" id="btn-login" >로그인</button>
</form>
</body>
</html>
