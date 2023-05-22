<%@page import="java.util.Enumeration"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");

	String name=request.getParameterValues("name")[0];
	String studentID=request.getParameterValues("studentID")[0];
	String gender=request.getParameterValues("gender")[0];
	String [] sub=request.getParameterValues("subject");
	String city=request.getParameterValues("city")[0];
	String [] hb=request.getParameterValues("hobby");

	String subject="";
	String hobby="";
	
	if(sub!=null){
		for(String s : sub){
			subject+=s+" ";
		}
	}

	if(hb!=null){
		for(String s : hb){
			hobby+=s+" ";
		}
	}
	
	

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>출력내용</h3>
<p> 이름 : <%= name %></p>
<p> 학번 : <%= studentID %></p>
<p> 성별 : <%= gender %></p>
<p> 좋아하는 과목 : <%= subject %></p>
<p> 출신도 : <%= city %></p>
<p> 취미 : <%= hobby %></p>

</body>
</html>