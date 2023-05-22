<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>표현식, 스크립릿</h3>
<hr>

<h3>구구단</h3>

<% for(int dan=2; dan<=9; dan++) { %>
	<p>**<%= dan %>**</p>
	<%for(int n=1;n<=9;n++) { %>
	<p><%= dan %> * <%= n %> = <%= dan * n %></p>
	<% } %>
	<p>--------------------------</p>
<% } %>
<hr>

<%
	for(int dan=2;dan<=9;dan++){
		out.println("<p>**"+dan+"**</p>");
		for(int n=1;n<=9;n++){
			out.println("<p>"+dan+"*"+n+"="+(dan*n)+"</p>");
		}
		out.println("<p>-------------------------------------</p>");
	}
%>

<table>

<% for(int i=1;i<=9;i++) { %>
	<tr height="30" align="left">
		<% for(int j=2;j<=9;j++) { %>
		<td style="width: 100px;"> <%= j+"*"+i+"="+i*j %> </td>
		<% } %>
	</tr>
<% } %>
</table>


</body>
</html>