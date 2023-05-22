<%@page import="java.sql.SQLException"%>
<%@page import="com.score.ScoreDAOImpl"%>
<%@page import="com.score.ScoreDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");
%>

<jsp:useBean id="dto" class="com.score.ScoreDTO"/>
<jsp:setProperty property="*" name="dto"/>

<%
	ScoreDAO dao=new ScoreDAOImpl();
	String pageNum=request.getParameter("page");

	String s="";

	try {
		dao.updateScore(dto);
		response.sendRedirect("list.jsp?page="+pageNum);
		return;
	} catch(SQLException e){
			s="자료 수정이 실패 되었습니다.";
			
		request.setAttribute("msg", s);
	}
%>

<jsp:forward page="update.jsp?hak=<%=dto.getHak()%>&page=<%=pageNum %>"/>