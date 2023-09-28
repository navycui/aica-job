<%@page import="bnet.library.util.CoreUtils"%>
<%@page import="java.util.Base64"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
%><%
	String url1 = "aHR0cDovL2Rldi1wb3J0YWwuYXRvcHMub3Iua3IvU3VwcG9ydEZvclVzZS9PbmVCeU9uZUlucXVpcnk=";
	// 로그인 페이지의 NextURl: aHR0cDovL2Rldi1wb3J0YWwuYXRvcHMub3Iua3IvU3VwcG9ydEZvclVzZS9PbmVCeU9uZUlucXVpcnk=
	// changePasswd
	// initPasswd
	String nextUrl = request.getParameter("nextUrl");
	if (CoreUtils.string.isNotBlank(nextUrl)) {
		session.setAttribute("nextUrl", nextUrl);
	}
	String url = decode(nextUrl);
	System.out.println("#####" + url);
%><%!
public String decode(String encodeUrl) {
	byte[] buf = Base64.getDecoder().decode(encodeUrl);
	String url = new String(buf);
	return url;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>sample</title>
</head>

<body>
	<textarea id="url11" rows="1" cols="90"><%=nextUrl %></textarea><br>
	<textarea id="url22" rows="1" cols="90"><%=url %></textarea><br><br>

	<textarea id="url1" rows="1" cols="90">http://dev-portal.atops.or.kr/Notice/Notice</textarea><br>
	<textarea id="url2" rows="1" cols="90"></textarea><br>
	<textarea id="url3" rows="1" cols="90"></textarea><br>
    <script>
        var url1 = document.getElementById("url1").value;
        console.log(url1);
        var url2 = btoa(url1);
        console.log(url2);
        var url3 = atob(url2);
        console.log(url3);

        document.getElementById("url2").value = url2;
        document.getElementById("url3").value = url3;
    </script>

</body>
</html>
