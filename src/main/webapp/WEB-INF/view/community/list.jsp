<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"></script>
<script type="text/javascript">
	$().ready(function(){
		
		$("#searchKeyword").keyup(function(event){
			if ( event.key == "enter" ) {
				/*엔터누르면 submit 되버림 input 하나일때  */
				movePage('0');
				/* form 입력값까지 전송 */
			}
			
			
		});
		
	});
</script>
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
		
		<form id="searchForm"  onsubmit="movePage('0')">
			${pageExplorer.make()}
			<div>
				<select id="searchType" name="searchType">
					<option value="1" ${search.searchType eq 1 ? 'selected' : ''}>글 제목</option>
					<option value="2" ${search.searchType eq 2 ? 'selected' : ''}>글 내용</option>
					<option value="3" ${search.searchType eq 3 ? 'selected' : ''}>글 제목 + 글 내용</option>
					<option value="4" ${search.searchType eq 4 ? 'selected' : ''}>작성자 Nickname</option>
					<option value="5" ${search.searchType eq 5 ? 'selected' : ''}>작성자 Email</option>
					<option value="6" ${search.searchType eq 6 ? 'selected' : ''}>첨부파일 이름</option>
					<option value="7" ${search.searchType eq 7 ? 'selected' : ''}>첨부파일 형식</option>
				</select>
				<input type="text" id="searchKeyword" name="searchKeyword" value="${search.searchKeyword}"/>
				<a href="<c:url value="/reset"/>">검색 초기화</a>
			</div>
		</form>
		
		<div>
			<a href="<c:url value="/write"/>"/>글쓰기</a>
		</div>
		
	</div>
	
		

</body>
</html>