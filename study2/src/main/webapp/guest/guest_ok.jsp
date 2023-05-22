<%@page import="java.sql.SQLException"%>
<%@page import="com.guest.GuestDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
	request.setCharacterEncoding("utf-8");
%>
	<jsp:useBean id="dto" class="com.guest.GuestDTO"/>
	<jsp:setProperty property="*" name="dto"/>
<%
	GuestDAO dao=new GuestDAO();

	try {
		dao.insertGuest(dto);
		response.sendRedirect("guest.jsp");
	} catch(SQLException e) {
		e.printStackTrace();
	}
	
%>

