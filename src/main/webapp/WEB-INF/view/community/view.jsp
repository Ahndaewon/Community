<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${community.title}</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/alert.css"/>">

<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"></script>

<script type="text/javascript" src="<c:url value="/static/js/alert.js"/>"></script>
<script type="text/javascript">
	$().ready(function(){
		
		loadReplies(0);
		function loadReplies(scrolltop){
			
			$.get("<c:url value="/api/reply/${community.id}"/>",{}, 
					function(response){
							for (var i in response) {
								appendReplies(response[i]);
							}
							
							$(window).scrollTop(scrolltop);
				
			});
			
		}
		
		
		
		
		$("#writeReplyBtn").click(function(){
			
			$.post("<c:url value="/api/reply/${community.id}"/>",
					$("#writeReplyFrom").serialize(),
					function(response){
						if ( response.status ){
							show("댓글 등록됨");
							
							$("#parentReplyId").val(0);
							$("#body").val("");
							$("#createReply").appendTo("#createReplyDiv");
							
							
							var scrolltop = $(window).scrollTop();
							
							/* appendReplies(response.reply); */
						$("#replies").html("");
						loadReplies(scrolltop);
							
						}
						else {
							alert("등록에 실패했습니다. 잠시 후에 다시 시도하세요");
						}
			});
			
		});
		
		
		$("#replies").on("click", ".re-reply", function(){
			var parentReplyId = $(this).closest(".reply").data("id");
			$("#parentReplyId").val(parentReplyId);
			$("#createReply").appendTo($(this).closest(".reply"));
		});
		
		function appendReplies(reply) {
			var replyDiv = $("<div class='reply' style='padding-left:" + ((reply.level-1)*20) +"px; ' data-id='" + reply.id + "'></div>");
			var nickname =  reply.memberVO.nickname + "( "+ reply.memberVO.email+")"; 
			var top = $("<span class='writer'>" + nickname + "</span><span class='regist-date'>" + reply.registDate + "</span>"); 
			replyDiv.append( top ); 
			
			var body = $("<div class='body'>" + reply.body+ "</div>");
			
			replyDiv.append( body );
			
			var registReReply = $("<div class='re-reply'>댓글달기</div>");
			
			replyDiv.append( registReReply );
			
			$("#replies").append(replyDiv);
			
			/* if ( reply.parentReplyId == 0 ) {
				$("#replies").append(replyDiv);
				
			} */
			
			/* else {
				$(".reply").each(function(index, data){
					var replyId =  $(data).data("id");
					if ( reply.parentReplyId == replyId ) {
						$(data).after(replyDiv);
					}
				});
			} */
			
		}
		
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
		<div id="createReplyDiv">
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
		<p><a href="<c:url value="/reset"/>">목록으로</a></p>
		
		<a href="<c:url value="/logout"/>">로그아웃</a>
	</div>
</body>
</html>