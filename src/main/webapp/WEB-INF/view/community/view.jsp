<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${community.title}</title>
<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"></script>
<script type="text/javascript">
	$().ready(function(){
		
		$("#writeReplyBtn").click(function(){
			
			$.post("<c:url value="/api/reply/${community.id}"/>",
					$("#writeReplyFrom").serialize(),
					function(response){
						alert("등록됨");
						console.log(response);
			});
			
		});
		
	});
</script>
</head>
<body>

	<div id="wrapper"></div>
	 	<jsp:include page="/WEB-INF/view/template/menu.jsp" />
	 	
		<h1>${community.title}</h1>
		
		<c:choose>
			<c:when test="${not empty community.memberVO}">
				<h3>${community.memberVO.nickname}(${community.memberVO.email}) ${community.requestIP}</h3>
			</c:when>
		
			<c:otherwise>
			탈퇴한 회원 ${community.requestIP}
			</c:otherwise>				 
		</c:choose>
		
		
		
		
		<p>${community.displayFilename}</p>
		<P>${community.body}</P>
		<hr />

		<div id="replies"></div>
		<div id="createReply">
			<form id="writeReplyFrom">
				<input type="hidden" id="parentReplyId" name="parentReplyId" value="0"/>
				<div>
				<textarea id="body" name="body" ></textarea>
				</div> 
				
				<div>
					<input type="button" id="writeReplyBtn" value="등록"/>
				</div>
			</form>
		
		</div>
		
		<p>${community.writeDate}</p>
		<p>조회수 : ${community.viewCount}</p>
		<c:if test="${not empty community.displayFilename }">
			<p>
				<a href="<c:url value="/get/${community.id}"/>">
					${community.displayFilename}
				</a>
			</p>
		</c:if>
		
		<a href="<c:url value="/recommend/${community.id}"/>">추천하기</a>( ${community.recommendCount} )
		<c:if test="${sessionScope.__USER__.id == community.memberVO.id}">
			<p>
				<a href="<c:url value="/remove/${community.id}"/>">삭제하기</a>
				<a href="<c:url value="/modify/${community.id}"/>">수정하기</a>
				
			</p>
		</c:if>
		<p><a href="<c:url value="/"/>">목록으로</a></p>
		
		<a href="<c:url value="/logout"/>">로그아웃</a>
	</div>
</body>
</html>