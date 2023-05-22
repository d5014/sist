<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");
	
	String op=request.getParameter("operator");
	int num1=Integer.parseInt(request.getParameter("num1"));
	int num2=Integer.parseInt(request.getParameter("num2"));
	int result=0;
	
	switch(op){
	case "+":
		result= num1+num2;break;
	case "-":
		result= num1-num2;break;
	case "*":
		result= num1*num2;break;
	case "/":
		result= num1/num2;break;
	}
	
	
/*	if(op.equals("+")){
		String.format("%d+%d=%d",num1,num2,num1+num2);
	} else if(op.equals("-")){
		String.format("%d-%d=%d",num1,num2,num1-num2);
	} else if(op.equals("*")){
		String.format("%d*%d=%d",num1,num2,num1*num2);
	} else if(op.equals("/")){
		String.format("%d/%d=%d",num1,num2,num1/num2);
	}
*/	
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>결과 출력</h3>
<p>연산 결과 : <%= result %></p>

</body>
</html>