package org.foi.nwtis.fsabolic.aplikacija_3.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.fsabolic.aplikacija_3.dretve.SakupljacLetovaAviona;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.AerodromiLetoviFacade;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.JmsPosiljatelj;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.LetoviPolasciFacade;
import org.foi.nwtis.klase.MrezniPomocnik;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
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
  /**
   * Referenca na objekt koji omogućuje pristup i upravljanje podacima o letovima aviona.
   */
  @Inject
  private AirportFacade airportFacade;
  /**
   * Referenca na objekt koji omogućuje pristup i upravljanje podacima o letovima aviona.
   */
  @Inject
  private AerodromiLetoviFacade aerodromLetoviFacade;
  /**
   * Referenca na objekt koji omogućuje pristup i upravljanje podacima o letovima aviona.
   */
  @Inject
  private LetoviPolasciFacade letoviPolasciFacade;

  /**
   * Dretva SakupljacLetovaAviona koja se koristi za prikupljanje letova aviona.
   */
  private SakupljacLetovaAviona sakupljacThread;

  /**
   * Referenca na objekt koji omogućuje slanje JMS poruka.
   */
  @EJB
  private JmsPosiljatelj jmsPosiljatelj;


  /***
   * Metoda za čitanje i pohranu postavki za vrijeme inicijalizacije konteksta
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

    sakupljacThread = new SakupljacLetovaAviona(konfig, jmsPosiljatelj, letoviPolasciFacade,
        aerodromLetoviFacade, airportFacade);
    sakupljacThread.start();

  }

  /***
   * Metoda koja se pokreće uništavanjem konteksta
   */
  @Override
  public void contextDestroyed(ServletContextEvent event) {

    if (sakupljacThread != null && sakupljacThread.isAlive()) {
      sakupljacThread.interrupt();

    }
  }
}
