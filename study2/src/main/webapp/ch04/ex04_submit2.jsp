<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<h3>submit 버튼</h3>


<form name="frm" action="ex04_ok.jsp" method="post">
	<p> 이름 : <input type="text" name="name"> </p>
	<p> 나이 : <input type="text" name="age"> </p>
	<p>
		<button type="submit">확인</button> |
		<button type="reset">다시입력</button>
	</p>
</form>

<script type="text/javascript">
function check(){
	const f=document.frm;
	let n=f.name;
	let a=f.age;
	
	if(! n.value){
		n.focus();
		return false;
	}
	if(! /^\d+$/.test(a.value)){
		a.focus();
		return false;
	}
	
	return true;
}

document.frm.addEventListener('submit', e=>{
	if(! check()){
		e.preventDefault();//이벤트 취소
	}
});

</script>

</body>
</html>