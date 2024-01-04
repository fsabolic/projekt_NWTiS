package org.foi.nwtis.fsabolic.aplikacija_2;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.foi.nwtis.fsabolic.aplikacija_2.rest.RestDnevnik;
import java.io.IOException;
import javax.sql.DataSource;
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
   * Injektirani pristup bazi podataka
   */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  private DataSource ds;

  /***
   * Injektirano zrno za pohranu zahtjeva u bazu podataka
   */
  @Inject
  private RestDnevnik restDnevnik;


  /***
   * Metoda za inicijalizaciju filtera
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  /***
   * Metoda za obradu dohvaćenih zahtjeva koja pohranjuje sve zahtjeve u bazu podataka
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


    restDnevnik.dodajZapis(cijeliZahtjev, "AP2");

    chain.doFilter(request, response);
  }

  /***
   * Metoda za uništavanje filtera pri gašenju aplikacije
   */
  @Override
  public void destroy() {}

}
