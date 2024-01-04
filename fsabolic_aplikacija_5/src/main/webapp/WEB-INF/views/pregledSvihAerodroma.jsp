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
<title>Pregled svih aerodroma</title>
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
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
		<label for="traziNaziv">Naziv:</label> <input type="text"
			name="traziNaziv" id="traziNaziv" value="${traziNaziv}"> <br>
		<label for="traziDrzavu">Država:</label> <input type="text"
			name="traziDrzavu" id="traziDrzavu" value="${traziDrzavu}"> <br>
		<button type="submit">Filtriraj tablicu</button>
		<br>
	</form>
	<%
	if (request.getAttribute("autentikacija") != null) {
	%><p id="autentikacija">Upozorenje, ne možete spremati letove ni
		vidjeti broj aerodroma! ${autentikacija}</p>
	<%
	}
	String poruka = (String) request.getAttribute("poruka");
	if (poruka != null) {
	%>
	<p><%=poruka%></p>
	<%
	}

	List<Aerodrom> aerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");
	if (aerodromi != null) {
	%>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;" rowspan="2">Icao</th>
				<th style="border: 1px solid black;" rowspan="2">Naziv</th>
				<th style="border: 1px solid black;" rowspan="2">Država</th>
				<th style="border: 1px solid black;" colspan="2">Lokacija</th>
				<th style="border: 1px solid black;" rowspan="2">Detalji</th>
				<th style="border: 1px solid black;" rowspan="2">Udaljenosti</th>
				<th style="border: 1px solid black;" rowspan="2">Spremi
					aerodrom</th>
			</tr>
			<tr>
				<th style="border: 1px solid black;">Latitude</th>
				<th style="border: 1px solid black;">Longitude</th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Aerodrom a : aerodromi) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=a.getIcao()%></td>
				<td style="border: 1px solid black;"><%=a.getNaziv()%></td>
				<td style="border: 1px solid black;"><%=a.getDrzava()%></td>
				<td style="border: 1px solid black;"><%=a.getLokacija().getLatitude()%></td>
				<td style="border: 1px solid black;"><%=a.getLokacija().getLongitude()%></td>
				<td style="border: 1px solid black;"><a
					href="<%=request.getContextPath()%>/mvc/aerodromi/pregledJednogAerodroma/<%=a.getIcao()%>">Detalji
				</a></td>
				<td style="border: 1px solid black;"><a
					href="<%=request.getContextPath()%>/mvc/aerodromi/udaljenostiAerodroma/<%=a.getIcao()%>">Udaljenosti
				</a></td>
				<td style="border: 1px solid black;">
					<form method="post"
						action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
						<input hidden="" type="text" name="traziNaziv" id="traziNaziv"
							value="${traziNaziv}"> <input hidden="" type="text"
							name="traziDrzavu" id="traziDrzavu" value="${traziDrzavu}"><input
							hidden="" type="text" name="stranica" id="stranica"
							value="${stranica-1}"><input hidden="" type="text"
							name="icao" id="icao" value="<%=a.getIcao()%>">
						<button type="submit">Spremi let</button>
						<br>
					</form>
				</td>

			</tr>
			<%
			}
			%>
		</tbody>

	</table>
	<div class="post-gumbi">
		<form method="post"
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
			<input hidden="" type="text" name="traziNaziv" id="traziNaziv"
				value="${traziNaziv}"> <input hidden="" type="text"
				name="traziDrzavu" id="traziDrzavu" value="${traziDrzavu}"><input
				hidden="" type="text" name="stranica" id="stranica"
				value="${stranica-2}">
			<button type="submit">Prethodna stranica</button>
			<br>
		</form>
		<form method="post"
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
			<input hidden="" type="text" name="traziNaziv" id="traziNaziv"
				value="${traziNaziv}"> <input hidden="" type="text"
				name="traziDrzavu" id="traziDrzavu" value="${traziDrzavu}">
			<button type="submit">Početak</button>
			<br>
		</form>
		<form method="post"
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
			<input hidden="" type="text" name="traziNaziv" id="traziNaziv"
				value="${traziNaziv}"> <input hidden="" type="text"
				name="traziDrzavu" id="traziDrzavu" value="${traziDrzavu}"><input
				hidden="" type="text" name="stranica" id="stranica"
				value="${stranica}">
			<button type="submit">Sljedeća stranica</button>
			<br>
		</form>
	</div>
	<%
	if (request.getAttribute("autentikacija") == null) {
	%>
	<p id="brojAerodromaStari">Statičan broj aerodroma:
		${brojAerodroma}</p>
	<p id="brojAerodromaNovi">Dinamičan broj aerodroma:
		${brojAerodroma}</p>

	<%
	}
	}
	%>
</body>