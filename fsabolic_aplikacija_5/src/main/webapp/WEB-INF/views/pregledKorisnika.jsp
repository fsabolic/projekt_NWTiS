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
<title>Pregled korisnika</title>
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
		if (jsonObject.brojKorisnika != null) {
			document.getElementById('brojKorisnikaNovi').innerText = "Dinamičan broj korisnika: "
					+ jsonObject.brojKorisnika
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
		href="${pageContext.servletContext.contextPath}/mvc/korisnici">Korisnici</a>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika">
		<label for="traziImeKorisnika">Ime:</label> <input type="text"
			name="traziImeKorisnika" id="traziImeKorisnika"> <br> <label
			for="traziPrezimeKorisnika">Prezime:</label> <input type="text"
			name="traziPrezimeKorisnika" id="traziPrezimeKorisnika"> <br>
		<button type="submit">Filtriraj tablicu</button>
		<br>
	</form>

	<%
	String poruka = (String) request.getAttribute("poruka");
	if (poruka != null) {
	%>
	<p><%=poruka%></p>
	<%
	}
	String korisnickoIme = (String) request.getAttribute("korisnickoIme");
	if (korisnickoIme == null) {
	%>
	<p>Morate se prijaviti kako bi mogli pregledati korisnike!</p>
	<%
	} else {
	List<Korisnik> korisnici = (List<Korisnik>) request.getAttribute("korisnici");
	if (korisnici != null) {
	%>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;" rowspan="2">Korisničko ime</th>
				<th style="border: 1px solid black;" rowspan="2">Lozinka</th>
				<th style="border: 1px solid black;" rowspan="2">Ime</th>
				<th style="border: 1px solid black;" rowspan="2">Prezime</th>
				<th style="border: 1px solid black;" rowspan="2">E-mail</th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Korisnik k : korisnici) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=k.getKorIme()%></td>
				<td style="border: 1px solid black;"><%=k.getLozinka()%></td>
				<td style="border: 1px solid black;"><%=k.getIme()%></td>
				<td style="border: 1px solid black;"><%=k.getPrezime()%></td>
				<td style="border: 1px solid black;"><%=k.getEmail()%></td>

			</tr>
			<%
			}
			%>
		</tbody>

	</table>
	<p id="brojKorisnikaStair">Statičan broj korisnika:
		${brojKorisnika}</p>
	<p id="brojKorisnikaNovi">Dinamičan broj korisnika:
		${brojKorisnika}</p>

	<%
	}
	}
	%>
</body>