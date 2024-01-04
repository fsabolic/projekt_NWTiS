package org.foi.nwtis.fsabolic.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;


/***
 * Klasa koja predstavlja pojedinu dretvu koja se izvodi za obradu zaprimljenih zahtjeva na glavnom
 * poslužitelju. Obavlja svu obradu podataka za glavni poslužitelj i tumači dobivene naredbe.
 * 
 * @author Fran Sabolić
 *
 */
public class MrezniRadnik extends Thread {
  /***
   * Statički određuje broj dretve kako bi se mogla imenovati
   */
  private static int brojDretve;
  /***
   * Određuje mrežnu utičnicu na koju se šalju zahtjevi
   */
  protected Socket mreznaUticnica;
  /***
   * Predstavlja konfiguraciju glavnog poslužitelja
   */
  protected Konfiguracija konfig;
  /***
   * Predstavlja instancu glavnog poslužitelja kako bi se s njime mogli razmijenjivati podaci
   */
  private GlavniPosluzitelj glavniPosluzitelj = null;

  /***
   * Regex validator za određivanje točnosti dobivene komande
   */
  private RegexValidator regexValidator =
      new RegexValidator("^((?<status>STATUS)" + "|(?<kraj>KRAJ)" + "|(?<test>TEST)"
          + "|(?<init>INIT)" + "|(?<pauza>PAUZA)" + "|(?<info>INFO (?<infoVrijednost>DA|NE))"
          + "|(?<udaljenost>UDALJENOST " + "(?<gps1>([-])?((([0-9]|[1-8][0-9]).[0-9]+)|90.[0]+)) "
          + "(?<gos1>([-])?((([0-9]|[1-9][0-9]|[1][0-7][0-9]).[0-9]+)|180.[0]+)) "
          + "(?<gps2>([-])?((([0-9]|[1-8][0-9]).[0-9]+)|90.[0]+)) "
          + "(?<gos2>([-])?((([0-9]|[1-9][0-9]|[1][0-7][0-9]).[0-9]+)|180.[0]+))))$");

