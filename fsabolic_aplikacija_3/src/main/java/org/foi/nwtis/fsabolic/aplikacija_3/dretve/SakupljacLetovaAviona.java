package org.foi.nwtis.fsabolic.aplikacija_3.dretve;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.fsabolic.aplikacija_3.jpa.AerodromiLetovi;
import org.foi.nwtis.fsabolic.aplikacija_3.jpa.LetoviPolasci;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.LetoviPolasciFacade;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.RollbackException;

/**
 * Sakupljač letova aviona.
 * 
 * Ova klasa predstavlja sakupljač letova aviona koji periodično preuzima informacije o letovima
 * aviona za odabrane aerodrome. Preuzeti letovi se sprema u bazu podataka i šalje odgovarajuća
 * poruka putem JMS-a. Sakupljanje letova se provodi u zadanim ciklusima prema konfiguracijskim
 * postavkama.
 */
public class SakupljacLetovaAviona extends Thread {
  /***
   * Fasada za upravljanje airodromima.
   */
  private AirportFacade airportFacade;
  /***
   * Fasada za upravljanje aerodromima čiji letovi se preuzimaju.
   */
  private AerodromiLetoviFacade aerodromiLetoviFacade;
  /***
   * Fasada za upravljanje polascima letova.
   */
  private LetoviPolasciFacade letoviPolasciFacade;

  /***
   * JMS pošiljatelj za slanje poruka.
   */
  private JmsPosiljatelj jmsPosiljatelj;

  /***
   * Trajanje svakog ciklusa u milisekundama.
   */
  private long trajanjeCiklusa;

  /***
   * Zastavica koja označava je li dretva završila.
   */
  private boolean kraj = false;

  /***
   * Konfiguracijski objekt koji sadrži različite postavke.
   */
  private Konfiguracija konfig;

  /***
   * Korisničko ime za pristup usluzi OpenSkyNetwork.
   */
  private String korisnik;

  /***
   * Početni datum za prikupljanje letova.
   */
  private String datumOd;

  /***
   * Završni datum za prikupljanje letova.
   */
  private String datumDo;

  /***
   * Email za pristup usluzi OpenSkyNetwork.
   */
  private String email;

  /***
   * Konstruktor klase SakupljacLetovaAviona.
   *
   * @param konfig Konfiguracijski objekt koji sadrži postavke.
   * @param jmsPosiljatelj JMS pošiljatelj za slanje poruka.
   * @param letoviPolasciFacade Fasada za upravljanje polascima letova.
   * @param aerodromiLetoviFacade Fasada za upravljanje aerodromima čiji letovi se preuzimaju
   * @param airportFacade Fasada za upravljanje aerodromima
   */
  public SakupljacLetovaAviona(Konfiguracija konfig, JmsPosiljatelj jmsPosiljatelj,
      LetoviPolasciFacade letoviPolasciFacade, AerodromiLetoviFacade aerodromiLetoviFacade,
      AirportFacade airportFacade) {
    this.aerodromiLetoviFacade = aerodromiLetoviFacade;
    this.airportFacade = airportFacade;
    this.letoviPolasciFacade = letoviPolasciFacade;
    this.jmsPosiljatelj = jmsPosiljatelj;
    this.konfig = konfig;
    this.trajanjeCiklusa = Long.parseLong(this.konfig.dajPostavku("ciklus.trajanje")) * 1000;
    this.korisnik = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    this.datumOd = konfig.dajPostavku("preuzimanje.od");
    this.datumDo = konfig.dajPostavku("preuzimanje.do");
    this.email = konfig.dajPostavku("OpenSkyNetwork.email");
  }

  /***
   * Nadjačana start metoda klase Thread
   */
  @Override
  public synchronized void start() {
    super.start();
  }

  /**
   * Pokreće izvršavanje dretve. Dretva sakuplja podatke o letovima aviona u zadanim vremenskim
   * intervalima i šalje poruke putem JMS-a.
   */
  @Override
  public void run() {
    String trenutniDatum = odrediPocetniDatum();
    while (!kraj && !dostigaoDatumDo(trenutniDatum)) {
      long poc = System.currentTimeMillis();
      List<AerodromiLetovi> aerodromi = aerodromiLetoviFacade.getAerodromiLetoviByAktivan(true);
      List<LetAviona> letovi = null;
      int brojLetovaNaDan = 0;
      for (AerodromiLetovi aerodrom : aerodromi) {
        letovi = dohvatiLetove(aerodrom.getIcao(), trenutniDatum);
        if (letovi != null) {
          brojLetovaNaDan +=
              letovi.stream().filter(let -> let.getEstArrivalAirport() != null).count();
          spremiLetove(letovi);
        }

      }
      long trajanje = System.currentTimeMillis() - poc;
      spavaj(trajanje);
      posaljiPoruku(trenutniDatum, brojLetovaNaDan);

      trenutniDatum = vratiIduciDan(trenutniDatum);
    }
  }

