<%@page
	import="org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodrom"%>
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
<title>Pregled aerodroma letova</title>
</head>
<script>
	const webSocket = new WebSocket(
			'ws://webpredmeti:8080/fsabolic_aplikacija_4/info');

	webSocket.onopen = function() {
		console.log('WebSocket veza otvorena');
	};

	webSocket.onmessage = function(event) {
		console.log('Poruka od poslužitelja:', event.data);
		let stringy = event.data;
		let jsonObject = JSON.parse(stringy);
		if (jsonObject.brojAerodroma != null) {
			document.getElementById('brojAerodromaNovi').innerText = "Dinamičan broj aerodroma: "
					+ jsonObject.brojAerodroma
					+ '   ('
					+ jsonObject.trenutnoVrijemePosluzitelja + ')';
		}
	};

	webSocket.onclose = function() {
		console.log('WebSocket se zatvara');
	};
</script>

<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<%
	if (request.getAttribute("autentikacija") != null) {
	%><p id="autentikacija">Za ovu funkcionalnost morate biti
		prijavljeni! ${autentikacija}</p>

	<%
	} else {

	List<Aerodrom> aerodromiLetova = (List<Aerodrom>) request.getAttribute("aerodromiLetova");
	if (aerodromiLetova != null) {
	%>
	<p id="brojAerodromaStari">Statičan broj aerodroma:
		${brojAerodroma}</p>
	<p id="brojAerodromaNovi">Dinamičan broj aerodroma:
		${brojAerodroma}</p>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;" rowspan="2">Icao</th>
				<th style="border: 1px solid black;" rowspan="2">Naziv</th>
				<th style="border: 1px solid black;" rowspan="2">Država</th>
				<th style="border: 1px solid black;" colspan="2">Lokacija</th>
				<th style="border: 1px solid black;" rowspan="2">Aktiviran</th>
				<th style="border: 1px solid black;" rowspan="2">Promijeni</th>
			</tr>
			<tr>
				<th style="border: 1px solid black;">Latitude</th>
				<th style="border: 1px solid black;">Longitude</th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Aerodrom a : aerodromiLetova) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=a.getIcao()%></td>
				<td style="border: 1px solid black;"><%=a.getNaziv()%></td>
				<td style="border: 1px solid black;"><%=a.getDrzava()%></td>
				<td style="border: 1px solid black;"><%=a.getLokacija().getLatitude()%></td>
				<td style="border: 1px solid black;"><%=a.getLokacija().getLongitude()%></td>
				<td style="border: 1px solid black;">
					<%
					if (a.isAktivan()) {
					%> Da<%
					} else {
					%>Pauza<%
					}
					%>
				</td>
				<td style="border: 1px solid black;"><a
					href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledAerodromaLetova?icao=<%=a.getIcao()%>&status=<%if(!a.isAktivan()){%>aktiviraj<%}else{%>pauziraj<%}%>">
						Promijeni status</a></td>

			</tr>
			<%
			}
			%>
		</tbody>

	</table>

	<%
	}
	}
	%>
</body>