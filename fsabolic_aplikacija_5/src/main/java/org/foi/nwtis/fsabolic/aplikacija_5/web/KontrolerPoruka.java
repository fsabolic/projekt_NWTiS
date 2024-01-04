package org.foi.nwtis.fsabolic.aplikacija_5.web;


import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.fsabolic.aplikacija_5.PomocnikKontroler;
import org.foi.nwtis.fsabolic.aplikacija_5.rest.RestKlijentNadzora;
import org.foi.nwtis.fsabolic.aplikacija_5.zrna.SakupljacJmsPoruka;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.xml.ws.WebServiceRef;

/**
 * KontrolerPoruka klasa predstavlja kontroler za upravljanje porukama.
 */
@Controller
@Path("poruke")
@RequestScoped
public class KontrolerPoruka {


  /**
   * Model objekt koji se koristi za prikazivanje podataka na korisničkom sučelju.
   */
  @Inject
  private Models model;

  /**
   * Referenca na ServletContext objekt koji omogućuje pristup kontekstu aplikacije.
   */
  @Inject
  private ServletContext kontekst;
  /**
   * Referenca na Http zahtjev objekt koji omogućuje pristup sesiji zahtjeva.
   */
  @Inject
  private HttpServletRequest httpZahtjev;

  /**
   * Metoda koja dohvaća listu poruka i prikazuje ih na stranici poruke.jsp.
   *
   * @param stranica broj stranice koju je potrebno prikazati
   */
  @GET
  @Path("")
  @View("poruke.jsp")
  public void getPoruke(@QueryParam("stranica") String stranica) {
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<String> poruke = null;
    try {
      poruke = SakupljacJmsPoruka.getPoruke(stranicaInt, brojRedova);
      if (poruke.isEmpty() || poruke == null) {
        int prijasnjaStranica = (stranicaInt / brojRedova);
        model.put("stranica", prijasnjaStranica);
        stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
        poruke = SakupljacJmsPoruka.getPoruke(stranicaInt, brojRedova);
      }

      PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
      model.put("poruke", poruke);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda koja briše poruke i prikazuje ih na stranici poruke.jsp.
   *
   */
  @POST
  @Path("")
  @View("poruke.jsp")
  public void obrisiPoruke() {
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    SakupljacJmsPoruka.obrisiPoruke();
    List<String> poruke = new CopyOnWriteArrayList<>();
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    model.put("poruke", poruke);

  }



}
