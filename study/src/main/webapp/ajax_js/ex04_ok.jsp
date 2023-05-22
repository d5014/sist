<%@ page contentType="text/xml; charset=UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	request.setCharacterEncoding("utf-8");

	String name= request.getParameter("name");
	String content= request.getParameter("content");
	
%>

<guest><!-- xml 루트 태그가 반드시 한개가 있어야 하며 대소문자 구분 해야됨 -->
	<dataCount>5</dataCount>
	<% for(int n=1;n<=5;n++) { %>
		<record num="<%= n %>">
			<name><%= (char)(n+64)+"-"+name %></name>
			<content><![CDATA[<%= (char)(n+64)+"-"+content%>]]></content>
			<!-- CDATA : 모두 문자열로 인식함 -->
		</record>
	<% } %>
</guest>

