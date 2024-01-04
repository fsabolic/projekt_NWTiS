package org.foi.nwtis.fsabolic.aplikacija_2.rest;

import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.AirportsDistanceMatrixFacade;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.DnevnikFacade;
import org.foi.nwtis.klase.JsonOdgovor;
import org.foi.nwtis.klase.MrezniPomocnik;
import org.foi.nwtis.podaci.Odgovor;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa za API aerodroma
 * 
 * @author Fran Sabolić
 *
 */
@Path("nadzor")
@RequestScoped
public class RestNadzor {

  /**
   * Injektiran pristup bazi podataka
   */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /***
   * Injektiran pristup aerodromima
   */
  @Inject
  AirportFacade airportFacade;

  /***
   * Injektiran pristup udaljenostima aerodroma
   */
  @Inject
  AirportsDistanceMatrixFacade admFacade;


  /***
   * Injektiran pristup dnevniku
   */
  @Inject
  DnevnikFacade dnevnikFacade;

  /**
   * Injektiran kontekst aplikacije
   */
  @Context
  ServletContext kontekst;

  /**
   * Dohvaća status aplikacije.
   *
   * @return odgovor sa statusom aplikacije u JSON formatu
   */
  @Path("")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response posaljiStatus() {
    String odgovor = posaljiZahtjevAplikaciji1("STATUS");
    return JsonOdgovor.vratiJsonOdgovor(Odgovor.kreirajNoviOdgovor(odgovor));
  }

  /**
   * Šalje određenu komandu aplikaciji.
   *
   * @param komanda komanda koja se šalje aplikaciji
   * @return odgovor aplikacije na poslanu komandu u JSON formatu
   */
  @Path("{komanda}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response posaljiKomandu(@PathParam("komanda") String komanda) {
    String odgovor = posaljiZahtjevAplikaciji1(komanda);
    return JsonOdgovor.vratiJsonOdgovor(Odgovor.kreirajNoviOdgovor(odgovor));
  }

  /**
   * Postavlja informaciju o određenoj vrsti.
   *
   * @param vrsta vrsta informacije
   * @return odgovor aplikacije na postavljenu informaciju u JSON formatu
   */
  @Path("INFO/{vrsta}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response postaviInfo(@PathParam("vrsta") String vrsta) {
    String odgovor = posaljiZahtjevAplikaciji1("INFO " + vrsta);
    return JsonOdgovor.vratiJsonOdgovor(Odgovor.kreirajNoviOdgovor(odgovor));
  }

  /**
   * Šalje zahtjev aplikaciji 1.
   *
   * @param zahtjev zahtjev koji se šalje aplikaciji 1
   * @return odgovor aplikacije 1 na poslani zahtjev
   */
  private String posaljiZahtjevAplikaciji1(String zahtjev) {
    KonfiguracijaApstraktna konfig = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    String adresaApp1 = konfig.dajPostavku("adresaAplikacije1");
    int portApp1 = Integer.parseInt(konfig.dajPostavku("mreznaVrataAplikacije1"));
    return MrezniPomocnik.slanjeZahtjevaNa(adresaApp1, portApp1, 0, zahtjev, 0);
  }
}
