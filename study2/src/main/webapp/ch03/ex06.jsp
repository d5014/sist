<%@ page contentType="text/html; charset=UTF-8"%>

<%
	String s;
	char ch='A'; 
	int row=10;
	int col=10;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<style type="text/css">
* {margin: 0; padding: 0; box-sizing: border-box;}
.layout {margin: 30px auto 10px; width: <%= col*30 %>px;}

.table {width:100%; border-spacing: 0; border-collapse: collapse;}
.table td { text-align: center; padding: 3px; }
</style>

<script type="text/javascript">
function sendOk(){
	const chks=document.querySelectorAll('input[type=checkbox]:checked');
	
	if(chks.length<1 || chks.length > 4){
		alert('좌석은 1~4개 선택 가능합니다.');
		return;
	}
	
	let s="";
	for(let c of chks){
		s+=c.value+' ';
	}
	
	alert(s);
	
	
	
	let arr=[];
	
	for(let c of chks){
		arr.push(c.value);
	}
	
	alert(arr.join());
	
/*
	const f=document.frm;
	
	let cnt=0;
	let arr=[];
	
	for(let c of f.chk){
		if(c.checked){
			cnt++;
			arr.push(c.value);
		}
	}
	
	if(cnt === 0){
		alert('좌석을 선택하세요.');
		return;
	}

	if(cnt > 4){
		alert('좌석을 최대 4개만 선택하세요.');
		return;
	}
	
	alert(arr.join());
*/
}
</script>

</head>
<body>

<div class="layout">
	<form name="frm">
		<table class="table">
			<%
				for(int i=1;i<=row;i++) {
					out.print("<tr>");
					for(int j=1;j<=col;j++){
						s=ch+"-"+j;
						out.println("<td>");
						out.println("<input type='checkbox' name='chk' value='"+s+"'>");
						out.println("</td>");
					}
					
					out.print("</tr>");
					ch++;
				}
			%>
		</table>
		<table class="table">
			<tr>
				<td style="text-align: right;">
					<button type="button" onclick="sendOk();">확인</button>
				</td>
			</tr>
		</table>
	</form>
</div>


</body>
</html>