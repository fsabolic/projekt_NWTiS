<%@page import="org.foi.nwtis.podaci.Dnevnik"%>
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
<title>Dnevnik</title>
<jsp:include page="stil.jsp" />
</head>
<body>
	<jsp:include page="zaglavlje.jsp" />
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>

	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?&vrsta=AP2">Aplikacija
		2</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?&vrsta=AP4">Aplikacija
		4</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?&vrsta=AP5">Aplikacija
		5</a>

	<br>
	<br>

	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;">ID zapisa</th>
				<th style="border: 1px solid black;">Zahtjev</th>
				<th style="border: 1px solid black;">Tip</th>
				<th style="border: 1px solid black;">Vremenska oznaka</th>
			</tr>
		</thead>
		<tbody>
			<%
			List<Dnevnik> zapisi = (List<Dnevnik>) request.getAttribute("zapisi");
			if (zapisi != null) {
			  for (Dnevnik d : zapisi) {
			%>
			<tr>
				<td style="border: 1px solid black;"><%=d.getIdZapisa()%></td>
				<td style="border: 1px solid black;"><%=d.getZahtjev()%></td>
				<td style="border: 1px solid black;"><%=d.getTip()%></td>
				<td style="border: 1px solid black;"><%=d.getVremenskaOznaka()%></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>

	</table>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?stranica=${stranica-2}&vrsta=${vrsta}">Prethodna
		stranica</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?vrsta=${vrsta}">Početak</a>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?stranica=${stranica}&vrsta=${vrsta}">Sljedeća
		stranica</a>
</body>
</html>