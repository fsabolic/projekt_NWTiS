package org.foi.nwtis.fsabolic.aplikacija_4.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.fsabolic.aplikacija_4.greska.PogresnaAutentikacija;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.AerodromiLetovi;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.fsabolic.aplikacija_4.socket.InfoWebSocket;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.InfoPodatak;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

/**
 * SOAP web servis koji omogućuje pristup aerodromima.
 */
@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  /**
   * Fasada za pristup podacima o aerodromima i letovima.
   */
  @Inject
  AerodromiLetoviFacade aerodromiLetoviFacade;

  /**
   * Fasada za pristup podacima o aerodromima.
   */
  @Inject
  AirportFacade airportFacade;

  /**
   * Fasada za pristup podacima o korisnicima.
   */
  @Inject
  KorisniciFacade korisniciFacade;

  /**
   * Dohvaća listu aerodroma za letove.
   *
   * @param korisnik korisničko ime
   * @param lozinka lozinka
   * @return lista aerodroma za letove
   * @throws PogresnaAutentikacija u slučaju pogrešne autentikacije
   */
  @WebMethod
  public List<Aerodrom> dajAerodromeZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka) throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);
    List<AerodromiLetovi> aerodromiLetovi = aerodromiLetoviFacade.findAll();
    List<Aerodrom> aerodromi = new ArrayList<>();
    for (AerodromiLetovi aL : aerodromiLetovi) {
      Aerodrom aerodrom = new Aerodrom(airportFacade.find(aL.getIcao()));
      aerodrom.setAktivan(aL.isAktivan());
      aerodromi.add(aerodrom);
    }
    return aerodromi;
  }

  /**
   * Dodaje aerodrom za letove.
   *
   * @param korisnik korisničko ime
   * @param lozinka lozinka
   * @param icao ICAO oznaka aerodroma
   * @return true ako je aerodrom uspješno dodan, inače false
   * @throws PogresnaAutentikacija u slučaju pogrešne autentikacije
   */
  @WebMethod
  public boolean dodajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao)
      throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);
    AerodromiLetovi noviAerodrom = new AerodromiLetovi();
    noviAerodrom.setIcao(icao);
    noviAerodrom.setAktivan(true);
    try {
      aerodromiLetoviFacade.create(noviAerodrom);
    } catch (Exception e) {
      return false;
    }
    String poruka = "" + aerodromiLetoviFacade.count();
    InfoPodatak info = new InfoPodatak();
    info.setBrojAerodroma(poruka);
    InfoWebSocket.dajInfo(info);
    return true;
  }

  /**
   * Pauzira aerodrom za letove.
   *
   * @param korisnik korisničko ime
   * @param lozinka lozinka
   * @param icao ICAO oznaka aerodroma
   * @return true ako je aerodrom uspješno pauziran, inače false
   * @throws PogresnaAutentikacija u slučaju pogrešne autentikacije
   */
  @WebMethod
  public boolean pauzirajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao)
      throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);
    try {
      aerodromiLetoviFacade.updateAktivanStatus(icao, false);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Aktivira aerodrom za letove.
   *
   * @param korisnik korisničko ime
   * @param lozinka lozinka
   * @param icao ICAO oznaka aerodroma
   * @return true ako je aerodrom uspješno aktiviran, inače false
   * @throws PogresnaAutentikacija u slučaju pogrešne autentikacije
   */
  @WebMethod
  public boolean aktivirajAerodromZaLetove(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao)
      throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);
    try {
      aerodromiLetoviFacade.updateAktivanStatus(icao, true);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
