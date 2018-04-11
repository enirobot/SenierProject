<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.mongo.board.*" %>
<%@ page session="false" %>
<html>
<head>
	<title>list</title>
	<!--
		findController에서 다시 돌아오는 부분.
		select 기능을 하는  Find()나 모든 document를 찾는 FindAll()둘 다 이 페이지로  list를 가지고 온다. 
	-->
</head>
<body>
<%
	List<News> list = (List<News>)request.getAttribute("list");
	//컨트롤러에서 보낸 request에서 list를 받는다.

	out.println("개수 : "+list.size()+"<br>");
	for(int i=0;i<list.size();i++){
		out.print(list.get(i).toString());%>
		<!-- 
			각 document마다  삭제를 할 수 있도록 버튼을 만듦. 
			list.get(i).getId()를 통해 id를 찾아 removeController로 보냄
		-->
		<form action="remove" method="post">
			<input type="hidden" name="id" value=<%=list.get(i).getId() %>>
			<input type="submit" value="삭제">
		</form>
<% 		
	}
%>
</body>
</html>
