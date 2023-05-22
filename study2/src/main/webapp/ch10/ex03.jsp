<%@page import="ch10.user.User"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");

	User user= new User("이자바",20,null);
	//user 객체 vo을 request에 저장
	request.setAttribute("vo", user);
	
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<!-- 파라미터 city=서울 을 추가하여 ex03_ok로 forward -->
<jsp:forward page="ex03_ok.jsp">
	<jsp:param value="서울" name="city"/>
</jsp:forward>

</body>
</html>