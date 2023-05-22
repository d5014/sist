<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	request.setCharacterEncoding("utf-8");

	String name= request.getParameter("name");
	String content= request.getParameter("content");
	
	//JSON 형식-1 : {"이름1":"값1", "이름2":"값2", "이름3":"값3"}
	//JSON 형식-2 : {"이름":[{"이름1":"값1", "이름2":"값2"}, {"이름1":"값1", "이름2":"값2"}]}
	
	/*StringBuilder sb=new StringBuilder();
	sb.append("{");
	sb.append("\"name\" : \""+name+"\"");
	sb.append(", \"content\" : \""+content+"\"");
	sb.append("}");
	out.print(sb.toString());
	*/
	
	JSONObject job=new JSONObject();//{} 괄호를 달아줌
	job.put("count",5);
	
	JSONArray jarr=new JSONArray();//json 객체를 배열로 담음
	for(int i=1;i<=5;i++){
		JSONObject ob=new JSONObject();
		ob.put("num",i);
		ob.put("name",(char)(i+64)+"-"+name);
		ob.put("content",(char)(i+64)+"-"+content);
		
		jarr.put(ob);
	}
	
	job.put("list",jarr);
	
	out.print(job.toString());
%>
