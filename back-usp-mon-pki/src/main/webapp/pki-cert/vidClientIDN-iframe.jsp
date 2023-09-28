<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.net.URL"%>
<%@ page import="bnet.library.util.CoreUtils.string"%>
<%@ page import="bnet.library.util.CoreUtils.validation"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%
	Logger logger = LoggerFactory.getLogger("vidClientIDN-iframe");

	StringBuffer bfUrl = request.getRequestURL();
	URL url = new URL(bfUrl.toString());

	String bizrno = request.getParameter("bizrno");
	String callback = request.getParameter("callback");

	if (string.isBlank(bizrno)) {
		bizrno = "";
	}
	if (string.isBlank(callback)) {
		callback = "";
	}

	boolean isBizrno = validation.isCompanyNumber(bizrno);
	boolean isProd = true;

	if (string.equals(url.getHost(), "pc.atops.or.kr") || string.equals(url.getHost(), "dev-portal.atops.or.kr")) {
		isProd = false;
	}

// 	logger.debug("Protocol : " + url.getProtocol());
// 	logger.debug("Authority : " + url.getAuthority());
// 	logger.debug("host : " + url.getHost());
// 	logger.debug("Path : " + url.getPath());
// 	logger.debug("Query : " + url.getQuery());
// 	logger.debug("Ref : " + url.getRef());
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>공동인증서 인증</title>
    <%-- b: 공동인증서 --%>
    <%-- 외부 JS --%>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/ext/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/ext/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/ext/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/ext/json2.js"></script>
    <%-- ML4WEB JS --%>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/ext/ML_Config.js"></script>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/crypto/magicjs_1.2.1.0.min.js"></script>
    <script type="text/javascript" src="/pki/MagicLine4Web/ML4Web/js/magic_e2e.js"></script>
    <%-- e: 공동인증서 --%>

    <script type="text/javascript">
    //<![CDATA[
    	var BIZRNO = "<%=bizrno%>";
    	var CALLBACK = "<%=callback%>";
    	var ORIGIN = "<%=request.getHeader("Origin")%>";
    	var objCert;

		$(document).ready(function() {
			if (BIZRNO == "") {
				alert("사업자등록번호를 입력하세요.");
				sendMessage(999, "");
				return;
			}
			<%-- 운영환경인 경우 사업자등록번호 규칙 검증 --%>
			<% if (isProd && !isBizrno) { %>
			alert("사업자등록번호 형식이 맞지 않습니다.");
			sendMessage(999, "");
			return;
			<% } %>

			$.getJSON("./vidClientIDN-init.jsp", function(data){
                var uuid = data.uuid;
                var cert = data.cert;

                var $form = $("form[name='reqForm']");

                <%--// 초기화 정상인 경우 인증서 선택창 호출 --%>
                if (cert != undefined && cert != "") {
                    objCert = JSON.parse(cert);

                    $form.find("input[name='idn']").val(BIZRNO);
                    $form.find("input[name='signData']").val(uuid);
                    $form.find("input[name='signOrigin']").val(uuid);

                    // 1 초 이후에 인증서 선택창 실행(모바일에서는 ml4web init 스크립트 구동으로인한 딜레이 시간이 0.5초도 짧다.)
                    setTimeout(function(){
                    	magicline.uiapi.MakeSignData(document.reqForm, null, mlCallBack);
                   	}, 1000);
                }
                else {
                    alert("공동인증서 모듈 초기화에 실패하였습니다.");
                    sendMessage(999, "");
                }
			});
		});

        <%--// MagicLine 결과값 수신 CallBack --%>
        <%--// code    : 전자서명 결과값 --%>
        <%--// message : 전자서명 메시지 --%>
        function mlCallBack(code, message){
            if(code==0){ <%--// 정상 --%>
                var $form = $("form[name='reqForm']");

				<%--// ajax post 전송 data 정의 --%>
				var paramJson = {
					"signOrigin": $form.find("input[name='signOrigin']").val(),
					"sign": encodeURIComponent( message.encMsg ),
					"vidRandom": "",
					"encData": ""
				}

				if(message.vidRandom != null){
					paramJson.vidRandom = encodeURIComponent(message.vidRandom);
				}

				<%--// 개인정보 암호화 --%>
				if(BIZRNO != ""){
				    var ml = new MagicE2E( objCert );
				    paramJson.encData = encodeURIComponent( ml.Encrypt("encIdn=" + BIZRNO) );
				}
				else {
				    alert("사업자등록번호를 입력하세요.");
				    sendMessage(code, "");
				    return false;
				}

    			$.post("./vidClientIDN-result.jsp", paramJson, function(data) {
                    if (data.code == "0") {
						sendMessage(data.code, data.key);
                    }
                    else {
                        alert(data.message);
                        sendMessage(data.code, "");
                    }
            	}, "json");
            }else if (code == 999) {
				console.log("[callback] 창닫기 수행");
				sendMessage(code, "");
            }else{ // 수신 오류
                alert("결과값 수신에 실패하였습니다.");
                sendMessage(code, "");
            }
        }

        <%--// 부모창에 메시지 보내기 --%>
        function sendMessage(code, data)
        {
    		var requestObj = {
   				funcNm : CALLBACK,
   				sessionKey : data,
   				code : code
   			};
   			var requestStr = JSON.stringify(requestObj);

   			window.parent.postMessage(requestStr, ORIGIN);
        }
    //]]>
    </script>
</head>
<body>
    <%-- b : 공동인증서 연동 구현 필수 선언 태그 --%>
    <form name="reqForm">
        <input type="hidden" id='sign' name='sign'/>
        <input type="hidden" id="signOrigin" name="signOrigin" />
        <input type="hidden" id="vidRandom" name="vidRandom"/>
        <input type="hidden" id="vidType" name="vidType" value="client"/>
        <input type="hidden" id="encData" name="encData" value=""/>
        <input type="hidden" id="signData" name="signData" value="" />
        <input type="hidden" id="idn" name="idn" value=""/>
    </form>
    <div id="dscertContainer">
        <iframe id="dscert" name="dscert" src="" width="100%" height="100%" allowTransparency="true" style="position:fixed;z-index:100010;top:0px;left:0px;width:100%;height:100%;" title=""></iframe>
    </div>
    <%-- e : 공동인증서 연동 구현 필수 선언 태그 s --%>
</body>
</html>