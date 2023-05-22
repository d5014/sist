<%@tag import="java.util.Calendar"%>
<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ tag trimDirectiveWhitespaces="true" %><!-- 불필요한 공백 제거 -->
<!-- JSP에서 코딩하는 것과 유사 -->
<%
	Calendar cal=Calendar.getInstance();
	String s=String .format("%tF %tT",cal,cal);
%>
<%= s %>
