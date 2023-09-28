<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String resultCode = session.getAttribute("resultCode") == null ? "unknown error" : session.getAttribute("resultCode").toString();
    String resultMessage = session.getAttribute("resultMessage") == null ? "unknown message" : session.getAttribute("resultMessage").toString();
    String errorMessage = session.getAttribute("errorMessage") == null ? "unknown message" : session.getAttribute("errorMessage").toString();
    if (session.getAttribute("resultCode") != null) {
        session.removeAttribute("resultCode");
    }

    if (session.getAttribute("resultMessage") != null) {
        session.removeAttribute("resultMessage");
    }

    if (session.getAttribute("isErrorPage") != null ) {
        session.removeAttribute("isErrorPage");
    }

%>
<!DOCTYPE html>
<html>
	<head>
	    <title>error</title>
	</head>
    <body>
        <script>
            var resultCode = "<%=resultCode%>";
            var resultMessage = "<%=resultMessage%>";
            var errorMessage = "<%=errorMessage%>";

            if(resultCode != "310017" && resultCode != "310012") {
                alert("resultCode : " + resultCode + "\nresultMessage : " + resultMessage + "\nerrorMessage : " + errorMessage);
                location.replace('./logout.jsp');
            }
        </script>
        <h1>resultCode : <%=resultCode%></h1>
        <h1>resultMessage : <%=resultMessage%></h1>
        <h1>errorMessage : <%=errorMessage %></h1>
    </body>
</html>
