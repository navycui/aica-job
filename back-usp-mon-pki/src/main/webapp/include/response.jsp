<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
	// Cross Origin 해결을 위해 기입
	response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	response.setHeader("Access-Control-Allow-Credentials", "true");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	//response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Auth");
	//response.setHeader("Access-Control-Allow-Headers", "X-Custom-Header");
%>