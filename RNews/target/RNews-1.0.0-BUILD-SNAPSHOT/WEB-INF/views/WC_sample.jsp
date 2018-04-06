<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="resources/wordcloud2.js"></script>

<style type="text/css">    
#canvas_cloud{
	width: 800px;
	height:800px;}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<div id="sourrounding_div" style="width:800px;height:800px">
		<canvas id="canvas_cloud"></canvas>
	</div>

<script type="text/javascript">
	var div = document.getElementById("sourrounding_div");
	var canvas = document.getElementById("canvas_cloud");

	canvas.height = div.offsetHeight;
	canvas.width  = div.offsetWidth;
	
	var arr = [];
	<c:forEach items="${wordcloud}" var="item">
		arr.push(["${item.word}", "${item.size}"]);
	</c:forEach>
	

	var options = 
	{
 		list : arr,
 		gridSize: 18,
  		weightFactor: 3,
 		shape : "circle",
 		click: function(item) {
 			    alert("word : "+ item[0] +" size : " + item[1]);
 		}
	}


	WordCloud(document.getElementById('canvas_cloud'), options); 

</script>
</body>
</html>