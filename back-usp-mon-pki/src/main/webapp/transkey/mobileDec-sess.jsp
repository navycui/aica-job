<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.raonsecure.transkey.*"%>
<%@ page import="bnet.library.util.CoreUtils.json"%>
<%@ include file="/include/response.jsp" %>
<%
	String id = request.getParameter("id");
	String name = request.getParameter("name");
	String encode = request.getParameter("hidden");
	String hmac = request.getParameter("hmac");
	String decode = "";
	if(name.length()>0){
		decode = TransKey.decode(name, request);
	} else {
		decode = TransKey.decodeForId(id, request);
	}

	HashMap<String, String> decRst = new HashMap<>();
	decRst.put("decode", decode);

	out.println(json.toString(decRst));
%>