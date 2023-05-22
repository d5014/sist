﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
.body-main {
	max-width: 700px;
	padding-top: 15px;
}

.guest-form { clear: both; border: #d5d5d5 solid 1px; padding: 15px; min-height: 50px; }
.guest-form > .form-header { padding-bottom: 7px; }
.guest-form > .form-header > .bold { font-weight: bold; }
.guest-form > .form-body { padding: 10px 0 3px; }
.guest-form > .form-footer { padding-top: 3px; text-align: right; }
.guest-form textarea { width: 100%; height: 75px; }
.guest-form button { padding: 8px 25px; }

.list-header { padding-top: 25px; padding-bottom: 7px; }
.list-header .guest-count { color: #3EA9CD; font-weight: 700; }

.guest-list { table-layout: fixed; word-break: break-all; }
.guest-list tr:nth-child(2n+1) { border: 1px solid #ccc; background: #f8f8f8; }
.guest-list td { padding-left: 7px; padding-right: 7px; }
.deleteGuest, .notifyGuest {cursor: pointer;}
.deleteGuest:hover, .notifyGuest:hover {color: #f28011;}
.more-box {text-align: right; padding-top: 5px; padding-bottom: 5px;}
.more-box .more {cursor: pointer;}
.more-box .more:hover {color: #252477; font-weight: 500; }

</style>
<script type="text/javascript">

function login(){
	location.href="${pageContext.request.contextPath}/member/login.do";
}

function ajaxFun(url, method, query, dataType, fn){//순서 3
	$.ajax({
		type : method,				// 메소드 : (get, post, push, delete ...)
		url : url,					// 요청 받을 서버 주소 
		data : query,				// 서버에 전송할 파라미터
		dataType : dataType,		// 서버에서 응답하는 형식(json, xml, text)
		success : function(data){	//json 데이터
			fn(data);//listPage에 있는 fn 함수 호출 -> printGuest로 넘어감
		},
		beforeSend:function(jqXHR){	// 서버 전송 전 
			jqXHR.setRequestHeader("AJAX",true);// 사용자 정의 헤더
		},
		error:function(jqXHR){
			if(jqXHR.status === 403){
				login();
				return false;
			} else if(jqXHR.status ===400){
				alert("요청 처리가 실패 했습니다.");
				return false;
			}
			console.log(jqXHR.responseText);
		}
	});
}

$(function(){//순서1 - 프로그램 실행과 동시에 listPage를 실행시킴
	listPage(1);
});

function listPage(page){//순서 2
	const url="${pageContext.request.contextPath}/guest/list.do";
	let query="pageNo="+page;
	
	const fn=function(data){//함수 생성
		printGuest(data);
	};
	
	ajaxFun(url,"get",query, "json",fn);
	
}

function printGuest(data){//순서 4
	let dataCount=data.dataCount;
	let pageNo=data.pageNo;
	let total_page=data.total_page;
	
	$(".guest-count").attr("data-pageNo",pageNo);
	$(".guest-count").attr("data-totalPage",total_page);
	
	$(".guest-count").html("방명록"+dataCount+"개");
	
	$(".more-box").hide();
	if(parseInt(dataCount)===0){//데이터가 없으면
		$(".guest-list-body").empty();//list를 비움 (웹은 정적이기때문에 지우지않으면 내용이 남아있을 수 있음.)
		return;
	}
	
	if(pageNo < total_page){
		$(".more-box").show();//전체페이지보다 적을때만 더보기 버튼이 보임
	}
	
	let out="";
	for(let item of data.list){
		let num=item.num;
		let userId=item.userId;
		let userName=item.userName;
		let content=item.content;
		let reg_date=item.reg_date;
		
		out+="<tr>";
		out+="	<td width='50%'>";
		out+="		<span style='font-weight:600'>"+userName+"</span>";
		out+="	</td>";
		out+="	<td width='50%' align='right'>";
		out+="	" + reg_date;
		if(item.deletePermission){
			out+=" | <span class='deleteGuest' data-num='"+num+"' data-page='"+pageNo+"'>삭제</span>";
		} else {
			out+=" | <span class='notifyGuest'>신고</span>";
		}
		out+="	</td>";
		out+="</tr>";
		out+="<tr>";
		out+="	<td colspan='2' valign='top'>"+content+"</td>";
		out+="</tr>";
	}
	
	$(".guest-list-body").append(out);//append : 기존에 내용을 추가
	
	
}

$(function(){
	$(".btnSend").click(function(){
		if( ! $("#content").val().trim()){
			$("#content").focus();
			return false;
		}
		
		let url="${pageContext.request.contextPath}/guest/insert.do";
		//let query="content="+encodeURIComponent($("#content").val());
		let query=$("form[name=guestForm]").serialize();//서버에 보낼 내용을 직렬화하여 query에 담음
		
		//서버가 응답한 경우 호출할 함수
		const fn=function(data){
			//console.log(data);
			$("#content").val("");
			$(".guest-list-body").empty();
			listPage(1);
		};
		
		//ajax 로 서버에 게시글 전송
		ajaxFun(url,"post", query, "json", fn);
		
	});
});

$(function(){
	$(".guest-list-body").on("click",".deleteGuest",function(){
		if(! confirm('게시글을 삭제 하시겠습니까 ? ')){
			return false;
		}
		
		let query="num="+$(this).attr("data-num");
		let url="${pageContext.request.contextPath}/guest/delete.do";
		
		const fn=function(data){
			$(".guest-list-body").empty();
			listPage(1);
		};
		
		ajaxFun(url,"post",query,"json",fn);
	});
	
});

$(function(){
	$("#listGuest .more").click(function(){
		let pageNo=$(".guest-count").attr("data-pageNo");
		let total_page=$(".guest-count").attr("data-totalPage");
		
		if(pageNo < total_page){
			pageNo++;
			listPage(pageNo);
		}
	});
});


</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	    <div class="body-title">
			<h2><i class="far fa-edit"></i> 방명록 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<form name="guestForm" method="post"><!-- ajax할때는 form 태그의 method는 적용되지않음(신경쓰지않아도 됨) -->
				<div class="guest-form">
					<div class="form-header">
						<span class="bold">방명록쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
					</div>
					<div class="form-body">
						<textarea name="content" id="content" class="form-control"></textarea>
					</div>
					<div class="form-footer">
						<button type="button" class="btn btnSend" > 등록하기 </button>
					</div>
				</div>
			</form>
			
			<div id="listGuest">
				<div class="list-header">
					<span class="guest-count">방명록 0개</span>
					<span class="guest-title">[목록]</span>
				</div>
				
				<table class="table guest-list">
					<tbody class="guest-list-body"></tbody>
				</table>
				
				<div class="more-box">
				<span class="more">더보기</span>
				</div>
			</div>
	    </div>
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>