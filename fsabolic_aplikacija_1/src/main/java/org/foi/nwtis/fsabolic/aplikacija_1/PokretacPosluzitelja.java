package org.foi.nwtis.fsabolic.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/***
 * Pokreće glavni poslužitelj i učitava njegovu konfiguraciju
 * 
 * @author Fran Sabolić
 *
 */
public class PokretacPosluzitelja {

  /***
   * Main funkcija koja se izvodi pokretanjem programa
   * 
   * @param args naziv konfiguracijske datoteke
   */
  public static void main(String[] args) {
    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke!");
      return;
    }
    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konf);
      if (pokretac.mreznaVrataSeKoriste(Integer.parseInt(konf.dajPostavku("mreznaVrata")))) {
        Logger.getGlobal().log(Level.SEVERE,
            "Mrežna vrata " + konf.dajPostavku("mreznaVrata") + " se već koriste!");
        return;
      }
      glavniPosluzitelj.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke! " + e.getMessage());
      return;
      
    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Neuspješno učitavanje svih postavki. Možda nisu sve unesene!");
      e.printStackTrace();
      return;
    }
  }

  /***
   * Provjerava koriste li se mrežna vrata
   * 
   * @param mreznaVrata port poslužitelja
   * @return vraća true ako se vrata koriste, inače false
   */
  private boolean mreznaVrataSeKoriste(int mreznaVrata) {
    try (ServerSocket probniSocket = new ServerSocket(mreznaVrata)) {
      return false;
    } catch (IOException e) {
      return true;
    }
  }

  /***
   * Provjerava ispravnost danih argumenata
   * 
   * @param args naziv datoteke
   * @return true ako su ispravni argumenti, inače false
   */
  private boolean provjeriArgumente(String[] args) {
    return (args.length == 1 ? true : false)
        && args[0].matches("^[a-zA-Z0-9._-]+\\.(json|txt|yaml|bin|xml)$");
  }

  /***
   * Pohranjuje konfiguraciju u memoriju pokretača
   * 
   * @param nazivDatoteke naziv konfiguracijske datoteke
   * @return vraća konfiguraciju
   * @throws NeispravnaKonfiguracija ukoliko konfiguracija nije uspješno učitana
   */
  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }
}
