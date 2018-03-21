<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/link.css"/>"/>
<style type="text/css">
	#nav > ul {
		padding: 0px;
		margin: 0px;
	}
	
	#nav li {
		display: inline-block;
		margin-right: 15px;
	}
	
	#nav li:FIRST-CHILD {
		margin-left: 0px;
		
	}

</style>

<div id="nav">
	<ul">
	
		<c:if test="${empty sessionScope.__USER__}">
			<li>
				<a href="<c:url value="/logout"/>">Regist/Login</a>
			</li>
		</c:if>
		
		<c:if test="${not empty sessionScope.__USER__}">
			<li>(${sessionScope.__USER__ .nickname}님)<a href="<c:url value="/logout"/>">logout</a></li>
		</c:if>
		
			<li>
				<a href="<c:url value="/"/>">community</a>
			</li>
			
			
		<c:if test="${not empty sessionScope.__USER__}">
			<li>
				<a href="<c:url value="/leave/process1"/>">탈퇴하기</a>
			</li>
		</c:if>
			
	</ul>
</div>