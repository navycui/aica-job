<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.raonsecure.transkey.*"%>
<%
// Cross Origin 해결을 위해 기입
response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
response.setHeader("Access-Control-Allow-Credentials", "true");
response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
response.setHeader("Access-Control-Max-Age", "3600");
//response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Auth");
//response.setHeader("Access-Control-Allow-Headers", "X-Custom-Header");


String frmId = request.getParameter("hidfrmId");
String id = request.getParameter("id");
String encode = request.getParameter("hidden");
String hmac = request.getParameter("hmac");
String keyboardType = request.getParameter("keyboardType_"+frmId);
String keyIndex = request.getParameter("keyIndex_"+frmId);
String fieldType = request.getParameter("fieldType_"+frmId);
String seedKey = request.getParameter("seedKey_"+frmId);
String initTime = request.getParameter("initTime_"+frmId);
String decode = TransKey.withoutSessionDecode(id, request);
%>
<%= decode %>