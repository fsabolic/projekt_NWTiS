<%@page import="org.foi.nwtis.podaci.UdaljenostAerodromDrzava"%>
<%@page import="javax.print.DocFlavor.STRING"%>
<%@page import="java.util.List"%>
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
<title>Udaljenosti 2</title>
<jsp:include page="stil.jsp" />
</head>
<body>
	<jsp:include page="zaglavlje.jsp" />
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<form method="POST"
		action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti2">
		<label for="icaoOd">Icao Od:</label> <input type="text" name="icaoOd"
			id="icaoOd" required><br> <label for="icaoDo">Icao
			Do:</label> <input type="text" name="icaoDo" id="icaoDo" required><br>

		<button type="submit">Pronađi udaljenost</button>
		<br>
	</form>
	<%
	if (request.getAttribute("udaljenost") != null) {
	  float udaljenost = (float) request.getAttribute("udaljenost");
	  if (udaljenost != -1) {
	%>
	<p>
		<strong>Udaljenosti između ${icaoOd}-${icaoDo}:</strong> ${udaljenost}
	</p>


	<%
	} else {
	%><p>Nema aerodroma s tim ICAO-om</p>
	<%
	}
	}
	%>
</body>
</html>