<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@page
	import="org.foi.nwtis.fsabolic.aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci"%>
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
<title>Aerodrom</title>
</head>


<body>
	<jsp:include page="zaglavlje.jsp" />
	<a class="pocetna-gumb"
		href="${pageContext.servletContext.contextPath}">Početna stranica</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a>
	<a class="prijasnja-gumb"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">Prikazi aerodroma</a>

	<table style="border: 1px solid black;">
		<thead>
			<tr>
				<th style="border: 1px solid black;" rowspan="2">Icao</th>
				<th style="border: 1px solid black;" rowspan="2">Naziv</th>
				<th style="border: 1px solid black;" rowspan="2">Država</th>
				<th style="border: 1px solid black;" colspan="2">Lokacija</th>
			</tr>
			<tr>
				<th style="border: 1px solid black;">Latitude</th>
				<th style="border: 1px solid black;">Longitude</th>
			</tr>
		</thead>
		<tbody>
			<%
			Aerodrom aerodrom = (Aerodrom) request.getAttribute("aerodrom");
			%>
			<tr>
				<td style="border: 1px solid black;"><%=aerodrom.getIcao()%></td>
				<td style="border: 1px solid black;"><%=aerodrom.getNaziv()%></td>
				<td style="border: 1px solid black;"><%=aerodrom.getDrzava()%></td>
				<td style="border: 1px solid black;"><%=aerodrom.getLokacija().getLatitude()%></td>
				<td style="border: 1px solid black;"><%=aerodrom.getLokacija().getLongitude()%></td>


			</tr>
		</tbody>
	</table>
	<%
	MeteoPodaci meteoPodaci = (MeteoPodaci) request.getAttribute("meteoPodaci");
	if (meteoPodaci != null) {
	%>
	<table>
		<tr>
			<th>Naziv atributa</th>
			<th>Vrijednost atributa</th>
		</tr>
		<tr>
			<td>Naziv oblaka</td>
			<td><%=meteoPodaci.getCloudsName()%></td>
		</tr>
		<tr>
			<td>Vrijednost oblaka</td>
			<td><%=meteoPodaci.getCloudsValue()%></td>
		</tr>
		<tr>
			<td>Jedinica vlage</td>
			<td><%=meteoPodaci.getHumidityUnit()%></td>
		</tr>
		<tr>
			<td>Vrijednost vlage</td>
			<td><%=meteoPodaci.getHumidityValue()%></td>
		</tr>
		<tr>
			<td>Zadnje ažuriranje</td>
			<td><%=meteoPodaci.getLastUpdate()%></td>
		</tr>
		<tr>
			<td>Način oborina</td>
			<td><%=meteoPodaci.getPrecipitationMode()%></td>
		</tr>
		<tr>
			<td>Jedinica oborina</td>
			<td><%=meteoPodaci.getPrecipitationUnit()%></td>
		</tr>
		<tr>
			<td>Vrijednost oborina</td>
			<td><%=meteoPodaci.getPrecipitationValue()%></td>
		</tr>
		<tr>
			<td>Jedinica tlaka</td>
			<td><%=meteoPodaci.getPressureUnit()%></td>
		</tr>
		<tr>
			<td>Vrijednost tlaka</td>
			<td><%=meteoPodaci.getPressureValue()%></td>
		</tr>
		<tr>
			<td>Izlazak sunca</td>
			<td><%=meteoPodaci.getSunRise()%></td>
		</tr>
		<tr>
			<td>Zalazak sunca</td>
			<td><%=meteoPodaci.getSunSet()%></td>
		</tr>
		<tr>
			<td>Maksimalna temperatura</td>
			<td><%=meteoPodaci.getTemperatureMax()%></td>
		</tr>
		<tr>
			<td>Minimalna temperatura</td>
			<td><%=meteoPodaci.getTemperatureMin()%></td>
		</tr>
		<tr>
			<td>Jedinica temperature</td>
			<td><%=meteoPodaci.getTemperatureUnit()%></td>
		</tr>
		<tr>
			<td>Vrijednost temperature</td>
			<td><%=meteoPodaci.getTemperatureValue()%></td>
		</tr>
		<tr>
			<td>Vidljivost</td>
			<td><%=meteoPodaci.getVisibility()%></td>
		</tr>
		<tr>
			<td>Ikona vremena</td>
			<td><img
				src="http://openweathermap.org/img/w/<%=meteoPodaci.getWeatherIcon()%>.png"
				alt="ikona" />
		</tr>
		<tr>
			<td>Broj vremena</td>
			<td><%=meteoPodaci.getWeatherNumber()%></td>
		</tr>
		<tr>
			<td>Vrijednost vremena</td>
			<td><%=meteoPodaci.getWeatherValue()%></td>
		</tr>
		<tr>
			<td>Kod smjera vjetra</td>
			<td><%=meteoPodaci.getWindDirectionCode()%></td>
		</tr>
		<tr>
			<td>Naziv smjera vjetra</td>
			<td><%=meteoPodaci.getWindDirectionName()%></td>
		</tr>
		<tr>
			<td>Vrijednost smjera vjetra</td>
			<td><%=meteoPodaci.getWindDirectionValue()%></td>
		</tr>
		<tr>
			<td>Naziv brzine vjetra</td>
			<td><%=meteoPodaci.getWindSpeedName()%></td>
		</tr>
		<tr>
			<td>Vrijednost brzine vjetra</td>
			<td><%=meteoPodaci.getWindSpeedValue()%></td>
		</tr>
	</table>

	<%
	} else {
	%><p>Nema zabilježenjih meteo podataka za dani aerodrom</p>
	<%
	}
	%>

</body>