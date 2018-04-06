<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html>
<head>
	<title>Home</title>
</head>
<body>

<!-- 모두 form 형식으로 각각의 action에 해당하는 컨트롤러에 요청과 입력값들을 보낸다. -->
<h3>Input</h3>
<form action="insert" method="post">
	뉴스 제목 : <input type="text" name="title"/><br>
	언론사 : <input type="text" name="company"/><br>
	주소 : <input type="text" name="site"/><br><br>
	<input type="submit" value="submit"/>
</form>
<br>

<h3>Select</h3>
<form action="find" method="post">
	<select name="where">
	<option value="title">제목</option>
	<option value="company">언론사</option>
	<option value="site">주소</option>
	</select>
	<input type="text" name="is"/>
	<input type="submit" value="submit"/>
</form>
<br><br>

<form action="findAll" method="post">
<h3>전체목록 </h3>
<input type="submit" value="submit"/>
</form>

<form action="crawling" method="post">
<h3>Crawling</h3>
<input type="submit" value="이동"/>
</form>

<form action="wordcloud" method="post">
<h3>Word cloud</h3>
<input type="submit" value="이동"/>
</form>

</body>
</html>
