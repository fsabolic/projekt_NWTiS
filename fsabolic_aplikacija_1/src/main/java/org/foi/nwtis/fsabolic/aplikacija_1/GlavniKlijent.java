package org.foi.nwtis.fsabolic.aplikacija_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.klase.MrezniPomocnik;


/***
 * Klasa za slanje zahtjeva na poslužitelj
 * 
 * @author Fran Sabolić
 *
 */
public class GlavniKlijent {

  /***
   * Regex validator za provjeru komandi koje će se slati
   */
  private RegexValidator regexValidator =
      new RegexValidator("^((?<status>STATUS)" + "|(?<kraj>KRAJ)" + "|(?<test>TEST)"
          + "|(?<init>INIT)" + "|(?<pauza>PAUZA)" + "|(?<info>INFO (?<infoVrijednost>DA|NE))"
          + "|(?<udaljenost>UDALJENOST " + "(?<gps1>([-])?((([0-9]|[1-8][0-9]).[0-9]+)|90.[0]+)) "
          + "(?<gos1>([-])?((([0-9]|[1-9][0-9]|[1][0-7][0-9]).[0-9]+)|180.[0]+)) "
          + "(?<gps2>([-])?((([0-9]|[1-8][0-9]).[0-9]+)|90.[0]+)) "
          + "(?<gos2>([-])?((([0-9]|[1-9][0-9]|[1][0-7][0-9]).[0-9]+)|180.[0]+))))$");
  /***
   * Adresa poslužitelja
   */
  private String adresa;
  /***
   * Mrežna vrata poslužitelja
   */
  private int mreznaVrata;
  /***
   * Definira koliko će klijent čekati na prihvaćanje zahtjeva
   */
  private int cekanje;

  /***
   * Main metoda koja se pokreće pokretanjem programa
   * 
   * @param args parametri za izvođenje klijenta
   */
  public static void main(String[] args) {
    var glavniKlijent = new GlavniKlijent();
    String klijentKomanda = glavniKlijent.argumentiUStringKomandu(args);
    if (!glavniKlijent.regexValidator.provjeriRegex(klijentKomanda)) {
      Logger.getGlobal().log(Level.SEVERE, "Nisu ispravni ulazni argumenti!");
      return;
    }
    glavniKlijent.odrediParametreZaPovezivanje();
    String posluziteljKomanda = glavniKlijent.sastaviKomanduZaPosluzitelj(args);

    Logger.getGlobal().log(Level.INFO, "Poslao sam: " + posluziteljKomanda);

    String odgovor = MrezniPomocnik.slanjeZahtjevaNa(glavniKlijent.adresa,
        glavniKlijent.mreznaVrata, glavniKlijent.cekanje, posluziteljKomanda, 0);

    if (odgovor != null)
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + odgovor);
  }

  /***
   * Pretvara proslijeđene argumente u jednu liniju
   * 
   * @param args parametri
   * @return spojeni parametri u komandu
   */
  private String argumentiUStringKomandu(String[] args) {
    StringBuilder unesenaKomanda = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      unesenaKomanda.append(args[i]).append(" ");
    }
    String sastavljenaKomanda = unesenaKomanda.toString().trim();
    return sastavljenaKomanda;
  }

  /***
   * Čita parametre za povezivanje iz početnih parametara
   * 
   */
  private void odrediParametreZaPovezivanje() {
    this.adresa = "localhost";
    this.mreznaVrata = 8123;
    this.cekanje = 500;
  }

  /***
   * Pretvara dane parametre klijenta u komandu za poslužitelja
   * 
   * @param args parametri
   * @return komanda za poslužitelja
   */
  private String sastaviKomanduZaPosluzitelj(String[] args) {
    StringBuilder komanda = new StringBuilder();

    if (regexValidator.provjeriGrupu("status")) {
      komanda.append("STATUS");
    } else if (regexValidator.provjeriGrupu("kraj")) {
      komanda.append("KRAJ");
    } else if (regexValidator.provjeriGrupu("init")) {
      komanda.append("INIT");
    } else if (regexValidator.provjeriGrupu("pauza")) {
      komanda.append("PAUZA");
    } else if (regexValidator.provjeriGrupu("info")) {
      komanda.append("INFO ").append(regexValidator.vratiVrijednostGrupe("infoVrijednost"));

    } else if (regexValidator.provjeriGrupu("test")) {
      komanda.append("TEST");
    } else if (regexValidator.provjeriGrupu("udaljenost")) {
      komanda.append("UDALJENOST ").append(regexValidator.vratiVrijednostGrupe("gps1")).append(" ")
          .append(regexValidator.vratiVrijednostGrupe("gos1")).append(" ")
          .append(regexValidator.vratiVrijednostGrupe("gps2")).append(" ")
          .append(regexValidator.vratiVrijednostGrupe("gos2"));
    }

    return komanda.toString().trim();
  }
}
