<%@page import="org.foi.nwtis.podaci.Odgovor"%>
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
<title>Nadzor</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>

	<div class="menu">
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/status"
			method="POST">
			<button type="submit" name="komanda" value="STATUS">STATUS</button>
		</form>
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/kraj"
			method="POST">
			<button type="submit" name="komanda" value="KRAJ">KRAJ</button>
		</form>
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/init"
			method="POST">
			<button type="submit" name="komanda" value="INIT">INIT</button>
		</form>
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/pauza"
			method="POST">
			<button type="submit" name="komanda" value="PAUZA">PAUZA</button>
		</form>
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/info/da"
			method="POST">
			<button type="submit" name="komanda" value="INFO DA">INFO DA</button>
		</form>
		<form
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/info/ne"
			method="POST">
			<button type="submit" name="komanda" value="INFO NE">INFO NE</button>
		</form>
	</div>


	<%
	Odgovor odgovor = (Odgovor) request.getAttribute("odgovor");
	if (odgovor != null) {
	%>
	<p>Pristigla poruka poslužitelja:</p>
	<p>
		- > status:
		<%=odgovor.getStatus()%></p>
	<p>
		- > opis:
		<%=odgovor.getOpis()%></p>
	<%
	}
	%>
</body>