  /***
   * Pohranjuje listu letova u bazu podataka
   * 
   * @param letovi lista letova
   */
  private void spremiLetove(List<LetAviona> letovi) {
    for (LetAviona la : letovi) {
      LetoviPolasci lp = new LetoviPolasci();
      lp.setIcao24(la.getIcao24());
      lp.setFirstSeen(la.getFirstSeen());
      lp.setAirport(airportFacade.find(la.getEstDepartureAirport()));
      lp.setLastSeen(la.getLastSeen());
      lp.setEstArrivalAirport(la.getEstArrivalAirport());
      lp.setCallsign(la.getCallsign());
      lp.setEstDepartureAirportHorizDistance(la.getEstDepartureAirportHorizDistance());
      lp.setEstDepartureAirportVertDistance(la.getEstDepartureAirportVertDistance());
      lp.setEstArrivalAirportHorizDistance(la.getEstArrivalAirportHorizDistance());
      lp.setEstArrivalAirportVertDistance(la.getEstArrivalAirportVertDistance());
      lp.setDepartureAirportCandidatesCount(la.getDepartureAirportCandidatesCount());
      lp.setArrivalAirportCandidatesCount(la.getArrivalAirportCandidatesCount());
      lp.setStored(new Timestamp(System.currentTimeMillis()));
      try {
        letoviPolasciFacade.create(lp);
      } catch (Exception e) {
        Logger.getGlobal().log(Level.INFO, "Duplikat");
      }

    }
  }

  /**
   * Provjerava je li trenutni datum dostigao zadanu krajnju datumsku granicu.
   *
   * @param trenutniDatum trenutni datum u formatu "dd.MM.yyyy"
   * @return true ako je trenutni datum dostigao krajnju datumsku granicu, inače false
   */
  private boolean dostigaoDatumDo(String trenutniDatum) {
    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
    Date datumDoParsiran = null;
    Date trenutniDatumParsiran = null;
    try {
      datumDoParsiran = formatDatuma.parse(datumDo);
      trenutniDatumParsiran = formatDatuma.parse(trenutniDatum);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (trenutniDatumParsiran.after(datumDoParsiran)) {
      return true;
    }
    return false;
  }

  /**
   * Određuje početni datum za preuzimanje letova aviona. Ako postoji posljednji datum u bazi
   * podataka, vraća sljedeći dan nakon tog datuma. Inače, vraća zadani početni datum.
   *
   * @return početni datum za preuzimanje letova aviona
   */
  private String odrediPocetniDatum() {
    String posljednjiDatumBaze = pronadiDatumZadnjegReda();
    if (posljednjiDatumBaze == null) {
      return datumOd;
    }
    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
    Date datumOdParsiran = null;
    Date posljednjiDatumParsiran = null;
    try {
      datumOdParsiran = formatDatuma.parse(datumOd);
      posljednjiDatumParsiran = formatDatuma.parse(posljednjiDatumBaze);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (posljednjiDatumParsiran.after(datumOdParsiran)) {
      return vratiIduciDan(formatDatuma.format(posljednjiDatumParsiran));
    }

    return datumOd;
  }

  /**
   * Pronalazi datum zadnjeg reda u bazi podataka letova polazaka. Vraća formatirani datum zadnjeg
   * reda ili null ako nema podataka.
   *
   * @return datum zadnjeg reda ili null ako nema podataka
   */
  private String pronadiDatumZadnjegReda() {
    LetoviPolasci lp = letoviPolasciFacade.findLast();
    if (lp == null)
      return null;
    Date datum = new Date(lp.getFirstSeen() * 1000L);
    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
    Logger.getGlobal().log(Level.INFO, "Posljednji datum: " + formatDatuma.format(datum));
    return formatDatuma.format(datum);

  }

  /**
   * Šalje poruku putem JMS-a s informacijama o trenutnom datumu i broju letova na taj dan.
   *
   * @param trenutniDatum trenutni datum
   * @param brojLetovaNaDan broj letova na dan
   */
  private void posaljiPoruku(String trenutniDatum, int brojLetovaNaDan) {
    String poruka =
        "Na dan: " + trenutniDatum + " preuzeto ukupno " + brojLetovaNaDan + " letova aviona.";
    Logger.getGlobal().log(Level.INFO, "Poruka: " + poruka);
    jmsPosiljatelj.saljiPoruku(poruka);
  }

  /**
   * Dohvaća listu letova aviona za određeni aerodrom i datum.
   *
   * @param aerodrom kod aerodroma
   * @param trenutniDatum trenutni datum
   * @return lista letova aviona ili null ako se dogodila iznimka prilikom dohvaćanja
   */
  private List<LetAviona> dohvatiLetove(String aerodrom, String trenutniDatum) {
    OSKlijentBP oskBP = new OSKlijentBP(email, korisnik);
    List<LetAviona> letovi = null;

    try {
      letovi = oskBP.getDepartures(aerodrom, pocetakDana(trenutniDatum), krajDana(trenutniDatum));
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
      return null;
    }
    return letovi;
  }

  /**
   * Zaustavlja izvođenje niti na određeno vrijeme.
   *
   * @param trajanje trajanje vremena u milisekundama
   */
  private void spavaj(long trajanje) {
    try {
      long trajanjeSpavanja = trajanjeCiklusa - (trajanje % trajanjeCiklusa);
      if (trajanjeSpavanja < 0)
        trajanjeSpavanja = 0;
      Thread.sleep(trajanjeSpavanja);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Vraća sljedeći datum nakon zadanog datuma.
   *
   * @param dan datum u formatu "dd.MM.yyyy"
   * @return sljedeći datum nakon zadanog datuma u istom formatu "dd.MM.yyyy"
   */
  public static String vratiIduciDan(String dan) {
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate datum = LocalDate.parse(dan, formater);
    LocalDate iduciDan = datum.plusDays(1);
    return iduciDan.format(formater);
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
   * Nadjačavanje interrupt naredbe klase Thread
   */
  @Override
  public void interrupt() {
    this.kraj = true;
    super.interrupt();
  }
}
