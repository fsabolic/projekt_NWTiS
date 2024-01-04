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
<title>Korisnici</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb" href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<div class="menu">
		<a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">5.2.1
			Registracija korisnika</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">5.2.2
			Prijava korisnika</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika">5.2.3
			Pregled korisnika</a>
	</div>
</body>