  /***
   * Konstruktor klase u kojemu se podaci iz dobivene konfiguracije zapisuju u memoriju dretve
   * 
   * @param mreznaUticnica mrežna utičnica na koju se šalju podaci i s koje se podaci primaju
   * @param konfig konfiguracija glavnog poslužitelja
   */
  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
  }

  /***
   * Proslijeđuje instancu glavnog poslužitelja i postavlja ju u atribut klase
   * 
   * @param gk instanca glavnog poslužitelja za komunikaciju između klasa
   */
  public void postaviGlavniPosluzitelj(GlavniPosluzitelj gk) {
    this.glavniPosluzitelj = gk;
  }

  /***
   * Nadjačana start metoda klase Thread
   */
  @Override
  public synchronized void start() {
    super.start();
  }

  /***
   * Nadjačana run metoda klase Thread u kojoj se imenuje dretva, čitaju podaci sa mrežne utičnice,
   * poziva se obrada zahtjeva i prosljeđuju se rezultati klijentu
   */
  @Override
  public void run() {
    this.setName("fsabolic_" + brojDretve++);
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      this.mreznaUticnica.shutdownInput();
      String dobivenaKomanda = poruka.toString();
      if (this.glavniPosluzitelj.vratiInfo())
        System.out.println(dobivenaKomanda);
      String odgovor = this.obradiZahtjev(dobivenaKomanda);
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Neuspješan rad s mrežnom utičnicom! Nije moguće čitati/pisati/zatvoriti/otvoriti utičnicu");
    }
  }

  /***
   * Provjerava točnost dobivene komande i poziva odgovarajuću metodu za obradu na temelju dobivene
   * komande
   * 
   * @param komanda komanda poslana od strane klijenta
   * @return ERROR ili OK odgovor, ovisno o naredbi koja je poslana
   */
  public String obradiZahtjev(String komanda) {


    if (!this.regexValidator.provjeriRegex(komanda))
      return "ERROR 05 Format dobivene komande nije točan!";
    if (this.regexValidator.provjeriGrupu("test")) {
      test();
      return "test";
    }
    if (this.regexValidator.provjeriGrupu("kraj"))
      return kraj();
    if (this.regexValidator.provjeriGrupu("status"))
      return status();
    if (this.regexValidator.provjeriGrupu("init"))
      return init();
    if (this.glavniPosluzitelj.dohvatiStatus() == 0)
      return "ERROR 01 Poslužitelj je pauziran";

    if (this.regexValidator.provjeriGrupu("pauza"))
      return pauza();
    if (this.regexValidator.provjeriGrupu("info"))
      return info(regexValidator.vratiVrijednostGrupe("infoVrijednost"));
    if (this.regexValidator.provjeriGrupu("udaljenost")) {
      float gpsSirina1 = Float.parseFloat(regexValidator.vratiVrijednostGrupe("gps1"));
      float gosDuzina1 = Float.parseFloat(regexValidator.vratiVrijednostGrupe("gos1"));
      float gpsSirina2 = Float.parseFloat(regexValidator.vratiVrijednostGrupe("gps2"));
      float gosDuzina2 = Float.parseFloat(regexValidator.vratiVrijednostGrupe("gos2"));
      return udaljenost(gpsSirina1, gosDuzina1, gpsSirina2, gosDuzina2);
    }
    return "ERROR 05 Nešto je pošlo po zlu";
  }

  /**
   * Klasa za testiranje višedretvenosti
   */
  public void test() {
    for (int i = 0; i < 10; i++) {
      try {
        this.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Logger.getGlobal().log(Level.INFO, " " + i);
    }
  }

  /***
   * Obrađuje KRAJ naredbu, gasi glavni poslužitelj
   * 
   * @return OK
   */
  public String kraj() {
    this.glavniPosluzitelj.zaustaviNoveZahtjeve();
    this.glavniPosluzitelj.ugasiServerSocket();
    return "OK";
  }

  /***
   * Obrađuje STATUS naredbu, vraća trenutni status poslužitelja
   * 
   * @return OK ako uspije
   */
  public String status() {
    return "OK " + this.glavniPosluzitelj.dohvatiStatus();
  }

  /***
   * Obrađuje INIT naredbu, inicijalizira poslužitelj
   * 
   * @return OK ako uspije, ERROR ako je poslužitelj već aktivan
   */
  public String init() {
    if (this.glavniPosluzitelj.dohvatiStatus() == 1) {
      return "ERROR 02 Poslužitelj je već aktivan";
    }
    this.glavniPosluzitelj.inicijalizirajPosluzitelj();
    return "OK";
  }

  /***
   * Obrađuje PAUSE naredbu, pauzira polužitelja
   * 
   * @return OK ako uspije, ERROR ako je poslužitelj već pauziran
   */
  public String pauza() {
    return "OK " + this.glavniPosluzitelj.pauzirajPosluzitelj();
  }

  /***
   * Obrađuje INFO naredbu, određuje ispisuje li se ili ne zaprimljena naredba na poslužitelju
   * 
   * @param vrijednost DA/NE vrijednost koja definira ispisuje li se ili ne na standardni izlaz
   * @return OK ako je sve u redu, određen ERROR ukoliko je došlo do pogreške
   */
  private String info(String vrijednost) {
    boolean trenutniInfo = this.glavniPosluzitelj.vratiInfo();
    if (vrijednost.equals("DA")) {
      if (trenutniInfo == true)
        return "ERROR 03 Ispis je već postavljen";
      this.glavniPosluzitelj.postaviInfo(true);
    } else if (vrijednost.equals("NE")) {
      if (trenutniInfo == false)
        return "ERROR 04 Ispis nije ni postavljen";
      this.glavniPosluzitelj.postaviInfo(false);
    } else {
      return "ERROR 05 Pogrešni parametri";
    }
    return "OK";
  }

  /***
   * Obrađuje UDALJENOST naredbu i vraća udaljenost dviju lokacija
   * 
   * @param gpsSirina1 širina
   * @param gosDuzina1 dužina
   * @param gpsSirina2 širina
   * @param gosDuzina2 dužina
   * @return udaljenost
   */
  private String udaljenost(float gpsSirina1, float gosDuzina1, float gpsSirina2,
      float gosDuzina2) {
    this.glavniPosluzitelj.inkrementirajBrojZahtjeva();
    return "OK " + izracunajUdaljenost(gpsSirina1, gosDuzina1, gpsSirina2, gosDuzina2);
  }

  /***
   * Računa udaljenost između dviju lokacija na temelju njihovih koordinata. Izvor: Haversine
   * formula to find distance between two points on a sphere, poveznica: https://shorturl.at/cilHJ
   * 
   * @param gpsSirina1 širina
   * @param gosDuzina1 dužina
   * @param gpsSirina2 širina
   * @param gosDuzina2 dužina
   * @return udaljenost
   */
  private Float izracunajUdaljenost(float gpsSirina1, float gosDuzina1, float gpsSirina2,
      float gosDuzina2) {
    double R = 6371.009;

    double radGps1 = Math.toRadians(gpsSirina1);
    double radGos1 = Math.toRadians(gosDuzina1);
    double radGps2 = Math.toRadians(gpsSirina2);
    double radGos2 = Math.toRadians(gosDuzina2);

    double dLat = radGps2 - radGps1;
    double dLon = radGos2 - radGos1;

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(radGps1) * Math.cos(radGps2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    float udaljenost = (float) (R * c);

    DecimalFormat df = new DecimalFormat("#####.##");
    udaljenost = Float.parseFloat(df.format(udaljenost));

    return udaljenost;
  }

  /***
   * Nadjačavanje interrupt naredbe klase Thread
   */
  @Override
  public void interrupt() {
    try {
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      String odgovor =
          "ERROR 05 Previše zahtjeva poslano na poslužitelj. Ovaj se odbacuje. Pokušajte ponovno!";
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Neuspješno zatvaranje utičnice!");
    }
    super.interrupt();
  }


}
