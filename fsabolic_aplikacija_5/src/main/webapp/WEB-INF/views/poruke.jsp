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
<title>Poruke</title>
<jsp:include page="stil.jsp" />
</head>
<body>
	<jsp:include page="zaglavlje.jsp" />
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<form action="${pageContext.servletContext.contextPath}/mvc/poruke"
		method="POST">
		<button type="submit" name="command" value="obrisi">Obriši
			sve poruke</button>
	</form>
	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;">Poruka</th>
			</tr>

		</thead>
		<tbody>
			<%
			List<String> poruke = (List<String>) request.getAttribute("poruke");
			if (poruke != null) {
			  for (String s : poruke) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=s%></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>

	</table>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/poruke?stranica=${stranica-2}">Prethodna
		stranica</a>
	<a href="${pageContext.servletContext.contextPath}/mvc/poruke">Početak</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/poruke?stranica=${stranica}">Sljedeća
		stranica</a>
</body>
</html>