<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
	request.setCharacterEncoding("utf-8");

	String []seats=request.getParameterValues("chks");
	String ss="";
	if(seats!=null){
		for(String s : seats){
			ss+=s+" ";
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

<h3>좌석 예약</h3>
<p>예약된 좌석 번호 : <%= ss %></p>
</body>
</html>