<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@include file="agentInfo.jsp"%>
<%
System.out.println("- HOST_SSO: " + HOST_SSO);
System.out.println("- HOST_USP: " + HOST_USP);
System.out.println("- HOST_FRONT: " + HOST_FRONT);


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
	<title>login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="/pki/static/component/css/sweetalert2.min.css">
	<script type="text/javascript" src="/pki/static/jquery/jquery.min.js" ></script>
	<script type="text/javascript" src="/pki/static/jquery/jquery-ui.min.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/jquery.blockUI.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/sweetalert2.all.min.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/mustache.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/component-config.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/component-old.js" ></script>
	<script type="text/javascript" src="/pki/static/component/js/component.js" ></script>

    <script type="text/javascript">
        $(function() {

            function aicaLogin() {
				var id = $("#loginId").val();
				var pw = $("#passwd").val();

				if (id == undefined || id.length == 0) {
                    alert("id를 입력하세요.");
                    return;
                }
                if (pw == undefined || pw.length == 0) {
                    alert("비밀번호를 입력하세요.");
                    return;
                }

                $component.postJson({
                	url: '<%=HOST_USP%>/member/api/login/member',
                	data: {
                		errorId:false,
                		errorPw:false,
                		isLock:false,
                		labelId:"로그인",
                		labelPw:"비밀번호",
                		open:false,
                		loginId: id,
                		passwd: pw,
                	},
                	success: function(data, status, xhr) {
                		// 비빌번호를 변경해야 한다면,
                		sessionStorage.setItem("changePasswd", true)
                		sessionStorage.setItem("initPasswd", true)
						if (data.changePasswd) {
							console.log("비밀번호를 변경해야 합니다.");

						}
                		if (data.initPasswd) {
                			console.log("비밀번호가 초기화되었습니다.");
                		}
                		// 비밀번호가 초기화 되었다면,

						ssoLogin(data.dbid, data.dbpw);
                	}
                });
            }

            function ssoLogin(id, pw) {
                //var action = "<%=IDPW_LOGIN_PROCESS%>";
                var action = "<%=OAUTH_LOGIN_PROCESS%>";

                var $form = $('#form-login-sso');
                $form.find("input[name=id]").val(id);
                $form.find("input[name=pw]").val(pw);
                $form.attr('action', action);
                $form.attr('method', 'post');
                $form.submit();
            }

            $("#btn-login").click(function(){
				aicaLogin();
            });
        });
    </script>
</head>

<body>

<h1>idpw login page</h1>
<!-- SSO 로그인 -->
<form id="form-login-sso">
    <input type="hidden" name="agentId" value="<%=agentId%>">
    <input type="hidden" name="id" placeholder="loginId">
    <input type="hidden" name="pw" placeholder="loginPw">
</form>

<!-- AICA 로그인 -->
<form id="form-login-aica">
    <input type="text" id="loginId" name="loginId" placeholder="아이디" value="test">
    <input type="password" id="passwd" name="passwd" placeholder="비밀번호" value="1234">
    <button type="button" id="btn-login" >로그인</button>
</form>
</body>
</html>
