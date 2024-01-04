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
<title>Udaljenosti 4</title>
<jsp:include page="stil.jsp" />
</head>
<body>
	<jsp:include page="zaglavlje.jsp" />
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<form method="POST"
		action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledUdaljenosti4">
		<label for="icaoOd">Icao Od:</label> <input type="text" name="icaoOd"
			id="icaoOd" required><br> <label for="drzava">Država:</label>
		<input type="text" name="drzava" id="drzava" required><br>
		<label for="km">Km:</label> <input type="text" name="km" id="km"
			required><br>

		<button type="submit">Pronađi udaljenosti</button>
		<br>
	</form>
	<%
	List<UdaljenostAerodromDrzava> udaljenosti =
	    (List<UdaljenostAerodromDrzava>) request.getAttribute("udaljenosti");

	if (request.getAttribute("greska") != null) {
	%><p><%=(String) request.getAttribute("greska")%></p>
	<%
	} else if (udaljenosti != null) {
	%>
	<p>Udaljenosti između:</p>
	<p>${icaoOd}-${icaoDo}</p>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;">Icao</th>
				<th style="border: 1px solid black;">Država</th>
				<th style="border: 1px solid black;">Udaljenost (km)</th>
			</tr>

		</thead>
		<tbody>
			<%
			for (UdaljenostAerodromDrzava u : udaljenosti) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=u.icao()%></td>
				<td style="border: 1px solid black;"><%=u.drzava()%></td>
				<td style="border: 1px solid black;"><%=u.km()%></td>
			</tr>
			<%
			}
			%>

		</tbody>

	</table>
	<%
	}
	%>
</body>
</html>