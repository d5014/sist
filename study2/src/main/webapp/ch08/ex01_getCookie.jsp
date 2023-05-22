<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
<h3>쿠키 가져오기</h3>
<!-- 클라이언트 컴퓨터에 저장된 쿠키를 서버로 가져오기 -->

<%
	Cookie[] cc=request.getCookies();//한개씩 가져올 수 없음(배열로 전체 쿠키를 가져옴)배

	if(cc!=null){
	for(Cookie c : cc){
		String name=c.getName();//쿠키이름
		String value=c.getValue();//쿠키값
		if(name.equals("subject3")){
			value=URLDecoder.decode(value,"utf-8");//인코딩한 쿠키값을 디코딩(한글)
		}
		out.print("<p>"+name+" : "+value+"</p>");
		}
	}
%>

<p><a href="ex01.jsp"> 돌아가기 </a></p>

</body>
</html>