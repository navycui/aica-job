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
	pwd1(use params):<%= TransKey.decode("pwd1", request.getParameter("transkey_pwd1_frm"),request.getParameter("transkey_HM_pwd1_frm"),session, request.getParameter("transkeyUuid_frm")).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1(use request):<%= TransKey.decode("pwd1", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1EncData:<%= request.getParameter("transkey_pwd1_frm") %><br></br>
	pwd1HmacData:<%= request.getParameter("transkey_HM_pwd1_frm") %><br></br>
	<hr></hr>
	pwd2:<%= TransKey.decode("pwd2", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd2EncData:<%= request.getParameter("transkey_pwd2_frm") %><br></br>
	pwd2HmacData:<%= request.getParameter("transkey_HM_pwd2_frm") %><br></br>
	<hr></hr>
	
	use_form_id = false
	<hr></hr>
	pwd1(use params):<%= TransKey.decode("pwd1", request.getParameter("transkey_pwd1"),request.getParameter("transkey_HM_pwd1"),session, request.getParameter("transkeyUuid")).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1(use request):<%= TransKey.decode("pwd1", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd1EncData:<%= request.getParameter("transkey_pwd1") %><br></br>
	pwd1HmacData:<%= request.getParameter("transkey_HM_pwd1") %><br></br>
	<hr></hr>
	pwd2:<%= TransKey.decode("pwd2", request).replaceAll("<", "&lt;").replaceAll(">", "&gt;")%><br></br>
	pwd2EncData:<%= request.getParameter("transkey_pwd2") %><br></br>
	pwd2HmacData:<%= request.getParameter("transkey_HM_pwd2") %><br></br>
	<hr></hr>
</body>
</html>