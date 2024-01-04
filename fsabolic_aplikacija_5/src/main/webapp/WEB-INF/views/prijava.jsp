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
<title>Prijava</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici">Korisnici</a>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/korisnici/prijaviKorisnika">
		<label for="korisnickoIme">Korisničko ime:</label> <input type="text"
			name="korisnickoIme" id="korisnickoIme" required> <br> <label
			for="lozinka">Lozinka:</label> <input type="password" name="lozinka"
			id="lozinka" required> <br>
		<button type="submit">Prijavi</button>
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