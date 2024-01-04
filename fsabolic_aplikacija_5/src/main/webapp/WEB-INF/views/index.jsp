<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="autor" content="${ime} ${prezime}">
<meta name="predmet" content="${predmet}">
<meta name="godina" content="${godina}">
<meta name="verzija" content="${verzija}">
<jsp:include page="stil.jsp" />
<title>Početna stranica</title>

</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<div class="menu">
		<a href="${pageContext.servletContext.contextPath}/mvc/korisnici">5.2
			Korisnici</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/nadzor">5.3
			Upravljanje poslužiteljem</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/poruke">5.4
			JMS poruke</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">5.5
			Aerodromi</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/letovi">5.6
			Letovi</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/dnevnik">5.7
			Dnevnik</a>

	</div>
</body>