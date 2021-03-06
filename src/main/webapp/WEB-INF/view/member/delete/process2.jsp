<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/common.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/input.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/button.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/delete.css"/>"/>
 

<script type="text/javascript" src="/static/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	
	$().ready(function(){
		
		$("#deleteBtn").click(function(){
			location.href = "<c:url value="/account/delete/y?token=${token}"/>"	//난수를 붙여줌
		});
		
		$("#ndeleteBtn").click(function(){
			location.href = "<c:url value="/account/delete/n?token=${token}"/>"			
		});
				
		$("#cancelBtn").click(function(){
			location.href = "<c:url value="/"/>"
		});
		
		$("#myCommunities").click(function(){
			window.open("<c:url value="/mypage/communities"/>", 
					"나의 추억들",
					"width:500, heigh500, scrollbar=yes, resizalbe=no");//mdn내문서 참조
		});
		
	});

</script>

</head>
<body>

	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp"/>
		
		<div id="progress">
			<ul>
				<li class="active">본인확인</li>
				<li class="active">게시글 삭제</li>
				<li>탈퇴 완료</li>
			</ul>
		</div>
		
		<div class="box" >
			<p style="text-align: center;">
			<span id="myCommunities">
				${sessionScope.__USER__nickname}님의 추억이 
				${myCommunitiesCount}개 있습니다. 
			</span>
			</p>
			<p style="text-align: center;"> 
				모든 추억을 삭제하시겠습니까?
			</p>
			<div style="text-align: center;">
				<div id="deleteBtn" class="button">삭제합니다.</div>
				<div id="ndeleteBtn" class="button">삭제하지 않습니다.</div>
				<div id="cancelBtn" class="button">홈으로</div>
			</div>
		</div>
	</div>


</body>
</html>