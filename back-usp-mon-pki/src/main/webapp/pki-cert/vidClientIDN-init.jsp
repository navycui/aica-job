<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.dreamsecurity.magice2e.MagicE2E" %>
<%@ page import="com.dreamsecurity.magice2e.util.Log"%>
<%@ page import="com.dreamsecurity.magicline.util.Base64" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bnet.library.util.CoreUtils.json"%>
<%@ page import="bnet.library.util.CoreUtils.string"%>
<%@ include file="/include/response.jsp" %>
<%
	String sessionString = "";
	int result = 0;
	StringBuffer sbCert = new StringBuffer();
	//MagicE2E.setConfPath("C:/Users/jung/eclipse-workspace/MagicLine4Web_Ratato/WebContent/WEB-INF/magicline/config/");
	// 세션에 값이 있는지 확인
	//Log.info(this.getClass().getName(), "- Session ID ["+ session.getId() +"]");


	MagicE2E temp = ( MagicE2E ) session.getAttribute("Magie2e");

	if( temp == null ){
		MagicE2E ml = new MagicE2E( sbCert );
		sessionString = sbCert.toString();
		session.setAttribute( "Magie2e", ml );
	}else{
		result = MagicE2E.init();
		if( result == 0 ){
			result = temp.open( sbCert );
			if( result == 0 ){
				sessionString = sbCert.toString();
			}else{
				temp.close();
				session.invalidate();
			}
		}else{
			temp.close();
			session.invalidate();
		}

	}

	Map<String, String> mResult = new HashMap<>();
	mResult.put("uuid", string.getNewId(""));
	mResult.put("cert", sessionString);

	out.print(json.toString(mResult));
%>