<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	Calendar cal=Calendar.getInstance();
	String s=String.format("%tF %tA %tT", cal, cal, cal);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- HTML 주석 -->
	<%-- JSP 주석 --%>
	
	<h3> 스프링 홈페이지 </h3>

	<p> 반가워요 </p>
	<p> 접속시간은 <%=s %> 입니다. </p>


</body>
</html>