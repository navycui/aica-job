<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.raonsecure.transkey.*"%>
<%@ page import="bnet.library.util.CoreUtils.json"%>
<%@ include file="/include/response.jsp" %>
<%
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

	HashMap<String, String> decRst = new HashMap<>();
	decRst.put("decode", decode);

	out.println(json.toString(decRst));
%>