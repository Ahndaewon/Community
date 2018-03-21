<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<c:url value="static/css/button.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="static/css/input.css"/>">
<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"></script>
<script type="text/javascript">





	$().ready(function(){
		
		$("#email").keyup(function(){
			var value = $(this).val();
			
			if ( value != "" ) {
				
				//Ajax Call (http://locahost:8080/api/exists/email)
				  /* email 파라미터 */
				$.post("<c:url value="/api/exists/email"/>", {
					email : value          
				}, function(response){
					
					console.log(response.response);
					
					if ( response.response ) {
						$("#email").removeClass("valid");
						$("#email").addClass("invalid");
						
					}
					else {
						$("#email").removeClass("invalid");
						$("#email").addClass("valid");
					}
				});
				/* Ajax콜을 하겠다 3가지 파라미터 있음 */ 
										
			}
			else {
				$(this).removeClass("valid");
				$(this).addClass("invalid");
			}
		});
		
		$("#nickname").keyup(function(){
			var value = $(this).val();
			if ( value != "" ) {
				
				$.post("<c:url value="/api/exists/nickname"/>", {
					nickname : value          
				}, function(response){
					
					console.log(response.response);
					
					if ( response.response ) {
						$("#nickname").removeClass("valid");
						$("#nickname").addClass("invalid");
						
					}
					else {
						$("#nickname").removeClass("invalid");
						$("#nickname").addClass("valid");
					}
				});

			
			}
			else {
				$(this).removeClass("valid");
				$(this).addClass("invalid");
			}
		});
		
	

		$("#password").keyup(function(){
			var value = $(this).val();
			var password = $("#password-confirm").val();
			
			if ( value != "" ) {
				$(this).removeClass("invalid");
				$(this).addClass("valid");
			}
			else {
				$(this).removeClass("valid");
				$(this).addClass("invalid");
			}
			
			if ( value != password ) {
				
				$(this).removeClass("valid");
				$(this).addClass("invalid");
				$("#password-confirm").removeClass("invalid");
				$("#password-confirm").addClass("valid");
			}
			
			else {
				
				$(this).removeClass("invalid");
				$(this).addClass("valid");
				$("#password-confirm").removeClass("invalid");
				$("#password-confirm").addClass("valid");
			}
			
			
		});

		$("#password-confirm").keyup(function(){
			var value = $(this).val();
			var password = $("#password").val();
			
			if ( value != password ) {
				
				$(this).removeClass("valid");
				$(this).addClass("invalid");
				$("#password").removeClass("invalid");
				$("#password").addClass("valid");
			
			}
			else {
				$(this).removeClass("invalid");
				$(this).addClass("valid");
				$("#password").removeClass("invalid");
				$("#password").addClass("valid");
			}
		});
		
		
		
		
		$("#registBtn").click(function(){
			
			if ( $("#email").val() == "" ) {
				alert("이메일을 입력하세요");
				$("#email").focus();
				$("#email").addClass("invalid");
				return false;
			}
			
			if ( $("#email").hasClass("invalid") ) {
				alert("작성한 이메일은 사용할 수 없습니다.1");
				$("#email").focus();
				return false;
			}
			
			else {
				$.post("<c:url value="/api/exists/email"/>", {
					email : $("#email").val()        
				}, function(response){
					
					if ( response.response ) {
						alert("작성한 이메일은 사용할 수 없습니다.2");
						$("#email").focus();
						return false;
					}
					
					if ( $("#nickname").val() == "" ) {
						alert("nickname을 입력하세요");
						$("#nickname").focus();
						$("#nickname").addClass("invalid");
						return false;
					}
					
					else {
						
						$.post("<c:url value="/api/exists/nickname"/>", {
							nickname : $("#nickname").val()          
						}, function(response){
							
							if ( response.response ) {
								
								alert("작성한 닉네임은 사용할 수 없습니다.2");
								$("#nickname").removeClass("valid");
								$("#nickname").addClass("invalid");
								return false;
							}
							else {
								$("#nickname").removeClass("invalid");
								$("#nickname").addClass("valid");
							}
							
							
							if ( $("#password").val() == "" ) {
								alert("password를 입력하세요");
								$("#password").focus();
								$("#password").addClass("invalid");
								return false;
							}
							
							
							if ( response.response ) {
								$("#email").removeClass("valid");
								$("#email").addClass("invalid");
								
							}
							else {
								$("#email").removeClass("invalid");
								$("#email").addClass("valid");
							}
							
							$("#registForm").attr({
								"method" : "post",
								"action" : "<c:url value="/regist"/>"
							})
							.submit();
							
							
							
						});
						
					}
					
										
					
				
					
				});
				/* Ajax콜을 하겠다 3가지 파라미터 있음 */ 
			}
			
			
		});
		
	});
</script>
</head>
<body>
	
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp"/>
		
		<form:form modelAttribute="registForm">
			<div>
				<!-- TODO email 중복검사하기 (ajax)  -->
				<input type="email" id="email" name="email"  placeholder="Email"
					   value="${registForm.email}"/>
			</div>
			<div>
				<form:errors path="email"/>
			</div>
			
			<div>
				<!-- TODO nickname 중복검사하기 (ajax)  -->
				<input type="text" id="nickname" name="nickname" placeholder="nickname"
					   value="${registForm.nickname}"/>
			</div>
			<div>
				<form:errors path="nickname"/>
			</div>
			
			<div>
				<input type="password" id="password" name="password" placeholder="password"
					   value="${registForm.password}"/>
			</div>
			<div>
				<form:errors path="nickname"/>
			</div>
			
			
			<div>
				<input type="password" id="password-confirm"  placeholder="password">
			</div>
			
			<div style="text-align: center;">
				<div id="registBtn" class="button">회원가입</div>
			</div>
			
		</form:form> 
		
	</div>
	
		
</body>
</html>