package org.foi.nwtis.fsabolic.aplikacija_4.ws;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.RestKlijentAerodroma;
import org.foi.nwtis.fsabolic.aplikacija_4.greska.PogresnaAutentikacija;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletContext;

/**
 * Web servis za dohvaćanje meteoroloških podataka.
 */
@WebService(serviceName = "meteo")
public class WsMeteo {

  /**
   * Injektiran objekt klase za rad s aerodromima
   */
  @Inject
  private AirportFacade airportFacade;

  /***
   * Injektiran kontekst aplikacije
   */
  @Inject
  ServletContext kontekst;

  /**
   * Dohvaća meteo podatke za određeni aerodrom.
   *
   * @param icao kod ICAO aerodroma
   * @return MeteoPodaci objekt koji sadrži informacije o trenutnom vremenu na aerodromu
   */
  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam(name = "icao") String icao) {
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    Aerodrom aerodrom = null;
    try {
      String URI = postavke.dajPostavku("adresaAplikacije2");
      RestKlijentAerodroma rca = new RestKlijentAerodroma(URI);
      aerodrom = rca.getAerodrom(icao);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (aerodrom == null)
      return null;

    String apiKey = postavke.dajPostavku("OpenWeatherMap.apikey");
    OWMKlijent owmk = new OWMKlijent(apiKey);

    MeteoPodaci meteoPodatak = null;

    try {
      meteoPodatak = owmk.getRealTimeWeather(aerodrom.getLokacija().getLatitude(),
          aerodrom.getLokacija().getLongitude());
    } catch (NwtisRestIznimka e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Greška pri dohvaćanju meteo podataka za dani aerodrom!");
      return null;
    }

    return meteoPodatak;

  }


}
