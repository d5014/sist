<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");
%>
<%--JSP 액션 태그 --%>

<%--
	Calculator calc=new Calculator();와 유사
 --%>
<jsp:useBean id="calc" class="ch05.calc.Calculator"/>
<%--
	calc.setNum1(Integer.parseInt(request.getParameter("num1")));
	calc.setNum2(Integer.parseInt(request.getParameter("num2")));
	calc.setOperator(request.getParameter("operator"));
	와 유사한 역할
 --%>
<jsp:setProperty name="calc" property="*"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>액션태그(jsp:userBean)을 이용한 연산</h3>
<%= calc.toString() %>


</body>
</html>