<%@page import="java.net.URLDecoder"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Enumeration"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>클라이언트로 부터 요청(request) 받은 정보</h3>

<%
	out.print("<p>method : "+request.getMethod()+"</p>");
	out.print("<p>Query String(GET 방식 파라미터) : "+request.getQueryString()+"</p>");
	out.print("<hr>");
	
	out.print("<p>[request로 넘어온 데이터(header 영역)]...</p>");
	Enumeration<String> e=request.getHeaderNames();
	while(e.hasMoreElements()){
		String name=e.nextElement();
		String value=request.getHeader(name);
		out.print("<p>"+name+":"+value+"</p>");
	}
	out.print("<hr>");
	
	out.print("<p>[request로 넘어온 데이터(body 영역 : post 파라미터)]...</p>");
	InputStream is=request.getInputStream();
	byte[] b=new byte[4096];
	int size;
	String s;
	
	while((size=is.read(b))!= -1){
		s=new String(b,0,size);
		out.print("<p>디코딩 하지 않은 경우 : "+s+"</p>");

		s=URLDecoder.decode(s,"utf-8");
		out.print("<p>디코딩한 경우 : "+s+"</p>");
	}
	out.print("<hr>");
	
%>
<!-- 
get 방식은 QueryString로 받아오지만 post 방식은 InputStream으로 받아옴
 -->
</body>
</html>