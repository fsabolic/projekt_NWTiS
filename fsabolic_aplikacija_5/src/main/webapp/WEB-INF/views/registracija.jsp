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
<title>Registracija</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici">Korisnici</a>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/korisnici/dodajKorisnika">
		<label for="korisnickoIme">Korisničko ime:</label> <input type="text"
			name="korisnickoIme" id="korisnickoIme" required> <br> <label
			for="lozinka">Lozinka:</label> <input type="password" name="lozinka"
			id="lozinka" required> <br> <label for="ime">Ime:</label>
		<input type="text" name="ime" id="ime" required> <br> <label
			for="prezime">Prezime:</label> <input type="text" name="prezime"
			id="prezime" required> <br> <label for="email">E-mail:</label>
		<input type="text" name="email" id="email" required> <br>
		<button type="submit">Dodaj korisnika</button>
		<br>
	</form>

	<%
	String poruka = (String) request.getAttribute("poruka");
	if (poruka != null) {
	%>
	<p><%=poruka%></p>
	<%
	}
	%>
</body>