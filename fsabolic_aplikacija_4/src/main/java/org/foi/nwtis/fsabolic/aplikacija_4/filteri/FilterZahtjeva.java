package org.foi.nwtis.fsabolic.aplikacija_4.filteri;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.klase.RestKlijentDnevnik;
import org.foi.nwtis.podaci.Dnevnik;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;

/***
 * Klasa za obradu svih dolazećih zahtjeva na aplikaciju koja pohranjuje zahtjeve u bazu podataka
 * 
 * @author Fran Sabolić
 *
 */


@WebFilter("/*")
public class FilterZahtjeva implements Filter {

  /***
   * Metoda za inicijalizaciju filtera
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  /***
   * Metoda za obradu dohvaćenih zahtjeva koja pohranjuje sve zahtjeve u bazu podataka
   * 
   * @param request dolazeći zahtjev
   * @param response pripremljeni odgovor
   * @param chain lanac filtera
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    StringBuilder urlZahtjeva = new StringBuilder(httpRequest.getRequestURL().toString());
    String stringUpita = httpRequest.getQueryString();
    String cijeliZahtjev = null;
    if (stringUpita == null) {
      cijeliZahtjev = urlZahtjeva.toString();
    } else {
      cijeliZahtjev = urlZahtjeva.append('?').append(stringUpita).toString();
    }


    Konfiguracija konfig = (Konfiguracija) request.getServletContext().getAttribute("konfig");
    String adresaAplikacije2 = konfig.dajPostavku("adresaAplikacije2");

    RestKlijentDnevnik rkd = new RestKlijentDnevnik(adresaAplikacije2);

    rkd.setZapis(cijeliZahtjev, "AP4");

    chain.doFilter(request, response);
  }

  /***
   * Metoda za uništavanje filtera pri gašenju aplikacije
   */
  @Override
  public void destroy() {}

}
