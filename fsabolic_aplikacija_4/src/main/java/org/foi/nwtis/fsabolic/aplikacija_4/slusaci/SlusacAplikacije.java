package org.foi.nwtis.fsabolic.aplikacija_4.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.klase.MrezniPomocnik;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/***
 * Slušač kojim se čita naziv konfiguracijske datotke i njen sadržaj se sprema u kontekst aplikacije
 * 
 * @author Fran Sabolić
 *
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
  /***
   * Metoda za čitanje i pohranu postavki za vrijeme inicijalizacije konteksta
   * @param event događaj koji se treba obraditi
   */
  @Override
  public void contextInitialized(ServletContextEvent event) {
    ServletContext kontekst = event.getServletContext();
    String nazivKonfiguracije = kontekst.getInitParameter("konfiguracija");
    String putanja = kontekst.getRealPath("/WEB-INF");
    nazivKonfiguracije = putanja + File.separator + nazivKonfiguracije;
    Konfiguracija konfig = null;
    try {
      konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivKonfiguracije);
      kontekst.setAttribute("konfig", konfig);

    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    String adresaApp1 = konfig.dajPostavku("adresaAplikacije1");
    int portApp1 = Integer.parseInt(konfig.dajPostavku("mreznaVrataAplikacije1"));
    String odgovor = MrezniPomocnik.slanjeZahtjevaNa(adresaApp1, portApp1, 0, "STATUS", 0);
    int status = Integer.parseInt(odgovor.split(" ")[1]);
    if (status == 0) {
      throw new RuntimeErrorException(null, "Poslužitelj u aplikaciji 1 ne radi!");
    }
  }

  /***
   * Metoda koja se pokreće uništavanjem konteksta
   * @param event događaj koji se treba obraditi
   * 
   */
  @Override
  public void contextDestroyed(ServletContextEvent event) {



  }

}
