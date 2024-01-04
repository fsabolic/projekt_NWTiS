package org.foi.nwtis.fsabolic.aplikacija_5.web;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsLetovi.endpoint.PogresnaAutentikacija_Exception;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsLetovi.endpoint.LetAviona;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsLetovi.endpoint.Letovi;
import org.foi.nwtis.fsabolic.aplikacija_5.PomocnikKontroler;
import org.foi.nwtis.fsabolic.aplikacija_5.rest.RestKlijentAerodroma;
import org.foi.nwtis.podaci.Aerodrom;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.xml.ws.WebServiceRef;
import jakarta.xml.ws.soap.SOAPFaultException;

/**
 * KontrolerLetova klasa predstavlja kontroler za upravljanje letovima.
 */
@Controller
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  /**
   * Referenca na web servis za letove.
   */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/fsabolic_aplikacija_4/letovi?wsdl")
  private Letovi service;

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
   * Prikazuje početnu stranicu letova.
   */
  @GET
  @Path("")
  @View("letovi.jsp")
  public void pocetak() {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
  }

  /***
   * Metoda koja se poziva GET metodom, dohvaća sve letove u intervalu i postavlja ih u model
   * 
   */
  @GET
  @Path("letoviInterval")
  @View("letoviInterval.jsp")
  public void dajLetoveIntervala() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }

  /***
   * Metoda koja se poziva POST metodom, dohvaća sve letove u intervalu i postavlja ih u model
   * 
   * @param stranica trenutna stranica koja se prikazuje
   * @param icao icao aerodroma koji se sprema
   * @param datumOd datum na koji počinje interval
   * @param datumDo datum na koji završava interval
   */
  @POST
  @Path("letoviInterval")
  @View("letoviInterval.jsp")
  public void dajLetoveIntervalaPost(@FormParam("icao") String icao,
      @FormParam("datumOd") String datumOd, @FormParam("datumDo") String datumDo,
      @FormParam("stranica") String stranica) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<LetAviona> letovi = null;
    try {
      try {
        var port = service.getWsLetoviPort();
        letovi = port.dajPolaskeInterval(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
            PomocnikKontroler.dajLozinku(httpZahtjev), icao, datumOd, datumDo,
            String.valueOf(stranicaInt), String.valueOf(brojRedova));
        if (letovi.isEmpty() || letovi == null) {
          int prijasnjaStranica = (stranicaInt / brojRedova);
          model.put("stranica", prijasnjaStranica);
          stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
          letovi = port.dajPolaskeInterval(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao, datumOd, datumDo,
              String.valueOf(stranicaInt), String.valueOf(brojRedova));
        }

        PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
        model.put("letovi", letovi);
        model.put("icao", icao);
        model.put("datumOd", datumOd);
        model.put("datumDo", datumDo);
      } catch (PogresnaAutentikacija_Exception e) {
        model.put("autentikacija", e.getMessage());
      }
    } catch (SOAPFaultException e) {
      model.put("greska", "Pogreška pri dohvaćanju letova!");
    }


  }


  /***
   * Metoda koja se poziva GET metodom, dohvaća sve letove na dani datum i postavlja ih u model
   * 
   */
  @GET
  @Path("letoviDatum")
  @View("letoviDatum.jsp")
  public void dajLetoveDatum() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }

  /***
   * Metoda koja se poziva POST metodom, dohvaća sve letove na dani datum i postavlja ih u model
   * 
   * @param stranica trenutna stranica koja se prikazuje
   * @param icao icao aerodroma koji se sprema
   * @param datum datum na koji se gledaju letovi
   */
  @POST
  @Path("letoviDatum")
  @View("letoviDatum.jsp")
  public void dajLetoveDatumPost(@FormParam("icao") String icao, @FormParam("datum") String datum,
      @FormParam("stranica") String stranica) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<LetAviona> letovi = null;
    try {
      try {
        var port = service.getWsLetoviPort();
        letovi = port.dajPolaskeNaDan(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
            PomocnikKontroler.dajLozinku(httpZahtjev), icao, datum, String.valueOf(stranicaInt),
            String.valueOf(brojRedova));
        if (letovi.isEmpty() || letovi == null) {
          int prijasnjaStranica = (stranicaInt / brojRedova);
          model.put("stranica", prijasnjaStranica);
          stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
          letovi = port.dajPolaskeNaDan(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao, datum, String.valueOf(stranicaInt),
              String.valueOf(brojRedova));
        }

        PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
        model.put("letovi", letovi);
        model.put("icao", icao);
        model.put("datum", datum);
      } catch (PogresnaAutentikacija_Exception e) {
        model.put("autentikacija", e.getMessage());
      }
    } catch (SOAPFaultException e) {
      model.put("greska", "Pogreška pri dohvaćanju letova!");
    }
  }


  /***
   * Metoda koja se poziva GET metodom, dohvaća sve letove na dani datum preko vanjskog servisa i
   * postavlja ih u model
   * 
   */
  @GET
  @Path("letoviDatumVanjski")
  @View("letoviDatumVanjski.jsp")
  public void dajLetoveDatumVanjski() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }

  /***
   * Metoda koja se poziva POST metodom, dohvaća sve letove na dani datum preko vanjskog servisa i
   * postavlja ih u model
   * 
   * @param stranica trenutna stranica koja se prikazuje
   * @param icao icao aerodroma koji se sprema
   * @param datum datum na koji se gledaju letovi
   */
  @POST
  @Path("letoviDatumVanjski")
  @View("letoviDatumVanjski.jsp")
  public void dajLetoveDatumVanjskiPost(@FormParam("icao") String icao,
      @FormParam("datum") String datum, @FormParam("stranica") String stranica) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<LetAviona> letovi = null;
    try {
      try {
        var port = service.getWsLetoviPort();
        letovi = port.dajPolaskeNaDanOS(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
            PomocnikKontroler.dajLozinku(httpZahtjev), icao, datum);
        if (letovi.isEmpty() || letovi == null) {
          int prijasnjaStranica = (stranicaInt / brojRedova);
          model.put("stranica", prijasnjaStranica);
          stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;

          letovi = port.dajPolaskeNaDanOS(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao, datum);
        }

        PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
        model.put("letovi", letovi);
        model.put("icao", icao);
        model.put("datum", datum);
      } catch (PogresnaAutentikacija_Exception e) {
        model.put("autentikacija", e.getMessage());
      }
    } catch (SOAPFaultException e) {
      model.put("greska", "Pogreška pri dohvaćanju letova!");
    }
  }
}
