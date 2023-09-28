<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.raonsecure.transkey.*"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>validate mobile</title></head>
<body>
	<hr></hr>
	use_form_id = true
	<hr></hr>
	initTime: <%= request.getParameter("initTime_frm") %><br></br>
	seedKey: <%= request.getParameter("seedKey_frm") %><br></br>
	<hr></hr>
	pwd1(use params):<%= TransKey.withoutSessionDecode("pwd1", request.getParameter("initTime_frm"), request.getParameter("keyboardType_pwd1_frm"), request.getParameter("keyIndex_pwd1_frm"), request.getParameter("fieldType_pwd1_frm"), request.getParameter("seedKey_frm"), request.getParameter("transkey_pwd1_frm"), request.getParameter("transkey_HM_pwd1_frm"), request.getParameter("transkey_ExE2E_pwd1_frm")).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1(use request):<%= TransKey.withoutSessionDecode("pwd1", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1EncData:<%= request.getParameter("transkey_pwd1_frm") %><br></br>
	pwd1HmacData:<%= request.getParameter("transkey_HM_pwd1_frm") %><br></br>
	keyboardType_pwd1:<%= request.getParameter("keyboardType_pwd1_frm") %><br></br>
	keyIndex_pwd1:<%= request.getParameter("keyIndex_pwd1_frm") %><br></br>
	fieldType_pwd1:<%= request.getParameter("fieldType_pwd1_frm") %><br></br>
	<hr></hr>
	pwd2:<%= TransKey.withoutSessionDecode("pwd2", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd2EncData:<%= request.getParameter("transkey_pwd2_frm") %><br></br>
	pwd2HmacData:<%= request.getParameter("transkey_HM_pwd2_frm") %><br></br>
	keyboardType_pwd2:<%= request.getParameter("keyboardType_pwd2_frm") %><br></br>
	keyIndex_pwd2:<%= request.getParameter("keyIndex_pwd2_frm") %><br></br>
	fieldType_pwd2:<%= request.getParameter("fieldType_pwd2_frm") %><br></br>
	<hr></hr>
	
	use_form_id = false
	<hr></hr>
	initTime: <%= request.getParameter("initTime") %><br></br>
	seedKey: <%= request.getParameter("seedKey") %><br></br>
	<hr></hr>
	pwd1(use params):<%= TransKey.withoutSessionDecode("pwd1", request.getParameter("initTime"), request.getParameter("keyboardType_pwd1"), request.getParameter("keyIndex_pwd1"), request.getParameter("fieldType_pwd1"), request.getParameter("seedKey"), request.getParameter("transkey_pwd1"), request.getParameter("transkey_HM_pwd1"), request.getParameter("transkey_ExE2E_pwd1")).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1(use request):<%= TransKey.withoutSessionDecode("pwd1", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1EncData:<%= request.getParameter("transkey_pwd1") %><br></br>
	pwd1HmacData:<%= request.getParameter("transkey_HM_pwd1") %><br></br>
	keyboardType_pwd1:<%= request.getParameter("keyboardType_pwd1") %><br></br>
	keyIndex_pwd1:<%= request.getParameter("keyIndex_pwd1") %><br></br>
	fieldType_pwd1:<%= request.getParameter("fieldType_pwd1") %><br></br>
	<hr></hr>
	pwd2:<%= TransKey.withoutSessionDecode("pwd2", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd2EncData:<%= request.getParameter("transkey_pwd2") %><br></br>
	pwd2HmacData:<%= request.getParameter("transkey_HM_pwd2") %><br></br>
	keyboardType_pwd2:<%= request.getParameter("keyboardType_pwd2") %><br></br>
	keyIndex_pwd2:<%= request.getParameter("keyIndex_pwd2") %><br></br>
	fieldType_pwd2:<%= request.getParameter("fieldType_pwd2") %><br></br>
	<hr></hr>
</body>
</html>