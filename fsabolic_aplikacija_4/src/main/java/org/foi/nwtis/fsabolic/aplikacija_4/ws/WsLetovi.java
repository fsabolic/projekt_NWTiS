package org.foi.nwtis.fsabolic.aplikacija_4.ws;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.fsabolic.aplikacija_4.greska.PogresnaAutentikacija;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.AerodromiLetovi;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.LetoviPolasciFacade;
import org.foi.nwtis.klase.JsonOdgovor;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
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
import jakarta.ws.rs.core.Context;

/**
 * Web servis za dohvaćanje meteoroloških podataka.
 */
@WebService(serviceName = "letovi")
public class WsLetovi {
  /***
   * Injektiran objekt za rad s polascima letova
   */
  @Inject
  private LetoviPolasciFacade letoviPolasciFacade;
  /***
   * Injektiran objekt za rad s korisnicima
   */
  @Inject
  private KorisniciFacade korisniciFacade;

  /***
   * Injektiran kontekst aplikacije
   */
  @Inject
  ServletContext kontekst;

  /**
   * Vraća polaske sa odabranog aerodroma u razdoblju između 2 datuma
   * 
   * @param korisnik korisničko ime za autentikaciju
   * @param lozinka lozinka za autentikaciju
   * @param icao icao aerodroma za kojeg promatramo polaske
   * @param danOd početni dan intervala
   * @param danDo zadnji dan intervala
   * @param odBroja početni broj od kojeg se vraćaj rezultati za stranjičenje
   * @param broj broj rezultata koji se vraćaju iz baze
   * @return popis letova s odabranog aerodroma
   * @throws PogresnaAutentikacija ukoliko korisnik ne prođe autentikaciju
   */
  @WebMethod
  public List<LetAviona> dajPolaskeInterval(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "danOd") String danOd, @WebParam(name = "danDo") String danDo,
      @WebParam(name = "odBroja") String odBroja, @WebParam(name = "broj") String broj)
      throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);

    int odBrojaInt, brojInt;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Krivi format parametara");
      return null;
    }

    long epohaOd = parseDanDatum(danOd);
    long epohaDo = parseDanDatum(danDo);

    List<LetoviPolasci> letoviPolasci =
        letoviPolasciFacade.findLetoviInterval(odBrojaInt-1, brojInt, epohaOd, epohaDo, icao);

    List<LetAviona> letoviAviona = new ArrayList<>();
    for (LetoviPolasci l : letoviPolasci) {
      letoviAviona.add(pretvoriLetoviPolasci(l));
    }

    return letoviAviona;
  }

  /**
   * Dohvaća polaske za određeni dan s navedene zračne luke.
   *
   * @param korisnik korisničko ime za autentikaciju
   * @param lozinka lozinka za autentikaciju
   * @param icao ICAO oznaka zračne luke za dohvat polazaka
   * @param dan dan za koji se traže polasci
   * @param odBroja početni indeks za paginaciju
   * @param broj broj polazaka za dohvat
   * @return lista letova s navedene zračne luke za određeni dan
   * @throws PogresnaAutentikacija ako korisnik ne prođe autentikaciju
   */
  @WebMethod
  public List<LetAviona> dajPolaskeNaDan(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "dan") String dan, @WebParam(name = "odBroja") String odBroja,
      @WebParam(name = "broj") String broj) throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);

    int odBrojaInt, brojInt;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Krivi format parametara");
      return null;
    }

    long epohaOd = pocetakDana(dan);
    long epohaDo = krajDana(dan);

    List<LetoviPolasci> letoviPolasci =
        letoviPolasciFacade.findLetoviInterval(odBrojaInt-1, brojInt, epohaOd, epohaDo, icao);

    List<LetAviona> letoviAviona = new ArrayList<>();
    for (LetoviPolasci l : letoviPolasci) {
      letoviAviona.add(pretvoriLetoviPolasci(l));
    }

    return letoviAviona;
  }

  /**
   * Dohvaća polaske za određeni dan s navedene zračne luke putem servisa OSKlijentBP.
   *
   * @param korisnik korisničko ime za autentikaciju
   * @param lozinka lozinka za autentikaciju
   * @param icao ICAO oznaka zračne luke za dohvat polazaka
   * @param dan dan za koji se traže polasci
   * @return lista letova s navedene zračne luke za određeni dan
   * @throws PogresnaAutentikacija ako korisnik ne prođe autentikaciju
   */
  @WebMethod
  public List<LetAviona> dajPolaskeNaDanOS(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka, @WebParam(name = "icao") String icao,
      @WebParam(name = "dan") String dan) throws PogresnaAutentikacija {
    WsKorisnici wk = new WsKorisnici();
    wk.setKorisniciFacade(korisniciFacade);
    wk.dajKorisnika(korisnik, lozinka, korisnik);

    long epohaOd = pocetakDana(dan);
    long epohaDo = krajDana(dan);

    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");

    String korisnikServis = postavke.dajPostavku("OpenSkyNetwork.korisnik");
    String email = postavke.dajPostavku("OpenSkyNetwork.email");

    OSKlijentBP oskbp = new OSKlijentBP(email, korisnikServis);
    List<LetAviona> letoviAviona = null;
    try {
      letoviAviona = oskbp.getDepartures(icao, epohaOd, epohaDo);
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, "Greška u dohvaćanju letova sa servisa");
    }

    return letoviAviona;
  }

  /**
   * Parsira dani datum u obliku teksta u UNIX vremensku oznaku početka tog dana.
   *
   * @param strDatum tekstualni zapis datuma u formatu "dd.MM.yyyy"
   * @return UNIX vremenska oznaka početka navedenog dana
   */
  private static long parseDanDatum(String strDatum) {
    LocalDate datum = LocalDate.parse(strDatum, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    LocalDateTime pocetakDana = datum.atStartOfDay();
    return pocetakDana.atZone(ZoneId.systemDefault()).toEpochSecond();
  }

  /***
   * Vraća početak dana u sekundama za dani datum u Stringu
   * 
   * @param strDatum datum u Stringu
   * @return početak dana u sekundama
   */
  public static long pocetakDana(String strDatum) {
    LocalDate datum = LocalDate.parse(strDatum, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    LocalDateTime pocetakDana = datum.atStartOfDay();
    return pocetakDana.atZone(ZoneId.systemDefault()).toEpochSecond();
  }

  /***
   * Vraća početak dana u sekundama za sljedeći dan nakon danog datuma u Stringu
   * 
   * @param strDatum datum u Stringu
   * @return početak sljedećeg dana u sekundama
   */
  public static long krajDana(String strDatum) {
    LocalDate datum = LocalDate.parse(strDatum, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    LocalDateTime pocetakDana = datum.plusDays(1).atStartOfDay();
    return pocetakDana.atZone(ZoneId.systemDefault()).toEpochSecond();
  }

  /***
   * Klasa za provjeru ispravnosti cijelobrojne vrijednosti za potrebe straničenja
   * 
   * @param vrijednost dobiveni parametar
   * @param zadanaVrijednost vrijednost koju varijbala poprima ukoliko nije dana vrijednost
   * @return dobro parsiran broj
   * @throws NumberFormatException
   */
  private int parsirajCijelobrojnuVrijednost(String vrijednost, int zadanaVrijednost)
      throws NumberFormatException {
    int vrijednostInt = (vrijednost != null) ? Integer.parseInt(vrijednost) : zadanaVrijednost;
    if (vrijednostInt < 1)
      throw new NumberFormatException("Vrijednost nije cijelobrojna");
    return vrijednostInt;
  }


  /**
   * Pretvara objekt tipa LetoviPolasci u objekt tipa LetAviona.
   *
   * @param let objekt tipa LetoviPolasci koji se pretvara
   * @return pretvoreni objekt tipa LetAviona
   */
  private LetAviona pretvoriLetoviPolasci(LetoviPolasci let) {
    LetAviona pretvoreniLet = new LetAviona();

    pretvoreniLet.setIcao24(let.getIcao24());
    pretvoreniLet.setFirstSeen(let.getFirstSeen());
    pretvoreniLet.setEstDepartureAirport(let.getAirport().getIcao());
    pretvoreniLet.setLastSeen(let.getLastSeen());
    pretvoreniLet.setEstArrivalAirport(let.getEstArrivalAirport());
    pretvoreniLet.setCallsign(let.getCallsign());
    pretvoreniLet.setEstDepartureAirportHorizDistance(let.getEstDepartureAirportHorizDistance());
    pretvoreniLet.setEstDepartureAirportVertDistance(let.getEstDepartureAirportVertDistance());
    pretvoreniLet.setEstArrivalAirportHorizDistance(let.getEstArrivalAirportHorizDistance());
    pretvoreniLet.setEstArrivalAirportVertDistance(let.getEstArrivalAirportVertDistance());
    pretvoreniLet.setDepartureAirportCandidatesCount(let.getDepartureAirportCandidatesCount());
    pretvoreniLet.setArrivalAirportCandidatesCount(let.getArrivalAirportCandidatesCount());

    return pretvoreniLet;
  }
}


