package org.foi.nwtis.fsabolic.aplikacija_5.web;


import java.net.URI;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.fsabolic.aplikacija_5.PomocnikKontroler;
import org.foi.nwtis.fsabolic.aplikacija_5.rest.RestKlijentNadzora;
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
 * KontrolerAerodroma klasa predstavlja kontroler za upravljanje aerodromima.
 */
@Controller
@Path("nadzor")
@RequestScoped
public class KontrolerNadzora {


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
   * Početni menu za nadzor
   */
  @GET
  @Path("")
  @View("nadzor.jsp")
  public void pocetak() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);


  }

  /**
   * Vraća status poslužitelja s aplikacije 1
   */
  @POST
  @Path("status")
  @View("nadzor.jsp")
  public void vratiStatus() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.getStatus());
  }

  /**
   * Gasi poslužitelj s aplikacije 1
   */
  @POST
  @Path("kraj")
  @View("nadzor.jsp")
  public void ugasiPosluzitelj() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.setKraj());

  }

  /**
   * Inicijalizira poslužitelj s aplikacije 1
   */
  @POST
  @Path("init")
  @View("nadzor.jsp")
  public void initPosluzitelj() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.setInit());

  }


  /**
   * Pauzira poslužitelj s aplikacije 1
   */
  @POST
  @Path("pauza")
  @View("nadzor.jsp")
  public void pauzirajPosluzitelj() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.setPauza());

  }

  /**
   * Postavlja ispis na standardni izlazi u poslužitelju s aplikacije 1
   */
  @POST
  @Path("info/da")
  @View("nadzor.jsp")
  public void postaviIspis() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.setInfoDa());

  }

  /**
   * Gasi ispis na standardni izlazi u poslužitelju s aplikacije 1
   */
  @POST
  @Path("info/ne")
  @View("nadzor.jsp")
  public void ugasiIspis() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentNadzora rkn = postaviRestKlijentNadzora();
    model.put("odgovor", rkn.setInfoNe());

  }

  /**
   * Postavlja klasu za upravljanje rest servisom za nadzor
   */

  public RestKlijentNadzora postaviRestKlijentNadzora() {
    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");
    String adresaAplikacije2 = konfig.dajPostavku("adresaAplikacije2");
    RestKlijentNadzora rkn = new RestKlijentNadzora(adresaAplikacije2);
    return rkn;
  }
}
