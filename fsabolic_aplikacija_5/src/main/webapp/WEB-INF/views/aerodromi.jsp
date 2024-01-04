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
<title>Aerodromi</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<div class="menu">
		<a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">5.5.1
			Pregled svih aerodroma</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledAerodromaLetova">5.5.3
			Pregled aerodroma za letove</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti1">5.5.4
			Pregled udaljenosti 1</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti2">5.5.5
			Pregled udaljenosti 2</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti3">5.5.6
			Pregled udaljenosti 3</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti4">5.5.7
			Pregled udaljenosti 4</a>
	</div>
</body>