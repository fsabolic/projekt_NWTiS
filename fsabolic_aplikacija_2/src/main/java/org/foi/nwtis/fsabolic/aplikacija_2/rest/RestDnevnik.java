package org.foi.nwtis.fsabolic.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.Dnevnik;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.DnevnikFacade;
import org.foi.nwtis.klase.JsonOdgovor;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa za API letova
 * 
 * @author Fran Sabolić
 *
 */
@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  /**
   * Injektiran pristup bazi podataka
   */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Injektiran kontekst aplikacije
   */
  @Context
  ServletContext kontekst;

  /***
   * Injektiran pristup dnevniku
   */
  @Inject
  DnevnikFacade dnevnikFacade;

  /**
   * Dohvaća zapise dnevnika.
   *
   * @param vrsta vrsta zapisa (opcionalno)
   * @param odBroja broj zapisa od kojeg se počinje dohvaćanje (opcionalno)
   * @param broj broj zapisa koji se dohvaća (opcionalno)
   * @return odgovor sa zapisa dnevnika u JSON formatu
   */
  @Path("")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajZapiseDnevnika(@QueryParam("vrsta") String vrsta,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {
    int odBrojaInt;
    int brojInt;
    try {
      odBrojaInt = (odBroja != null) ? Integer.parseInt(odBroja) : 1;
      brojInt = (broj != null) ? Integer.parseInt(broj) : 20;
      if (odBrojaInt < 1) {
        odBrojaInt = 1;
      }
      if (brojInt < 1) {
        brojInt = 1;
      }
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Krivi format parametara");
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Krivi format parametara");
    }
    return JsonOdgovor
        .vratiJsonOdgovor(dnevnikFacade.getRequestsByType(vrsta, odBrojaInt - 1, brojInt));
  }

  /**
   * Dodaje zapis u dnevnik.
   *
   * @param zahtjev zahtjev koji se bilježi
   * @param tip tip zapisa
   * @return odgovor sa statusom HTTP 200 OK
   */
  @Path("")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response dodajZapis(@QueryParam("zahtjev") String zahtjev, @QueryParam("tip") String tip) {
    Dnevnik dnevnik = new Dnevnik();
    dnevnik.setZahtjev(zahtjev);
    dnevnik.setTip(tip);
    dnevnik.setVremenskaOznaka(new Timestamp(System.currentTimeMillis()));
    dnevnikFacade.create(dnevnik);
    return Response.ok().build();
  }
}
