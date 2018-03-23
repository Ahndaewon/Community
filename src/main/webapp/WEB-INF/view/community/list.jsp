<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
		
		<div>
			${pageExplorer.totalCount}건의 게시글이 검색 되었습니다.
		</div>
		
		<table>
			<tr>
				<th>ID</th> <!-- th는 글자가 센터로 -->
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
		<c:forEach items="${pageExplorer.list}" var="community">
			<tr>
				<td>${community.id}</td> <!-- td는 글자가 모두 left -->
				
				
				 <td>
				 <a href="<c:url value="/read/${community.id}"/>">${community.title}</a>
				 <c:if test="${not empty community.displayFilename }">
					<img src="<c:url value="/static/img/file.png"/>"
					alt="${community.displayFilename}" 
					style="width:20px;"/>
				</c:if>
				 </td>
				
				<!-- nickname(email) -->
				<td>
					<c:choose>
						<c:when test="${not empty community.memberVO}">
						${community.memberVO.nickname}(${community.memberVO.email})
						</c:when>
						
						<c:otherwise>
							탈퇴한 회원
						</c:otherwise>				 
					</c:choose>
					(${community.userId})</td>
				
				<td>${community.writeDate}</td>
				<td>&nbsp;&nbsp;${community.viewCount}</td>
			</tr>
		
		
		</c:forEach>
		<c:if test="${empty pageExplorer.list}"><!-- 비어있으면  안비어 있으면 not empty-->
			<tr>
				<td colspan="5">등록된 게시글이 없습니다.</td>
			</tr>
		</c:if>
		</table>
		
		<form id="searchForm" >
			${pageExplorer.make()}
		</form>
		
		<div>
			<a href="<c:url value="/write"/>"/>글쓰기</a>
		</div>
		
	</div>
	
		

</body>
</html>