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
<title>Udaljenosti aerodroma ${icao}</title>
<jsp:include page="stil.jsp" />
</head>
<body>
	<jsp:include page="zaglavlje.jsp" />
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">Svi
		aerodromi</a>
	<%
	List<UdaljenostAerodromDrzava> udaljenosti =
	    (List<UdaljenostAerodromDrzava>) request.getAttribute("udaljenosti");

	if (udaljenosti != null) {
	%>
	<p>Udaljenosti aerodroma ${icao}:</p>
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
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostiAerodroma/${icao}?stranica=${stranica-2}">Prethodna
		stranica</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostiAerodroma/${icao}">Početak</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostiAerodroma/${icao}?stranica=${stranica}">Sljedeća
		stranica</a>
	<%
	}
	%>
</body>
</html>