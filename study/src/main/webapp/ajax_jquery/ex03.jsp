<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>


</head>
<body>

<h2>jQuery AJAX : POST - $.post()</h2>

<form name="calcForm">
	<input type="text" name="num1" id="num1">
	<select name="oper" id="oper">
		<option value="+">더하기</option>
		<option value="-">빼기</option>
		<option value="*">곱하기</option>
		<option value="/">나누기</option>
	</select>
	<input type="text" name="num2" id="num2">
	<button type="button" class="btnSend">결과</button>
</form>
<hr>
<div class="result-box"></div>

<script type="text/javascript">

$(function(){
	$(".btnSend").click(function(){
		let n1=$("#num1").val();
		let n2=$("#num2").val();
		let op=$("#oper").val();
		
		let url="ex03_ok.jsp";
		//AJAX - POST
		$.post(url,{num1:n1, num2:n2, oper:op},function(data){
			$(".result-box").html(data);//실행순서 2
		});
		
		$(".result-box").html("결과");//실행순서 1
		
	});
});

</script>

</body>
</html>