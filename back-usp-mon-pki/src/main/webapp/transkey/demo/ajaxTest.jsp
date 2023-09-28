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
%>
<%= decode %>
