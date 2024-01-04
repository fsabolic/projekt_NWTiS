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
<title>Letovi</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<div class="menu">
		<a
			href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviInterval">5.6.1
			Pregled spremljenih letova u zadanom intervalu</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviDatum">5.6.2
			Pregled spremljenih letova na zadani dan</a> <a
			href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviDatumVanjski">5.6.3
			Pregled letova na zadani dan</a>
	</div>
</body>