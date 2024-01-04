<%@page
	import="org.foi.nwtis.fsabolic.aplikacija_4.ws.WsLetovi.endpoint.LetAviona"%>
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@page import="java.util.List"%>
<%@page
	import="org.foi.nwtis.fsabolic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik"%>
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
<title>Letovi na datum preko vanjskog servisa</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/letovi">Letovi</a>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/letovi/letoviDatumVanjski">
		<label for="icao">ICAO:</label> <input type="text" name="icao"
			id="icao" value="${icao}"> <br> <label for="datum">Datum:</label>
		<input type="text" name="datum" id="datum" value="${datum}"> <br>

		<button type="submit">Filtriraj tablicu</button>
		<br>
	</form>
	<%
	if (request.getAttribute("autentikacija") != null) {
	%><p id="autentikacija">Morate biti prijavljeni za prikaz letova!
		${autentikacija}</p>
	<%
	}


	List<LetAviona> letovi = (List<LetAviona>) request.getAttribute("letovi");
	if (letovi != null) {
	%>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;">Icao 24</th>
				<th style="border: 1px solid black;">First seen</th>
				<th style="border: 1px solid black;">Est. Departure Airport</th>
				<th style="border: 1px solid black;">Last seen</th>
				<th style="border: 1px solid black;">Est. Arrival Airport</th>
				<th style="border: 1px solid black;">Callsign</th>
				<th style="border: 1px solid black;">Est. Departure Airport
					Horiz. Distance</th>
				<th style="border: 1px solid black;">Est. Departure Airport
					Vert. Distance</th>
				<th style="border: 1px solid black;">Est. Arrival Airport
					Horiz. Distance</th>
				<th style="border: 1px solid black;">Est. Arrival Airport Vert.
					Distance</th>
				<th style="border: 1px solid black;">Departure Airport
					Candidates Count</th>
				<th style="border: 1px solid black;">Arrival Airport Candidates
					Count</th>
			</tr>

		</thead>
		<tbody>
			<%
			for (LetAviona la : letovi) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=la.getIcao24()%></td>
				<td style="border: 1px solid black;"><%=new java.text.SimpleDateFormat("dd.MM.yyyy")
    .format(new java.util.Date(la.getFirstSeen() * 1000L))%></td>
				<td style="border: 1px solid black;"><%=la.getEstDepartureAirport()%></td>
				<td style="border: 1px solid black;"><%=new java.text.SimpleDateFormat("dd.MM.yyyy")
    .format(new java.util.Date(la.getLastSeen() * 1000L))%></td>
				<td style="border: 1px solid black;"><%=la.getEstArrivalAirport()%></td>
				<td style="border: 1px solid black;"><%=la.getCallsign()%></td>
				<td style="border: 1px solid black;"><%=la.getEstDepartureAirportHorizDistance()%></td>
				<td style="border: 1px solid black;"><%=la.getEstDepartureAirportVertDistance()%></td>
				<td style="border: 1px solid black;"><%=la.getEstArrivalAirportHorizDistance()%></td>
				<td style="border: 1px solid black;"><%=la.getEstArrivalAirportVertDistance()%></td>
				<td style="border: 1px solid black;"><%=la.getDepartureAirportCandidatesCount()%></td>
				<td style="border: 1px solid black;"><%=la.getArrivalAirportCandidatesCount()%></td>
			</tr>
			<%
			}
			%>
		</tbody>

	</table>

	<%
	}
	String greska = (String) request.getAttribute("greska");
	if (greska != null) {
	%>
	<p><%=greska%></p>
	<%
	}
	%>
</body>